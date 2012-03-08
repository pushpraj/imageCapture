package in.pushpraj.imageCapture;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends Activity
{
    private static final int CAMERA_PIC_REQUEST = 1337;

    private Location currentLocation;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this);
                               builder.setMessage("Need GPS enabled!!")
                                .setCancelable(false)
                                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).create().show();
          return;
        }

        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        currentLocation = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        LocationListener mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

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


    public class MyLocationListener implements LocationListener
    {
        public void onLocationChanged(Location loc)
        {
            currentLocation = loc;

            String Text = "My current location is: " + "Latitud = " + loc.getLatitude() +"Longitud = " + loc.getLongitude();
            Toast.makeText(getApplicationContext(),Text, Toast.LENGTH_SHORT).show();
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(),"Gps Disabled", Toast.LENGTH_SHORT ).show();
        }

        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }

    }
}
