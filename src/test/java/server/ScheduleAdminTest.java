package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class ScheduleAdminTest {
    /* Test 0: Declaring ScheduleAdmin object
     * Description: ScheduleAdmin object should be running in background on application start.
     * Expected Output: ScheduleAdmin object is declared
     */
    ScheduleAdmin scheduleAdmin;

    /* Test 1: Constructing a ScheduleAdmin object
     * Description: ScheduleAdmin Object should be able to be created to handle requests from control panel
     * Expected Output: ScheduleAdmin object is instantiated from ScheduleAdmin class
     */
    @BeforeEach
    @Test
    public void setUpScheduleAdmin() {
        scheduleAdmin = new ScheduleAdmin();
    }

    /* Test 2: Declaring Database Mock object
     * Description: Database Mock simulates Maria DB for testing purposes.
     * Expected Output: DatabaseMock object for Schedule Table is declared
     */
    DatabaseMock<String, ArrayList<String>> scheduleTableMock;

    /* Test 3: Constructing a DatabaseMock object for Schedule Table
     * Description: DatabaseMock should be used to verify unit tests and setup of the DB
     * Expected Output: DatabaseMock object is instantiated to mimic Schedule Table
     */
    @BeforeEach
    @Test
    public void setUpScheduleTableMock() {
        scheduleTableMock = new DatabaseMock<>();
        // Add a dummy value (insert more as necessary or create separate function to populate)
        // TODO: CHECK DATE-TIME/TIMESTAMP OBJECTS STORAGE
        scheduleTableMock.addValue("11/04/2020-10:00", new ArrayList<String>( Arrays.asList("Billboard1", "2 mins")));
    }


    // TODO: Unit Testing for Server-Side Schedules


}