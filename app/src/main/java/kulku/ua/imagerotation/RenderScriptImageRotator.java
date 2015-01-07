package kulku.ua.imagerotation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by andrii.lavrinenko on 07.01.2015.
 */
public class RenderScriptImageRotator extends ImageRotator {
    public final Context mContext;

    public RenderScriptImageRotator(File targetFile, int angle, Context context) {
        super(targetFile, angle);
        mContext = context;
    }

    public RenderScriptImageRotator(File targetFile, Context context) {
        this(targetFile, 90, context);
    }

    public Bitmap blur() {
        Bitmap inBitmap = BitmapFactory.decodeFile(getTargetFile().getPath());

        //Create the context and I/O allocations
        final RenderScript rs = RenderScript.create(mContext);
        final Allocation input = Allocation.createFromBitmap(rs, inBitmap,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());

        //Blur the image
        final ScriptIntrinsicBlur script =
                ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(40f);
        script.setInput(input);
        script.forEach(output);
        Bitmap outBitmap = inBitmap.copy(inBitmap.getConfig(), true);
        output.copyTo(outBitmap);

        //Make the image greyscale
//        final ScriptIntrinsicColorMatrix scriptColor =
//                ScriptIntrinsicColorMatrix.create(rs, Element.U8_4(rs));
//        scriptColor.setGreyscale();
//        scriptColor.forEach(input, output);
//        Bitmap grayBitmap = inBitmap.copy(inBitmap.getConfig(), true);
//        output.copyTo(grayBitmap);

        rs.destroy();

        return outBitmap;
        //Show the results
//        mNormalImage.setImageBitmap(inBitmap);
//        mBlurImage.setImageBitmap(outBitmap);
//        mColorImage.setImageBitmap(grayBitmap);
        //We don't need RenderScript anymore
    }

    @Override
    public void rotateImage() throws FileNotFoundException {
    }
}
