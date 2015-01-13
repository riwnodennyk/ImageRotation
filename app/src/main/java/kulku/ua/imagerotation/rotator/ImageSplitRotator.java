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
import kulku.ua.imagerotation.utils.Rotation;
import kulku.ua.imagerotation.utils.Utils;

/**
 * Created by aindrias on 06.01.2015.
 */
public class ImageSplitRotator extends ImageRotator {

    public static final String TAG = ImageSplitRotator.class.getSimpleName();
    public final int mRowsCols;
    private Matrix mRotateMatrix;
    private Rotation mRotation;
    private File[][] mRotatedPatches;
    private int mAngleCcw;
    private Bitmap mBitmap;

    ImageSplitRotator() {
        mRowsCols = 3;
    }

    public Bitmap rotate(Bitmap bitmap, int angleCcw) {
        mBitmap = bitmap;
        mAngleCcw = angleCcw;
        mRotateMatrix = new Matrix();
        mRotateMatrix.postRotate(mAngleCcw);

        mRotation = new Rotation(angleCcw, mRowsCols, mRowsCols);

        Bitmap combined = null;
        try {
            splitOriginalBitmap();
            combined = combineRotatedBitmap();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return combined;
    }


    private void splitOriginalBitmap() throws FileNotFoundException {
        mRotatedPatches = new File[mRowsCols][mRowsCols];

        int chunkHeight = mBitmap.getHeight() / mRowsCols;
        int chunkWidth = mBitmap.getWidth() / mRowsCols;
        for (int yCoord = 0, y = 0; y < mRowsCols; y++, yCoord += chunkHeight) {
            for (int xCoord = 0, x = 0; x < mRowsCols; x++, xCoord += chunkWidth) {
                Bitmap cutRotatedPatch =
                        Bitmap.createBitmap(mBitmap, xCoord, yCoord, chunkWidth, chunkHeight, mRotateMatrix, true);
                mRotatedPatches[x][y] = savePatch(cutRotatedPatch, x, y);
            }
        }
        Utils.logHeap("splitOriginalBitmap");
        mBitmap.recycle();
    }


    private Bitmap combineRotatedBitmap() throws FileNotFoundException {
        int rotatedHeight = Utils.newHeight(mBitmap, mAngleCcw);
        int rotatedWidth = Utils.newWidth(mBitmap, mAngleCcw);

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

}
