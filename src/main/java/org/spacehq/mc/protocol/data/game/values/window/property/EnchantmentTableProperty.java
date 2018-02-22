// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.window.property;

public enum EnchantmentTableProperty implements WindowProperty
{
    LEVEL_SLOT_1, 
    LEVEL_SLOT_2, 
    LEVEL_SLOT_3, 
    XP_SEED, 
    ENCHANTMENT_SLOT_1, 
    ENCHANTMENT_SLOT_2, 
    ENCHANTMENT_SLOT_3;
    
    public static int getEnchantment(final int type, final int level) {
        return type | level << 8;
    }
    
    public static int getEnchantmentType(final int enchantmentInfo) {
        return enchantmentInfo & 0xFF;
    }
    
    public static int getEnchantmentLevel(final int enchantmentInfo) {
        return enchantmentInfo >> 8;
    }
}
