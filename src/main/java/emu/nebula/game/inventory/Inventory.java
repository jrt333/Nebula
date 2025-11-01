package emu.nebula.game.inventory;

import java.util.List;

import emu.nebula.GameConstants;
import emu.nebula.Nebula;
import emu.nebula.data.GameData;
import emu.nebula.game.player.PlayerManager;
import emu.nebula.proto.Public.Item;
import emu.nebula.proto.Public.Res;
import emu.nebula.game.player.Player;
import emu.nebula.game.player.PlayerChangeInfo;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;

@Getter
public class Inventory extends PlayerManager {
    private final Int2ObjectMap<GameResource> resources;
    private final Int2ObjectMap<GameItem> items;
    
    public Inventory(Player player) {
        super(player);
        
        this.resources = new Int2ObjectOpenHashMap<>();
        this.items = new Int2ObjectOpenHashMap<>();
    }
    
    // Resources
    
    public synchronized int getResourceCount(int id) {
        var res = this.resources.get(id);
        return res != null ? res.getCount() : 0;
    }
    
    // Items
    
    public synchronized int getItemCount(int id) {
        var item = this.getItems().get(id);
        return item != null ? item.getCount() : 0;
    }
    
    // Add/Remove items
    
    public PlayerChangeInfo addItem(int id, int count) {
        return this.addItem(id, count, null);
    }
    
    public synchronized PlayerChangeInfo addItem(int id, int count, PlayerChangeInfo change) {
        // Changes
        if (change == null) {
            change = new PlayerChangeInfo();
        }
        
        // Sanity
        if (count == 0) {
            return change;
        }
        
        // Get game data
        var data = GameData.getItemDataTable().get(id);
        if (data == null) {
            return change;
        }
        
        // Set amount
        int amount = count;
        
        // Add item
        switch (data.getItemType()) {
            case Res -> {
                var res = this.resources.get(id);
                int diff = 0;
                
                if (amount > 0) {
                    // Add resource
                    if (res == null) {
                        res = new GameResource(this.getPlayer(), id, amount);
                        this.resources.put(res.getResourceId(), res);
                        
                        diff = amount;
                    } else {
                        diff = res.add(amount);
                    }
                    
                    res.save();
                } else {
                    // Remove resource
                    if (res == null) {
                        break;
                    }
                    
                    diff = res.add(amount);
                    res.save();
                    
                    if (res.getCount() < 0) {
                        this.resources.remove(id);
                    }
                }
                
                if (diff != 0) {
                    var proto = Res.newInstance()
                            .setTid(id)
                            .setQty(diff);
                    
                    change.add(proto);
                }
            }
            case Item -> {
                var item = this.items.get(id);
                int diff = 0;
                
                if (amount > 0) {
                    // Add resource
                    if (item == null) {
                        item = new GameItem(this.getPlayer(), id, amount);
                        this.items.put(item.getItemId(), item);
                        
                        diff = amount;
                    } else {
                        diff = item.add(amount);
                    }
                    
                    item.save();
                } else {
                    // Remove resource
                    if (item == null) {
                        break;
                    }
                    
                    diff = item.add(amount);
                    item.save();
                    
                    if (item.getCount() < 0) {
                        this.resources.remove(id);
                    }
                }
                
                if (diff != 0) {
                    var proto = Item.newInstance()
                            .setTid(id)
                            .setQty(diff);
                    
                    change.add(proto);
                }
            }
            case Disc -> {
                if (amount <= 0) {
                    break;
                }
                
                var disc = getPlayer().getCharacters().addDisc(id);
                
                if (disc != null) {
                    change.add(disc.toProto());
                }
            }
            case Char -> {
                if (amount <= 0) {
                    break;
                }
                
                var character = getPlayer().getCharacters().addCharacter(id);

                if (character != null) {
                    change.add(character.toProto());
                }
            }
            case Energy -> {
                this.getPlayer().addEnergy(amount, change);
            }
            case WorldRankExp -> {
                this.getPlayer().addExp(amount, change);
            }
            default -> {
                // Not implemented
            }
        }
        
        //
        return change;
    }

    @Deprecated
    public synchronized PlayerChangeInfo addItems(List<ItemParam> params, PlayerChangeInfo change) {
        // Changes
        if (change == null) {
            change = new PlayerChangeInfo();
        }
        
        for (ItemParam param : params) {
            this.addItem(param.getId(), param.getCount(), change);
        }
        
        return change;
    }
    
    public synchronized PlayerChangeInfo addItems(ItemParamMap params) {
        return this.addItems(params, null);
    }
    
    public synchronized PlayerChangeInfo addItems(ItemParamMap params, PlayerChangeInfo change) {
        // Changes
        if (change == null) {
            change = new PlayerChangeInfo();
        }
        
        // Sanity
        if (params == null || params.isEmpty()) {
            return change;
        }
        
        // Add items
        for (var param : params.entries()) {
            this.addItem(param.getIntKey(), param.getIntValue(), change);
        }
        
        return change;
    }
    
