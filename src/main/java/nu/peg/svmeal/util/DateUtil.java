package nu.peg.svmeal.util;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for working with dates
 *
 * @author Joel Messerli @04.05.2017
 */
public final class DateUtil {
  public static final Pattern datePattern = Pattern.compile("^(\\d{2})\\.(\\d{2})\\.$");

  private static final int FIRST_IDX = 0;
  private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

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
    if (dateIdx >= datesWithoutYear.size() || dateIdx < 0 || datesWithoutYear.size() == 0) {
      LOGGER.debug("dateIdx is out of bounds or the list is empty");
      return null;
    }

    LocalDate today = LocalDate.now();
    int currentYear = today.getYear();
    String firstDateString = datesWithoutYear.get(FIRST_IDX);
    LocalDate lastDate = parseDayMonthStringWithYear(firstDateString, currentYear);
    if (lastDate.isBefore(today)) {
      lastDate = lastDate.plusYears(1);
      currentYear++;
    }

    for (int idx = 1; idx <= dateIdx; idx++) {
      String currDayMonth = datesWithoutYear.get(idx);
      LocalDate tempDate = parseDayMonthStringWithYear(currDayMonth, currentYear);

      if (tempDate.isBefore(lastDate) || tempDate.isEqual(lastDate)) {
        tempDate = tempDate.plusYears(1);
        currentYear++;
      }

      lastDate = tempDate;
    }

    return lastDate;
  }

  private static LocalDate parseDayMonthStringWithYear(String dayMonthDate, int year) {
    Matcher firstDateMatcher = datePattern.matcher(dayMonthDate);
    if (!firstDateMatcher.matches()) {
      LOGGER.debug("The first date does not match the specified format");
      return null;
    }
    try {
      int firstDateDay = Integer.parseInt(firstDateMatcher.group(1));
      int firstDateMonth = Integer.parseInt(firstDateMatcher.group(2));
      return LocalDate.of(year, firstDateMonth, firstDateDay);
    } catch (NumberFormatException nfe) {
      LOGGER.debug("NumberFormatException while parsing the first date");
      return null;
    }
  }

  public static Supplier<LocalDate> dateSequenceGenerator(LocalDate startDate) {
    final LocalDate[] lastDate = {startDate.minusDays(1)};
    return () -> (lastDate[0] = lastDate[0].plusDays(1));
  }

  private DateUtil() {}
}
