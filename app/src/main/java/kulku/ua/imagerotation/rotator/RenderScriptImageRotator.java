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

    RenderScriptImageRotator(int angle, Context context) {
        super(angle);
        mContext = context;
    }

    @Override
    public Bitmap rotate(Bitmap bitmap) {
        Utils.logHeap("before source decodeSource");
        if (getAngle() == 0) return bitmap;
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

        int targetHeight = getAngle() == 180 ? bitmap.getHeight() : bitmap.getWidth();
        int targetWidth = getAngle() == 180 ? bitmap.getWidth() : bitmap.getHeight();
        Bitmap.Config config = bitmap.getConfig();
        Bitmap target = Bitmap.createBitmap(targetWidth, targetHeight, config);
        final Allocation targetAllocation = Allocation.createFromBitmap(rs, target,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        Utils.logHeap("after Allocation createFromBitmap");
        script.set_direction(getAngle());
        script.forEach_transform(targetAllocation, targetAllocation);
        Utils.logHeap("after rotateOperation");
        targetAllocation.copyTo(target);
        Utils.logHeap("after copyTo");

        rs.destroy();
        return target;
    }
}
