package kulku.ua.imagerotation.utils;

import android.graphics.Bitmap;

/**
 * Created by aindrias on 06.01.2015.
 */
public class Rotation {

    private int mAngle;
    private int mWidth;
    private int mHeight;

    public Rotation(Bitmap bitmap, int angle) {
        this(angle, bitmap.getWidth(), bitmap.getHeight());
    }

    public Rotation(int angle, int width, int height) {
        mAngle = angle;
        mWidth = width;
        mHeight = height;
        //                alpha => after[x][y] = before[cos(alpha)x - sin(alpha) y ][sin(alpha)x + cos(alpha)y];
    }

    public int getX(int x, int y) {
        switch (mAngle) {
            case 0:
                return x;
            case 90:
                return y;
            case 180:
                return mWidth - 1 - x;
            case 270:
                return mHeight - 1 - y;
            default:
                throw new IllegalStateException();
        }
    }

    public int getY(int x, int y) {
        switch (mAngle) {
            case 0:
                return y;
            case 90:
                return mWidth - 1 - x;
            case 180:
                return mHeight - 1 - y;
            case 270:
                return x;
            default:
                throw new IllegalStateException();
        }
    }
}
