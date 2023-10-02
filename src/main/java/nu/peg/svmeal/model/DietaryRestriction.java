package nu.peg.svmeal.model;

import java.util.Set;

public enum DietaryRestriction {
  VEGETARIAN,
  VEGAN;

  public static DietaryRestriction determineDietaryRestrictionByClassNames(
      Set<String> classNames) {
    if (classNames == null) {
      return null;
    }

    if (classNames.contains("label-vegetarian")) {
      return VEGETARIAN;
    } else if (classNames.contains("label-vegan")) {
      return VEGAN;
    }
    return null;
  }
}
