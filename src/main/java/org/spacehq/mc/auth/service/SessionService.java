// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.service;

import java.util.List;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.UUID;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.io.ByteArrayOutputStream;
import org.spacehq.mc.auth.exception.property.PropertyException;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;
import org.spacehq.mc.auth.util.Base64;
import org.spacehq.mc.auth.exception.property.ProfileTextureException;
import org.spacehq.mc.auth.exception.profile.ProfileException;
import org.spacehq.mc.auth.exception.profile.ProfileLookupException;
import org.spacehq.mc.auth.exception.profile.ProfileNotFoundException;
import org.spacehq.mc.auth.util.UUIDSerializer;
import java.util.Collection;
import org.spacehq.mc.auth.exception.request.RequestException;
import org.spacehq.mc.auth.util.HTTP;
import org.spacehq.mc.auth.data.GameProfile;
import java.net.Proxy;
import com.google.gson.Gson;
import java.security.PublicKey;

public class SessionService
{
    private static final String BASE_URL = "https://sessionserver.mojang.com/session/minecraft/";
    private static final String JOIN_URL = "https://sessionserver.mojang.com/session/minecraft/join";
    private static final String HAS_JOINED_URL = "https://sessionserver.mojang.com/session/minecraft/hasJoined";
    private static final String PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile";
    private static final PublicKey SIGNATURE_KEY;
    private static final Gson GSON;
    private Proxy proxy;
    
    public SessionService() {
        this(Proxy.NO_PROXY);
    }
    
    public SessionService(final Proxy proxy) {
        if (proxy == null) {
            throw new IllegalArgumentException("Proxy cannot be null.");
        }
        this.proxy = proxy;
    }
    
    public void joinServer(final GameProfile profile, final String authenticationToken, final String serverId) throws RequestException {
        final JoinServerRequest request = new JoinServerRequest(authenticationToken, profile.getId(), serverId);
        HTTP.makeRequest(this.proxy, "https://sessionserver.mojang.com/session/minecraft/join", request, (Class<Object>)null);
    }
    
    public GameProfile getProfileByServer(final String name, final String serverId) throws RequestException {
        final HasJoinedResponse response = HTTP.makeRequest(this.proxy, "https://sessionserver.mojang.com/session/minecraft/hasJoined?username=" + name + "&serverId=" + serverId, null, HasJoinedResponse.class);
        if (response != null && response.id != null) {
            final GameProfile result = new GameProfile(response.id, name);
            if (response.properties != null) {
                result.getProperties().addAll(response.properties);
            }
            return result;
        }
        return null;
    }
    
    public GameProfile fillProfileProperties(final GameProfile profile) throws ProfileException {
        if (profile.getId() == null) {
            return profile;
        }
        try {
            final MinecraftProfileResponse response = HTTP.makeRequest(this.proxy, "https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDSerializer.fromUUID(profile.getId()) + "?unsigned=false", null, MinecraftProfileResponse.class);
            if (response == null) {
                throw new ProfileNotFoundException("Couldn't fetch profile properties for " + profile + " as the profile does not exist.");
            }
            if (response.properties != null) {
                profile.getProperties().addAll(response.properties);
            }
            return profile;
        }
        catch (RequestException e) {
            throw new ProfileLookupException("Couldn't look up profile properties for " + profile + ".", e);
        }
    }
    
    public GameProfile fillProfileTextures(final GameProfile profile, final boolean requireSecure) throws PropertyException {
        final GameProfile.Property textures = profile.getProperty("textures");
        if (textures != null) {
            if (!textures.hasSignature()) {
                throw new ProfileTextureException("Signature is missing from textures payload.");
            }
            if (!textures.isSignatureValid(SessionService.SIGNATURE_KEY)) {
                throw new ProfileTextureException("Textures payload has been tampered with. (signature invalid)");
            }
            MinecraftTexturesPayload result;
            try {
                final String json = new String(Base64.decode(textures.getValue().getBytes("UTF-8")));
                result = SessionService.GSON.fromJson(json, MinecraftTexturesPayload.class);
            }
            catch (Exception e) {
                throw new ProfileTextureException("Could not decode texture payload.", e);
            }
            if (result.profileId == null || !result.profileId.equals(profile.getId())) {
                throw new ProfileTextureException("Decrypted textures payload was for another user. (expected id " + profile.getId() + " but was for " + result.profileId + ")");
            }
            if (result.profileName == null || !result.profileName.equals(profile.getName())) {
                throw new ProfileTextureException("Decrypted textures payload was for another user. (expected name " + profile.getName() + " but was for " + result.profileName + ")");
            }
            if (requireSecure) {
                if (result.isPublic) {
                    throw new ProfileTextureException("Decrypted textures payload was public when secure data is required.");
                }
                final Calendar limit = Calendar.getInstance();
                limit.add(5, -1);
                final Date validFrom = new Date(result.timestamp);
                if (validFrom.before(limit.getTime())) {
                    throw new ProfileTextureException("Decrypted textures payload is too old. (" + validFrom + ", needs to be at least " + limit + ")");
                }
            }
            if (result.textures != null) {
                profile.getTextures().putAll(result.textures);
            }
        }
        return profile;
    }
    
    @Override
    public String toString() {
        return "SessionService{}";
    }
    
    static {
        InputStream in = null;
        try {
            in = SessionService.class.getResourceAsStream("/yggdrasil_session_pubkey.der");
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final byte[] buffer = new byte[4096];
            int length = -1;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(out.toByteArray());
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            SIGNATURE_KEY = keyFactory.generatePublic(spec);
        }
        catch (Exception e) {
            throw new ExceptionInInitializerError("Missing/invalid yggdrasil public key.");
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException ex) {}
            }
        }
        GSON = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDSerializer()).create();
    }
    
    private static class JoinServerRequest
    {
        private String accessToken;
        private UUID selectedProfile;
        private String serverId;
        
        protected JoinServerRequest(final String accessToken, final UUID selectedProfile, final String serverId) {
            this.accessToken = accessToken;
            this.selectedProfile = selectedProfile;
            this.serverId = serverId;
        }
    }
    
    private static class HasJoinedResponse
    {
        public UUID id;
        public List<GameProfile.Property> properties;
    }
    
    private static class MinecraftProfileResponse
    {
        public UUID id;
        public String name;
        public List<GameProfile.Property> properties;
    }
    
    private static class MinecraftTexturesPayload
    {
        public long timestamp;
        public UUID profileId;
        public String profileName;
        public boolean isPublic;
        public Map<GameProfile.TextureType, GameProfile.Texture> textures;
    }
}
