// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game;

import org.spacehq.opennbt.tag.builtin.CompoundTag;

public class ItemStack
{
    private int id;
    private int amount;
    private int data;
    private CompoundTag nbt;
    
    public ItemStack(final int id) {
        this(id, 1);
    }
    
    public ItemStack(final int id, final int amount) {
        this(id, amount, 0);
    }
    
    public ItemStack(final int id, final int amount, final int data) {
        this(id, amount, data, null);
    }
    
    public ItemStack(final int id, final int amount, final int data, final CompoundTag nbt) {
        this.id = id;
        this.amount = amount;
        this.data = data;
        this.nbt = nbt;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public int getData() {
        return this.data;
    }
    
    public CompoundTag getNBT() {
        return this.nbt;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ItemStack itemStack = (ItemStack)o;
        if (this.amount != itemStack.amount) {
            return false;
        }
        if (this.data != itemStack.data) {
            return false;
        }
        if (this.id != itemStack.id) {
            return false;
        }
        if (this.nbt != null) {
            if (this.nbt.equals(itemStack.nbt)) {
                return true;
            }
        }
        else if (itemStack.nbt == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.id;
        result = 31 * result + this.amount;
        result = 31 * result + this.data;
        result = 31 * result + ((this.nbt != null) ? this.nbt.hashCode() : 0);
        return result;
    }
}
