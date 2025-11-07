package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.PlayerWorldClassRewardReceive.PlayerWorldClassRewardReq;
import emu.nebula.net.HandlerId;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.player_world_class_reward_receive_req)
public class HandlerPlayerWorldClassRewardReceiveReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = PlayerWorldClassRewardReq.parseFrom(message);
        
        // Receive rewards
        var change = session.getPlayer().getQuestManager().receiveWorldClassReward(req.getClassX());
        
        if (change == null) {
            return session.encodeMsg(NetMsgId.player_world_class_reward_receive_failed_ack);
        }
        
        // Template
        return session.encodeMsg(NetMsgId.player_world_class_reward_receive_succeed_ack, change.toProto());
    }

}
