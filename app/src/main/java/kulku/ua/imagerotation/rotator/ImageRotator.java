package kulku.ua.imagerotation.rotator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Created by andrii.lavrinenko on 07.01.2015.
 */
public abstract class ImageRotator {

    public static final String TAG = "ImageRotator";
    private final int mAngle;
    private final File mOriginalFile;

    public ImageRotator(File originalFile, int angle) {
        mOriginalFile = originalFile;
        mAngle = angle;
    }

    public static ImageRotator split(File targetFile) {
        return new ImageSplitRotator(targetFile);
    }

    public static ImageRotator renderScript(File targetFile, Context context) {
        return new RenderScriptImageRotator(targetFile, context);
    }

    public static ImageRotator usual(File targetFile) {
        return new UsualRotator(targetFile);
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

    public abstract android.graphics.Bitmap rotatedImage();

    public int getAngle() {
        return mAngle;
    }

    public Bitmap getOriginalBitmap() {
        return BitmapFactory.decodeFile(getOriginalFile().getPath());
    }

    public File getOriginalFile() {
        return mOriginalFile;
    }

}
