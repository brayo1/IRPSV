package prototype.com.irpsv;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }
    public void traffic(View arg0) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:254-020-3341411"));

        if (ActivityCompat.checkSelfPermission(Contact.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);

    }
    public void ntsa(View view){

        Intent obra5 = new Intent(this, NTSA.class);
        startActivity(obra5);

    }
    public void ambulance(View view){

        Intent obra6 = new Intent(this, Ambulance.class);
        startActivity(obra6);

    }

}
