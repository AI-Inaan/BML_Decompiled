package mv.com.bml.mibapi;

import android.content.Context;
import android.util.Log;
import com.squareup.otto.Subscribe;
import io.paperdb.Paper;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import mv.com.bml.mibapi.apievents.SessionTimeoutEvent;

public class BMLDataCache {
    private static final String TAG = "mv.bml.mibapi.DataCache";
    private static BMLDataCache _instance;
    private ConcurrentHashMap<String, CacheEntry> _cache = new ConcurrentHashMap<>();

    public static class CacheEntry {
        public Object cache_object;
        public long expiry;
        public long insertion_timestamp;
    }

    public BMLDataCache() {
        BMLApi.getEventBus().register(this);
    }

    public static BMLDataCache getInstance() {
        if (_instance == null) {
            _instance = new BMLDataCache();
        }
        return _instance;
    }

    public void clearCache() {
        this._cache.clear();
    }

    public void clearCacheAndDisk(Context context) {
        clearCache();
        clearDisk(context);
    }

    public void clearDisk(Context context) {
        Paper.init(context);
        Paper.clear(context);
    }

    public Object getCacheItem(String str) {
        Object obj;
        if (this._cache.containsKey(str)) {
            obj = this._cache.get(str);
        } else if (!Paper.exist(str)) {
            return null;
        } else {
            obj = Paper.get(str);
        }
        CacheEntry cacheEntry = (CacheEntry) obj;
        long time = new Date().getTime();
        if (cacheEntry.expiry <= 0 || time - cacheEntry.insertion_timestamp < cacheEntry.expiry) {
            return cacheEntry.cache_object;
        }
        invalidateCache(str);
        return null;
    }

    public boolean hasValidCacheItem(String str) {
        return getCacheItem(str) != null;
    }

    public void initDiskCache(Context context) {
        Paper.init(context);
    }

    public void invalidateCache(String str) {
        for (String contentEquals : this._cache.keySet()) {
            if (str.contentEquals(contentEquals)) {
                this._cache.remove(str);
            }
        }
        Paper.delete(str);
    }

    @Subscribe
    public void onSessionTimeout(SessionTimeoutEvent sessionTimeoutEvent) {
        clearCache();
    }

    public void setCacheItem(String str, Object obj, long j, boolean z) {
        CacheEntry cacheEntry = new CacheEntry();
        cacheEntry.expiry = j;
        cacheEntry.cache_object = obj;
        cacheEntry.insertion_timestamp = new Date().getTime();
        invalidateCache(str);
        if (z) {
            Paper.put(str, cacheEntry);
        } else {
            this._cache.put(str, cacheEntry);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Data cache entry made - key: ");
        sb.append(str);
        Log.d(TAG, sb.toString());
    }

    public void setCacheItem(String str, Object obj, boolean z) {
        setCacheItem(str, obj, 0, z);
    }
}
