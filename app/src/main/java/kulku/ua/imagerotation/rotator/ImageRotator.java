package kulku.ua.imagerotation.rotator;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by andrii.lavrinenko on 07.01.2015.
 */
public abstract class ImageRotator {
    public static final String TAG = "ImageRotator";
    public final static int FLIP_VERTICAL = -1;
    public final static int FLIP_HORIZONTAL = -2;

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

    public static ImageRotator openGL() {
        throw new IllegalStateException(""); //todo
    }

    public static ImageRotator pixelByPixel() {
        return new PixelByPixelFileRotator();
    }

    public abstract android.graphics.Bitmap rotate(Bitmap bitmap, int angleCcw);

}
