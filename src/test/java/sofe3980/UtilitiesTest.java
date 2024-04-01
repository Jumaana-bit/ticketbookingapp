package sofe3980;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalTime;

public class UtilitiesTest {

    @Test
    public void testConvertTo12HourFormat() {
        // Example time in 24-hour format
        LocalTime time = LocalTime.of(14, 30); // 14:30 in 24-hour format
        String expected = "02:30 PM"; // Expected 12-hour format
        
        String result = Utilities.convertTo12HourFormat(time);
        assertEquals("Time should be converted to 12-hour format correctly", expected, result);
    }

    @Test
    public void testConvertTo24HourFormat() {
        // Example time in 12-hour format
        String time = "02:30 PM"; // 2:30 PM in 12-hour format
        LocalTime expected = LocalTime.of(14, 30); // Expected 24-hour format
        
        LocalTime result = Utilities.convertTo24HourFormat(time);
        assertEquals("Time should be converted to 24-hour format correctly", expected, result);
    }
}
