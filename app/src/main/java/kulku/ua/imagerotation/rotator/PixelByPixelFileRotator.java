package kulku.ua.imagerotation.rotator;

import android.graphics.Bitmap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import kulku.ua.imagerotation.MyActivity;

/**
 * Created by andrii.lavrinenko on 11.01.2015.
 */
public class PixelByPixelFileRotator extends ImageRotator {
    private static final String ROTATED_IMAGE_FILENAME = MyActivity.STORAGE_EMULATED_0_DCIM_CAMERA + "temp"
            + "." + "jpg";

    PixelByPixelFileRotator(int angle) {
        super(angle);
    }

    @Override
    public Bitmap rotate(Bitmap bitmap) {
        if (getAngle() != 90)
            throw new IllegalStateException("");
        try {
            writeToTemp(bitmap);

            final int newWidth = bitmap.getHeight();
            final int newHeight = bitmap.getWidth();
            Bitmap.Config config = bitmap.getConfig();
            return readFromTemp(newWidth, newHeight, config);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap readFromTemp(int width, int height, Bitmap.Config config) throws IOException {
        Bitmap rotatedBitmap = Bitmap.createBitmap(width, height, config);
        DataInputStream inputStream = null;
        try {
            inputStream = new DataInputStream(
                    new BufferedInputStream(new FileInputStream(ROTATED_IMAGE_FILENAME)));
            for (int y = 0; y < height; ++y)
                for (int x = 0; x < width; ++x) {
                    final int pixel = inputStream.readInt();
                    rotatedBitmap.setPixel(x, y, pixel);
                }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        new File(ROTATED_IMAGE_FILENAME).delete();
        return rotatedBitmap;
    }

    private void writeToTemp(Bitmap originalBitmap) throws IOException {
        DataOutputStream outStream = null;
        try {
            outStream = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(ROTATED_IMAGE_FILENAME)));
            final int height = originalBitmap.getHeight();
            for (int x = 0; x < originalBitmap.getWidth(); ++x)
                for (int y = height - 1; y >= 0; --y) {
                    final int pixel = originalBitmap.getPixel(x, y);
                    outStream.writeInt(pixel);
                }
            outStream.flush();
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException ignored) {
                }
            }
        }
        originalBitmap.recycle();
    }
}
