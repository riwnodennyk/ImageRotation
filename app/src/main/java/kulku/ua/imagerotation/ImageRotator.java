package kulku.ua.imagerotation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by aindrias on 06.01.2015.
 */
public class ImageRotator {

    public static final String TAG = ImageRotator.class.getSimpleName();
    public final int mRowsCols;
    public final int mAngle;
    private final BitmapFactory.Options mOptions;
    private final Matrix mRotateMatrix;
    private final Rotation mRotation;
    private File mTargetFile;
    private int mOriginalHeight;
    private int mOriginalWidth;
    private File[][] mRotatedPatches;
    private int mChunkHeight;
    private int mChunkWidth;

    public ImageRotator(File targetFile) {
        this(targetFile, 90);
    }

    public ImageRotator(File targetFile, int angle) {
        mTargetFile = targetFile;
        mAngle = angle;

        mRowsCols = 3;

        mRotateMatrix = new Matrix();
        mRotateMatrix.postRotate(mAngle);

        mRotation = new Rotation(mAngle, mRowsCols);

        mOptions = new BitmapFactory.Options();
        mOptions.inSampleSize = 1;
        mOptions.inPurgeable = true;
        mOptions.inInputShareable = true;
        mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }

    public void rotateImage() throws FileNotFoundException {
        long time = System.currentTimeMillis();
        measureDimensions();
        splitOriginalBitmap();
        combineRotatedBitmap();
        Log.d(TAG, "combined in " + (System.currentTimeMillis() - time));
    }

    private void measureDimensions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(mTargetFile.getPath(), options);
        mOriginalHeight = options.outHeight;
        mOriginalWidth = options.outWidth;

        mChunkHeight = mOriginalHeight / mRowsCols;
        mChunkWidth = mOriginalWidth / mRowsCols;
    }

    private void splitOriginalBitmap() throws FileNotFoundException {
        mRotatedPatches = new File[mRowsCols][mRowsCols];
        Bitmap bitmap = BitmapFactory.decodeFile(mTargetFile.getPath(), mOptions);
        for (int yCoord = 0, y = 0; y < mRowsCols; y++, yCoord += mChunkHeight) {
            for (int xCoord = 0, x = 0; x < mRowsCols; x++, xCoord += mChunkWidth) {
                Bitmap rotatedPatch = cutRotatedPatch(bitmap, xCoord, yCoord);
                mRotatedPatches[x][y] = savePatch(rotatedPatch, x, y);
            }
        }
        bitmap.recycle();
    }


    private void combineRotatedBitmap() throws FileNotFoundException {
        int rotatedHeight = (mAngle == 0 || mAngle == 180) ? mOriginalHeight : mOriginalWidth;
        int rotatedWidth = (mAngle == 0 || mAngle == 180) ? mOriginalWidth : mOriginalHeight;

        Bitmap rotatedBitmap = Bitmap.createBitmap(rotatedWidth, rotatedHeight,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(rotatedBitmap);

        int rotatedChunkHeight = rotatedHeight / mRowsCols;
        int rotatedChunkWidth = rotatedWidth / mRowsCols;
        for (int x = 0; x < mRowsCols; x++)
            for (int y = 0; y < mRowsCols; y++) {
                File file = mRotatedPatches[mRotation.getX(x, y)][mRotation.getY(x, y)];
                drawPatchOntoCanvas(canvas, x * rotatedChunkWidth, y * rotatedChunkHeight, file);
                file.delete();
            }
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(mTargetFile);
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // do nothing here
                }
            }
        }
        rotatedBitmap.recycle();
    }


    private void drawPatchOntoCanvas(Canvas canvas, int xCoord, int yCoord, File patch) {
        Bitmap bitmap = BitmapFactory.decodeFile(patch.getPath(), mOptions);
        canvas.drawBitmap(bitmap, xCoord, yCoord, null);
        bitmap.recycle();
    }

    private File savePatch(Bitmap pieceOfBitmap, int x, int y) throws FileNotFoundException {
        File file = new File(MyActivity.STORAGE_EMULATED_0_DCIM_CAMERA + "temp"
                + x + "_" + y + "." + "jpg");

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            pieceOfBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);// Using PNG increases the execution time from 7 sec to 30 sec
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // do nothing here
                }
            }
        }
        pieceOfBitmap.recycle();
        return file;
    }

    private Bitmap cutRotatedPatch(Bitmap bitmap, int xCoord, int yCoord) {
        return Bitmap.createBitmap(bitmap, xCoord, yCoord, mChunkWidth, mChunkHeight, mRotateMatrix, true);
    }
}
