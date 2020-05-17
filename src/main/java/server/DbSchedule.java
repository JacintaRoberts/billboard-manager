package server;

public class DbSchedule {

    // Set Fields for DbSchedule
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
    public DbSchedule(String BillboardName, String Duration, String CreationDateTime, String Repeat,
                      String Sunday, String Monday, String Tuesday,
                      String Wednesday, String Thursday, String Friday, String Saturday) {
        this.BillboardName = BillboardName;
        this.Duration = Duration;
        this.CreationDateTime;
        this.Repeat;
        this.Sunday;
        this.Monday;
        this.Tuesday;
        this.Wednesday;
        this.Thursday;
        this.Friday;
        this.Saturday;
    }


}
