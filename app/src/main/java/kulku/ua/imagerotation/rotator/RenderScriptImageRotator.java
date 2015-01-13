package kulku.ua.imagerotation.rotator;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.example.android.rs.hellocompute.ScriptC_flip;

import kulku.ua.imagerotation.utils.Utils;

/**
 * Created by andrii.lavrinenko on 07.01.2015.
 */
public class RenderScriptImageRotator extends ImageRotator {
    public final Context mContext;

    RenderScriptImageRotator(Context context) {
        super();
        mContext = context;
    }

    @Override
    public Bitmap rotateImage(Bitmap bitmap, int angleCcw) {
        Utils.logHeap("RenderScriptImageRotator before source decodeSource");
        if (angleCcw == 0) return bitmap;
        int targetHeight = angleCcw == 180 ? bitmap.getHeight() : bitmap.getWidth();
        int targetWidth = angleCcw == 180 ? bitmap.getWidth() : bitmap.getHeight();
        Bitmap.Config config = bitmap.getConfig();
        Utils.logHeap("RenderScriptImageRotator after source decodeSource");

        RenderScript rs = RenderScript.create(mContext);
        Utils.logHeap("RenderScriptImageRotator after createRenderScript");

        ScriptC_flip script = new ScriptC_flip(rs);
        script.set_sourceWidth(bitmap.getWidth());
        script.set_sourceHeight(bitmap.getHeight());
        Allocation sourceAllocation = Allocation.createFromBitmap(rs, bitmap,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        bitmap.recycle();
        script.set_sourceImage(sourceAllocation);

        Bitmap target = Bitmap.createBitmap(targetWidth, targetHeight, config);
        Utils.logHeap("RenderScriptImageRotator before Allocation.createFromBitmap");
        final Allocation targetAllocation = Allocation.createFromBitmap(rs, target,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        Utils.logHeap("RenderScriptImageRotator after Allocation createFromBitmap");
        script.set_direction(angleCcw);
        script.forEach_flip(targetAllocation, targetAllocation);
        Utils.logHeap("RenderScriptImageRotator after rotateOperation");

        targetAllocation.copyTo(target);
        Utils.logHeap("RenderScriptImageRotator  after copyTo");

        rs.destroy();
        return target;
    }
}
