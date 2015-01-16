package kulku.ua.imagerotation.rotator;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import kulku.ua.imagerotation.utils.Utils;
import ua.kulku.rs.ScriptC_rotator;

/**
 * Created by andrii.lavrinenko on 07.01.2015.
 */
public class RenderScriptImageRotator extends ImageRotator {
    public final Context mContext;

    RenderScriptImageRotator(Context context) {
        mContext = context;
    }

    @Override
    public Bitmap rotate(Bitmap bitmap, int angleCcw) {
        Utils.logHeap("before source decodeSource");
        if (angleCcw == 0) return bitmap;
        Utils.logHeap("after source decodeSource");

        RenderScript rs = RenderScript.create(mContext);
        Utils.logHeap("after createRenderScript");

        ScriptC_rotator script = new ScriptC_rotator(rs);
        script.set_inWidth(bitmap.getWidth());
        script.set_inHeight(bitmap.getHeight());
        Allocation sourceAllocation = Allocation.createFromBitmap(rs, bitmap,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        bitmap.recycle();
        script.set_inImage(sourceAllocation);

        int targetHeight = Utils.newHeight(bitmap, angleCcw);
        int targetWidth = Utils.newWidth(bitmap, angleCcw);
        Bitmap.Config config = bitmap.getConfig();
        Bitmap target = Bitmap.createBitmap(targetWidth, targetHeight, config);
        final Allocation targetAllocation = Allocation.createFromBitmap(rs, target,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        Utils.logHeap("after Allocation createFromBitmap");
        switch (angleCcw) {
            case FLIP_HORIZONTAL:
                script.forEach_flip_horizontally(targetAllocation, targetAllocation);
                break;
            case FLIP_VERTICAL:
                script.forEach_flip_vertically(targetAllocation, targetAllocation);
                break;
            case 90:
                script.forEach_rotate_90_clockwise(targetAllocation, targetAllocation);
                break;
            case 180:
                script.forEach_flip_vertically(targetAllocation, targetAllocation);
                break;
            case 270:
                script.forEach_rotate_270_clockwise(targetAllocation, targetAllocation);
                break;
        }
        Utils.logHeap("after rotateOperation");
        targetAllocation.copyTo(target);
        Utils.logHeap("after copyTo");

        rs.destroy();
        return target;
    }
}
