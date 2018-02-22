// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.exception.request;

public class UserMigratedException extends InvalidCredentialsException
{
    private static final long serialVersionUID = 1L;
    
    public UserMigratedException() {
    }
    
    public UserMigratedException(final String message) {
        super(message);
    }
    
    public UserMigratedException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public UserMigratedException(final Throwable cause) {
        super(cause);
    }
}
