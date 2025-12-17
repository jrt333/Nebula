package emu.nebula.game.achievement;

import emu.nebula.util.ints.IntBiPredicate;

import lombok.Getter;

@Getter
public enum AchievementParamCond {
    EQUALS              (0, (param, value) -> value == param),
    ANY                 (1, (param, value) -> true),
    NOT_EQUALS          (2, (param, value) -> value != param),
    GREATER_THAN        (3, (param, value) -> value > param),
    GREATER_THAN_OR_EQ  (4, (param, value) -> value >= param),
    LESS_THAN           (5, (param, value) -> value < param),
    LESS_THAN_OR_EQ     (6, (param, value) -> value <= param);
    
    private final int value;
    private final IntBiPredicate operation;
    
    // Static cache
    private static AchievementParamCond[] CACHE;
    
    static {
        CACHE = new AchievementParamCond[AchievementParamCond.values().length];
        for (AchievementParamCond type : AchievementParamCond.values()) {
            CACHE[type.getValue()] = type;
        }
    }
    
    private AchievementParamCond(int value, IntBiPredicate operation) {
        this.value = value;
        this.operation = operation;
    }
    
    public boolean test(int param, int value) {
        return this.getOperation().test(param, value);
    }
    
    public static AchievementParamCond getByValue(int value) {
        try {
            return CACHE[value];
        } catch (Exception e) {
            return AchievementParamCond.EQUALS;
        }
    }
}
