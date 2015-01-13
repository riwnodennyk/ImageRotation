package kulku.ua.imagerotation.rotator;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.Arrays;

/**
 * Created by andrii.lavrinenko on 07.01.2015.
 */
public abstract class ImageRotator {

    public static final String TAG = "ImageRotator";
    private final int mAngle;

    public ImageRotator(int angle) {
        mAngle = angle;
        if (!Arrays.asList(0, 90, 180, 270).contains(angle)) {
            throw new IllegalArgumentException("angle of " + angle + " is not supported.");
        }
    }

    public static ImageRotator split(int angle) {
        return new ImageSplitRotator(angle);
    }

    public static ImageRotator renderScript(int angle, Context context) {
        return new RenderScriptImageRotator(angle, context);
    }

    public static ImageRotator usual(int angle) {
        return new UsualRotator(angle);
    }

    public static ImageRotator jni(Context context, int angle) {
       return new JniRotator(angle);
    }

    public static ImageRotator openGL(Context context) {
        throw new IllegalStateException("");
    }

    public static ImageRotator bitByBit(int angle) {
        return new PixelByPixelFileRotator(angle);
    }

    public abstract android.graphics.Bitmap rotate(Bitmap bitmap);

    public int getAngle() {
        return mAngle;
    }

}
