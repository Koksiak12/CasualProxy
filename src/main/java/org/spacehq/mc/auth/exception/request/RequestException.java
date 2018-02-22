// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.exception.request;

public class RequestException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public RequestException() {
    }
    
    public RequestException(final String message) {
        super(message);
    }
    
    public RequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public RequestException(final Throwable cause) {
        super(cause);
    }
}
