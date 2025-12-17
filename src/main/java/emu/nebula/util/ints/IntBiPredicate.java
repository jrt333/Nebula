package emu.nebula.util.ints;

import java.util.function.BiPredicate;

public interface IntBiPredicate extends BiPredicate<Integer, Integer>{

    @Deprecated
    @Override
    default boolean test(Integer t, Integer u) {
        return test(t, u);
    }
    
    public boolean test(int t, int u);

}
