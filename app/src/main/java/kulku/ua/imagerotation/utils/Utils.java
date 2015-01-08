package kulku.ua.imagerotation.utils;

import android.os.Debug;
import android.util.Log;

import java.text.DecimalFormat;

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
}
