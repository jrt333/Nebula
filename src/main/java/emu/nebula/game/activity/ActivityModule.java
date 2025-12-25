package emu.nebula.game.activity;

import emu.nebula.game.GameContext;
import emu.nebula.game.GameContextModule;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;

@Getter
public class ActivityModule extends GameContextModule {
    private IntList activities;

    public ActivityModule(GameContext context) {
        super(context);
        this.activities = new IntArrayList();
        
        // Hardcode these activities for now
        // TODO make an activity json file to read activity ids from
        
        // Trial activities
        this.activities.add(700105);
        this.activities.add(700106);
        this.activities.add(700107);
        this.activities.add(700108);

        // Tower defense activity
        this.activities.add(102001);
        
        // Beyond the dream
        //this.activities.add(1010201);
        //this.activities.add(1010203);
        //this.activities.add(1010204);
        
        // Christmas 2025
        this.activities.add(2010101);
        this.activities.add(2010103);
        this.activities.add(2010104);
        
        // Christmas 2025 login events
        this.activities.add(301011);
        this.activities.add(301012);
        
        // Trekker versus
        this.activities.add(600001);
    }
    
}
