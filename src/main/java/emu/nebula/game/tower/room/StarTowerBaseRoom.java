package emu.nebula.game.tower.room;

import java.util.ArrayList;
import java.util.List;

import emu.nebula.data.resources.StarTowerStageDef;
import emu.nebula.game.tower.StarTowerGame;
import emu.nebula.game.tower.cases.CaseType;
import emu.nebula.game.tower.cases.StarTowerBaseCase;
import emu.nebula.game.tower.cases.StarTowerSyncHPCase;
import emu.nebula.proto.PublicStarTower.InteractEnterReq;
import emu.nebula.proto.PublicStarTower.StarTowerRoomCase;
import emu.nebula.proto.PublicStarTower.StarTowerRoomData;
import emu.nebula.proto.StarTowerApply.StarTowerApplyReq;
import lombok.Getter;
import us.hebi.quickbuf.RepeatedMessage;

@Getter
public class StarTowerBaseRoom {
    // Game
    private transient StarTowerGame game;
    private transient StarTowerStageDef stage;
    
    // Map info
    private int mapId;
    private int mapTableId;
    private String mapParam;
    private int paramId;
    
    // Cases
    private int lastCaseId = 0;
    private List<StarTowerBaseCase> cases;
    
    // Misc
    private boolean hasDoor;
    
    public StarTowerBaseRoom(StarTowerGame game, StarTowerStageDef stage) {
        this.game = game;
        this.stage = stage;
        this.cases = new ArrayList<>();
    }
    
    public int getType() {
        return stage.getRoomType();
    }
    
    public boolean hasDoor() {
        return this.hasDoor;
    }
    
    // Map info
    
    public void setMapInfo(StarTowerApplyReq req) {
        this.mapId = req.getMapId();
        this.mapTableId = req.getMapTableId();
        this.mapParam = req.getMapParam();
        this.paramId = req.getParamId();
    }
    
    public void setMapInfo(InteractEnterReq req) {
        this.mapId = req.getMapId();
        this.mapTableId = req.getMapTableId();
        this.mapParam = req.getMapParam();
        this.paramId = req.getParamId();
    }
    
    // Cases
    
    public int getNextCaseId() {
        return ++this.lastCaseId;
    }
    
    public StarTowerBaseCase getCase(int id) {
        return this.getCases().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public StarTowerBaseCase addCase(StarTowerBaseCase towerCase) {
        return this.addCase(null, towerCase);
    }
    
    public StarTowerBaseCase addCase(RepeatedMessage<StarTowerRoomCase> cases, StarTowerBaseCase towerCase) {
        // Add to cases list
        this.getCases().add(towerCase);
        
        // Set game for tower case
        towerCase.register(this);
        
        // Add case to proto
        if (cases != null) {
            cases.add(towerCase.toProto());
        }
        
        // Check if door case
        if (towerCase.getType() == CaseType.OpenDoor) {
            this.hasDoor = true;
        }
        
        // Complete
        return towerCase;
    }
    
    // Events
    
    public void onEnter() {
        // Create door case
        this.getGame().createExit();
        
        // Create sync hp case
        this.getGame().addCase(new StarTowerSyncHPCase());
    }
    
    // Proto
    
    public emu.nebula.proto.PublicStarTower.StarTowerRoom toProto() {
        var proto = emu.nebula.proto.PublicStarTower.StarTowerRoom.newInstance()
                .setData(this.getDataProto());
        
        for (var towerCase : this.getCases()) {
            proto.addCases(towerCase.toProto());
        }
        
        return proto;
    }
    
    private StarTowerRoomData getDataProto() {
        var proto = StarTowerRoomData.newInstance()
                .setFloor(this.getGame().getFloorCount())
                .setMapId(this.getMapId())
                .setRoomType(this.getType())
                .setMapTableId(this.getMapTableId());
        
        if (this.getMapParam() != null && !this.getMapParam().isEmpty()) {
            proto.setMapParam(this.getMapParam());
        }
        
        if (this.getParamId() != 0) {
            proto.setParamId(this.getParamId());
        }
        
        return proto;
    }
}
