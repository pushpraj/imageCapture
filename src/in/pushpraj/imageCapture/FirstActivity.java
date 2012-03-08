package in.pushpraj.imageCapture;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FirstActivity extends Activity
{
    private static final int CAMERA_PIC_REQUEST = 1337;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button button = (Button) findViewById(R.id.my_button);
        button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                   builder.setMessage("Button Clicked!")
                    .setCancelable(false)
                    .setPositiveButton("Ok", null).create().show();
                 //startActivity(new Intent(FirstActivity.this, CameraPreview.class));
                 Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                 startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
             }
         });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == CAMERA_PIC_REQUEST) {
        // do something
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ImageView image = (ImageView) findViewById(R.id.photoResultView);
        image.setImageBitmap(thumbnail);

    }
}
}
