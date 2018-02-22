// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.exception.property;

public class PropertyException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public PropertyException() {
    }
    
    public PropertyException(final String message) {
        super(message);
    }
    
    public PropertyException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public PropertyException(final Throwable cause) {
        super(cause);
    }
}
