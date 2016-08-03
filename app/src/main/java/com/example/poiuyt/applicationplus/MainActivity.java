package com.example.poiuyt.applicationplus;

import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    Button sms, fb, email, location;
    TextView maps;
    private final int REQUEST_LOCATION = 200;
    GoogleApiClient googleApiClient = null;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initComponent();
        bindEventHandlers();
    }

    private void initComponent() {
        setContentView(R.layout.activity_main);
        sms = (Button) findViewById(R.id.sms);
        maps = (TextView) findViewById(R.id.checkin);
        fb = (Button) findViewById(R.id.fb);
        email = (Button) findViewById(R.id.email);
        location = (Button) findViewById(R.id.location);

    }

    private void bindEventHandlers() {
        sms.setOnClickListener(this);
        email.setOnClickListener(this);
        fb.setOnClickListener(this);
        location.setOnClickListener(this);
        maps.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fb:
//                actionFacebook();
                break;
            case R.id.email:
                sendEmail();
                break;
            case R.id.sms:
                sendSms();
                break;
            case R.id.location:
                checkLocation();
                break;
            case R.id.checkin:
                startActivity(new Intent(MainActivity.this, MapActivity.class));
                break;
            default:
                break;
        }
    }

    public void sendSms() {
        intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("sms_body", "Body of Message");
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
//        }
    }

    public void sendEmail() {

/* Fill it with Data */
        intent.setType("message/rfc822");
        intent.setData(Uri.parse("mailto:" + "recipient@example.com"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"webmaster@website.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "my subject");
        intent.putExtra(Intent.EXTRA_TEXT, "body text");

/* Send it off to the Activity-Chooser */
        startActivity(Intent.createChooser(intent, "Send mail..."));
    }

    public void actionFacebook() {

    }

    public void checkLocation() {
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        googleApiClient.connect();


    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                Status status = result.getStatus();
//                    LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            status.startResolutionForResult(MainActivity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * expansion panel
     bottom bar
     */
}
