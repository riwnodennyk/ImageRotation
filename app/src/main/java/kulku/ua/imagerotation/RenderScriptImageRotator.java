package kulku.ua.imagerotation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Debug;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

/**
 * Created by andrii.lavrinenko on 07.01.2015.
 */
public class RenderScriptImageRotator extends ImageRotator {
    public static final String TAG = "RenderScriptImageRotator";
    public final Context mContext;

    public RenderScriptImageRotator(File targetFile, int angle, Context context) {
        super(targetFile, angle);
        mContext = context;
    }

    public RenderScriptImageRotator(File targetFile, Context context) {
        this(targetFile, 90, context);
    }

    public static void logHeap(String s) {
        Double allocated = (double) Debug.getNativeHeapAllocatedSize() / (double) (1048576);
        Double available = (double) Debug.getNativeHeapSize() / 1048576.0;
        Double free = (double) Debug.getNativeHeapFreeSize() / 1048576.0;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);

        Log.d(TAG, "debug. ======= " + s);
        Log.d(TAG, "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free)");
        Log.d(TAG, "debug.memory: allocated: "
                + df.format((double) (Runtime.getRuntime().totalMemory() / 1048576))
                + "MB of " + df.format((double) (Runtime.getRuntime().maxMemory() / 1048576))
                + "MB (" + df.format((double) (Runtime.getRuntime().freeMemory() / 1048576)) + "MB free)");
    }

    public Bitmap blur() {
        logHeap("before source decodeSource");
        Bitmap source = BitmapFactory.decodeFile(getTargetFile().getPath());
        logHeap("after source decodeSource");

        final RenderScript rs = RenderScript.create(mContext);
        logHeap("after createRenderScript");

        Allocation blur = blur(source, rs);
        logHeap("after blur");

        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap.Config config = source.getConfig();
        source.recycle();

        logHeap("before createTargetBitmap");
        Bitmap target = Bitmap.createBitmap(width, height, config);
        logHeap("after createTargetBitmap");

        blur.copyTo(target);
        logHeap("after copyTo");

        rs.destroy();
        return target;

        //Make the image greyscale
//        final ScriptIntrinsicColorMatrix scriptColor =
//                ScriptIntrinsicColorMatrix.create(rs, Element.U8_4(rs));
//        scriptColor.setGreyscale();
//        scriptColor.forEach(input, output);
//        Bitmap grayBitmap = source.copy(source.getConfig(), true);
//        output.copyTo(grayBitmap);

        //Show the results
//        mNormalImage.setImageBitmap(source);
//        mBlurImage.setImageBitmap(outBitmap);
//        mColorImage.setImageBitmap(grayBitmap);
        //We don't need RenderScript anymore
    }

    private Allocation blur(Bitmap inBitmap, RenderScript rs) {
        logHeap("before Allocation createFromBitmap");
        final Allocation input = Allocation.createFromBitmap(rs, inBitmap,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        logHeap("after Allocation createFromBitmap");

        final ScriptIntrinsicBlur script =
                ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        logHeap("after ScriptIntrinsicBlur");

        script.setRadius(25f);
        script.setInput(input);
        script.forEach(output);
        return output;
    }

    @Override
    public void rotateImage() throws FileNotFoundException {
    }
}
