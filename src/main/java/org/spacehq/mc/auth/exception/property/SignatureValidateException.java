// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.exception.property;

public class SignatureValidateException extends PropertyException
{
    private static final long serialVersionUID = 1L;
    
    public SignatureValidateException() {
    }
    
    public SignatureValidateException(final String message) {
        super(message);
    }
    
    public SignatureValidateException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public SignatureValidateException(final Throwable cause) {
        super(cause);
    }
}
