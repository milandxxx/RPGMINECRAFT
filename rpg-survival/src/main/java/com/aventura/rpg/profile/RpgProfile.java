package com.aventura.rpg.profile;

import net.minecraft.nbt.NbtCompound;

public final class RpgProfile {
    public static final int MAX_LEVEL = 50;
    private static final String ROOT_KEY = "aventura_rpg";
    private int level = 1, experience, unspentPoints, strength, intelligence, vitality, mana = 20;
    private RpgClass rpgClass = RpgClass.ADVENTURER;

    public int level() { return level; }
    public int experience() { return experience; }
    public int unspentPoints() { return unspentPoints; }
    public int strength() { return strength + rpgClass.strengthBonus(); }
    public int intelligence() { return intelligence + rpgClass.intelligenceBonus(); }
    public int vitality() { return vitality + rpgClass.vitalityBonus(); }
    public int mana() { return mana; }
    public int maximumMana() { return 20 + intelligence() * 5 + rpgClass.manaBonus(); }
    public RpgClass rpgClass() { return rpgClass; }
    public int experienceToNextLevel() { return 100 + (level - 1) * 25; }

    public int addExperience(int amount) {
        if (amount <= 0 || level >= MAX_LEVEL) return 0;
        experience += amount;
        int gainedLevels = 0;
        while (level < MAX_LEVEL && experience >= experienceToNextLevel()) {
            experience -= experienceToNextLevel(); level++; unspentPoints += 3; mana = maximumMana(); gainedLevels++;
        }
        if (level >= MAX_LEVEL) experience = 0;
        return gainedLevels;
    }

    public boolean chooseClass(RpgClass selectedClass) {
        if (selectedClass == RpgClass.ADVENTURER || rpgClass != RpgClass.ADVENTURER) return false;
        rpgClass = selectedClass; mana = maximumMana(); return true;
    }

    public boolean spendPoint(Attribute attribute) {
        if (unspentPoints <= 0) return false;
        switch (attribute) { case STRENGTH -> strength++; case INTELLIGENCE -> intelligence++; case VITALITY -> vitality++; }
        unspentPoints--; mana = Math.min(mana, maximumMana()); return true;
    }

    public void restoreMana(int amount) { mana = Math.min(maximumMana(), mana + Math.max(0, amount)); }

    public void writeNbt(NbtCompound ownerTag) {
        NbtCompound tag = new NbtCompound();
        tag.putInt("level", level); tag.putInt("experience", experience); tag.putInt("points", unspentPoints);
        tag.putInt("strength", strength); tag.putInt("intelligence", intelligence); tag.putInt("vitality", vitality);
        tag.putInt("mana", mana); tag.putString("class", rpgClass.name()); ownerTag.put(ROOT_KEY, tag);
    }

    public void readNbt(NbtCompound ownerTag) {
        if (!ownerTag.contains(ROOT_KEY)) return;
        NbtCompound tag = ownerTag.getCompound(ROOT_KEY);
        level = Math.clamp(tag.getInt("level"), 1, MAX_LEVEL); experience = Math.max(0, tag.getInt("experience"));
        unspentPoints = Math.max(0, tag.getInt("points")); strength = Math.max(0, tag.getInt("strength"));
        intelligence = Math.max(0, tag.getInt("intelligence")); vitality = Math.max(0, tag.getInt("vitality")); mana = Math.max(0, tag.getInt("mana"));
        try { rpgClass = RpgClass.valueOf(tag.getString("class")); } catch (IllegalArgumentException exception) { rpgClass = RpgClass.ADVENTURER; }
        mana = Math.min(mana, maximumMana());
    }

    public enum Attribute { STRENGTH, INTELLIGENCE, VITALITY }
}
