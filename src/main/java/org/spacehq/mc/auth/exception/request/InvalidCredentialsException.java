// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.exception.request;

public class InvalidCredentialsException extends RequestException
{
    private static final long serialVersionUID = 1L;
    
    public InvalidCredentialsException() {
    }
    
    public InvalidCredentialsException(final String message) {
        super(message);
    }
    
    public InvalidCredentialsException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public InvalidCredentialsException(final Throwable cause) {
        super(cause);
    }
}
