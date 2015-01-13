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
import kulku.ua.imagerotation.utils.Rotation;
import kulku.ua.imagerotation.utils.Utils;

/**
 * Created by andrii.lavrinenko on 11.01.2015.
 */
public class PixelByPixelFileRotator extends ImageRotator {
    private static final String ROTATED_IMAGE_FILENAME = MyActivity.STORAGE_EMULATED_0_DCIM_CAMERA + "temp"
            + "." + "jpg";
    private Rotation mRotation;


    @Override
    public Bitmap rotate(Bitmap bitmap, int angleCcw) {
        try {
            writeToTemp(bitmap);

            mRotation = new Rotation(bitmap, angleCcw);
            return readFromTemp(bitmap, angleCcw);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap readFromTemp(Bitmap bitmap, int angleCcw) throws IOException {
        final int newWidth = Utils.newWidth(bitmap, angleCcw);
        final int newHeight = Utils.newHeight(bitmap, angleCcw);
        Bitmap rotatedBitmap = Bitmap.createBitmap(newWidth, newHeight, bitmap.getConfig());
        DataInputStream inputStream = null;
        try {
            inputStream = new DataInputStream(
                    new BufferedInputStream(new FileInputStream(ROTATED_IMAGE_FILENAME)));
            int originalHeight = bitmap.getHeight();
            for (int x = 0; x < bitmap.getWidth(); ++x) {
                for (int y = 0; y < originalHeight; ++y) {
                    final int pixel = inputStream.readInt();
                    rotatedBitmap.setPixel(mRotation.getX(x, y), mRotation.getY(x, y), pixel);
                }
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
                for (int y = 0; y < height; ++y) {
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
