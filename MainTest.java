
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainTest {
    static DayOfMonth dMonth = new DayOfMonth();

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

        int secondCron = dMonth.setTimeByCronExpression(second, currentTime.getSecond(), 0, 59);
        int minuteCron = dMonth.setTimeByCronExpression(minute, currentTime.getMinute(), 0, 59);
        int hourCron = dMonth.setTimeByCronExpression(hour, currentTime.getHour(), 0, 24);
        int monthCron = dMonth.setTimeByCronExpression(month, currentTime.getMonth().getValue(), 1, 12);
        int yearCron = dMonth.setTimeByCronExpression(year, currentTime.getYear(), currentTime.getYear(), 9999);
        int dayOfMonthCron = dMonth.setTimeByCronExpression(dayOfMonth, currentTime.getDayOfMonth(), 1,
                dMonth.getMaxDayOfMonth(monthCron, yearCron));

        int dayOfWeekCron = dMonth.setTimeByCronExpression(dayOfWeek, currentTime.getDayOfWeek().getValue(), 1, 7);
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
            else if (!dMonth.checkCurrentTimeMatchAsterisk(year)) {
                LocalDateTime l = LocalDateTime.of(yearCron, monthCron, dayOfMonthCron,
                        hourCron, minuteCron,
                        secondCron);

                if (l.isBefore(nextExecutionTime))
                    return null;
            }
            if (monthCron > nextExecutionTime.getMonthValue())
                nextExecutionTime = nextExecutionTime
                        .withMonth(monthCron)
                        .withDayOfMonth(1)
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0);

            if (dayOfMonthCron > nextExecutionTime.getDayOfMonth() &&
                    !dMonth.checkCurrentTimeMatchAsterisk(dayOfMonth))

                nextExecutionTime = nextExecutionTime
                        .withDayOfMonth(dayOfMonthCron)
                        .withHour(0)
                        .withMinute(0)
                        .withSecond(0);

            if (hourCron > nextExecutionTime.getHour() && !dMonth.checkCurrentTimeMatchAsterisk(hour))
                nextExecutionTime = nextExecutionTime
                        .withHour(hourCron)
                        .withMinute(0)
                        .withSecond(0);

            if (minuteCron > nextExecutionTime.getMinute() &&
                    !dMonth.checkCurrentTimeMatchAsterisk(minute))
                nextExecutionTime = nextExecutionTime
                        .withMinute(minuteCron)
                        .withSecond(0);

            System.out.println(nextExecutionTime);
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

    public static boolean checkCronFieldIsValid(int nextTime, String cronField) {
        if (dMonth.checkCurrentTimeMatchAsterisk(cronField) || Integer.parseInt(cronField) == nextTime)
            return true;
        return false;
    }

    public static String formattedTime(LocalDateTime localDateTime) {
        String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("EEEE, yyyy-MM-dd HH:mm:ss"));
        return formattedTime;
    }

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String cronExpression = "1 2 3 4 5 2026 *";// Monday, 2026-05-04 03:02:01
        System.out.println("===============================================");
        System.out.println("Cron Expression: " + cronExpression);
        LocalDateTime cronTime = getNextExecutionTime(cronExpression, localDateTime);

        System.out.println("Time now: " + formattedTime(localDateTime));

        if (cronTime != null) {
            System.out.println("Expression Time: " + formattedTime(cronTime));
        } else {
            System.out.println("Date error!");
        }
        System.out.println("===============================================");

    }
}
