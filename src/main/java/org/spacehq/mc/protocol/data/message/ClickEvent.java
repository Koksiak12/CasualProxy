// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.message;

public class ClickEvent implements Cloneable
{
    private ClickAction action;
    private String value;
    
    public ClickEvent(final ClickAction action, final String value) {
        this.action = action;
        this.value = value;
    }
    
    public ClickAction getAction() {
        return this.action;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public ClickEvent clone() {
        return new ClickEvent(this.action, this.value);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ClickEvent that = (ClickEvent)o;
        return this.action == that.action && this.value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        int result = this.action.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }
}
