package kulku.ua.imagerotation.rotator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import kulku.ua.imagerotation.MyActivity;
import kulku.ua.imagerotation.utils.Utils;

/**
 * Created by aindrias on 06.01.2015.
 */
public class ImageSplitRotator extends ImageRotator {

    public static final String TAG = ImageSplitRotator.class.getSimpleName();
    public final int mRowsCols;
    private final Matrix mRotateMatrix;
    private final Rotation mRotation;
    private int mOriginalHeight;
    private int mOriginalWidth;
    private File[][] mRotatedPatches;
    private int mChunkHeight;
    private int mChunkWidth;

    ImageSplitRotator(int angle) {
        super(angle);
        mRowsCols = 3;

        mRotateMatrix = new Matrix();
        mRotateMatrix.postRotate(getAngle());

        mRotation = new Rotation(getAngle(), mRowsCols);
    }

    public Bitmap rotate(Bitmap bitmap) {
        Bitmap combined = null;
        try {
            splitOriginalBitmap(bitmap);
            combined = combineRotatedBitmap();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return combined;
    }


    private void splitOriginalBitmap(Bitmap bitmap) throws FileNotFoundException {
        mOriginalHeight = bitmap.getHeight();
        mOriginalWidth = bitmap.getWidth();

        mChunkHeight = mOriginalHeight / mRowsCols;
        mChunkWidth = mOriginalWidth / mRowsCols;

        mRotatedPatches = new File[mRowsCols][mRowsCols];
        for (int yCoord = 0, y = 0; y < mRowsCols; y++, yCoord += mChunkHeight) {
            for (int xCoord = 0, x = 0; x < mRowsCols; x++, xCoord += mChunkWidth) {
                Bitmap rotatedPatch = cutRotatedPatch(bitmap, xCoord, yCoord);
                mRotatedPatches[x][y] = savePatch(rotatedPatch, x, y);
            }
        }
        Utils.logHeap("splitOriginalBitmap");
        bitmap.recycle();
    }


    private Bitmap combineRotatedBitmap() throws FileNotFoundException {
        int rotatedHeight = (getAngle() == 0 || getAngle() == 180) ? mOriginalHeight : mOriginalWidth;
        int rotatedWidth = (getAngle() == 0 || getAngle() == 180) ? mOriginalWidth : mOriginalHeight;

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
        Utils.logHeap("combineRotatedBitmap");
        return rotatedBitmap;
    }

    private void drawPatchOntoCanvas(Canvas canvas, int xCoord, int yCoord, File patch) {
        Bitmap bitmap = BitmapFactory.decodeFile(patch.getPath());
        canvas.drawBitmap(bitmap, xCoord, yCoord, null);//todo try using matrix here
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

    /**
     * Created by aindrias on 06.01.2015.
     */
    public static class Rotation {

        private int mAngle;
        private int mSize;

        public Rotation(int angle, int size) {
            mAngle = angle;
            mSize = size;
            //                alpha => after[x][y] = before[cos(alpha)x - sin(alpha) y ][sin(alpha)x + cos(alpha)y];
        }

        public int getX(int x, int y) {
            switch (mAngle) {
                case 0:
                    return x;
                case 90:
                    return y;
                case 180:
                    return mSize - 1 - x;
                case -90:
                    return mSize - 1 - y;
                default:
                    throw new IllegalStateException();
            }
        }

        public int getY(int x, int y) {
            switch (mAngle) {
                case 0:
                    return y;
                case 90:
                    return mSize - 1 - x;
                case 180:
                    return mSize - 1 - y;
                case -90:
                    return x;
                default:
                    throw new IllegalStateException();
            }
        }
    }
}
