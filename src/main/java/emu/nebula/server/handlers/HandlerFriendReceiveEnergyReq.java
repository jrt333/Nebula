package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.FriendReceiveEnergy.FriendReceiveEnergyReq;
import emu.nebula.proto.FriendReceiveEnergy.FriendReceiveEnergyResp;
import emu.nebula.net.HandlerId;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.friend_receive_energy_req)
public class HandlerFriendReceiveEnergyReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = FriendReceiveEnergyReq.parseFrom(message);
        
        // Send energy
        var result = session.getPlayer().getFriendList().recvEnergy(req.getUIds());
        
        // Build response
        var rsp = FriendReceiveEnergyResp.newInstance()
                .setChange(result.getChange().toProto())
                .setReceiveEnergyCnt(result.getEnergy());
        
        for (long uid : result.getUids()) {
            rsp.addUIds(uid);
        }
        
        // Encode and send
        return session.encodeMsg(NetMsgId.friend_receive_energy_succeed_ack, rsp);
    }

}
