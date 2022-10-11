package com.example.cartracking;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    //biometric variables
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    RelativeLayout fingerPrintLayout;
    //Runtime permissions
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final String TAG = MainActivity.class.getSimpleName();
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private boolean mRequestinLoctionUpdates = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fingerPrintLayout = findViewById(R.id.fingerPrintLayout);
        //location retreval


        ///biometric manager
//        check the harware activities
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(getApplicationContext(), "Enter pass code", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(getApplicationContext(), "Hardware unavailable", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(getApplicationContext(), "No fingerprint assigned", Toast.LENGTH_SHORT).show();
                break;

        }
//fingerprint authentication and launches the fingerprint pop up
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                fingerPrintLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Car Tracking")
                .setDescription("Use FingerPrint To Login").setDeviceCredentialAllowed(true).build();
//        prompt info
        biometricPrompt.authenticate(promptInfo);


    }
//Onclick Activities
//    buttons activity

    public void StartCar(View view) {
        startActivity(new Intent(this, CarStart.class));
    }
    public void StopCar(View view) {
        startActivity(new Intent(this, StopActivity.class));
    }
    public void placeHolder(View view) {
        startActivity(new Intent(this, PlaceHolder.class));
    }

    public void TrackCar(View view) {
       requestPermissions();
    }



    //Track Button
//    public void TrackCar(View view) {
//        final int REQUEST_CHECK_SETTINGS=99;
//        final long UPDATE_INTERVAL_IN_MILLISECONDS =9999;
//         final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =999;
//        final String TAG =MainActivity.class.getSimpleName() ;
//         FusedLocationProviderClient mFusedLocationClient;
//         SettingsClient mSettingsClient;
//         LocationRequest mLocationRequest;
//         LocationSettingsRequest mLocationSettingsRequest;
//        LocationCallback mLocationCallback;
//        final Location[] mCurrentLocation = new Location[0];
//        boolean mRequestinLoctionUpdates =false;
//
//        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
//        mSettingsClient=LocationServices.getSettingsClient(this);
//        mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//                mCurrentLocation[-1] =locationResult.getLastLocation();
//                double latitude= mCurrentLocation[-1].getLatitude();
//                double loongitude = mCurrentLocation[-1].getLongitude();
//            }
//        };
//        mLocationRequest = LocationRequest.create()
//                .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
//                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        LocationSettingsRequest.Builder builder =new LocationSettingsRequest.Builder();
//        builder.addLocationRequest(mLocationRequest);
//        mLocationSettingsRequest=builder.build();
//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//
//
//    }
////checking for permissions
//private void Opensettings() {
//    Intent intent = new Intent();
//    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//    Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
//    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    startActivity(intent);
//}
//private void startLocationUpdates(){
//        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
//                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
//                    @SuppressLint("MissingPermission")
//                    @Override
//                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());
//
//
//                    }
//                })
//                .addOnFailureListener(this, new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        int statusCode=((ApiException) e).getStatusCode();
//                        switch (statusCode){
//                            case LocationSettingsStatusCodes
//                                    .RESOLUTION_REQUIRED:
//                                Log.i(TAG, "Location settings is not satisfied. Attempting to update location settings");
//
//                            try {
//                                ResolvableApiException rae=(ResolvableApiException) e;
//                                rae.startResolutionForResult(MainActivity.this,REQUEST_CHECK_SETTINGS);
//                            } catch (IntentSender.SendIntentException sendIntentException) {
//                                sendIntentException.printStackTrace();
//                                Log.i(TAG, "PendingIntent unable to execute request");
//                            }break;
//                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                                String errorMessage = "Location settings are inadequate, and cannot br fixed here. Fix in settings";
//                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
//}
//
//private void stopsLocationUpdates(){
//        mFusedLocationClient.removeLocationUpdates(mLocationCallback).addOnCompleteListener(this,task -> Log.d(TAG, "Location updates stopped"));
//}
//private  boolean checkPermissiona(){
//        int permissionState=ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        return permissionState== PackageManager.PERMISSION_GRANTED;
//}
//@Override
//protected void onResume(){
//        super. onResume();
//        if(mRequestinLoctionUpdates&&checkPermissiona() ) {
//            startLocationUpdates();
//        }
//        }
//        @Override
//    protected void onPause(){
//        super.onPause();
//        if (mRequestinLoctionUpdates) {
//            stopsLocationUpdates();
//        }
//        }
//
//}
//    permision popup for gps
    private void requestPermissions() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(MainActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
//                      Intent intent = new Intent();
//                      intent.setAction(String.valueOf(MapsActivity.class));
//                      startActivity(intent);
                        mapActiviyt();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                      Intent intent = new Intent();
//                      intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
//                      Uri uri= Uri.fromParts("package",getPackageName(),"");
//                      intent.setData(uri);
//                      startActivity(intent);

                        denied();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    private void mapActiviyt() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    private void denied() {
        Toast.makeText(MainActivity.this, "Permission Denied, Can't track location", Toast.LENGTH_SHORT).show();
    }


}