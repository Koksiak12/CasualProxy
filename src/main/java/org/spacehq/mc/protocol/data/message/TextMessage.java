// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.message;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonElement;

public class TextMessage extends Message
{
    private String text;
    
    public TextMessage(final String text) {
        this.text = text;
    }
    
    @Override
    public String getText() {
        return this.text;
    }
    
    @Override
    public TextMessage clone() {
        return (TextMessage)new TextMessage(this.getText()).setStyle(this.getStyle().clone()).setExtra(this.getExtra());
    }
    
    @Override
    public JsonElement toJson() {
        if (this.getStyle().isDefault() && this.getExtra().isEmpty()) {
            return new JsonPrimitive(this.text);
        }
        final JsonElement e = super.toJson();
        if (e.isJsonObject()) {
            final JsonObject json = e.getAsJsonObject();
            json.addProperty("text", this.text);
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
        final TextMessage that = (TextMessage)o;
        return this.text.equals(that.text);
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.text.hashCode();
        return result;
    }
}
