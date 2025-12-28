package emu.nebula.game.friends;

import emu.nebula.game.player.PlayerChangeInfo;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import lombok.Getter;

@Getter
public class FriendRecvEnergyResult {
    private PlayerChangeInfo change;
    private LongList uids;
    private int energy;
    
    public FriendRecvEnergyResult() {
        this.change = new PlayerChangeInfo();
        this.uids = new LongArrayList();
    }

    public void addEnergy(int amount) {
        this.energy += amount;
    }
}
