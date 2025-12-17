package emu.nebula.util.ints;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class String2IntMap extends Object2IntOpenHashMap<String> {
    private static final long serialVersionUID = -7301945177198000055L;

    public int get(String key) {
        return super.getInt(key);
    }

    public FastEntrySet<String> entries() {
        return super.object2IntEntrySet();
    }
}
