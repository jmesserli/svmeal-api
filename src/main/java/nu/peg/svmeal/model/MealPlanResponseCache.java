package nu.peg.svmeal.model;

import java.util.HashMap;
import java.util.Map;

public class MealPlanResponseCache {

    private long maxAge;
    private Map<CacheKey, CacheValue> cache;

    /**
     * @param maxAge Maximum age of cached value in seconds
     */
    public MealPlanResponseCache(long maxAge) {
        this.maxAge = maxAge;
        this.cache = new HashMap<>();
    }

    public void add(int dayOffset, Restaurant restaurant, MealPlanResponse response) {
        cache.put(new CacheKey(dayOffset, restaurant), new CacheValue(System.currentTimeMillis(), response));
    }

    public MealPlanResponse get(int dayOffset, Restaurant restaurant) {
        CacheKey cacheKey = new CacheKey(dayOffset, restaurant);
        CacheValue value = cache.getOrDefault(cacheKey, null);

        if (value != null && ((System.currentTimeMillis() - value.getTimestamp()) / 1000D) > maxAge) {
            cache.remove(cacheKey);

            return null;
        } else if (value == null) {
            return null;
        }

        return value.getValue();
    }

    private static class CacheKey {
        private int dayOffset;
        private Restaurant restaurant;

        CacheKey(int dayOffset, Restaurant restaurant) {
            this.dayOffset = dayOffset;
            this.restaurant = restaurant;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CacheKey)) return false;

            CacheKey cacheKey = (CacheKey) o;

            return dayOffset == cacheKey.dayOffset && restaurant == cacheKey.restaurant;
        }
    }

    private static class CacheValue {
        private long timestamp;
        private MealPlanResponse value;

        CacheValue(long timestamp, MealPlanResponse value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        long getTimestamp() {
            return timestamp;
        }

        MealPlanResponse getValue() {
            return value;
        }
    }

}
