package own.alessio.com.robotbugs;

/**
 * MyWorld Created by alessio on 6.2.16.
 * Robot bugs movements and measurement methods.
 */
public class AntMovements {

    enum TurnInt {
        LEFT,
        NONE,
        RIGHT
    }

    enum MoveInt{
        FORWARD,
        STAY,
        BACKWARD
    }

    public static float angleOf(TurnInt turn) {
        switch (turn) {
            case LEFT:
                return -1.0f;
            case NONE:
                return 0.0f;
            case RIGHT:
                return 1.0f;
            default:
                return 0.0f;
        }
    }

    public static float lengthOf(MoveInt moveInt) {
        switch (moveInt) {
            case FORWARD:
                return 1.0f;
            case STAY:
                return 0.0f;
            case BACKWARD:
                return -1.0f;
            default:
                return 0.0f;
        }
    }
}