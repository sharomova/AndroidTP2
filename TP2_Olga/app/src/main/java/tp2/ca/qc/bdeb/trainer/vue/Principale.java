package tp2.ca.qc.bdeb.trainer.vue;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


import tp2.ca.qc.bdeb.trainer.R;
import tp2.ca.qc.bdeb.trainer.modele.Cource;
import tp2.ca.qc.bdeb.trainer.modele.DbHelper;
import tp2.ca.qc.bdeb.trainer.modele.Utilisateur;

/**
 * Created by Olga on 2015-11-11.
 */
public class Principale extends Inscription implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mStepCounterSensor;
    private Sensor mStepDetectorSensor;
    Cource cource;
    DbHelper db;
    Utilisateur user;
    Button btnMap;
    TextView nom;
    TextView distance;
    TextView calories;
    TextView steps;
    CheckBox chRun;
    CheckBox chVelo;
    double nombreCalories = 0;
    int id ;
    Chronometer chronometer;
    ImageButton start;
    ImageButton stop;
    String type = "";
    String date;
    double mille = 0.6214;
    double constantVelo = 5.63;
    String time = "0";
    String dic = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale);
        initialiser();
        db = DbHelper.getInstance(this);
        Intent intentP = getIntent();
        time = intentP.getStringExtra(Inscription.TEMPS);
        dic = intentP.getStringExtra(Inscription.DISTANCE);
        type = intentP.getStringExtra(Inscription.TYPE);
        int h = intentP.getIntExtra(Inscription.TEMPS, -1);
        if(h != -1) {
            chronometer.setBase(SystemClock.elapsedRealtime() - h);
            chronometer.start();
        }

        id = db.verifierUser();
        if (id == -1) {
            //afficher la page inscription
            Intent intent = new Intent(Principale.this, Inscription.class);
            //intent.putExtra(USER, id);
            startActivity(intent);
        } else {
            user = db.getUser(id);
            if(dic == null){
                dic = "0";
            }
            ajouterDonnesDansView();
        }
        //changer affichage des chiffres
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            public void onChronometerTick(Chronometer c) {
                int cTextSize = c.getText().length();
                if (cTextSize == 5) {
                    chronometer.setText("00:" + c.getText().toString());
                } else if (cTextSize == 7) {
                    chronometer.setText("0" + c.getText().toString());
                }

            }
        });


        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chRun.isChecked() && !chVelo.isChecked()){
                    Toast.makeText(Principale.this, getResources().getString(R.string.select), Toast.LENGTH_SHORT).show();
                }else {
                    //afficher la carte
                    prendreDonnesDeVue();
                    Intent intent = new Intent(Principale.this, MapsActivity.class);
                    intent.putExtra(TEMPS, (int) (SystemClock.elapsedRealtime() - chronometer.getBase()));
                    intent.putExtra(Inscription.TYPE, type);
                    startActivity(intent);
                }
            }

        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chRun.isChecked() && !chVelo.isChecked()){
                    Toast.makeText(Principale.this, getResources().getString(R.string.select), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Principale.this, getResources().getString(R.string.start), Toast.LENGTH_SHORT).show();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();

                }
            }

        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prendreDonnesDeVue();
                cource = new Cource(1, user.getId(), Integer.valueOf(steps.getText().toString()),
                        nombreCalories, chronometer.getText().toString(),
                        distance.getText().toString(), type, date);
                db.ajouterCource(cource);
                Toast.makeText(Principale.this, getResources().getString(R.string.stop), Toast.LENGTH_SHORT).show();
                chronometer.stop();
            }

        });
        //pour que on puisse check 1 fois
        chRun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chRun.isChecked() || type.equals(RUN)) {
                    chVelo.setChecked(false);
                    chRun.setChecked(true);
                }
            }
        });
        chVelo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (chVelo.isChecked() || type.equals(VELO)) {
                    chRun.setChecked(false);
                    chVelo.setChecked(true);
                }

            }
        });

    }


    /**
     * methode qui initialise les variables
     */
    private void initialiser() {
        btnMap = (Button) findViewById(R.id.principale_btnRetour);
        nom = (TextView) findViewById(R.id.principale_txtNom);
        distance = (TextView) findViewById(R.id.principale_txtDistance);
        calories = (TextView) findViewById(R.id.principale_txtCalories);
        steps = (TextView) findViewById(R.id.principale_txtPas);
        chronometer = (Chronometer) findViewById(R.id.principale_chronometer);
        start = (ImageButton) findViewById(R.id.principale_imgBtnRun);
        stop = (ImageButton) findViewById(R.id.principale_imgBtnStop);
        chRun = (CheckBox) findViewById(R.id.principale_chbRunning);
        chVelo = (CheckBox) findViewById(R.id.principale_chbBice);
        mSensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    /**
     * methode qui ajoute les donnes dans la View
     */
    private void ajouterDonnesDansView() {
        nom.setText(String.valueOf(user.getNom()));
        distance.setText(dic.toString());
        steps.setText(String.valueOf("0"));
        if(type == VELO){
            chVelo.setChecked(true);
            //calcule des calories
            nombreCalories = constantVelo * mille * Double.parseDouble(distance.getText().toString());
        } else
        if(type == RUN){
            chRun.setChecked(true);
            //calcule des calories
            nombreCalories = mille * Double.parseDouble(distance.getText().toString());
        }
        calories.setText(String.valueOf(nombreCalories));
    }

    /**
     * methode qui cherche les donnes de la vue
     */
    private void prendreDonnesDeVue(){
        //cherche la date
        SimpleDateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy");
        Locale locale = Locale.getDefault();
        Date actuelle = new Date();
        date = dfDate.format(actuelle);
        //chercher le type
        if (((CheckBox) chVelo).isChecked()) {
            type = VELO;
        } else if (((CheckBox) chRun).isChecked()) {
            type = RUN;
        }

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
//        if(type.equals(RUN)) {
            Sensor sensor = event.sensor;
            float[] values = event.values;
            int value = -1;

            if (values.length > 0) {
                value = (int) values[0];
            }

            if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
//                steps.setText(value);
            } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                // For test only. Only allowed value is 1.0 i.e. for step taken
          //      steps.setText(value);
            }
        }
  //  }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {

        super.onResume();

        mSensorManager.registerListener(this, mStepCounterSensor,

                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor,

                SensorManager.SENSOR_DELAY_FASTEST);

    }
    @Override
    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }


}