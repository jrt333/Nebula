package emu.nebula.data.resources;

import emu.nebula.data.BaseDef;
import emu.nebula.data.ResourceType;
import emu.nebula.util.Utils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;

@Getter
@ResourceType(name = "SubNoteSkillDropGroup.json")
public class SubNoteSkillDropGroupDef extends BaseDef {
    private int Id;
    private int GroupId;
    private int SubNoteSkillId;
    
    private static Int2ObjectMap<IntList> GROUPS = new Int2ObjectOpenHashMap<>();
    
    @Override
    public int getId() {
        return Id;
    }
    
    @Override
    public void onLoad() {
        var packageList = GROUPS.computeIfAbsent(this.GroupId, i -> new IntArrayList());
        packageList.add(this.SubNoteSkillId);
    }
    
    public static int getRandomDrop(int groupId) {
        var dropList = GROUPS.get(groupId);
        
        if (dropList == null) {
            return 0;
        }
        
        return Utils.randomElement(dropList);
    }

    public static IntList getGroup(int groupId) {
        return GROUPS.get(groupId);
    }
}
