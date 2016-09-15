package tp2.ca.qc.bdeb.trainer.vue;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.Toast;

import tp2.ca.qc.bdeb.trainer.R;
import tp2.ca.qc.bdeb.trainer.modele.DbHelper;
import tp2.ca.qc.bdeb.trainer.modele.Utilisateur;

public class Inscription extends AppCompatActivity {
    public final static String USER = "USER";
    public final static String RUN = "RUN";
    public final static String VELO = "VELO";
    public final static String TEMPS = "TEMPS";
    public final static String DISTANCE = "DISTANCE";
    public final static String TYPE = "TYPE";
    public final static String ID = "ID";
    private String female = "Female";
    private String male = "Male";
    private ShareActionProvider mShareActionProvider;
    MenuItem mnuPartager;
    Utilisateur user;
    DbHelper db;
    Button btnValider;
    RadioButton rdbMale;
    RadioButton rdbFemale;
    RadioGroup radioGroup;
    EditText nom;
    EditText age;
    String sexe;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        db = DbHelper.getInstance(this);
        initialiser();

        //pour prendre le sexe
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.inscription_rdbFemale) {
                    sexe = female;
                } else if (checkedId == R.id.inscription_rdbMale) {
                    sexe = male;
                }
            }
        });
        btnValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prendreDonnesMedia();
                id = db.getIdUser(nom.getText().toString());
                if(id == -1) {
                    if (user.getNom().equals("")) {
                        Toast.makeText(Inscription.this, getResources().getString(R.string.nomVide), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Inscription.this, getResources().getString(R.string.validated), Toast.LENGTH_SHORT).show();
                        db.ajouterUser(user);
                        id = db.getIdUser(nom.getText().toString());
                    }

                    //afficher la page principale
                    Intent intent = new Intent(Inscription.this, Principale.class);
                    intent.putExtra(USER, id);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inscription, menu);

        // Assigner le menu à son composant dans l’Activity
        mnuPartager = menu.findItem(R.id.partager_menu);
        //Récupérer le ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(mnuPartager);
        mShareActionProvider.setShareIntent(doShare());
        return true;
    }
    public Intent doShare() {
// populate the share intent with data
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources()
                .getString(R.string.app_name));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Texte à écrire");
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //On regarde quel item a été cliqué grâce à son id et on déclenche une action
        switch (item.getItemId()) {
            case R.id.newactiv:

                return true;
            case R.id.user:
                Toast.makeText(Inscription.this, getResources().getString(R.string.create_new_user), Toast.LENGTH_SHORT).show();
                Intent user = new Intent(Inscription.this, Inscription.class);
                startActivity(user);
                return true;
            case R.id.race:
                Toast.makeText(Inscription.this, getResources().getString(R.string.create_new_race), Toast.LENGTH_SHORT).show();
                Intent race = new Intent(Inscription.this, Principale.class);
                startActivity(race);
                return true;
            case R.id.stat:
                Toast.makeText(Inscription.this, getResources().getString(R.string.cumulative_statistics), Toast.LENGTH_SHORT).show();
                Intent stat = new Intent(Inscription.this, Statistique.class);
                stat.putExtra(USER, id);
                startActivity(stat);
                return true;
            case R.id.history:
                Toast.makeText(Inscription.this, getResources().getString(R.string.previous_races), Toast.LENGTH_SHORT).show();
                Intent history = new Intent(Inscription.this, HistoireActivity.class);
                startActivity(history);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * methode qui initialise les variables
     */
    private void initialiser () {
        btnValider = (Button) findViewById(R.id.inscription_btnAdd);
        radioGroup = (RadioGroup) findViewById(R.id.inscription_rdGroupe);
        rdbFemale = (RadioButton) findViewById(R.id.inscription_rdbFemale);
        rdbMale = (RadioButton) findViewById(R.id.inscription_rdbMale);
        nom = (EditText) findViewById(R.id.inscription_txtNom);
        age = (EditText) findViewById(R.id.inscription_txtAge);

    }

    /**
     * methode qui prende les donnes de vue
     */
    private  void prendreDonnesMedia (){
       user = new Utilisateur(1, nom.getText().toString(), age.getText().toString(), sexe);
    }
}
