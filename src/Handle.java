/*
 * Copyright 2016 © Capital One Financial Corporation All Rights Reserved.
 * This software contains valuable trade secrets and proprietary information of Capital One and is
 * protected by law. It may not be copied or distributed in any form or medium, disclosed to third
 * parties, reverse engineered or used in any manner without prior written authorization from Capital One.
 */

// -------------------------------------------------------------------------

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
