package tp2.ca.qc.bdeb.trainer.vue;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

import tp2.ca.qc.bdeb.trainer.R;
import tp2.ca.qc.bdeb.trainer.modele.Cource;
import tp2.ca.qc.bdeb.trainer.modele.DbHelper;


/**
 * Created by Olga on 2015-12-08.
 */
public class Statistique extends Inscription {
    DbHelper db;
    ArrayList<Cource> coureces;
    int idUser;
    int nbRun = 0;
    int nbVelo = 0;
    double dictanceVelo = 0.0;
    double calorieVelo = 0.0;
    double dictanceRun = 0.0;
    double calorieRun = 0.0;
    TextView txtDistanceVelo;
    TextView txtCaloriesVelo;
    TextView txtDistanceRun;
    TextView txtCaloriesRun;
    TextView txtNbVelo;
    TextView txtNbRun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistique);
        db = DbHelper.getInstance(this);
        initialiser();
        trouverDonnes();
        afficherDonnes();

        }

    /**
     * methode qui faire la calculation
     */

    private void trouverDonnes(){
        idUser = db.verifierUser();
        coureces = db.getAllCourceDeUser(idUser);
        for (Cource cour: coureces)
        {
            if(cour.getType().equals(Inscription.VELO)) {
                calorieVelo = calorieVelo + cour.getCalories();
                dictanceVelo = dictanceVelo + Double.parseDouble(cour.getDistance());
                ++nbVelo;
            }
            if(cour.getType().equals(Inscription.RUN)) {
                calorieRun = calorieRun + cour.getCalories();
                dictanceRun = dictanceRun + Double.parseDouble(cour.getDistance());
                ++nbRun;
            }
        }

    }

    /**
     * methode qui initialise les donnes
     */

    private void initialiser(){
        txtDistanceVelo = (TextView) findViewById(R.id.stat_velo_dictance);
        txtCaloriesVelo = (TextView) findViewById(R.id.stat_velo_cal);
        txtDistanceRun = (TextView) findViewById(R.id.stat_run_dictance);
        txtCaloriesRun = (TextView) findViewById(R.id.stat_run_cal);
        txtNbRun = (TextView) findViewById(R.id.stat_run);
        txtNbVelo = (TextView) findViewById(R.id.stat_velo);
    }

    /**
     * methode qui affiche la statistique sur la vue
     */

    private void afficherDonnes(){
        txtCaloriesVelo.setText(String.valueOf(calorieVelo) + " cal.");
        txtCaloriesRun.setText(String.valueOf(calorieRun) + " cal.");
        txtDistanceRun.setText(String.valueOf(dictanceRun) + " km");
        txtDistanceVelo.setText(String.valueOf(dictanceVelo) + " km");
        txtNbRun.setText(String.valueOf(nbRun));
        txtNbVelo.setText(String.valueOf(nbVelo));

    }
}
