package nu.peg.svmeal.domain.model;

import static java.util.function.Function.identity;

import java.util.Optional;
import java.util.Set;

public enum DietaryRestriction {
  VEGETARIAN,
  VEGAN,
//
;

  public static DietaryRestriction determineDietaryRestrictionByClassNames(Set<String> classNames) {
    if (classNames == null) {
      return null;
    }

    return classNames.stream()
        .map(DietaryRestriction::fromLabelName)
        .filter(Optional::isPresent)
        .findFirst()
        .flatMap(identity())
        .orElse(null);
  }

  private static Optional<DietaryRestriction> fromLabelName(String labelName) {
    return switch (labelName) {
      case "label-vegetarian" -> Optional.of(VEGETARIAN);
      case "label-vegan" -> Optional.of(VEGAN);
      default -> Optional.empty();
    };
  }
}
