package hu.bsstudio.robonaut;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class InterviewQuestionTest {

    /*
        Given a time in 12-hour AM/PM format, convert it to military (24-hour) time.
        Note:
            - 12:00:00AM on a 12-hour clock is 00:00:00 on a 24-hour clock.
            - 12:00:00PM on a 12-hour clock is 12:00:00 on a 24-hour clock.
        Input format:
            hh:mm:ssAM or hh:mm:ssPM
     */
    private String toMilitaryHours(final String regularHours) {
        String hour = regularHours.substring(0, 2);
        final String minute = regularHours.substring(3, 5);
        final String second = regularHours.substring(6, 8);
        final String amPmOfTheDay = regularHours.substring(8, 10);

        int hourAsInt = Integer.parseInt(hour);
        if (amPmOfTheDay.equals("AM") && hourAsInt == 12) {
            hourAsInt = 0;
        } else if (amPmOfTheDay.equals("PM") && hourAsInt < 12) {
            hourAsInt += 12;
        }
        hour = String.format("%02d", hourAsInt);

        return hour + ':' + minute + ':' + second;
    }

    @Test
    void shouldAddTwelveAndRemovePM() {
        final var regularHours = "07:05:45PM";

        final var expected = toMilitaryHours(regularHours);

        assertThat(expected).isEqualTo("19:05:45");
    }

    @Test
    void shouldRemoveAM() {
        final var regularHours = "07:05:45AM";

        final var expected = toMilitaryHours(regularHours);

        assertThat(expected).isEqualTo("07:05:45");
    }

    @Test
    void testMidnight() {
        final var regularHours = "12:00:00AM";

        final var expected = toMilitaryHours(regularHours);

        assertThat(expected).isEqualTo("00:00:00");
    }

    @Test
    void testNoon() {
        final var regularHours = "12:00:00PM";

        final var expected = toMilitaryHours(regularHours);

        assertThat(expected).isEqualTo("12:00:00");
    }
}
