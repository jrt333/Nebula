package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import emu.nebula.game.inventory.ItemParamMap;
import lombok.Getter;

@Getter
@ResourceType(name = "WorldClass.json")
public class WorldClassDef extends BaseDef {
    private int Id;
    private int Exp;
    private String Reward;
    
    private transient ItemParamMap rewards;
    
    @Override
    public int getId() {
        return Id;
    }
    
    @Override
    public void onLoad() {
        if (this.Reward != null) {
            this.rewards = ItemParamMap.fromJsonString(this.Reward);
        } else {
            this.rewards = new ItemParamMap();
        }
    }
}
