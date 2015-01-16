package kulku.ua.imagerotation.rotator;

import android.graphics.Bitmap;

import com.jni.bitmap_operations.JniBitmapHolder;

import kulku.ua.imagerotation.utils.Utils;

/**
 * Created by aindrias on 13.01.2015.
 */
public class NdkRotator extends ImageRotator {
    JniBitmapHolder bitmapHolder = new JniBitmapHolder();

    @Override
    public Bitmap rotate(Bitmap bitmap, int angleCcw) {
        Utils.logHeap("NdkRotator before storeBitmap");
        bitmapHolder.storeBitmap(bitmap);
        bitmap.recycle();
        Utils.logHeap("NdkRotator after storeBitmap");
        switch (angleCcw) {
            case FLIP_HORIZONTAL:
                bitmapHolder.flipBitmapHorizontal();
                break;
            case FLIP_VERTICAL:
                bitmapHolder.flipBitmapVertical();
                break;
            case 90:
                bitmapHolder.rotateBitmapCcw90();
                break;
            case 180:
                bitmapHolder.rotateBitmap180();
                break;
            case 270:
                bitmapHolder.rotateBitmapCw90();
                break;
        }
        Utils.logHeap("NdkRotator after rotate");

        Bitmap bitmapAndFree = bitmapHolder.getBitmapAndFree();
        Utils.logHeap("NdkRotator after getBitmapAndFree");
        return bitmapAndFree;
    }
}
