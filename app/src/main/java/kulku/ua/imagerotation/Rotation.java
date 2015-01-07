package kulku.ua.imagerotation;

/**
 * Created by aindrias on 06.01.2015.
 */
public class Rotation {

    private int mAngle;
    private int mSize;

    public Rotation(int angle, int size) {
        mAngle = angle;
        mSize = size;
//                alpha => after[x][y] = before[cos(alpha)x - sin(alpha) y ][sin(alpha)x + cos(alpha)y];
    }

    public int getX(int x, int y) {
        switch (mAngle) {
            case 0:
                return x;
            case 90:
                return y;
            case 180:
                return mSize - 1 - x;
            case -90:
                return mSize - 1 - y;
            default:
                throw new IllegalStateException();
        }
    }

    public int getY(int x, int y) {
        switch (mAngle) {
            case 0:
                return y;
            case 90:
                return mSize - 1 - x;
            case 180:
                return mSize - 1 - y;
            case -90:
                return x;
            default:
                throw new IllegalStateException();
        }
    }
}
