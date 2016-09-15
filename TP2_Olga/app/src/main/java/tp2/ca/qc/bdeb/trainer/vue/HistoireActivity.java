package tp2.ca.qc.bdeb.trainer.vue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import tp2.ca.qc.bdeb.trainer.R;
import tp2.ca.qc.bdeb.trainer.modele.Cource;
import tp2.ca.qc.bdeb.trainer.modele.DbHelper;

/**
 * Created by Olga on 2015-11-14.
 */
public class HistoireActivity extends Inscription {
    // La ListView de notre Activity
    private ListView lstView;
    ArrayList<Cource> cources;
    DbHelper db;
    // Un ArrayAdapter ne supporte que des TextView à // l’intérieur. Il existe d’autres Adapter
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histoire);
        db = DbHelper.getInstance(this);
        int id = db.verifierUser();
        cources = db.getAllCourceDeUser(id);
        //Sorting cource avec date
        Collections.sort(cources, new Comparator<Cource>() {
            @Override
            public int compare(Cource lhs, Cource rhs) {
                // TODO Auto-generated method stub
                return lhs.getDate().compareToIgnoreCase(rhs.getDate());
            }
        });

        lstView = (ListView) findViewById(R.id.histoire_liste);
        adapter = new ArrayAdapterHistoire(this, R.layout.activity_un_cource, cources);
        lstView.setAdapter(adapter);
        //click sur un média
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               // Toast.makeText(HistoireActivity.this, getResources().getString(R.string.modifierAmi),
                     //   Toast.LENGTH_SHORT).show();
                int idCource = cources.get(position).getId();
                Intent intent = new Intent(HistoireActivity.this, DetaileHistoire.class);
                intent.putExtra(Inscription.ID, idCource);
                startActivity(intent);
            }
        });

    }


}
