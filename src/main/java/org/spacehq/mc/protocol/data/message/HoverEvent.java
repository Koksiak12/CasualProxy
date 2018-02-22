// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.message;

public class HoverEvent implements Cloneable
{
    private HoverAction action;
    private Message value;
    
    public HoverEvent(final HoverAction action, final Message value) {
        this.action = action;
        this.value = value;
    }
    
    public HoverAction getAction() {
        return this.action;
    }
    
    public Message getValue() {
        return this.value;
    }
    
    public HoverEvent clone() {
        return new HoverEvent(this.action, this.value.clone());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final HoverEvent that = (HoverEvent)o;
        return this.action == that.action && this.value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        int result = this.action.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }
}
