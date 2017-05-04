package nu.peg.svmeal.util;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link DateUtil}
 *
 * @author Joel Messerli @04.05.2017
 */
public class DateUtilTest {
    private final static int FIFTH_DATE_IDX = 4;
    public static final int SECOND_DATE_IDX = 1;

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.");

    @Test
    public void generatesDateSequence() {
        LocalDate today = LocalDate.now();
        List<LocalDate> actualList = Stream.generate(DateUtil.dateSequenceGenerator(today))
                .limit(3)
                .collect(Collectors.toList());

        LocalDate[] expectedItems = {today, today.plusDays(1), today.plusDays(2)};

        assertThat(actualList).containsExactly(expectedItems);
    }

    @Test
    public void parsesDatesInSequence() {
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);

        List<LocalDate> dateSequence = Stream.generate(DateUtil.dateSequenceGenerator(firstDayOfMonth))
                .limit(10)
                .collect(Collectors.toList());

        List<String> formattedDates = dateSequence.stream()
                .map(dateFormat::format)
                .collect(Collectors.toList());

        assertThat(DateUtil.tryParseDateFromRange(formattedDates, FIFTH_DATE_IDX)).isEqualTo(dateSequence.get(FIFTH_DATE_IDX));
    }

    @Test
    public void parsesDatesSkippingYearInSameMonth() {
        LocalDate secondDayOfCurrentMonth = LocalDate.now().withDayOfMonth(2);
        LocalDate firstDayOfNextYearsCurrentMonth = LocalDate.of(
                secondDayOfCurrentMonth.getYear() + 1,
                secondDayOfCurrentMonth.getMonth(),
                secondDayOfCurrentMonth.getDayOfMonth() - 1
        );

        List<String> formattedDates = Arrays.asList(secondDayOfCurrentMonth, firstDayOfNextYearsCurrentMonth).stream()
                .map(dateFormat::format)
                .collect(Collectors.toList());

        assertThat(DateUtil.tryParseDateFromRange(formattedDates, SECOND_DATE_IDX)).isEqualTo(firstDayOfNextYearsCurrentMonth);
    }

    @Test
    public void handlesYearChangesCorrectly() {
        LocalDate december29 = LocalDate.now().withMonth(12).withDayOfMonth(29);

        List<LocalDate> yearCrossingDateSequence = Stream.generate(DateUtil.dateSequenceGenerator(december29))
                .limit(4)
                .collect(Collectors.toList());

        List<String> formattedDates = yearCrossingDateSequence.stream()
                .map(dateFormat::format)
                .collect(Collectors.toList());

        int januaryFirstIdx = 2;
        assertThat(DateUtil.tryParseDateFromRange(formattedDates, januaryFirstIdx)).isEqualTo(yearCrossingDateSequence.get(januaryFirstIdx));
    }
}