package fr.ufc.l3info.oprog;

import java.util.Locale;

public class Velo implements IVelo{
    /**
     String cadre type
     */
    private String cadre;
    /**
     boolean estDecrocher
     */
    private boolean estDrecrocher=true;
    /**
     boolean estAbimer
     */
    private boolean estAbimer=false;
    /**
     double kilometre
     */
    private double kilometre=0.0;

    /**
     * constructeur Velo set le cadre mixte
     */
    public Velo(){this.setCadre('M');}

    /**
     * constructeur Velo set le cadre en fonction du param d'entrer
     * @param t type du cadre
     */
    public Velo(char t){this.setCadre(t);}

    /**
     * getteur du kilometrage
     * @return
     */
    @Override
    public double kilometrage() {return this.kilometre;}

    /**
     * methode qui nous dis quand est la prochaine revision
     * @return le nombre de kilometre restant avant la revison
     */
    @Override
    public double prochaineRevision() {
        return 500.0-this.kilometre;
    }

    /**
     * setteur de parcourir
     * @param km le nombre de kilomètres parcourus.
     */
    @Override
    public void parcourir(double km) {

        if(km > 0.0 && this.estDrecrocher ){
            this.kilometre=this.kilometre+km;
        }
    }

    /**
     * methode tarif
     * @return le tarif
     */
    @Override
    public double tarif() {return 2.0;}

    /**
     * decrocher ou non le velo
     * @return si le velo est decrocher ou non et pourquoi
     */
    @Override
    public int decrocher() {
        if(!estDrecrocher){
            this.estDrecrocher=true;
            return 0;
        }
        return -1;
    }

    /**
     * arimer ou non le velo
     * @return si le velo est arrimer ou non et pourquoi
     */
    @Override
    public int arrimer() {
        if(estDrecrocher){
            this.estDrecrocher=false;
            return 0;
        }
        return -1;
    }

    /**
     * setteur abimer
     */
    @Override
    public void abimer() {this.estAbimer=true;}

    /**
     * getteur abimer
     * @return abimer
     */
    @Override
    public boolean estAbime() {return this.estAbimer;}

    /**
     * revise le velo ou non et si non pourquoi
     * @return si le velo est reviser ou non et pourquoi
     */
    @Override
    public int reviser() {
        if(this.estDrecrocher){
            this.kilometre=0.0;
            this.reparer();
            return 0;
        }
        return -1;
    }

    /**
     * methode pour reparer le velo ou non
     * @return si le velo est reparer ou non et pourquoi
     */
    @Override
    public int reparer() {
        if(this.estAbimer && this.estDrecrocher){
            this.estAbimer=false;
            return 0;
        }
        if(!this.estDrecrocher){return -1;}
        return -2;
    }

    /**
     * toString
     * @return le toString
     */
    @Override
    public String toString(){
        return "Vélo cadre "+this.cadre+ " - "+String.format(Locale.ENGLISH, "%.1f", this.kilometrage())+" km"+(this.prochaineRevision() > 0 ? "":" (révision nécessaire)");
    }



    /**
     * setteur pour l'attribut cadre
     * @param t char pour type à mettre
     */
    private void setCadre(char t){
        if(t == 'H' || t == 'h'){
            this.cadre="homme";
            return;
        }
        if(t == 'F' || t == 'f'){
            this.cadre="femme";
            return;
        }
        this.cadre="mixte";
    }
}
