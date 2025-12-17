package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import emu.nebula.data.ResourceType.LoadPriority;

import lombok.Getter;

@Getter
@ResourceType(name = "MainScreenCG.json", loadPriority = LoadPriority.LOW)
public class MainScreenCGDef extends BaseDef {
    private int Id;
    private boolean IsShown;
    
    @Override
    public int getId() {
        return Id;
    }
}
