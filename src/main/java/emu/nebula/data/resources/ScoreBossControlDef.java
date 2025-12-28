package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import emu.nebula.util.Utils;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import lombok.Getter;

@Getter
@ResourceType(name = "ScoreBossControl.json")
public class ScoreBossControlDef extends BaseDef {
    private int Id;
    private String StartTime;
    private String EndTime;
    private IntOpenHashSet LevelGroup;
    
    private transient long startDate;
    private transient long endDate;
    
    @Override
    public int getId() {
        return Id;
    }
    
    @Override
    public void onLoad() {
        this.startDate = Utils.dateToMilliseconds(this.StartTime) / 1000;
        this.endDate = Utils.dateToMilliseconds(this.EndTime) / 1000;
    }
}
