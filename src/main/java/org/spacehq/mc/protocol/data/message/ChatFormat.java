// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.message;

public enum ChatFormat
{
    BOLD, 
    UNDERLINED, 
    STRIKETHROUGH, 
    ITALIC, 
    OBFUSCATED;
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
    
    public static ChatFormat byName(String name) {
        name = name.toLowerCase();
        for (final ChatFormat format : values()) {
            if (format.toString().equals(name)) {
                return format;
            }
        }
        return null;
    }
}
