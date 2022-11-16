package fr.ufc.l3info.oprog;

import java.util.Locale;

public class Velo implements IVelo{

    private static final double intervalleRevision = 500.0;
    private enum TypeCadres {homme, femme, mixte}

    private TypeCadres typeCadre;
    private double kilometrage;
    private boolean decrocher;
    private boolean abimer;
    private double distanceAvantRevision;
    private double tarif;

    public Velo() {
        this.typeCadre = TypeCadres.mixte;
        this.decrocher = true;
        this.abimer = false;
        this.distanceAvantRevision = Velo.intervalleRevision;
        this.tarif = 2.0;
    }

    public Velo(char t) {
        this();
        switch (t) {
            case 'F':
            case 'f':
                this.typeCadre = TypeCadres.femme;
                break;
            case 'H':
            case 'h':
                this.typeCadre = TypeCadres.homme;
                break;
            default:
                this.typeCadre = TypeCadres.mixte;
        }
    }

    /**
     * Indique le nombre de kilomètres total qu'a déjà parcouru le vélo.
     * @return le kilométrage du vélo.
     */
    public double kilometrage() {
        return this.kilometrage;
    }

    /**
     * Indique le nombre de kilomètres qu'il reste au vélo avant la prochaine révision.
     * Une valeur négative ou nulle indiquera qu'il est temps d'effectuer la révision.
     * @return le nombre de kilomètres avant la prochaine révision.
     */
    public double prochaineRevision() {
        return this.distanceAvantRevision;
    }

    /**
     * Fait parcourir au vélo en cours d'emprunt le nombre de kilomètres passé en paramètre.
     * Si le vélo n'est pas emprunté, cette méthode est sans effet.
     * @param km le nombre de kilomètres parcourus.
     */
    public void parcourir(double km) {
        if(this.decrocher && km > 0.0 && !Double.isInfinite(km)) {
            this.kilometrage += km;
            this.distanceAvantRevision -= km;
        }
    }

    /**
     * Tarif horaire pour le vélo.
     * @return Le tarif de location pour une heure de vélo (positif ou nul)
     */
    public double tarif() {
        return this.tarif;
    }

    /**
     * Permet de décrocher le vélo de sa borne.
     * @return  0 si le vélo a effectivement pu être décroché,
     *          -1 si le vélo est déjà décroché.
     */
    public int decrocher() {
        if (this.decrocher){
            return -1;
        }
        this.decrocher = true;
        return 0;
    }

    /**
     * Permet de raccrocher le vélo à une borne.
     * @return  0 si le vélo a effectivement pu être accroché,
     *          -1 si le vélo est déjà accroché.
     */
    public int arrimer() {
        if(this.decrocher) {
            this.decrocher = false;
            return 0;
        }
        return -1;
    }

    /**
     * Abime le vélo, quelque soit son état d'origine.
     */
    public void abimer() {
        this.abimer = true;
    }

    /**
     * Indique si le vélo est en bon état ou abimé.
     * @return true si le vélo est abimé, false sinon.
     */
    public boolean estAbime() {
        return this.abimer;
    }

    /**
     * Permet de réviser le vélo lorsque celui-ci est décroché. Cette action a pour effet
     * de réinitialiser le décompte des kilomètres à parcourir avant la prochaine révision.
     * Si le vélo était abimé, la révision a pour effet de le réparer.
     * @return  0 si la révision a pu être effectuée,
     *          -1 sinon (le vélo est encore accroché).
     */
    public int reviser() {
        if(!this.decrocher) {
            return -1;
        }
        this.distanceAvantRevision = Velo.intervalleRevision;
        this.abimer = false;
        return 0;
    }

    /**
     * Permet de réparer un vélo. La réparation s'effectue sur un vélo abimé qui n'est pas accroché.
     * @return  0 si le vélo a pu être réparé,
     *          -1 si le vélo est accroché,
     *          -2 si le vélo est décroché, mais qu'il n'est pas abimé.
     */
    public int reparer() {
        if(!this.decrocher) {
            return -1;
        }

        if(!this.abimer) {
            return -2;
        }
        this.abimer = false;
        return 0;
    }

    /**
     * Génère une chaîne de caractères décrivant le vélo.
     * @return une chaîne décrivant le vélo.
     */
    public String toString() {
        String s = "Vélo cadre "+ this.typeCadre + " - " + String.format(Locale.ENGLISH, "%.1f", this.kilometrage) +" km";
        if(this.distanceAvantRevision <= 0) {
            s += " (révision nécessaire)";
        }
        return s;
    }
}
