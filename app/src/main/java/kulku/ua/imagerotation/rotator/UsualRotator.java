package kulku.ua.imagerotation.rotator;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.File;

import kulku.ua.imagerotation.utils.Utils;

/**
 * Created by andrii.lavrinenko on 08.01.2015.
 */
class UsualRotator extends ImageRotator {
    public UsualRotator(File targetFile) {
        super(targetFile, 90);
    }

    @Override
    public Bitmap rotatedImage() {
        Utils.logHeap("before Rotation");
        Bitmap original = getOriginalBitmap();

        Matrix mRotateMatrix = new Matrix();
        mRotateMatrix.postRotate(getAngle());

        Bitmap target = Bitmap.createBitmap(original, 0, 0, original.getWidth(), original.getHeight(),
                mRotateMatrix, true);
        original.recycle();
        Utils.logHeap("after Rotation");
        return target;
    }

}
