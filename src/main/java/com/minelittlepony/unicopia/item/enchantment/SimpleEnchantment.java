package com.minelittlepony.unicopia.item.enchantment;

import com.minelittlepony.unicopia.Unicopia;
import com.minelittlepony.unicopia.entity.Living;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class SimpleEnchantment extends Enchantment {

    private final boolean cursed;

    private final boolean treasure;

    private final boolean allItems;

    private final int maxLevel;

    private final EquipmentSlot[] slots;

    protected SimpleEnchantment(Rarity rarity, EnchantmentTarget target, boolean cursed, int maxLevel, EquipmentSlot... slots) {
        super(rarity, target, slots);
        this.cursed = cursed;
        this.allItems = false;
        this.maxLevel = maxLevel;
        this.slots = slots;
        this.treasure = cursed ? Unicopia.getConfig().makeCursesTreasure.get() : false;
    }

    protected SimpleEnchantment(Rarity rarity, boolean cursed, int maxLevel, EquipmentSlot... slots) {
        super(rarity, EnchantmentTarget.VANISHABLE, slots); // vanishable includes breakable. It's the one that accepts the widest variety of items
        this.cursed = cursed;
        this.allItems = true;
        this.maxLevel = maxLevel;
        this.slots = slots;
        this.treasure = cursed ? Unicopia.getConfig().makeCursesTreasure.get() : false;
    }

    public void onUserTick(Living<?> user, int level) {

    }

    public void onEquipped(Living<?> user) {

    }

    public void onUnequipped(Living<?> user) {

    }

    @Override
    public boolean isAcceptableItem(ItemStack itemStack) {
       return allItems || super.isAcceptableItem(itemStack);
    }

    public EquipmentSlot[] getSlots() {
        return slots;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public boolean isCursed() {
        return cursed;
    }

    @Override
    public boolean isTreasure() {
        return treasure;
    }

    public static class Data {
        public float level;

        public boolean update(int level) {
            if (level == this.level) {
                return false;
            }
            this.level = level;
            return true;
        }
    }
}
