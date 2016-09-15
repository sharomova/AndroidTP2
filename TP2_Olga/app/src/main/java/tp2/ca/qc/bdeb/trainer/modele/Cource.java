package tp2.ca.qc.bdeb.trainer.modele;

/**
 * Created by Olga on 2015-11-14.
 */
public class Cource {
    int id;
    int id_user;
    int pas;
    double calories;
    String temps;
    String distance;
    String type;
    String date;


    public Cource(int id, int id_user, int pas, double calories, String temps, String distance, String type, String date) {
        this.id_user = id_user;
        this.pas = pas;
        this.calories = calories;
        this.temps = temps;
        this.distance = distance;
        this.type = type;
        this.date = date;
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public int getPas() {
        return pas;
    }

    public void setPas(int pas) {
        this.pas = pas;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}
