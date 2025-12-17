package emu.nebula.game.achievement;

import java.util.List;

import emu.nebula.data.resources.AchievementDef;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

import lombok.Getter;

// Because achievements in the data files do not have params, we will hardcode them here
public class AchievementHelper {
    // Cache
    private static IntSet incrementalAchievementSet = new IntOpenHashSet();
    
    @Getter
    private static Int2ObjectMap<List<AchievementDef>> cache = new Int2ObjectOpenHashMap<>();
    
    public static List<AchievementDef> getAchievementsByCondition(int condition) {
        return cache.get(condition);
    }
    
    //
    
    public static boolean isIncrementalAchievement(int condition) {
        return incrementalAchievementSet.contains(condition);
    }
    
    // Fix params
    
    public static void init() {
        // Cache total achievements
        for (var condition : AchievementCondition.values()) {
            if (condition.name().endsWith("Total") || condition.name().endsWith("Times")) {
                incrementalAchievementSet.add(condition.getValue());
            }
        }
        
        incrementalAchievementSet.remove(AchievementCondition.AchievementTotal.getValue());
        
        incrementalAchievementSet.add(AchievementCondition.ItemsAdd.getValue());
        incrementalAchievementSet.add(AchievementCondition.ItemsDeplete.getValue());
        
        incrementalAchievementSet.add(AchievementCondition.TowerItemsGet.getValue());
        incrementalAchievementSet.add(AchievementCondition.TowerEnterRoom.getValue());
    }
}
