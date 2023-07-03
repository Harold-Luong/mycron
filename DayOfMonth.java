public class DayOfMonth {
    public boolean isTimeValid(int value, int min, int max) {
        return value >= min && value <= max;
    }

    public int getMaxDayOfMonth(int month, int year) {

        if (month == 2) {
            return isLeapYear(year) ? 29 : 28;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        } else {
            return 31;
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) ? true : false;

    }

    /**
     * Checks if the given cron field matches the asterisk (*) wildcard.
     *
     * @param cronField The cron field to be checked.
     * @return true if the cron field matches the asterisk wildcard, false
     *         otherwise.
     */
    public boolean checkCurrentTimeMatchAsterisk(String cronField) {
        return cronField.equals("*");
    }

    public int setTimeByCronExpression(String cronExpression, int currentTime, int min, int max) {
        if (checkCurrentTimeMatchAsterisk(cronExpression)) {
            return currentTime; // Trả về thời gian hiện tại nếu trường cronExpression là "*"
        } else {
            try {
                int time = Integer.parseInt(cronExpression);
                if (isTimeValid(time, min, max)) {
                    return time; // Trả về giá trị ngày nếu là số hợp lệ
                } else {
                    return -1; // Trả về -1 nếu giá trị ngày không hợp lệ
                }
            } catch (NumberFormatException e) {
                return -1; // Trả về -1 nếu trường cronExpression không phải là cũng không phải "*"
            }
        }
    }

}
