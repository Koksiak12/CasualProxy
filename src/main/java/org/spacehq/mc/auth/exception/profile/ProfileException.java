// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.exception.profile;

public class ProfileException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public ProfileException() {
    }
    
    public ProfileException(final String message) {
        super(message);
    }
    
    public ProfileException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ProfileException(final Throwable cause) {
        super(cause);
    }
}
