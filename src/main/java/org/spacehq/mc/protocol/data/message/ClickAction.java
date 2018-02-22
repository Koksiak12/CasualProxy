// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.message;

public enum ClickAction
{
    RUN_COMMAND, 
    SUGGEST_COMMAND, 
    OPEN_URL, 
    OPEN_FILE;
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
    
    public static ClickAction byName(String name) {
        name = name.toLowerCase();
        for (final ClickAction action : values()) {
            if (action.toString().equals(name)) {
                return action;
            }
        }
        return null;
    }
}
