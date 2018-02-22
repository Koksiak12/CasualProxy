// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.attribute;

import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.ModifierOperation;
import java.util.UUID;
import org.spacehq.mc.protocol.data.game.values.entity.ModifierType;

public class AttributeModifier
{
    private ModifierType type;
    private UUID uuid;
    private double amount;
    private ModifierOperation operation;
    
    public AttributeModifier(final ModifierType type, final double amount, final ModifierOperation operation) {
        this.type = type;
        this.uuid = MagicValues.value(UUID.class, type);
        this.amount = amount;
        this.operation = operation;
    }
    
    public AttributeModifier(final UUID uuid, final double amount, final ModifierOperation operation) {
        this.type = MagicValues.key(ModifierType.class, uuid);
        this.uuid = uuid;
        this.amount = amount;
        this.operation = operation;
    }
    
    public ModifierType getType() {
        return this.type;
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    public ModifierOperation getOperation() {
        return this.operation;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final AttributeModifier that = (AttributeModifier)o;
        return Double.compare(that.amount, this.amount) == 0 && this.operation == that.operation && this.type == that.type;
    }
    
    @Override
    public int hashCode() {
        int result = this.type.hashCode();
        final long temp = Double.doubleToLongBits(this.amount);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        result = 31 * result + this.operation.hashCode();
        return result;
    }
}
