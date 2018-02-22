// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.exception.profile;

public class ProfileLookupException extends ProfileException
{
    private static final long serialVersionUID = 1L;
    
    public ProfileLookupException() {
    }
    
    public ProfileLookupException(final String message) {
        super(message);
    }
    
    public ProfileLookupException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ProfileLookupException(final Throwable cause) {
        super(cause);
    }
}
