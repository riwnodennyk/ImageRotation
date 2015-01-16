package kulku.ua.imagerotation.utils;

import android.graphics.Bitmap;
import android.os.Debug;
import android.util.Log;

import java.text.DecimalFormat;

import kulku.ua.imagerotation.MyActivity;
import kulku.ua.imagerotation.rotator.ImageRotator;

/**
 * Created by andrii.lavrinenko on 08.01.2015.
 */
public class Utils {
    public static void logHeap(String s) {
        Double allocated = (double) Debug.getNativeHeapAllocatedSize() / (double) (1048576);
        Double available = (double) Debug.getNativeHeapSize() / 1048576.0;
        Double free = (double) Debug.getNativeHeapFreeSize() / 1048576.0;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        Log.d(ImageRotator.TAG, "debug. ======= " + s);
        Log.d(ImageRotator.TAG, "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)");
        Log.d(ImageRotator.TAG, "debug.memory: allocated: "
                + df.format((double) (Runtime.getRuntime().totalMemory() / 1048576))
                + "MB of " + df.format((double) (Runtime.getRuntime().maxMemory() / 1048576))
                + "MB (" + df.format((double) (Runtime.getRuntime().freeMemory() / 1048576)) + "MB free)");
    }

    public static Bitmap downscaleToMaximum(Bitmap bitmap) {
        if (bitmap.isRecycled())
            throw new IllegalArgumentException("A recycled bitmap " + bitmap);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        if (height > MyActivity.MAX_BITMAP_SIZE) {
            width *= MyActivity.MAX_BITMAP_SIZE / height;
            height = (int) MyActivity.MAX_BITMAP_SIZE;
        }
        if (width > MyActivity.MAX_BITMAP_SIZE) {
            height *= MyActivity.MAX_BITMAP_SIZE / width;
            width = (int) MyActivity.MAX_BITMAP_SIZE;
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public static int newHeight(Bitmap bitmap, int angleCcw) {
        return angleCcw == 90 || angleCcw == 270 ? bitmap.getWidth() : bitmap.getHeight();
    }

    public static int newWidth(Bitmap bitmap, int angleCcw) {
        return angleCcw == 90 || angleCcw == 270 ? bitmap.getHeight() : bitmap.getWidth();
    }
}
