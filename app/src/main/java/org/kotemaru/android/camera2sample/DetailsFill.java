package org.kotemaru.android.camera2sample;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;

public class DetailsFill extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    public GoogleApiClient client;
    public Location lastKnownLocation;

    public LocationRequest locationRequest;
    public String bitmapUri;

    private TextView location;
    private EditText name;
    private EditText number;
    private EditText email;
    public Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_fill);

        if(getIntent().hasExtra("img")) {
            System.out.println("here");
            bitmapUri = getIntent().getStringExtra("img");
        }


        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .setAccountName("kaushalop7@gmail.com")
                .build();

        location = (TextView) findViewById(R.id.textView);
        name = (EditText) findViewById(R.id.editText);
        number = (EditText) findViewById(R.id.editText2);
        email = (EditText) findViewById(R.id.editText3);
        //new Task1().execute();

        spinner = (Spinner) findViewById(R.id.spinner1);
        String[] items = new String[]{"Bin Full (Complain/Request)",
        "Green Waste"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this,"I am here checking !", Toast.LENGTH_LONG).show();
        LocationServices.FusedLocationApi.requestLocationUpdates(client,locationRequest, this);

        displayInLocationBox();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLocation = location;
        displayInLocationBox();
    }

    private void displayInLocationBox() {

        location.setText(""+lastKnownLocation.getLatitude()+":"+lastKnownLocation.getLongitude()+"");

    }

    public void mail(View view){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"arshad.beast15@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Waste Management : Category :"+spinner.getSelectedItem());
        i.putExtra(Intent.EXTRA_TEXT   , "NAME : "+name.getText()+" NUMBER : "+number.getText()+" EMAIL : "+email.getText());
        i.putExtra(Intent.EXTRA_STREAM, Uri.parse(bitmapUri));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DetailsFill.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }


}
