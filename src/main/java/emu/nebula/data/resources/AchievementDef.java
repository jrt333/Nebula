package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import emu.nebula.data.custom.AchievementParamDef;
import emu.nebula.game.achievement.AchievementHelper;
import emu.nebula.game.achievement.AchievementParamCond;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;

@Getter
@ResourceType(name = "Achievement.json")
public class AchievementDef extends BaseDef {
    private int Id;
    private int Type;
    private int CompleteCond;
    private int AimNumShow;
    private int[] Prerequisites;
    
    // Reward
    private int Tid1;
    private int Qty1;
    
    // Custom params
    private transient int param1;
    private transient AchievementParamCond paramCond1;
    private transient int param2;
    private transient AchievementParamCond paramCond2;
    
    @Override
    public int getId() {
        return Id;
    }

    @Deprecated
    public void setParams(int param1, int param2) {
        this.param1 = param1;
        this.param2 = param2;
    }

    public void setParams(AchievementParamDef params) {
        this.param1 = params.getParam1();
        this.param2 = params.getParam2();
        
        this.paramCond1 = AchievementParamCond.getByValue(params.getParamCond1());
        this.paramCond2 = AchievementParamCond.getByValue(params.getParamCond2());
    }

    /**
     * Checks if this achievement requires params to match
     */
    public boolean matchParam1(int value) {
        if (this.paramCond1 == null) {
            this.paramCond1 = AchievementParamCond.EQUALS;
        }
        
        return this.paramCond1.test(this.param1, value);
    }
    
    /**
     * Checks if this achievement requires params to match
     */
    public boolean matchParam2(int value) {
        if (this.paramCond2 == null) {
            this.paramCond2 = AchievementParamCond.EQUALS;
        }
        
        return this.paramCond2.test(this.param2, value);
    }

    @Override
    public void onLoad() {
        // Add to cached achievement list
        var list = AchievementHelper.getCache().computeIfAbsent(this.CompleteCond, i -> new ObjectArrayList<>());
        list.add(this);
    }
}
