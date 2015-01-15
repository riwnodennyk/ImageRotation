package kulku.ua.imagerotation.rotator;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import kulku.ua.imagerotation.utils.Utils;

/**
 * Created by andrii.lavrinenko on 08.01.2015.
 */
class UsualRotator extends ImageRotator {

    @Override
    public Bitmap rotate(Bitmap bitmap, int angleCcw) {
        Utils.logHeap("UsualRotator before Rotation");

        Matrix mRotateMatrix = new Matrix();
        mRotateMatrix.postRotate(-angleCcw);
        Bitmap target = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                mRotateMatrix, true);
        if (angleCcw != 0)
            bitmap.recycle();
        Utils.logHeap("UsualRotator after Rotation");
        return target;
    }

}
