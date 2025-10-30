package emu.nebula.net;

import lombok.SneakyThrows;
import us.hebi.quickbuf.ProtoMessage;

public abstract class NetHandler {

    public boolean requireSession() {
        return true;
    }
    
    public boolean requirePlayer() {
        return true;
    }
    
    // Packet encoding helper functions
    
    public byte[] encodeMsg(int msgId, byte[] packet) {
        return PacketHelper.encodeMsg(msgId, packet);
    }

    @SneakyThrows
    public byte[] encodeMsg(int msgId, ProtoMessage<?> proto) {
        return PacketHelper.encodeMsg(msgId, proto);
    }
    
    public byte[] encodeMsg(int msgId) {
        return PacketHelper.encodeMsg(msgId);
    }
    
    // Handler

    public abstract byte[] handle(GameSession session, byte[] message) throws Exception;

}
