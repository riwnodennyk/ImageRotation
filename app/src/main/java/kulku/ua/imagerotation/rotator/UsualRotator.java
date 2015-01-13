package kulku.ua.imagerotation.rotator;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import kulku.ua.imagerotation.utils.Utils;

/**
 * Created by andrii.lavrinenko on 08.01.2015.
 */
class UsualRotator extends ImageRotator {
    public UsualRotator(int angle) {
        super(angle);
    }

    @Override
    public Bitmap rotate(Bitmap bitmap) {
        Utils.logHeap("before Rotation");

        Matrix mRotateMatrix = new Matrix();
        mRotateMatrix.postRotate(getAngle());

        Bitmap target = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                mRotateMatrix, true);
        bitmap.recycle();
        Utils.logHeap("after Rotation");
        return target;
    }

}
