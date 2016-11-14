/**
 * The Handle stores position information about record returned by MemManager.
 */
public class Handle {

    /**
     * The position that MemManager returned.
     */
    private int position;

    public Handle( int position ) {

        this.position = position;
    }

    public int getPosition() {

        return position;
    }

    public void setPosition( int newPosition ) {

        this.position = newPosition;
    }

}
