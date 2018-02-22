// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.game.values.world;

public class CustomSound implements Sound
{
    private String name;
    
    public CustomSound(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final CustomSound that = (CustomSound)o;
        return this.name.equals(that.name);
    }
    
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
