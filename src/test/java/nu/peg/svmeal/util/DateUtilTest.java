package nu.peg.svmeal.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DateUtil}
 *
 * @author Joel Messerli @04.05.2017
 */
class DateUtilTest {
  private static final int FIFTH_DATE_IDX = 4;
  public static final int SECOND_DATE_IDX = 1;

  private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.");

  @Test
  void generatesDateSequence() {
    LocalDate today = LocalDate.now();
    List<LocalDate> actualList =
        Stream.generate(DateUtil.dateSequenceGenerator(today)).limit(3).toList();

    LocalDate[] expectedItems = {today, today.plusDays(1), today.plusDays(2)};

    assertThat(actualList).containsExactly(expectedItems);
  }

  @Test
  void parsesDatesInSequence() {
    LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1).plusMonths(1);

    List<LocalDate> dateSequence =
        Stream.generate(DateUtil.dateSequenceGenerator(firstDayOfMonth)).limit(10).toList();

    List<String> formattedDates = dateSequence.stream().map(dateFormat::format).toList();

    assertThat(DateUtil.tryParseDateFromRange(formattedDates, FIFTH_DATE_IDX))
        .isEqualTo(dateSequence.get(FIFTH_DATE_IDX));
  }

  @Test
  void parsesDatesSkippingYearInSameMonth() {
    LocalDate secondDayOfNextMonth = LocalDate.now().withDayOfMonth(2).plusMonths(1);
    LocalDate firstDayOfNextYearsNextMonth =
        LocalDate.of(
            secondDayOfNextMonth.getYear() + 1,
            secondDayOfNextMonth.getMonth(),
            secondDayOfNextMonth.getDayOfMonth() - 1);

    List<String> formattedDates =
        Stream.of(secondDayOfNextMonth, firstDayOfNextYearsNextMonth)
            .map(dateFormat::format)
            .toList();

    assertThat(DateUtil.tryParseDateFromRange(formattedDates, SECOND_DATE_IDX))
        .isEqualTo(firstDayOfNextYearsNextMonth);
  }

  @Test
  void handlesYearChangesCorrectly() {
    LocalDate december30 = LocalDate.now().withMonth(12).withDayOfMonth(30);

    List<LocalDate> yearCrossingDateSequence =
        Stream.generate(DateUtil.dateSequenceGenerator(december30)).limit(4).toList();

    List<String> formattedDates =
        yearCrossingDateSequence.stream().map(dateFormat::format).toList();

    int januaryFirstIdx = 2;
    assertThat(DateUtil.tryParseDateFromRange(formattedDates, januaryFirstIdx))
        .isEqualTo(yearCrossingDateSequence.get(januaryFirstIdx));
  }

  @Test
  void bugInvalidYearIfFirstDateIsBeforeToday() {
    final LocalDate now = LocalDate.now();
    LocalDate bugDate = LocalDate.of(now.getYear(), 12, 16);

    final var sequence =
        Stream.generate(DateUtil.dateSequenceGenerator(bugDate.minusDays(1))).limit(5).toList();

    List<String> formattedDates = sequence.stream().map(dateFormat::format).toList();

    for (int i = 0; i < 5; i++) {
      assertThat(DateUtil.tryParseDateFromRange(formattedDates, i)).isEqualTo(sequence.get(i));
    }
  }
}
