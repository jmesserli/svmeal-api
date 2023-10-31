package nu.peg.svmeal.infrastructure.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CircuitBreakers {
  public static final String SV_SEARCH = "svSearch";
  public static final String SV_MENU = "svMenu";
}
