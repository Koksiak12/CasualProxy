// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.exception.request;

public class ServiceUnavailableException extends RequestException
{
    private static final long serialVersionUID = 1L;
    
    public ServiceUnavailableException() {
    }
    
    public ServiceUnavailableException(final String message) {
        super(message);
    }
    
    public ServiceUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ServiceUnavailableException(final Throwable cause) {
        super(cause);
    }
}
