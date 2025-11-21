package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;

import lombok.Getter;

@Getter
@ResourceType(name = "AffinityGift.json")
public class AffinityGiftDef extends BaseDef {
    private int Id;
    private int BaseAffinity;
    private int[] Tags;
    
    @Override
    public int getId() {
        return Id;
    }
    
}
