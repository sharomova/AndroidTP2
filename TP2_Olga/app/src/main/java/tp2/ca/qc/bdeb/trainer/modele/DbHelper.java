package tp2.ca.qc.bdeb.trainer.modele;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Olga on 2015-11-10.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "app.db"; // Votre nom de BD
    public static final int DBVERSION = 1; // Votre numéro de version de BD
    private Context context;
    private static DbHelper instance = null; //L’unique instance de DbHelper possible
    // Mettre ici toutes les constantes de noms de tables et de colonnes
    private static final String TABLE_USER = "user";
    private static final String TABLE_COURCE = "cource";
    // Noms de colonnes de USER
    private static final String USER_ID = "_id";
    private static final String USER_NOM = "nom";
    private static final String USER_AGE = "age";
    private static final String USER_SEX = "sex";
    // Noms de colonnes de cource
    private static final String COURCE_ID = "_id";
    private static final String COURCE_ID_USER = "iduser";
    private static final String COURCE_TEMPS = "temps";
    private static final String COURCE_DISTANCE = "distance";
    private static final String COURCE_CALORIES = "calories";
    private static final String COURCE_PAS = "pas";
    private static final String COURCE_DATE = "date";
    private static final String COURCE_TYPE = "type";

    public static DbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DbHelper(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Constructeur de DBHelper
     */
    private DbHelper(Context context) {
        super(context, DB_NAME, null, DBVERSION);
        this.context = context;
    }

    /**
     * Appeler lors de l’appel « normal » de la création de BD
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlUser = "CREATE TABLE " + TABLE_USER +
                "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_NOM +
                " TEXT," + USER_AGE + " TEXT," + USER_SEX + " TEXT)";
        db.execSQL(sqlUser);

        String sqlCource = "CREATE TABLE " + TABLE_COURCE +
                "(" + COURCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COURCE_ID_USER +
                " INTEGER," +  COURCE_PAS + " INTEGER," + COURCE_CALORIES + " REAL," +
                COURCE_TEMPS + " TEXT," + COURCE_DISTANCE + " TEXT," +
                 COURCE_TYPE + " TEXT," + COURCE_DATE + " TEXT)";
        db.execSQL(sqlCource);

    }

    /**
     * methode qui ajoute les donnes de l'utilisateur
     * @param user
     */
    public void ajouterUser(Utilisateur user) {
        SQLiteDatabase db = this.getWritableDatabase(); // On veut écrire dans la BD
        ContentValues values = new ContentValues();
        values.put(USER_NOM, user.getNom());
        values.put(USER_AGE, user.getAge());
        values.put(USER_SEX, user.getSex());
// Insérer le nouvel enregistrement
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Fermer la connexion
    }

    /**
     * methode qui ajoute les donnes du cource
     * @param cource int id, int id_user, int pas, double calories, String temps, String distance, String type, String date
     */
    public void ajouterCource(Cource cource) {
        SQLiteDatabase db = this.getWritableDatabase(); // On veut écrire dans la BD
        ContentValues values = new ContentValues();
        values.put(COURCE_ID_USER, cource.getId_user());
        values.put(COURCE_PAS, cource.getPas());
        values.put(COURCE_CALORIES, cource.getCalories());
        values.put(COURCE_TEMPS, cource.getTemps());
        values.put(COURCE_DISTANCE, cource.getDistance());
        values.put(COURCE_TYPE, cource.getType());
        values.put(COURCE_DATE, cource.getDate());
// Insérer le nouvel enregistrement
        long id = db.insert(TABLE_COURCE, null, values);
        db.close(); // Fermer la connexion
    }

    /**
     * methode que cherche un user avec id
     * @param id
     * @return donnes user
     */
    public Utilisateur getUser(int id) {
        Utilisateur user = null;
        SQLiteDatabase db = this.getReadableDatabase(); // On veut lire dans la BD
        Cursor cursor = db.query(TABLE_USER, new String[]{USER_ID,
                        USER_NOM, USER_AGE, USER_SEX}, USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
             user = new Utilisateur(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3));
        }
        cursor.close();
        db.close(); // Fermer la connexion
// Retourner l'user
        return user;
    }

    /**
     * methode qui cherche un cource avec son id
     * @param id
     * @return cource
     */
    public Cource getCource(int id) {
        Cource cource = null;
        SQLiteDatabase db = this.getReadableDatabase(); // On veut lire dans la BD
        Cursor cursor = db.query(TABLE_COURCE, new String[]{COURCE_ID,
                        COURCE_ID_USER, COURCE_PAS, COURCE_CALORIES, COURCE_TEMPS, COURCE_DISTANCE,
                        COURCE_TYPE, COURCE_DATE}, COURCE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            cource = new Cource(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                    cursor.getDouble(3), cursor.getString(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7));
        }
        cursor.close();
        db.close(); // Fermer la connexion
// Retourner l'cource
        return cource;
    }

    /**
     * methode qui cherche les coures d'un user
     * @param idUser
     * @return liste courece
     */

    public ArrayList<Cource> getAllCourceDeUser(int idUser) {
        SQLiteDatabase db = this.getReadableDatabase(); // On veut lire dans la BD
        ArrayList<Cource> cources = new ArrayList<Cource>();
        String selectQuery = "SELECT  * FROM " + TABLE_COURCE;

        Log.e(DB_NAME, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                if(cursor.getInt(1) == idUser) {
                    cources.add(new Cource(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                            cursor.getDouble(3), cursor.getString(4), cursor.getString(5),
                            cursor.getString(6), cursor.getString(7)));
                }

            } while (cursor.moveToNext());
        }
        db.close(); // Fermer la connexion
        return (cources);
    }

    /**
     * methode qui trouve id d'utilisateur
     * @return id
     */

    public int verifierUser(){
        SQLiteDatabase db = this.getReadableDatabase(); // On veut lire dans la BD
        int id = -1;
        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        Log.e(DB_NAME, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToLast();
            do {
                    id = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        db.close(); // Fermer la connexion
        return id;
    }

    /**
     * methode qui cherche id de user
     * @param nom
     * @return id
     */
    public int getIdUser(String nom) {
        SQLiteDatabase db = this.getReadableDatabase(); // On veut lire dans la BD
        int id = -1;
            String selectQuery = "SELECT  * FROM " + TABLE_USER;
            Log.e(DB_NAME, selectQuery);
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null && cursor.getCount()>0) {
                cursor.moveToFirst();
                do {
                    if (cursor.getString(1).equals(nom) && cursor.getString(1)!= null) {
                        id = cursor.getInt(0);
                    }
                } while (cursor.moveToNext());
            }
        db.close(); // Fermer la connexion
        return id;
    }

    /**
     * methode qui prende tous les cource dans la base de donnes
     *
     * @return la liste des cource
     */
    public ArrayList<Cource> getTousCource() {
        SQLiteDatabase db = this.getReadableDatabase(); // On veut lire dans la BD
        ArrayList<Cource> cources = new ArrayList<Cource>();
        String selectQuery = "SELECT  * FROM " + TABLE_COURCE;

        Log.e(DB_NAME, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                cources.add(new Cource(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                        cursor.getDouble(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7)));

            } while (cursor.moveToNext());
        }
        db.close(); // Fermer la connexion
        return (cources);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Mettre les modifications de votre BD ici
    }
}
