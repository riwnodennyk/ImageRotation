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


public class MyActivity extends Activity {

    //    public static final String STORAGE_EMULATED_0_DCIM_CAMERA = "/storage/emulated/0/Download/";
    public static final String STORAGE_EMULATED_0_DCIM_CAMERA = "/storage/emulated/0/DCIM/Camera/";
    public static final float MAX_BITMAP_SIZE = 4096f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ImageView imageContainer = (ImageView) findViewById(R.id.image_container);
//        File file = new File(STORAGE_EMULATED_0_DCIM_CAMERA + "apple.png");
//        File file = new File(STORAGE_EMULATED_0_DCIM_CAMERA + "house.jpg");
//        File file = new File(STORAGE_EMULATED_0_DCIM_CAMERA + "IMG_20141206_144918.jpg");
        File file = new File(STORAGE_EMULATED_0_DCIM_CAMERA + "IMG_20141206_140944.jpg");
//        File file = new File(STORAGE_EMULATED_0_DCIM_CAMERA + "cloud.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        ImageRotator imageRotator = ImageRotator.jni(this, 90);
        long l = System.currentTimeMillis();
        Bitmap bm = imageRotator.rotate(bitmap);
        Log.d("Rotated in", "" + imageRotator.getClass().getSimpleName() + " " + (System.currentTimeMillis() - l) + " ms");
//        Bitmap bm = BitmapFactory.decodeFile(file.getPath());

        int height = bm.getHeight();
        int width = bm.getWidth();
        if (height > MAX_BITMAP_SIZE) {
            width *= MAX_BITMAP_SIZE / height;
            height = (int) MAX_BITMAP_SIZE;
        }
        if (width > MAX_BITMAP_SIZE) {
            height *= MAX_BITMAP_SIZE / width;
            width = (int) MAX_BITMAP_SIZE;
        }
        bm = Bitmap.createScaledBitmap(bm, width, height, false);
        imageContainer.setImageBitmap(bm);
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
