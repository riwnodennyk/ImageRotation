package kulku.ua.imagerotation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import kulku.ua.imagerotation.rotator.ImageRotator;
import kulku.ua.imagerotation.utils.Utils;

import static java.util.Arrays.asList;
import static kulku.ua.imagerotation.rotator.ImageRotator.FLIP_HORIZONTAL;
import static kulku.ua.imagerotation.rotator.ImageRotator.FLIP_VERTICAL;
import static kulku.ua.imagerotation.rotator.ImageRotator.ndk;
import static kulku.ua.imagerotation.rotator.ImageRotator.renderScript;
import static kulku.ua.imagerotation.rotator.ImageRotator.usual;


public class MyActivity extends Activity {

    public static final String STORAGE_EMULATED_0_DCIM_CAMERA = "/storage/emulated/0/DCIM/Camera/";
    public static final String PATH = STORAGE_EMULATED_0_DCIM_CAMERA + "IMG_20141206_150847.jpg";
    public static final float MAX_BITMAP_SIZE = 4096f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        testDrive();
//        setImage();
    }

    private void testDrive() {
//        while(true)
        for (ImageRotator imageRotator : asList(renderScript(this), ndk())) {
            for (Integer angle : asList(90, 180, FLIP_HORIZONTAL, FLIP_VERTICAL)) {
                Bitmap bitmap = BitmapFactory.decodeFile(PATH);
                long l = System.currentTimeMillis();
                imageRotator.rotate(bitmap, angle);
                Log.d("Rotated to " + angle, "" + imageRotator.getClass().getSimpleName()
                        + " " + (System.currentTimeMillis() - l) + " ms");
            }
        }
    }

    private void setImage() {
        ImageView imageContainer = (ImageView) findViewById(R.id.image_container);
        Bitmap bitmap = BitmapFactory.decodeFile(PATH);
        imageContainer.setImageBitmap(Utils.downscaleToMaximum(usual().rotate(bitmap, 270)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
