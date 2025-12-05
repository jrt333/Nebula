package emu.nebula.server.routes;

import emu.nebula.Nebula;
import emu.nebula.game.player.Player;
import emu.nebula.util.JsonUtils;
import io.javalin.http.ContentType;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

public class RemoteHandler implements Handler {

    static class RemoteCommandRequest {
        public String token;
        public String command;
    }

    // Cache: Token -> UID
    private static final java.util.Map<String, Integer> tokenCache = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public void handle(@NotNull Context ctx) throws Exception {
        // Allow cross-origin calls for the standalone web console
        ctx.header("Access-Control-Allow-Origin", "*");
        ctx.header("Access-Control-Allow-Headers", "Content-Type");
        if (!Nebula.getConfig().getRemoteCommand().useRemoteServices) {
            ctx.status(403);
            ctx.result("{\"Code\":403,\"Msg\":\"RemoteServer not enable\"}");
            return;
        }

        // Parse body
        RemoteCommandRequest req = JsonUtils.decode(ctx.body(), RemoteCommandRequest.class);
        if (req == null || req.token == null || req.command == null) {
            ctx.status(400);
            ctx.result("{\"Code\":400,\"Msg\":\"Invalid request\"}");
            return;
        }

        String token = req.token;
        String command = req.command;
        String adminKey = Nebula.getConfig().getRemoteCommand().getServerAdminKey();

        // Check admin key
        if (token.equals(adminKey)) {
            var result = Nebula.getCommandManager().invoke(null, command);
            Nebula.getLogger().warn("\u001B[38;2;252;186;3mRemote Server (Using Admin Key) sent command: /" + command + "\u001B[0m");
            writeCommandResponse(ctx, result);
            return;
        }

        // Check player
        Player player = null;

        // 1. Try cache
        Integer cachedUid = tokenCache.get(token);
        if (cachedUid != null) {
            player = Nebula.getGameContext().getPlayerModule().getPlayer(cachedUid);
            // Verify token matches (in case player changed token or cache is stale)
            if (player != null && !token.equals(player.getRemoteToken())) {
                player = null;
                tokenCache.remove(token);
            }
        }

        // 2. Fallback to DB if not in cache or cache invalid
        if (player == null) {
            player = Nebula.getGameDatabase().getObjectByField(Player.class, "remoteToken", token);
            if (player != null) {
                tokenCache.put(token, player.getUid());
            }
        }

        if (player != null) {
            // Append target UID to command to ensure it targets the player
            // CommandArgs parses @UID to set the target
            String finalCommand = command + " @" + player.getUid();

            Nebula.getLogger().info("Remote Player Request [" + player.getUid() + "]: " + finalCommand);

            // Execute as console (null sender) but targeting the player
            var result = Nebula.getCommandManager().invoke(null, finalCommand);

            writeCommandResponse(ctx, result);
            return;
        }

        // Invalid token
        ctx.status(403);
        ctx.result("{\"Code\":403,\"Msg\":\"Invalid token\"}");
    }
    private void writeCommandResponse(Context ctx, emu.nebula.command.CommandResult result) {
        boolean success = result.getCommand() != null && (result.getMessage() == null || !result.getMessage().startsWith("Error -"));
        int code = success ? 200 : 400;
        String msg = result.getMessage() == null ? "" : result.getMessage();

        ctx.status(code);
        ctx.contentType(ContentType.APPLICATION_JSON);
        ctx.result(String.format("{\"Code\":%d,\"Data\":{},\"Msg\":\"%s\"}", code, msg));
    }
}
