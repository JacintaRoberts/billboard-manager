package server;

import java.util.ArrayList;

public class ScheduleList {
    // Set Fields for DbSchedule
    private String serverResponse;
    private ArrayList<String> BillboardName;
    private ArrayList<String> StartTime;
    private ArrayList<String> Duration;
    private ArrayList<String> CreationDateTime;
    private ArrayList<String> Repeat;
    private ArrayList<String> Sunday;
    private ArrayList<String> Monday;
    private ArrayList<String> Tuesday;
    private ArrayList<String> Wednesday;
    private ArrayList<String> Thursday;
    private ArrayList<String> Friday;
    private ArrayList<String> Saturday;

    // Constructor for DbSchedule
    public ScheduleList(String serverResponse,
                        ArrayList<String> BillboardName,
                        ArrayList<String> StartTime,
                        ArrayList<String> Duration,
                        ArrayList<String> CreationDateTime,
                        ArrayList<String> Repeat,
                        ArrayList<String> Sunday,
                        ArrayList<String> Monday,
                        ArrayList<String> Tuesday,
                        ArrayList<String> Wednesday,
                        ArrayList<String> Thursday,
                        ArrayList<String> Friday,
                        ArrayList<String> Saturday) {
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
    public ArrayList<String> getScheduleBillboardName() {
        return BillboardName;
    }
    public ArrayList<String> getStartTime() {
        return StartTime;
    }
    public ArrayList<String> getDuration() {
        return Duration;
    }
    public ArrayList<String> getCreationDateTime() {
        return CreationDateTime;
    }
    public ArrayList<String> getRepeat() {
        return Repeat;
    }
    public ArrayList<String> getSunday() {
        return Sunday;
    }
    public ArrayList<String> getMonday() {
        return Monday;
    }
    public ArrayList<String> getTuesday() {
        return Tuesday;
    }
    public ArrayList<String> getWednesday() {
        return Wednesday;
    }
    public ArrayList<String> getThursday() {
        return Thursday;
    }
    public ArrayList<String> getFriday() {
        return Friday;
    }
    public ArrayList<String> getSaturday() {
        return Saturday;
    }
}
