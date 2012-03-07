package in.pushpraj.imageCapture;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FirstActivity extends Activity
{
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
             }
         });

    }
}
