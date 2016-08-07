package nu.peg.svmeal.model;

import nu.peg.svmeal.persistence.CacheDatabaseHandler;

public class MealPlanResponseCache {

    private final int maxAge;
    private final CacheDatabaseHandler dbHandler;

    /**
     * @param maxAge Maximum age of cached value in seconds
     */
    public MealPlanResponseCache(int maxAge) {
        this.maxAge = maxAge;
        this.dbHandler = new CacheDatabaseHandler();
    }

    public void add(int dayOffset, Restaurant restaurant, MealPlanResponse response) {
        dbHandler.add(dayOffset, restaurant, response);
    }

    public MealPlanResponse get(int dayOffset, Restaurant restaurant) {
        dbHandler.cleanOld(maxAge);
        return dbHandler.get(dayOffset, restaurant);
    }
}
