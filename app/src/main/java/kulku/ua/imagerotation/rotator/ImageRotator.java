package kulku.ua.imagerotation.rotator;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by andrii.lavrinenko on 07.01.2015.
 */
public abstract class ImageRotator {

    public static final String TAG = "ImageRotator";

    public ImageRotator() {
    }

    public static ImageRotator split() {
        return new ImageSplitRotator();
    }

    public static ImageRotator renderScript(Context context) {
        return new RenderScriptImageRotator(context);
    }

    public static ImageRotator ndk() {
        return new NdkRotator();
    }

    public static ImageRotator usual() {
        return new UsualRotator();
    }

    public static ImageRotator jni(File targetFile, Context context) {
        throw new IllegalStateException("");
    }

    public static ImageRotator openGL(File targetFile, Context context) {
        throw new IllegalStateException("");
    }

    public static ImageRotator bitByBit(File targetFile, Context context) {
        throw new IllegalStateException("");
    }

    public abstract android.graphics.Bitmap rotateImage(Bitmap bitmap, int angleCcw);

}
