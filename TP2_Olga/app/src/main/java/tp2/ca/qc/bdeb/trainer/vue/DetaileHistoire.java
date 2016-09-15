package tp2.ca.qc.bdeb.trainer.vue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import tp2.ca.qc.bdeb.trainer.R;
import tp2.ca.qc.bdeb.trainer.modele.Cource;
import tp2.ca.qc.bdeb.trainer.modele.DbHelper;
import tp2.ca.qc.bdeb.trainer.modele.Utilisateur;

/**
 * Created by Olga on 2015-11-14.
 */
public class DetaileHistoire extends Inscription {
    Cource cource;
    DbHelper db;
    Utilisateur user;
    TextView nom;
    TextView distance;
    TextView calories;
    TextView steps;
    TextView temps;
    Button fermer;
    ImageView type;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histoire_detaile);
        initialiser();
        db = DbHelper.getInstance(this);
        Intent intentP = getIntent();
        id = intentP.getIntExtra(Inscription.ID, -1);
        cource = db.getCource(id);
        user = db.getUser(cource.getId_user());
        placerDonnesDansVue ();

        fermer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(DetaileHistoire.this, HistoireActivity.class);
                    startActivity(intent);
            }

        });

    }

    /**
     * methode qui initialise les variables
     */
    private void initialiser() {
        nom = (TextView) findViewById(R.id.histoir_detaile_txtNom);
        distance = (TextView) findViewById(R.id.histoire_detaile_txtDistance);
        calories = (TextView) findViewById(R.id.histoire_detaile_txtCalories);
        steps = (TextView) findViewById(R.id.histoire_detaile_txtPas);
        temps = (TextView) findViewById(R.id.histoire_detaile_txtTime);
        fermer = (Button) findViewById(R.id.histoire_detaile_btnRetour);
        type = (ImageView) findViewById(R.id.histoire_detaile_imgTaining);


    }

    /**
     * methode qui ajoute les donnes dans vue
     */
    private void placerDonnesDansVue (){
        nom.setText(user.getNom().toString());
        distance.setText(cource.getDistance().toString());
        calories.setText(String.valueOf(cource.getCalories()));
        steps.setText(String.valueOf(cource.getPas()));
       temps.setText(String.valueOf(cource.getTemps()));
        if (cource.getType().equals(Inscription.RUN)) {
            type.setImageResource(R.drawable.clicknrun);
        } else if (cource.getType().equals(Inscription.VELO)) {
            type.setImageResource(R.drawable.bicycle);
        }

    }
}
