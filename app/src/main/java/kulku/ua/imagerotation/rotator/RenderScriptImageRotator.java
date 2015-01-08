package kulku.ua.imagerotation.rotator;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.example.android.rs.hellocompute.ScriptC_flip;

import java.io.File;

import kulku.ua.imagerotation.utils.Utils;

/**
 * Created by andrii.lavrinenko on 07.01.2015.
 */
public class RenderScriptImageRotator extends ImageRotator {
    public final Context mContext;

    RenderScriptImageRotator(File targetFile, int angle, Context context) {
        super(targetFile, angle);
        mContext = context;
    }

    RenderScriptImageRotator(File targetFile, Context context) {
        this(targetFile, 270, context);
    }

    @Override
    public Bitmap rotatedImage() {
        Utils.logHeap("before source decodeSource");
        Bitmap source = getOriginalBitmap();
        if (getAngle() == 0) return source;
        int targetHeight = getAngle() == 180 ? source.getHeight() : source.getWidth();
        int targetWidth = getAngle() == 180 ? source.getWidth() : source.getHeight();
        Bitmap.Config config = source.getConfig();
        Utils.logHeap("after source decodeSource");

        RenderScript rs = RenderScript.create(mContext);
        Utils.logHeap("after createRenderScript");

        Utils.logHeap("before Allocation createFromBitmap");
        ScriptC_flip script = new ScriptC_flip(rs);
        script.set_sourceWidth(source.getWidth());
        script.set_sourceHeight(source.getHeight());
        Allocation sourceAllocation = Allocation.createFromBitmap(rs, source,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        source.recycle();
        script.set_sourceImage(sourceAllocation);

        Bitmap target = Bitmap.createBitmap(targetWidth, targetHeight, config);
        final Allocation targetAllocation = Allocation.createFromBitmap(rs, target,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        Utils.logHeap("after Allocation createFromBitmap");
        script.set_direction(getAngle());
        script.forEach_flip(targetAllocation, targetAllocation);
        Utils.logHeap("after rotateOperation");


        Utils.logHeap("before createTargetBitmap");
        Utils.logHeap("after createTargetBitmap");

        targetAllocation.copyTo(target);
        Utils.logHeap("after copyTo");

        rs.destroy();
        return target;
    }
}
