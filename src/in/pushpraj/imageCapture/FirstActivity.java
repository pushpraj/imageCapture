package in.pushpraj.imageCapture;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.*;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class FirstActivity extends Activity
{
    private static final int CAMERA_PIC_REQUEST = 1337;

    private Location currentLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;

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

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        printLocation(currentLocation);

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
         addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
        }
        catch (Exception e) {

        }



        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener);

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

    /** Register for the updates when Activity is in foreground */
	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 1, locationListener);
	}

	/** Stop the updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(locationListener);
	}

    private void printLocation(Location location) {
		if (location == null)
            Log.v("","\nLocation[unknown]\n\n");
		else
			Log.v("","\n\n" + location.toString());
	}

    public class MyLocationListener implements LocationListener
    {
        public void onLocationChanged(Location loc)
        {
            currentLocation = loc;

            printLocation(currentLocation);
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
