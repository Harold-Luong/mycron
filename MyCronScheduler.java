
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class MyCronScheduler {
    /**
     * Check if the scheduled time is equal to the current time.
     * 
     * @param cronFields
     * @param currentTime
     * @return
     */
    public static boolean isMatch(String[] cronFields, LocalDateTime currentTime) {
        int currentMinute = currentTime.getMinute();
        int currentHour = currentTime.getHour();
        int currentDayOfMonth = currentTime.getDayOfMonth();
        int currentMonth = currentTime.getMonthValue();
        int currentDayOfWeek = currentTime.getDayOfWeek().getValue();

        return isFieldMatch(cronFields[0], currentMinute)
                && isFieldMatch(cronFields[1], currentHour)
                && isFieldMatch(cronFields[2], currentDayOfMonth)
                && isFieldMatch(cronFields[3], currentMonth)
                && isFieldMatch(cronFields[4], currentDayOfWeek);
    }

    /**
     * 
     * @param cronField
     * @param currentValue
     * @return
     */
    public static boolean isFieldMatch(String cronField, int currentValue) {
        return cronField.equals("*") || cronField.equals(String.valueOf(currentValue));
    }

    /**
     * 
     */
    public static void runJob() {
        System.out.println("Job is running at " + LocalDateTime.now());
    }

    public static LocalDateTime next(String[] cronFields, LocalDateTime currentTime) {
        int currentMinute = currentTime.getMinute();
        int currentHour = currentTime.getHour();
        int currentDayOfMonth = currentTime.getDayOfMonth();
        int currentMonth = currentTime.getMonthValue();

        int nextMinute = findNextValue(cronFields[0], currentMinute, 59);
        int nextHour = findNextValue(cronFields[1], currentHour, 23);

        LocalDate nextDate = LocalDate.from(currentTime).plusDays(1);

        return LocalDateTime.of(nextDate.getYear(), currentMonth, currentDayOfMonth, nextHour, currentMinute);
    }

    public static int findNextValue(String cronField, int currentValue, int maxLimit) {
        if (cronField.equals("*")) {
            return (currentValue + 1) % (maxLimit + 1);
        } else {
            int nextValue = Integer.parseInt(cronField);
            if (nextValue <= currentValue) {
                nextValue = (nextValue + 1) % (maxLimit + 1);
            }
            return nextValue;
        }
    }

    public static void sleepUntil(LocalDateTime targetTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        long delaySeconds = ChronoUnit.SECONDS.between(currentTime, targetTime);
        System.out.println(targetTime);
        if (delaySeconds > 0) {
            try {
                Thread.sleep(delaySeconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static void mycron(String cronExpression) {

        String[] cronFields = cronExpression.split(" ");

        while (true) {
            LocalDateTime currentTime = LocalDateTime.now();

            if (isMatch(cronFields, currentTime)) {
                runJob();
                System.out.println("Next time start: " + next(cronFields, currentTime));
            }

            LocalDateTime nextExecution = next(cronFields, currentTime);
            sleepUntil(nextExecution);
        }

    }

    public static void main(String[] args) {

        String cronExpression = "26 * * * *";// min hour day(month) month day(week)
        mycron(cronExpression);

    }
}