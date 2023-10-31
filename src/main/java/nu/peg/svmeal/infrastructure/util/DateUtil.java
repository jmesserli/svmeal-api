package nu.peg.svmeal.infrastructure.util;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for working with dates
 *
 * @author Joel Messerli @04.05.2017
 */
public final class DateUtil {
  public static final Pattern datePattern = Pattern.compile("^(\\d{2})\\.(\\d{2})\\.$");

  private static final int FIRST_IDX = 0;

  /**
   * Tries to parse dates that are missing a year, handling new years correctly (hopefully)
   *
   * <p>To do this, the first date is assumed to be in the current year
   *
   * @param datesWithoutYear List of dates in the format MM.dd.
   * @param dateIdx Zero based index of the date
   * @return Parsed date or null if the parsing failed
   */
  public static LocalDate tryParseDateFromRange(List<String> datesWithoutYear, int dateIdx) {
    if (dateIdx >= datesWithoutYear.size() || dateIdx < 0) {
      throw new IllegalStateException("dateIdx is out of bounds or the list is empty");
    }

    LocalDate today = LocalDate.now();
    int currentYear = today.getYear();

    String firstDateString = datesWithoutYear.get(FIRST_IDX);
    LocalDate currentDate = parseDayMonthStringWithYear(firstDateString, currentYear);
    if (currentDate.isBefore(today) && currentDate.getMonth() == Month.JANUARY) {
      currentDate = currentDate.plusYears(1);
      currentYear++;
    }

    for (int idx = 1; idx <= dateIdx; idx++) {
      String currDayMonth = datesWithoutYear.get(idx);
      LocalDate tempDate = parseDayMonthStringWithYear(currDayMonth, currentYear);

      if (tempDate.isBefore(currentDate) || tempDate.isEqual(currentDate)) {
        tempDate = tempDate.plusYears(1);
        currentYear++;
      }

      currentDate = tempDate;
    }

    return currentDate;
  }

  private static LocalDate parseDayMonthStringWithYear(String dayMonthDate, int year) {
    Matcher firstDateMatcher = datePattern.matcher(dayMonthDate);
    if (!firstDateMatcher.matches()) {
      throw new IllegalArgumentException("The first date does not match the specified format");
    }
    try {
      int firstDateDay = Integer.parseInt(firstDateMatcher.group(1));
      int firstDateMonth = Integer.parseInt(firstDateMatcher.group(2));
      return LocalDate.of(year, firstDateMonth, firstDateDay);
    } catch (NumberFormatException nfe) {
      throw new IllegalStateException("NumberFormatException while parsing the first date");
    }
  }

  public static Supplier<LocalDate> dateSequenceGenerator(LocalDate startDate) {
    final LocalDate[] lastDate = {startDate.minusDays(1)};
    return () -> {
      lastDate[0] = lastDate[0].plusDays(1);
      return lastDate[0];
    };
  }

  private DateUtil() {}
}
