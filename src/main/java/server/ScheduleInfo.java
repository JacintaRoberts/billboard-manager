package server;


import java.io.Serializable;

import static server.Server.*;

/**
 * The ScheduleInfo is a class which is used as an serializable object to send particular schedule information to the control panel from the database result.
 */

public class ScheduleInfo implements Serializable {
    // Set Fields for DbSchedule
    private ServerAcknowledge serverResponse;
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
    public ScheduleInfo(ServerAcknowledge serverResponse,
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
    public ServerAcknowledge getScheduleServerResponse() {
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
