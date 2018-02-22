// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.attribute;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import org.spacehq.mc.protocol.data.game.values.entity.AttributeType;

public class Attribute
{
    private AttributeType type;
    private double value;
    private List<AttributeModifier> modifiers;
    
    public Attribute(final AttributeType type) {
        this(type, type.getDefault());
    }
    
    public Attribute(final AttributeType type, final double value) {
        this(type, value, new ArrayList<AttributeModifier>());
    }
    
    public Attribute(final AttributeType type, final double value, final List<AttributeModifier> modifiers) {
        this.type = type;
        this.value = value;
        this.modifiers = modifiers;
    }
    
    public AttributeType getType() {
        return this.type;
    }
    
    public double getValue() {
        return this.value;
    }
    
    public List<AttributeModifier> getModifiers() {
        return new ArrayList<AttributeModifier>(this.modifiers);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Attribute attribute = (Attribute)o;
        return Double.compare(attribute.value, this.value) == 0 && this.modifiers.equals(attribute.modifiers) && this.type == attribute.type;
    }
    
    @Override
    public int hashCode() {
        int result = this.type.hashCode();
        final long temp = Double.doubleToLongBits(this.value);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        result = 31 * result + this.modifiers.hashCode();
        return result;
    }
}
