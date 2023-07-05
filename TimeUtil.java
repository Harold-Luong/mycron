/**
 * The CheckDay class provides methods for validating time values, determining
 * the maximum number of days in a month,
 * checking if a cron field matches an asterisk (*), and setting time based on a
 * cron expression.
 */
public class TimeUtil {

    /**
     * Checks if a given value is within the specified range.
     *
     * @param value The value to be checked.
     * @param min   The minimum allowed value.
     * @param max   The maximum allowed value.
     * @return true if the value is within the range, false otherwise.
     */
    public boolean isTimeValid(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Determines the maximum number of days in a given month of a specific year.
     *
     * @param month The month (1-12) for which to determine the maximum days.
     * @param year  The year for which to determine the maximum days.
     * @return The maximum number of days in the specified month and year.
     */
    public int getMaxDayOfMonth(int month, int year) {
        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return 31;
        }
    }

    /**
     * Checks if a given year is a leap year.
     *
     * @param year The year to be checked.
     * @return true if the year is a leap year, false otherwise.
     */
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) ? true : false;
    }

    /**
     * Checks if a cron field matches an asterisk (*).
     *
     * @param cronField The cron field to be checked.
     * @return true if the cron field is "*", false otherwise.
     */
    public boolean checkCurrentTimeMatchAsterisk(String cronField) {
        return cronField.equals("*");
    }

    /**
     * Sets the time based on a cron expression, considering the current time,
     * minimum, and maximum values.
     *
     * @param cronExpression The cron expression to set the time.
     * @param currentTime    The current time.
     * @param min            The minimum allowed value.
     * @param max            The maximum allowed value.
     * @return The updated time based on the cron expression. Returns currentTime if
     *         cronExpression is "*", returns -1 if
     *         the cronExpression is not a valid number or is out of the specified
     *         range.
     */
    public int setTimeByCronExpression(String cronExpression, int currentTime, int min, int max) {
        if (checkCurrentTimeMatchAsterisk(cronExpression)) {
            return currentTime; // Return the current time if cronExpression is "*"
        } else {
            try {
                int time = Integer.parseInt(cronExpression);
                if (isTimeValid(time, min, max)) {
                    return time; // Return the time value if it is a valid number
                } else {
                    return -1; // Return -1 if the time value is not valid
                }
            } catch (NumberFormatException e) {
                return -1; // Return -1 if cronExpression is neither a valid number nor "*"
            }
        }
    }

}
