
public class TimeCronExpression {

    private String second;
    private String minute;
    private String hour;
    private String dayOfMonth;
    private String month;
    private String year;
    private String dayOfWeek;

    public TimeCronExpression(String cronExpression) {
        String[] parts = cronExpression.split(" ");
        String second = parts[0];
        String minute = parts[1];
        String hour = parts[2];
        String dayOfMonth = parts[3];
        String month = parts[4];
        String year = parts[5];
        String dayOfWeek = parts[6];

        setSecond(second);
        setMinute(minute);
        setHour(hour);
        setDayOfMonth(dayOfMonth);
        setMonth(month);
        setYear(year);
        setDayOfWeek(dayOfWeek);

    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

}
