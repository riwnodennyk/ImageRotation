package kulku.ua.imagerotation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;


public class MyActivity extends Activity {

    public static final String STORAGE_EMULATED_0_DCIM_CAMERA = "/storage/emulated/0/Download/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        ImageView imageContainer = (ImageView) findViewById(R.id.image_container);
        File file = new File(STORAGE_EMULATED_0_DCIM_CAMERA + "cloud.jpg");
//      ImageRotator imageRotator = new ImageSplitRotator(file);
        RenderScriptImageRotator imageRotator = new RenderScriptImageRotator(file, this);
//        try {
//            imageRotator.rotateImage();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        Bitmap blur = imageRotator.blur();
        imageContainer.setImageBitmap(blur);
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
