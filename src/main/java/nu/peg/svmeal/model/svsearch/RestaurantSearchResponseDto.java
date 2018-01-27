package nu.peg.svmeal.model.svsearch;

import javax.validation.constraints.NotNull;

public class RestaurantSearchResponseDto {
    @NotNull
    public EmptyObject empty;

    public static class EmptyObject {
        @NotNull
        public String callbackfunc;
    }
}
