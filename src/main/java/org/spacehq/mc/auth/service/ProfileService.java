// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.auth.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import org.spacehq.mc.auth.exception.request.RequestException;
import org.spacehq.mc.auth.exception.profile.ProfileNotFoundException;
import java.util.UUID;
import java.util.Collection;
import org.spacehq.mc.auth.util.HTTP;
import org.spacehq.mc.auth.data.GameProfile;
import java.util.Set;
import java.util.HashSet;
import java.net.Proxy;

public class ProfileService
{
    private static final String BASE_URL = "https://api.mojang.com/profiles/";
    private static final String SEARCH_URL = "https://api.mojang.com/profiles/minecraft";
    private static final int MAX_FAIL_COUNT = 3;
    private static final int DELAY_BETWEEN_PAGES = 100;
    private static final int DELAY_BETWEEN_FAILURES = 750;
    private static final int PROFILES_PER_REQUEST = 100;
    private Proxy proxy;
    
    public ProfileService() {
        this(Proxy.NO_PROXY);
    }
    
    public ProfileService(final Proxy proxy) {
        if (proxy == null) {
            throw new IllegalArgumentException("Proxy cannot be null.");
        }
        this.proxy = proxy;
    }
    
    public void findProfilesByName(final String[] names, final ProfileLookupCallback callback) {
        this.findProfilesByName(names, callback, false);
    }
    
    public void findProfilesByName(final String[] names, final ProfileLookupCallback callback, final boolean async) {
        final Set<String> criteria = new HashSet<String>();
        for (final String name : names) {
            if (name != null && !name.isEmpty()) {
                criteria.add(name.toLowerCase());
            }
        }
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (final Set<String> request : partition(criteria, 100)) {
                    Exception error = null;
                    int failCount = 0;
                    boolean tryAgain = true;
                    while (failCount < 3 && tryAgain) {
                        tryAgain = false;
                        try {
                            final GameProfile[] profiles = HTTP.makeRequest(ProfileService.this.proxy, "https://api.mojang.com/profiles/minecraft", request, GameProfile[].class);
                            failCount = 0;
                            final Set<String> missing = new HashSet<String>(request);
                            for (final GameProfile profile : profiles) {
                                missing.remove(profile.getName().toLowerCase());
                                callback.onProfileLookupSucceeded(profile);
                            }
                            for (final String name : missing) {
                                callback.onProfileLookupFailed(new GameProfile((UUID)null, name), new ProfileNotFoundException("Server could not find the requested profile."));
                            }
                            try {
                                Thread.sleep(100L);
                            }
                            catch (InterruptedException ex) {}
                        }
                        catch (RequestException e) {
                            error = e;
                            if (++failCount >= 3) {
                                for (final String name2 : request) {
                                    callback.onProfileLookupFailed(new GameProfile((UUID)null, name2), error);
                                }
                            }
                            else {
                                try {
                                    Thread.sleep(750L);
                                }
                                catch (InterruptedException ex2) {}
                                tryAgain = true;
                            }
                        }
                    }
                }
            }
        };
        if (async) {
            new Thread(runnable, "ProfileLookupThread").start();
        }
        else {
            runnable.run();
        }
    }
    
    private static Set<Set<String>> partition(final Set<String> set, final int size) {
        final List<String> list = new ArrayList<String>(set);
        final Set<Set<String>> ret = new HashSet<Set<String>>();
        for (int i = 0; i < list.size(); i += size) {
            final Set<String> s = new HashSet<String>();
            s.addAll(list.subList(i, Math.min(i + size, list.size())));
            ret.add(s);
        }
        return ret;
    }
    
    public interface ProfileLookupCallback
    {
        void onProfileLookupSucceeded(final GameProfile p0);
        
        void onProfileLookupFailed(final GameProfile p0, final Exception p1);
    }
}
