// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.status;

public class VersionInfo
{
    private String name;
    private int protocol;
    
    public VersionInfo(final String name, final int protocol) {
        this.name = name;
        this.protocol = protocol;
    }
    
    public String getVersionName() {
        return this.name;
    }
    
    public int getProtocolVersion() {
        return this.protocol;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final VersionInfo that = (VersionInfo)o;
        return this.protocol == that.protocol && this.name.equals(that.name);
    }
    
    @Override
    public int hashCode() {
        int result = this.name.hashCode();
        result = 31 * result + this.protocol;
        return result;
    }
}
