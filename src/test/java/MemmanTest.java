import junit.framework.TestCase;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * MemMan Test class.
 */
public class MemmanTest extends TestCase {

    @Test
    public void test() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {

        String[] str = {"10", "32", "input.txt"};
        Memman.main( str );
    }

}
