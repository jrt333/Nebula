package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.FriendSendEnergy.FriendSendEnergyReq;
import emu.nebula.proto.FriendSendEnergy.FriendSendEnergyResp;
import emu.nebula.net.HandlerId;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.friend_send_energy_req)
public class HandlerFriendSendEnergyReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = FriendSendEnergyReq.parseFrom(message);
        
        // Send energy
        var results = session.getPlayer().getFriendList().sendEnergy(req.getUIds());
        
        // Build response
        var rsp = FriendSendEnergyResp.newInstance();
        
        for (long uid : results) {
            rsp.addUIds(uid);
        }
        
        // Encode and send
        return session.encodeMsg(NetMsgId.friend_send_energy_succeed_ack, rsp);
    }

}
