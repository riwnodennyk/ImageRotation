package kulku.ua.imagerotation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;

import kulku.ua.imagerotation.rotator.ImageRotator;

import static java.util.Arrays.asList;
import static kulku.ua.imagerotation.rotator.ImageRotator.ndk;
import static kulku.ua.imagerotation.rotator.ImageRotator.renderScript;
import static kulku.ua.imagerotation.rotator.ImageRotator.split;
import static kulku.ua.imagerotation.rotator.ImageRotator.usual;


public class MyActivity extends Activity {

    public static final String STORAGE_EMULATED_0_DCIM_CAMERA = "/storage/emulated/0/DCIM/Camera/";
    public static final float MAX_BITMAP_SIZE = 4096f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ImageView imageContainer = (ImageView) findViewById(R.id.image_container);
        File file = new File(STORAGE_EMULATED_0_DCIM_CAMERA + "IMG_20141206_150847.jpg");
        for (ImageRotator imageRotator : asList(renderScript(this), ndk(), split(), usual())) {
            for (Integer angle : asList(90, 180)) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                long l = System.currentTimeMillis();
                imageRotator.rotateImage(bitmap, angle);
                Log.d("Rotated to " + angle, "" + imageRotator.getClass().getSimpleName()
                        + " " + (System.currentTimeMillis() - l) + " ms");
            }
        }
//        imageContainer.setImageBitmap(Utils.downscaleToMaximum(rotated));
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
