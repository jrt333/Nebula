package emu.nebula.data.custom;

import emu.nebula.data.BaseDef;
import emu.nebula.data.GameData;
import emu.nebula.data.ResourceType;
import emu.nebula.data.ResourceType.LoadPriority;
import lombok.Getter;

@Getter
@ResourceType(name = "AchievementParam.json", useInternal = true, loadPriority = LoadPriority.LOW)
public class AchievementParamDef extends BaseDef {
    private int Id;
    
    private int Param1;
    private int ParamCond1;
    private int Param2;
    private int ParamCond2;
    
    @Override
    public int getId() {
        return Id;
    }

    @Override
    public void onLoad() {
        var achievement = GameData.getAchievementDataTable().get(this.Id);
        if (achievement == null) {
            return;
        }
        
        achievement.setParams(this);
    }
}
