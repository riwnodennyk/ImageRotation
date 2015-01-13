package kulku.ua.imagerotation.rotator;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import kulku.ua.imagerotation.utils.Utils;

/**
 * Created by andrii.lavrinenko on 11.01.2015.
 */
class JniRotator extends ImageRotator {
    JniRotator(int angle) {
        super(angle);
    }

    @Override
    public Bitmap rotate(Bitmap bitmap) {
        Utils.logHeap("before Rotation");

        Matrix mRotateMatrix = new Matrix();
        mRotateMatrix.postRotate(getAngle());

        Bitmap target = getBitmap(bitmap, mRotateMatrix);
        bitmap.recycle();
        Utils.logHeap("after Rotation");
        return target;
    }

    private Bitmap getBitmap(Bitmap bitmap, Matrix mRotateMatrix) {
        return null;
    }
}
