// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.exception.property;

public class PropertyDeserializeException extends PropertyException
{
    private static final long serialVersionUID = 1L;
    
    public PropertyDeserializeException() {
    }
    
    public PropertyDeserializeException(final String message) {
        super(message);
    }
    
    public PropertyDeserializeException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public PropertyDeserializeException(final Throwable cause) {
        super(cause);
    }
}
