// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.packet.status.server;

import java.io.OutputStream;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import org.spacehq.mc.auth.util.Base64;
import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import java.awt.image.BufferedImage;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.data.status.PlayerInfo;
import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.data.status.VersionInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.spacehq.packetlib.io.NetInput;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.packetlib.packet.Packet;

public class StatusResponsePacket implements Packet
{
    private ServerStatusInfo info;
    
    private StatusResponsePacket() {
    }
    
    public StatusResponsePacket(final ServerStatusInfo info) {
        this.info = info;
    }
    
    public ServerStatusInfo getInfo() {
        return this.info;
    }
    
    @Override
    public void read(final NetInput in) throws IOException {
        final JsonObject obj = new Gson().fromJson(in.readString(), JsonObject.class);
        final JsonObject ver = obj.get("version").getAsJsonObject();
        final VersionInfo version = new VersionInfo(ver.get("name").getAsString(), ver.get("protocol").getAsInt());
        final JsonObject plrs = obj.get("players").getAsJsonObject();
        GameProfile[] profiles = new GameProfile[0];
        if (plrs.has("sample")) {
            final JsonArray prof = plrs.get("sample").getAsJsonArray();
            if (prof.size() > 0) {
                profiles = new GameProfile[prof.size()];
                for (int index = 0; index < prof.size(); ++index) {
                    final JsonObject o = prof.get(index).getAsJsonObject();
                    profiles[index] = new GameProfile(o.get("id").getAsString(), o.get("name").getAsString());
                }
            }
        }
        final PlayerInfo players = new PlayerInfo(plrs.get("max").getAsInt(), plrs.get("online").getAsInt(), profiles);
        final JsonElement desc = obj.get("description");
        final Message description = Message.fromJson(desc);
        BufferedImage icon = null;
        if (obj.has("favicon")) {
            icon = this.stringToIcon(obj.get("favicon").getAsString());
        }
        this.info = new ServerStatusInfo(version, players, description, icon);
    }
    
    @Override
    public void write(final NetOutput out) throws IOException {
        final JsonObject obj = new JsonObject();
        final JsonObject ver = new JsonObject();
        ver.addProperty("name", this.info.getVersionInfo().getVersionName());
        ver.addProperty("protocol", this.info.getVersionInfo().getProtocolVersion());
        final JsonObject plrs = new JsonObject();
        plrs.addProperty("max", this.info.getPlayerInfo().getMaxPlayers());
        plrs.addProperty("online", this.info.getPlayerInfo().getOnlinePlayers());
        if (this.info.getPlayerInfo().getPlayers().length > 0) {
            final JsonArray array = new JsonArray();
            for (final GameProfile profile : this.info.getPlayerInfo().getPlayers()) {
                final JsonObject o = new JsonObject();
                o.addProperty("name", profile.getName());
                o.addProperty("id", profile.getIdAsString());
                array.add(o);
            }
            plrs.add("sample", array);
        }
        obj.add("version", ver);
        obj.add("players", plrs);
        obj.add("description", this.info.getDescription().toJson());
        if (this.info.getIcon() != null) {
            obj.addProperty("favicon", this.iconToString(this.info.getIcon()));
        }
        out.writeString(obj.toString());
    }
    
    private BufferedImage stringToIcon(String str) throws IOException {
        if (str.startsWith("data:image/png;base64,")) {
            str = str.substring("data:image/png;base64,".length());
        }
        final byte[] bytes = Base64.decode(str.getBytes("UTF-8"));
        final ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        final BufferedImage icon = ImageIO.read(in);
        in.close();
        if (icon != null && (icon.getWidth() != 64 || icon.getHeight() != 64)) {
            throw new IOException("Icon must be 64x64.");
        }
        return icon;
    }
    
    private String iconToString(final BufferedImage icon) throws IOException {
        if (icon.getWidth() != 64 || icon.getHeight() != 64) {
            throw new IOException("Icon must be 64x64.");
        }
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(icon, "PNG", out);
        out.close();
        final byte[] encoded = Base64.encode(out.toByteArray());
        return "data:image/png;base64," + new String(encoded, "UTF-8");
    }
    
    @Override
    public boolean isPriority() {
        return false;
    }
}
