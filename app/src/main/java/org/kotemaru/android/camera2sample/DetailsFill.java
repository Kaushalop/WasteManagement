package org.kotemaru.android.camera2sample;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;

public class DetailsFill extends AppCompatActivity implements
        LocationListener {


    public Location lastKnownLocation;

    public LocationManager locationManager;
    public LocationRequest locationRequest;
    public String bitmapUri;
    public Criteria criteria;
    public String bestProvider;

    private TextView location;
    private EditText name;
    private EditText number;
    private EditText email;
    public Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_fill);

        if (getIntent().hasExtra("img")) {
            System.out.println("here");
            bitmapUri = getIntent().getStringExtra("img");
        }

        location = (TextView) findViewById(R.id.textView);
        name = (EditText) findViewById(R.id.editText);
        number = (EditText) findViewById(R.id.editText2);
        email = (EditText) findViewById(R.id.editText3);


        spinner = (Spinner) findViewById(R.id.spinner1);
        String[] items = new String[]{"Bin Full (Complain/Request)",
                "Green Waste"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        getLocation();


    }
    public void mail(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"arshad.beast15@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Waste Management : Category :" + spinner.getSelectedItem());
        i.putExtra(Intent.EXTRA_TEXT, "NAME : " + name.getText() + " NUMBER : " + number.getText() + " EMAIL : " + email.getText()+" LOCATION : "+location.getText());
        i.putExtra(Intent.EXTRA_STREAM, Uri.parse(bitmapUri));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DetailsFill.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }


    protected void getLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

        //You can still do this if you like, you might get lucky:
        Location locationval = locationManager.getLastKnownLocation(bestProvider);
        if (locationval != null) {
            Log.e("TAG", "GPS is on");
            Toast.makeText(DetailsFill.this, "latitude:" + locationval.getLatitude() + " longitude:" + locationval.getLongitude(), Toast.LENGTH_SHORT).show();

            location.setText("" + locationval.getLatitude() + ":" + locationval.getLongitude() + "");


        } else {
            //This is what you need:
            locationManager.requestLocationUpdates(bestProvider, 1000, 0f, this);
        }


    }

    @Override
    public void onLocationChanged(Location locationval) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        locationManager.removeUpdates(this);

        //open the map:
        Toast.makeText(DetailsFill.this, "latitude:" + locationval.getLatitude() + " longitude:" + locationval.getLongitude(), Toast.LENGTH_SHORT).show();
        location.setText("" + locationval.getLatitude() + ":" + locationval.getLongitude() + "");
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
