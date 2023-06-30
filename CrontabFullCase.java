
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CrontabFullCase {
    static int c = 0;

    /**
     * Returns the formatted time string for a given LocalDateTime object,
     * including the day of the week.
     *
     * @param localDateTime The LocalDateTime object to be formatted.
     * @return The formatted time string in the format "DayOfWeek, yyyy-MM-dd
     *         HH:mm:ss".
     */
    public static String formattedTime(LocalDateTime localDateTime) {
        String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("EEEE, yyyy-MM-dd HH:mm:ss"));
        return formattedTime;
    }

    /**
     * Checks if the given cron field matches the asterisk (*) wildcard.
     *
     * @param cronField The cron field to be checked.
     * @return true if the cron field matches the asterisk wildcard, false
     *         otherwise.
     */
    public static boolean checkCurrentTimeMatchAsterisk(String cronField) {
        return cronField.equals("*");
    }

    /**
     * Checks if the given cron field is valid for the next time value.
     *
     * @param nextTime  The next time value to be checked against the cron field.
     * @param cronField The cron field to be checked.
     * @return true if the cron field is valid for the next time value, false
     *         otherwise.
     */
    public static boolean checkCronFieldIsValid(int nextTime, String cronField) {
        if (checkCurrentTimeMatchAsterisk(cronField) || Integer.parseInt(cronField) == nextTime)
            return true;
        return false;
    }

    /**
     * Sets the execution time based on the given cron field and current time.
     *
     * @param cronField   The cron field that specifies the execution time.
     * @param currentTime The current time to be used if the cron field is an
     *                    asterisk (*).
     * @return The execution time based on the cron field and current time.
     */
    public static int setExecutionTime(String cronField, int currentTime) {
        return checkCurrentTimeMatchAsterisk(cronField) ? currentTime : Integer.parseInt(cronField);
    }

    /**
     * Calculates the next execution time based on the current time and cron
     * expression.
     *
     * @param currentTime    The current time from which to calculate the next
     *                       execution time.
     * @param cronExpression The cron expression representing the scheduling
     *                       pattern.
     * @return The LocalDateTime object representing the next execution time.
     */
    public static LocalDateTime getNextExecutionTime(LocalDateTime currentTime, String cronExpression) {
        // Tách chuỗi cronExpression
        String[] parts = cronExpression.split(" ");
        String second = parts[0];
        String minute = parts[1];
        String hour = parts[2];
        String dayOfMonth = parts[3];
        String month = parts[4];
        String year = parts[5];
        String dayOfWeek = parts[6];

        // int nextSecond = setExecutionTime(second, currentTime.getSecond());
        int nextMinuteCron = setExecutionTime(minute, currentTime.getMinute());
        int nextHourCron = setExecutionTime(hour, currentTime.getHour());
        int nextDayCron = setExecutionTime(dayOfMonth, currentTime.getDayOfMonth());
        int nextMonthCron = setExecutionTime(month, currentTime.getMonthValue());
        int nextYearCron = setExecutionTime(year, currentTime.getYear());

        // Khởi tạo thời gian tiếp theo bằng thời gian hiện tại cộng thêm 1 giây
        LocalDateTime nextExecutionTime = currentTime.plusSeconds(1);
        // kiểm tra năm tuyền vào có lớn hơn năm hiện tại không
        if (nextYearCron > nextExecutionTime.getYear())
            nextExecutionTime = nextExecutionTime
                    .withYear(nextYearCron)
                    .withMonth(1)
                    .withDayOfMonth(1)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0);

        if (nextMonthCron > nextExecutionTime.getMonthValue())
            nextExecutionTime = nextExecutionTime
                    .withMonth(nextMonthCron)
                    .withDayOfMonth(1)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0);

        if (nextDayCron > nextExecutionTime.getDayOfMonth() &&
                !checkCurrentTimeMatchAsterisk(dayOfMonth))

            nextExecutionTime = nextExecutionTime
                    .withDayOfMonth(nextDayCron)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0);

        if (nextHourCron > nextExecutionTime.getHour() && !checkCurrentTimeMatchAsterisk(hour))
            nextExecutionTime = nextExecutionTime
                    .withHour(nextHourCron)
                    .withMinute(0)
                    .withSecond(0);

        if (nextMinuteCron > nextExecutionTime.getMinute() &&
                !checkCurrentTimeMatchAsterisk(minute))
            nextExecutionTime = nextExecutionTime
                    .withMinute(nextMinuteCron)
                    .withSecond(0);

        System.out.println("Time set : " + formattedTime(nextExecutionTime));

        while (true) {
            boolean[] matches = new boolean[7];
            matches[0] = checkCronFieldIsValid(nextExecutionTime.getSecond(), second);
            matches[1] = checkCronFieldIsValid(nextExecutionTime.getMinute(), minute);
            matches[2] = checkCronFieldIsValid(nextExecutionTime.getHour(), hour);
            matches[3] = checkCronFieldIsValid(nextExecutionTime.getDayOfMonth(), dayOfMonth);
            matches[4] = checkCronFieldIsValid(nextExecutionTime.getMonthValue(), month);
            matches[5] = checkCronFieldIsValid(nextExecutionTime.getYear(), year);
            matches[6] = checkCronFieldIsValid(nextExecutionTime.getDayOfWeek().getValue(), dayOfWeek);
            c++;
            boolean allMatch = true;
            for (boolean match : matches) {
                if (!match) {
                    allMatch = false;
                    c++;
                    break;
                }
            }

            if (allMatch) {
                break;
            }
            nextExecutionTime = nextExecutionTime.plusSeconds(1);
        }
        System.out.println(c);
        return nextExecutionTime;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        LocalDateTime currentTime = LocalDateTime.now();
        // Specify the cron expression (e.g., "30 * * * * * *")
        // (giây, phút, giờ, ngày, tháng, năm, ngày trong tuần)
        // String cronExpression = "5 * * * * * *";
        // String cronExpression = "* 5 * * * * *";
        // String cronExpression = "* * 5 * * * *";
        String cronExpression = "* * * * * * *";
        // String cronExpression = "* * * * 5 * *";
        // String cronExpression = "* * * * * 2025 *";

        LocalDateTime nextExecutionTime = getNextExecutionTime(currentTime, cronExpression);

        System.out.println("Cron Expression: " + cronExpression);
        System.out.println("Current Time: " + formattedTime(currentTime));
        System.out.println("Next Execution Time: " + formattedTime(nextExecutionTime));
        System.out.println("===============================================");

        long endTime = System.currentTimeMillis();
        System.out.println("Run time: " + (endTime - startTime) + " ms");

    }

}
