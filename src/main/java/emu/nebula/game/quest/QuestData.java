package emu.nebula.game.quest;

public interface QuestData {
    
    /**
     * Quest id, used by the client
     */
    public int getId();
    
    /**
     * This is the computed key for the quest in hashmaps
     */
    public default long getQuestKey() {
        return ((long) this.getQuestType() << 32) + this.getId();
    }
    
    public int getQuestType();
    
    public int getCompleteCond();
    
    public int[] getCompleteCondParams();
    
}
