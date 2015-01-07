package kulku.ua.imagerotation;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by andrii.lavrinenko on 07.01.2015.
 */
public abstract class ImageRotator {

    private final int mAngle;
    private final File mTargetFile;

    public ImageRotator(File targetFile, int angle) {
        mTargetFile = targetFile;
        mAngle = angle;
    }

    public abstract void rotateImage() throws FileNotFoundException;

    public int getAngle() {
        return mAngle;
    }

    public File getTargetFile() {
        return mTargetFile;
    }
}
