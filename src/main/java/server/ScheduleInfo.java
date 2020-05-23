package server;


public class ScheduleInfo {
    // Set Fields for DbSchedule
    private String serverResponse;
    private String BillboardName;
    private String StartTime;
    private String Duration;
    private String CreationDateTime;
    private String Repeat;
    private String Sunday;
    private String Monday;
    private String Tuesday;
    private String Wednesday;
    private String Thursday;
    private String Friday;
    private String Saturday;

    // Constructor for DbSchedule
    public ScheduleInfo(String serverResponse,
                        String BillboardName,
                        String StartTime,
                        String Duration,
                        String CreationDateTime,
                        String Repeat,
                        String Sunday,
                        String Monday,
                        String Tuesday,
                        String Wednesday,
                        String Thursday,
                        String Friday,
                        String Saturday) {
        this.serverResponse = serverResponse;
        this.BillboardName = BillboardName;
        this.StartTime = StartTime;
        this.Duration = Duration;
        this.CreationDateTime = CreationDateTime;
        this.Repeat = Repeat;
        this.Sunday = Sunday;
        this.Monday = Monday;
        this.Tuesday = Tuesday;
        this.Wednesday = Wednesday;
        this.Thursday = Thursday;
        this.Friday = Friday;
        this.Saturday = Saturday;
    }

    // Set Getters for DbSchedule
    public String getScheduleServerResponse() {
        return serverResponse;
    }
    public String getScheduleBillboardName() {
        return BillboardName;
    }
    public String getStartTime() {
        return StartTime;
    }
    public String getDuration() {
        return Duration;
    }
    public String getCreationDateTime() {
        return CreationDateTime;
    }
    public String getRepeat() {
        return Repeat;
    }
    public String getSunday() {
        return Sunday;
    }
    public String getMonday() {
        return Monday;
    }
    public String getTuesday() {
        return Tuesday;
    }
    public String getWednesday() {
        return Wednesday;
    }
    public String getThursday() {
        return Thursday;
    }
    public String getFriday() {
        return Friday;
    }
    public String getSaturday() {
        return Saturday;
    }

}
