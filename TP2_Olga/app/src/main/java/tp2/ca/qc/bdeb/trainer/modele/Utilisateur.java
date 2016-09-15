package tp2.ca.qc.bdeb.trainer.modele;

/**
 * Created by Olga on 2015-11-10.
 */
public class Utilisateur {
    String nom;
    String age;
    String sex;
    int id;

    public Utilisateur(int id, String nom, String age, String sex) {
        this.age = age;
        this.nom = nom;
        this.sex = sex;
        this.id = id;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


}
