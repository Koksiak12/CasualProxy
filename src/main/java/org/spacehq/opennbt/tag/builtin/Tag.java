// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.tag.builtin;

import java.lang.reflect.Array;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;

public abstract class Tag implements Cloneable
{
    private String name;
    
    public Tag(final String name) {
        this.name = name;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public abstract Object getValue();
    
    public abstract void read(final DataInputStream p0) throws IOException;
    
    public abstract void write(final DataOutputStream p0) throws IOException;
    
    public abstract Tag clone();
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Tag)) {
            return false;
        }
        final Tag tag = (Tag)obj;
        if (!this.getName().equals(tag.getName())) {
            return false;
        }
        if (this.getValue() == null) {
            return tag.getValue() == null;
        }
        if (tag.getValue() == null) {
            return false;
        }
        if (!this.getValue().getClass().isArray() || !tag.getValue().getClass().isArray()) {
            return this.getValue().equals(tag.getValue());
        }
        final int length = Array.getLength(this.getValue());
        if (Array.getLength(tag.getValue()) != length) {
            return false;
        }
        for (int index = 0; index < length; ++index) {
            final Object o = Array.get(this.getValue(), index);
            final Object other = Array.get(tag.getValue(), index);
            if ((o == null && other != null) || (o != null && !o.equals(other))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        final String name = (this.getName() != null && !this.getName().equals("")) ? ("(" + this.getName() + ")") : "";
        String value = "";
        if (this.getValue() != null) {
            value = this.getValue().toString();
            if (this.getValue().getClass().isArray()) {
                final StringBuilder build = new StringBuilder();
                build.append("[");
                for (int index = 0; index < Array.getLength(this.getValue()); ++index) {
                    if (index > 0) {
                        build.append(", ");
                    }
                    build.append(Array.get(this.getValue(), index));
                }
                build.append("]");
                value = build.toString();
            }
        }
        return this.getClass().getSimpleName() + name + " { " + value + " }";
    }
}
