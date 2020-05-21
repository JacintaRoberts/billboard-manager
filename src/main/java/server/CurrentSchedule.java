package server;

import java.util.ArrayList;

public class CurrentSchedule {
    // Set Fields for DbSchedule
    private String serverResponse;
    private ArrayList<String> BillboardName;
    private ArrayList<String> StartTime;
    private ArrayList<String> CreationDateTime;

    // Constructor for DbSchedule
    public CurrentSchedule(String serverResponse,
                        ArrayList<String> BillboardName,
                        ArrayList<String> StartTime,
                        ArrayList<String> CreationDateTime) {
        this.serverResponse = serverResponse;
        this.BillboardName = BillboardName;
        this.StartTime = StartTime;
        this.CreationDateTime = CreationDateTime;
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
    public ArrayList<String> getCreationDateTime() {
        return CreationDateTime;
    }
}
