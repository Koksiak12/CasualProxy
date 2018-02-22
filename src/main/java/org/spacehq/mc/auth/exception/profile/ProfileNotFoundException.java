// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.exception.profile;

public class ProfileNotFoundException extends ProfileException
{
    private static final long serialVersionUID = 1L;
    
    public ProfileNotFoundException() {
    }
    
    public ProfileNotFoundException(final String message) {
        super(message);
    }
    
    public ProfileNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ProfileNotFoundException(final Throwable cause) {
        super(cause);
    }
}
