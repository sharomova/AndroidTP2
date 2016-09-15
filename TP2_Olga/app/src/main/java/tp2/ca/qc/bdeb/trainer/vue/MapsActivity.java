package tp2.ca.qc.bdeb.trainer.vue;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tp2.ca.qc.bdeb.trainer.R;
import tp2.ca.qc.bdeb.trainer.modele.Cource;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private SupportMapFragment mapView;
    private double distance;
    private String type;
    private double tempsLimite = 0.0;
    private double tempsStop = 60.0;
    private int cons = 1000;
    private int compteur = 60;
    Location prochen;
    Location localisationPremier;
    TextView txtDistance;
    Chronometer chronometer;
    Button btnRetoure;
    ImageButton btnStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        initialiser();
        Intent intent = getIntent();
        int h = intent.getIntExtra(Inscription.TEMPS, -1);
        type = intent.getStringExtra(Inscription.TYPE);
        if (h/1000 > 0){
         chronometer.setBase(SystemClock.elapsedRealtime() - h);
            chronometer.start();
        }


        // Prend le MapView du layout et l'afficher
        mapView = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));

        if (mMap == null) {
            mMap = ((MapView) findViewById(R.id.map)).getMap();
        }
        mapView.onCreate(savedInstanceState);
        mMap = mapView.getMap();


        // Trouver l’objet LocationManager à partir du service système LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Créer un critère pour trouver le provider
        Criteria criteria = new Criteria();
        // Trouver le nom du meilleur provider
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        localisationPremier = locationManager.getLastKnownLocation(provider);
        mMap.addMarker(new MarkerOptions().position(new LatLng(localisationPremier.getLatitude(),
                localisationPremier.getLongitude())).title("Marker"));


        btnRetoure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //afficher la page principale
                Intent intent = new Intent(MapsActivity.this, Principale.class);
                String dic = txtDistance.getText().toString();
                intent.putExtra(Inscription.TEMPS, (int) (SystemClock.elapsedRealtime()
                        - chronometer.getBase()));
                intent.putExtra(Inscription.DISTANCE, dic);
                intent.putExtra(Inscription.TYPE, type);
                startActivity(intent);
            }

        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this, getResources().getString(R.string.start),
                        Toast.LENGTH_SHORT).show();
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }

        });
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer c) {
                tempsLimite = ((int) (SystemClock.elapsedRealtime() - chronometer.getBase())) / cons;
                if (tempsLimite == tempsStop) {
                    tempsStop = tempsStop + compteur;
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    // Créer un critère pour trouver le provider
                    Criteria criteria = new Criteria();
                    // Trouver le nom du meilleur provider
                    String provider = locationManager.getBestProvider(criteria, true);
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && checkSelfPermission
                            (Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                    Location localisation = locationManager.getLastKnownLocation(provider);
                    prochen = locationManager.getLastKnownLocation(provider);
                    // Ajouter une ligne rouge entre 2 positions
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(localisationPremier.getLatitude(),
                                    localisationPremier.getLongitude()),
                                    new LatLng(prochen.getLatitude(), prochen.getLongitude()))
                            .width(5)
                            .color(Color.RED));
                    distance = getDistanceMeters(localisationPremier.getLatitude(),
                            localisationPremier.getLongitude(), prochen.getLatitude(),
                            prochen.getLongitude());
                    txtDistance.setText(String.valueOf(distance));
                }
            }
        });
    }

    /**
     * methode qui calcule la distance
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */

    public static double getDistanceMeters(double lat1, double lon1, double lat2, double lon2) {

        int Radius=6371;//radius of earth in Km

        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double dic = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*dic;
        double km=valueResult/1;
        double meter=valueResult%1000;
        return Math.round(km);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
       mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
    /**
     * methode qui initialise les variables
     */
    private void initialiser() {
        btnRetoure = (Button) findViewById(R.id.map_btnRetour);
        txtDistance = (TextView) findViewById(R.id.map_txtDistance);
        chronometer = (Chronometer) findViewById(R.id.map_chronometer);
        btnStart = (ImageButton) findViewById(R.id.map_btnStart);
    }

}
