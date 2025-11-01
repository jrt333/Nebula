package emu.nebula.server.handlers;

import emu.nebula.net.NetHandler;
import emu.nebula.net.NetMsgId;
import emu.nebula.proto.EnergyBuy.EnergyBuyResp;
import emu.nebula.net.HandlerId;
import emu.nebula.net.GameSession;

@HandlerId(NetMsgId.energy_buy_req)
public class HandlerEnergyBuyReq extends NetHandler {

    @Override
    public byte[] handle(GameSession session, byte[] message) throws Exception {
        // Buy energy
        var change = session.getPlayer().getInventory().buyEnergy();
        
        if (change == null) {
            return session.encodeMsg(NetMsgId.energy_buy_failed_ack);
        }
        
        // Build response
        var rsp = EnergyBuyResp.newInstance()
                .setChange(change.toProto())
                .setCount(0); // TODO max energy buy count per day
        
        // Encode and send
        return session.encodeMsg(NetMsgId.energy_buy_succeed_ack, rsp);
    }

}
