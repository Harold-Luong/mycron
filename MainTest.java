
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainTest {
    static TimeUtil timeUtil = new TimeUtil();

    /**
     * Calculates the next execution time based on the given cron expression and
     * current time.
     *
     * @param cronExpression The cron expression representing the time settings.
     * @param currentTime    The current time.
     * @return The next execution time as a LocalDateTime object, or null if no
     *         valid execution time is found.
     */
    public static LocalDateTime getNextExecutionTime(String cronExpression, LocalDateTime currentTime) {
        TimeCronExpression time = new TimeCronExpression(cronExpression);
        String second = time.getSecond();
        String minute = time.getMinute();
        String hour = time.getHour();
        String dayOfMonth = time.getDayOfMonth();
        String month = time.getMonth();
        String year = time.getYear();
        String dayOfWeek = time.getDayOfWeek();

        LocalDateTime nextExecutionTime = currentTime.plusSeconds(1);

        int secondCron = timeUtil.setTimeByCronExpression(second, currentTime.getSecond(), 0, 59);
        int minuteCron = timeUtil.setTimeByCronExpression(minute, currentTime.getMinute(), 0, 59);
        int hourCron = timeUtil.setTimeByCronExpression(hour, currentTime.getHour(), 0, 23);
        int monthCron = timeUtil.setTimeByCronExpression(month, currentTime.getMonth().getValue(), 1, 12);
        int yearCron = timeUtil.setTimeByCronExpression(year, currentTime.getYear(), currentTime.getYear(), 9999);
        int dayOfMonthCron = timeUtil.setTimeByCronExpression(dayOfMonth, currentTime.getDayOfMonth(), 1,
                timeUtil.getMaxDayOfMonth(monthCron, yearCron));
        int dayOfWeekCron = timeUtil.setTimeByCronExpression(dayOfWeek, currentTime.getDayOfWeek().getValue(), 1, 7);

        boolean isValid = (secondCron > -1) && (minuteCron > -1) && (hourCron > -1) && (dayOfMonthCron > -1)
                && (monthCron > -1)
                && (yearCron > -1) && (dayOfWeekCron > -1);
        if (isValid) {
            if (yearCron > nextExecutionTime.getYear())
                nextExecutionTime = nextExecutionTime
                        .withYear(yearCron)
                        .withMonth(1)
                        .withDayOfMonth(1)
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0);

            if (monthCron > nextExecutionTime.getMonthValue() &&
                    !timeUtil.checkCurrentTimeMatchAsterisk(month))
                nextExecutionTime = nextExecutionTime
                        .withMonth(monthCron)
                        .withDayOfMonth(1)
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0);

            if (dayOfMonthCron > nextExecutionTime.getDayOfMonth() &&
                    !timeUtil.checkCurrentTimeMatchAsterisk(dayOfMonth))
                nextExecutionTime = nextExecutionTime
                        .withDayOfMonth(dayOfMonthCron)
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0);

            if (hourCron > nextExecutionTime.getHour() &&
                    !timeUtil.checkCurrentTimeMatchAsterisk(hour))
                nextExecutionTime = nextExecutionTime
                        .withHour(hourCron)
                        .withMinute(0)
                        .withSecond(0);

            if (minuteCron > nextExecutionTime.getMinute() &&
                    !timeUtil.checkCurrentTimeMatchAsterisk(minute))
                nextExecutionTime = nextExecutionTime
                        .withMinute(minuteCron)
                        .withSecond(0);

            while (true) {
                boolean[] matches = new boolean[7];
                matches[0] = checkCronFieldIsValid(nextExecutionTime.getSecond(), second);
                matches[1] = checkCronFieldIsValid(nextExecutionTime.getMinute(), minute);
                matches[2] = checkCronFieldIsValid(nextExecutionTime.getHour(), hour);
                matches[3] = checkCronFieldIsValid(nextExecutionTime.getDayOfMonth(), dayOfMonth);
                matches[4] = checkCronFieldIsValid(nextExecutionTime.getMonthValue(), month);
                matches[5] = checkCronFieldIsValid(nextExecutionTime.getYear(), year);
                matches[6] = checkCronFieldIsValid(nextExecutionTime.getDayOfWeek().getValue(), dayOfWeek);
                boolean allMatch = true;
                for (boolean match : matches) {
                    if (!match) {
                        allMatch = false;
                        break;
                    }
                }
                if (allMatch) {
                    return nextExecutionTime;
                }
                nextExecutionTime = nextExecutionTime.plusSeconds(1);
            }

        }
        return null;
    }

    /**
     * Checks if a cron field value matches the given next time value.
     *
     * @param nextTime  The next time value to check.
     * @param cronField The cron field value to compare.
     * @return true if the cron field value matches the next time value, false
     *         otherwise.
     */
    public static boolean checkCronFieldIsValid(int nextTime, String cronField) {
        if (timeUtil.checkCurrentTimeMatchAsterisk(cronField) || Integer.parseInt(cronField) == nextTime)
            return true;
        return false;
    }

    public static String formattedTime(LocalDateTime localDateTime) {
        String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("EEEE, yyyy-MM-dd HH:mm:ss"));
        return formattedTime;
    }

    static void test(String cronExpression) {
        LocalDateTime localDateTime = LocalDateTime.now();

        System.out.println("===============================================");
        System.out.println("Cron Expression: " + cronExpression);
        System.out.println("Time now: " + formattedTime(localDateTime));
        LocalDateTime cronTime;
        for (int i = 0; i < 6; i++) {
            cronTime = getNextExecutionTime(cronExpression, localDateTime);

            if (cronTime != null) {
                System.out.println("Expression Time: " + formattedTime(cronTime));
                localDateTime = cronTime;
            } else {
                System.out.println("Date error!");
            }

        }

        System.out.println("===============================================");

    }

    public static void main(String[] args) {
        String cronExpression = "* * 12 * * * *";// pass
        String cronExpression1 = "0 0 5 * * 2023 *";// pass
        String cronExpression2 = "0 0 0 * * * *";// pass
        String cronExpression3 = "0 0 0 1 * * *";// pass
        String cronExpression4 = "0 0 0 1 1 * *";// pass
        String cronExpression5 = "0 0 0 1 1 * 2";// pass
        String cronExpression6 = "0 0 0 1 1 2023 *";// f
        String cronExpression7 = "0 0 0 1 1 2024 *";// pass
        String cronExpression8 = "* 12 * 7 * 2022 *";// pass

        test(cronExpression1);
    }
}
