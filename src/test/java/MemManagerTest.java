import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Test MemManager class.
 */
public class MemManagerTest extends TestCase {

    /**
     * declaration
     */
    MemManager mem;

    String name = "Blind Lemon Jefferson";

    @Before
    public void setUp()
            throws Exception {
        //
        mem = new MemManager( 32 );
    }


    @Test
    public void testGet() {

        byte[] data = Client.stringToByte( name );
        Handle handle = mem.insert( data, data.length );
        byte[] space = new byte[ data.length ];
        int copySize = mem.get( space, handle, space.length );
        assertEquals( 21, copySize );
    }

}
