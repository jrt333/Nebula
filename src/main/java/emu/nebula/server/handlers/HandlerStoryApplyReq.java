package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.StoryApply.StoryApplyReq;
import emu.nebula.net.HandlerId;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.story_apply_req)
public class HandlerStoryApplyReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = StoryApplyReq.parseFrom(message);
        
        // Apply for story
        session.getPlayer().getStoryManager().apply(req.getIdx());
        
        // Encode response
        return session.encodeMsg(NetMsgId.story_apply_succeed_ack);
    }

}
