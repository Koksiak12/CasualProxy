// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.data.message;

import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public abstract class Message implements Cloneable
{
    private MessageStyle style;
    private List<Message> extra;
    
    public Message() {
        this.style = new MessageStyle();
        this.extra = new ArrayList<Message>();
    }
    
    public abstract String getText();
    
    public String getFullText() {
        final StringBuilder build = new StringBuilder(this.getText());
        for (final Message msg : this.extra) {
            build.append(msg.getFullText());
        }
        return build.toString();
    }
    
    public MessageStyle getStyle() {
        return this.style;
    }
    
    public List<Message> getExtra() {
        return new ArrayList<Message>(this.extra);
    }
    
    public Message setStyle(final MessageStyle style) {
        this.style = style;
        return this;
    }
    
    public Message setExtra(final List<Message> extra) {
        this.extra = new ArrayList<Message>(extra);
        for (final Message msg : this.extra) {
            msg.getStyle().setParent(this.style);
        }
        return this;
    }
    
    public Message addExtra(final Message message) {
        this.extra.add(message);
        message.getStyle().setParent(this.style);
        return this;
    }
    
    public Message removeExtra(final Message message) {
        this.extra.remove(message);
        message.getStyle().setParent(null);
        return this;
    }
    
    public Message clearExtra() {
        for (final Message msg : this.extra) {
            msg.getStyle().setParent(null);
        }
        this.extra.clear();
        return this;
    }
    
    @Override
    public String toString() {
        return this.getFullText();
    }
    
    public abstract Message clone();
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Message message = (Message)o;
        return this.extra.equals(message.extra) && this.style.equals(message.style);
    }
    
    @Override
    public int hashCode() {
        int result = this.style.hashCode();
        result = 31 * result + this.extra.hashCode();
        return result;
    }
    
    public String toJsonString() {
        return this.toJson().toString();
    }
    
    public JsonElement toJson() {
        final JsonObject json = new JsonObject();
        json.addProperty("color", this.style.getColor().toString());
        for (final ChatFormat format : this.style.getFormats()) {
            json.addProperty(format.toString(), Boolean.valueOf(true));
        }
        if (this.style.getClickEvent() != null) {
            final JsonObject click = new JsonObject();
            click.addProperty("action", this.style.getClickEvent().getAction().toString());
            click.addProperty("value", this.style.getClickEvent().getValue());
            json.add("clickEvent", click);
        }
        if (this.style.getHoverEvent() != null) {
            final JsonObject hover = new JsonObject();
            hover.addProperty("action", this.style.getHoverEvent().getAction().toString());
            hover.add("value", this.style.getHoverEvent().getValue().toJson());
            json.add("hoverEvent", hover);
        }
        if (this.style.getInsertion() != null) {
            json.addProperty("insertion", this.style.getInsertion());
        }
        if (this.extra.size() > 0) {
            final JsonArray extra = new JsonArray();
            for (final Message msg : this.extra) {
                extra.add(msg.toJson());
            }
            json.add("extra", extra);
        }
        return json;
    }
    
    public static Message fromString(final String str) {
        try {
            return fromJson(new JsonParser().parse(str));
        }
        catch (Exception e) {
            return new TextMessage(str);
        }
    }
    
    public static Message fromJson(final JsonElement e) {
        if (e.isJsonPrimitive()) {
            return new TextMessage(e.getAsString());
        }
        if (e.isJsonObject()) {
            final JsonObject json = e.getAsJsonObject();
            Message msg = null;
            if (json.has("text")) {
                msg = new TextMessage(json.get("text").getAsString());
            }
            else {
                if (!json.has("translate")) {
                    throw new IllegalArgumentException("Unknown message type in json: " + json.toString());
                }
                Message[] with = new Message[0];
                if (json.has("with")) {
                    final JsonArray withJson = json.get("with").getAsJsonArray();
                    with = new Message[withJson.size()];
                    for (int index = 0; index < withJson.size(); ++index) {
                        final JsonElement el = withJson.get(index);
                        if (el.isJsonPrimitive()) {
                            with[index] = new TextMessage(el.getAsString());
                        }
                        else {
                            with[index] = fromJson(el.getAsJsonObject());
                        }
                    }
                }
                msg = new TranslationMessage(json.get("translate").getAsString(), with);
            }
            final MessageStyle style = new MessageStyle();
            if (json.has("color")) {
                style.setColor(ChatColor.byName(json.get("color").getAsString()));
            }
            for (final ChatFormat format : ChatFormat.values()) {
                if (json.has(format.toString()) && json.get(format.toString()).getAsBoolean()) {
                    style.addFormat(format);
                }
            }
            if (json.has("clickEvent")) {
                final JsonObject click = json.get("clickEvent").getAsJsonObject();
                style.setClickEvent(new ClickEvent(ClickAction.byName(click.get("action").getAsString()), click.get("value").getAsString()));
            }
            if (json.has("hoverEvent")) {
                final JsonObject hover = json.get("hoverEvent").getAsJsonObject();
                style.setHoverEvent(new HoverEvent(HoverAction.byName(hover.get("action").getAsString()), fromJson(hover.get("value"))));
            }
            if (json.has("insertion")) {
                style.setInsertion(json.get("insertion").getAsString());
            }
            msg.setStyle(style);
            if (json.has("extra")) {
                final JsonArray extraJson = json.get("extra").getAsJsonArray();
                final List<Message> extra = new ArrayList<Message>();
                for (int index2 = 0; index2 < extraJson.size(); ++index2) {
                    final JsonElement el2 = extraJson.get(index2);
                    if (el2.isJsonPrimitive()) {
                        extra.add(new TextMessage(el2.getAsString()));
                    }
                    else {
                        extra.add(fromJson(el2.getAsJsonObject()));
                    }
                }
                msg.setExtra(extra);
            }
            return msg;
        }
        throw new IllegalArgumentException("Cannot convert " + e.getClass().getSimpleName() + " to a message.");
    }
}
