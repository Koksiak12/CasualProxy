// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.message;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.Arrays;

public class TranslationMessage extends Message
{
    private String translationKey;
    private Message[] translationParams;
    
    public TranslationMessage(final String translationKey, final Message... translationParams) {
        this.translationKey = translationKey;
        this.translationParams = translationParams;
        this.translationParams = this.getTranslationParams();
        for (final Message param : this.translationParams) {
            param.getStyle().setParent(this.getStyle());
        }
    }
    
    public String getTranslationKey() {
        return this.translationKey;
    }
    
    public Message[] getTranslationParams() {
        final Message[] copy = Arrays.copyOf(this.translationParams, this.translationParams.length);
        for (int index = 0; index < copy.length; ++index) {
            copy[index] = copy[index].clone();
        }
        return copy;
    }
    
    @Override
    public Message setStyle(final MessageStyle style) {
        super.setStyle(style);
        for (final Message param : this.translationParams) {
            param.getStyle().setParent(this.getStyle());
        }
        return this;
    }
    
    @Override
    public String getText() {
        return this.translationKey;
    }
    
    @Override
    public TranslationMessage clone() {
        return (TranslationMessage)new TranslationMessage(this.getTranslationKey(), this.getTranslationParams()).setStyle(this.getStyle().clone()).setExtra(this.getExtra());
    }
    
    @Override
    public JsonElement toJson() {
        final JsonElement e = super.toJson();
        if (e.isJsonObject()) {
            final JsonObject json = e.getAsJsonObject();
            json.addProperty("translate", this.translationKey);
            final JsonArray params = new JsonArray();
            for (final Message param : this.translationParams) {
                params.add(param.toJson());
            }
            json.add("with", params);
            return json;
        }
        return e;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final TranslationMessage that = (TranslationMessage)o;
        return this.translationKey.equals(that.translationKey) && Arrays.equals(this.translationParams, that.translationParams);
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.translationKey.hashCode();
        result = 31 * result + Arrays.hashCode(this.translationParams);
        return result;
    }
}
