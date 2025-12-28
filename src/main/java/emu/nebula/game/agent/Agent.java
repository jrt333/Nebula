package emu.nebula.game.agent;

import dev.morphia.annotations.Entity;
import emu.nebula.Nebula;
import emu.nebula.data.resources.AgentDef;
import emu.nebula.proto.Public.AgentInfo;
import lombok.Getter;

@Getter
@Entity(useDiscriminator = false)
public class Agent {
    private int id;
    private int duration;   // Minutes
    private long start;     // Seconds
    private int[] charIds;
    
    private long end;       // Milliseconds
    
    @Deprecated // Morphia only
    public Agent() {
        
    }
    
    public Agent(AgentDef data, int duration, int[] charIds) {
        this.id = data.getId();
        this.start = Nebula.getCurrentServerTime();
        this.duration = duration;
        this.charIds = charIds;
        
        this.end = (this.start + (duration * 60)) * 1000;
    }
    
    public boolean isCompleted() {
        return System.currentTimeMillis() >= this.getEnd();
    }
    
    // Proto
    
    public AgentInfo toProto() {
        var proto = AgentInfo.newInstance()
                .setId(this.getId())
                .setProcessTime(this.getDuration())
                .setStartTime(this.getStart())
                .addAllCharIds(this.getCharIds());
        
        return proto;
    }
}
