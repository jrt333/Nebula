package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.ActivityTrekkerVersusSettle.ActivityTrekkerVersusSettleReq;
import emu.nebula.proto.ActivityTrekkerVersusSettle.ActivityTrekkerVersusSettleResp;
import emu.nebula.net.HandlerId;
import emu.nebula.game.activity.type.TrekkerVersusActivity;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.activity_trekker_versus_settle_req)
public class HandlerActivityTrekkerVersusSettleReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Parse request
        var req = ActivityTrekkerVersusSettleReq.parseFrom(message);
        
        // Get activity
        var activity = session.getPlayer().getActivityManager().getActivity(TrekkerVersusActivity.class, req.getActivityId());
        
        if (activity == null) {
            return session.encodeMsg(NetMsgId.activity_trekker_versus_settle_failed_ack);
        }
        
        // Settle
        activity.settle(req.getPassed(), req.getTime());
        
        // Handle client events for achievements
        session.getPlayer().getAchievementManager().handleClientEvents(req.getEvents());
        
        // Build response
        var rsp = ActivityTrekkerVersusSettleResp.newInstance()
                .setShow(activity.getShowProto());
        
        // Encode and send
        return session.encodeMsg(NetMsgId.activity_trekker_versus_settle_succeed_ack, rsp);
    }

}