    public PlayerChangeInfo removeItem(int id, int count) {
        return this.removeItem(id, count, null);
    }
    
    public synchronized PlayerChangeInfo removeItem(int id, int count, PlayerChangeInfo change) {
        if (count > 0) {
            count = -count;
        }
        
        return this.addItem(id, count, change);
    }
    
    public synchronized PlayerChangeInfo removeItems(ItemParamMap params) {
        return this.removeItems(params, null);
    }
    
    public synchronized PlayerChangeInfo removeItems(ItemParamMap params, PlayerChangeInfo change) {
        // Changes
        if (change == null) {
            change = new PlayerChangeInfo();
        }
        
        // Sanity
        if (params == null || params.isEmpty()) {
            return change;
        }
        
        // Remove items
        for (var param : params.entries()) {
            this.removeItem(param.getIntKey(), param.getIntValue(), change);
        }
        
        return change;
    }
    
    /**
     * Checks if the player has enough quanity of this item
     */
    public synchronized boolean verifyItem(int id, int count) {
        // Sanity check
        if (count == 0) {
            return true;
        } else if (count < 0) {
            // Return false if we are trying to verify negative numbers
            return false;
        }
        
        // Get game data
        var data = GameData.getItemDataTable().get(id);
        if (data == null) {
            return false;
        }
        
        boolean result = switch (data.getItemType()) {
            case Res -> {
                yield this.getResourceCount(id) >= count;
            }
            case Item -> {
                yield this.getItemCount(id) >= count;
            }
            case Disc -> {
                yield getPlayer().getCharacters().hasDisc(id);
            }
            case Char -> {
                yield getPlayer().getCharacters().hasCharacter(id);
            }
            default -> {
                // Not implemented
                yield false;
            }
        };
        
        //
        return result;
    }
    
    public synchronized boolean verifyItems(ItemParamMap params) {
        boolean hasItems = true;
        
        for (var param : params.entries()) {
            hasItems = this.verifyItem(param.getIntKey(), param.getIntValue());
            
            if (!hasItems) {
                return hasItems;
            }
        }
        
        return hasItems;
    }
    
    // Utility functions
    
    public PlayerChangeInfo produce(int id, int num) {
        // Get production data
        var data = GameData.getProductionDataTable().get(id);
        if (data == null) {
            return null;
        }
        
        // Get materials
        var materials = data.getMaterials().mulitply(num);
        
        // Verify that we have the materials
        if (!this.verifyItems(materials)) {
            return null;
        }
        
        // Create change info
        var change = new PlayerChangeInfo();
        
        // Remove items
        this.removeItems(materials, change);
        
        // Add produced items
        this.addItem(data.getProductionId(), data.getProductionPerBatch() * num, change);
        
        // Success
        return change.setSuccess(true);
    }
    
    public PlayerChangeInfo buyEnergy() {
        // Create change info
        var change = new PlayerChangeInfo();
        
        // Make sure we have the gems
        if (!this.verifyItem(GameConstants.ENERGY_BUY_ITEM_ID, 30)) {
            return change;
        }
        
        // Remove gems
        this.removeItem(GameConstants.ENERGY_BUY_ITEM_ID, 30, change);
        
        // Add energy
        this.getPlayer().addEnergy(60, change);
        
        // Success
        return change;
    }
    
    public PlayerChangeInfo useItem(int id, int count, PlayerChangeInfo change) {
        // Changes
        if (change == null) {
            change = new PlayerChangeInfo();
        }
        
        // Get item data
        var data = GameData.getItemDataTable().get(id);
        if (data == null || data.getUseParams() == null) {
            return change;
        }
        
        // Make sure we have this item
        if (!this.verifyItem(id, count)) {
            return change;
        }
        
        // Success
        boolean success = false;
        
        // Apply use
        switch (data.getUseAction()) {
            case 2 -> {
                // Add items
                this.addItems(data.getUseParams(), change);
                // Success
                success = true;
            }
            default -> {
                // Not implemented
            }
        }

        // Consume item if successful
        if (success) {
            this.removeItem(id, count, change);
        }
        
        // Success
        return change;
    }
    
    // Database
    
    public void loadFromDatabase() {
        var db = Nebula.getGameDatabase();
        
        db.getObjects(GameItem.class, "playerUid", getPlayerUid()).forEach(item -> {
            // Get data
            var data = GameData.getItemDataTable().get(item.getItemId());
            if (data == null) return;
            
            // Add
            this.items.put(item.getItemId(), item);
        });
        
        db.getObjects(GameResource.class, "playerUid", getPlayerUid()).forEach(res -> {
            // Get data
            var data = GameData.getItemDataTable().get(res.getResourceId());
            if (data == null) return;
            
            // Add
            this.resources.put(res.getResourceId(), res);
        });
    }
}
