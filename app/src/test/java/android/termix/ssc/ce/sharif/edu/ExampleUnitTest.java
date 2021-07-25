package android.termix.ssc.ce.sharif.edu;

import android.termix.ssc.ce.sharif.edu.model.CourseSession;
import android.termix.ssc.ce.sharif.edu.model.Session;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void conflict_isCorrect() {
        CourseSession main = new CourseSession(null, new Session(0, 10, 30, 12, 0));

        // scenario 1
        assertFalse(main.hasConflict(new CourseSession(null,
                new Session(0, 9, 0, 10, 30))));
        assertFalse(main.hasConflict(new CourseSession(null,
                new Session(0, 12, 0, 13, 30))));

        // scenario 2
        assertFalse(main.hasConflict(new CourseSession(null,
                new Session(0, 9, 0, 10, 0))));
        assertFalse(main.hasConflict(new CourseSession(null,
                new Session(0, 12, 30, 13, 30))));

        // scenario 3
        assertTrue(main.hasConflict(new CourseSession(null,
                new Session(0, 9, 0, 11, 0))));
        assertTrue(main.hasConflict(new CourseSession(null,
                new Session(0, 11, 0, 13, 0))));

        // scenario 4
        assertTrue(main.hasConflict(new CourseSession(null,
                new Session(0, 10, 0, 12, 30))));
        assertTrue(main.hasConflict(new CourseSession(null,
                new Session(0, 11, 0, 11, 30))));

        // scenario 5
        assertTrue(main.hasConflict(new CourseSession(null,
                new Session(0, 10, 30, 11, 0))));
        assertTrue(main.hasConflict(new CourseSession(null,
                new Session(0, 11, 30, 12, 0))));
        assertTrue(main.hasConflict(new CourseSession(null,
                new Session(0, 10, 30, 12, 30))));
        assertTrue(main.hasConflict(new CourseSession(null,
                new Session(0, 10, 0, 12, 0))));
    }
}