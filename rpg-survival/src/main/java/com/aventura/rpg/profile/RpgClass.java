package com.aventura.rpg.profile;

public enum RpgClass {
    ADVENTURER("Aventurero", 0, 0, 0, 0), WARRIOR("Guerrero", 4, 0, 3, 0),
    MAGE("Mago", 0, 5, 0, 30), ARCHER("Arquero", 2, 1, 1, 10), ROGUE("Picaro", 3, 1, 0, 10);

    private final String displayName;
    private final int strengthBonus, intelligenceBonus, vitalityBonus, manaBonus;

    RpgClass(String displayName, int strengthBonus, int intelligenceBonus, int vitalityBonus, int manaBonus) {
        this.displayName = displayName; this.strengthBonus = strengthBonus; this.intelligenceBonus = intelligenceBonus;
        this.vitalityBonus = vitalityBonus; this.manaBonus = manaBonus;
    }
    public String displayName() { return displayName; }
    public int strengthBonus() { return strengthBonus; }
    public int intelligenceBonus() { return intelligenceBonus; }
    public int vitalityBonus() { return vitalityBonus; }
    public int manaBonus() { return manaBonus; }
}
