package fr.ufc.l3info.oprog;

import jdk.nashorn.internal.ir.annotations.Ignore;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.*;

public class Station{

    private String nom;
    private int capacite;
    private IRegistre registre;
    private double latitude;
    private double longitude;
    private IVelo[] bornes;

    /**
     * constructeur de la classe, qui possède un nom, des coordonnées GPS (latitude et longitude) pour géolocaliser la
     * station, une capacité qui indique le nombre de bornes où on peut arrimer des vélos.
     * @param nom
     * @param latitude
     * @param longitude
     * @param capacite
     */
    public Station(String nom, double latitude, double longitude, int capacite){
        this.nom=nom;
        this.capacite = (capacite < 0) ? 0: capacite;
        this.latitude=latitude;
        this.longitude=longitude;

        bornes=new IVelo[this.capacite];
        for( int i = 0 ; i < capacite ; ++i){
            bornes[i]=null;
        }
    }

    /**
     * permet de connecter la station à un registre des emprunts, décrits ci-après.
     * @param registre
     */
    public void setRegistre(IRegistre registre){this.registre=registre;}

    /**
     * permet de récupérer le nom de la station.
     * @return name
     */
    public String getNom(){return nom;}

    /**
     * permet de consulter la capacité de la station.
     * @return capacity
     */
    public int capacite(){return capacite;}

    /**
     * permet de connaître le nombre de bornes actuellement inoccupées
     * @return nbBorne libre
     */
    public int nbBornesLibres(){
        int c=0;
        for(int i = 0 ; i < capacite ; ++i){
            if(bornes[i] == null){c++;}
        }
        return c;
    }

    /**
     * permet de récupérer le vélo qui est actuellement arrimé à la borne b. Si le numéro de la borne est incorrect
     * (les bornes sont numérotées de 1 à la capacité de la station), ou si la borne est vide.txt,
     * la méthode retourne null. Dans le cas contraire, elle renvoie l'instance de vélo associé à la borne.
     * @param b
     * @return
     */
    public IVelo veloALaBorne(int b){
        if(b > capacite || b <= 0){return null;}
        return bornes[b-1];
    }


    /**
     * réalise l'emprunt du vélo qui est à la borne b. Le vélo ne peut être emprunté que si l'abonné n'a pas déjà un
     * emprunt en cours. Si tout est correct (paramètres valides, borne non vide.txt, abonné non bloqué, registre connecté,
     * décrochage du vélo effectif, enregistement de l'emprunt, etc.) la méthode notifie le registre et renvoie le vélo
     * qui était à la borne, sinon la méthode renvoie null sans distinguer la cause de l'erreur.
     * @param a
     * @param b
     * @return
     */
    public IVelo emprunterVelo(Abonne a, int b){
        if(registre == null || a == null
                || registre.nbEmpruntsEnCours(a) != 0
                || nbBornesLibres() == capacite || a.estBloque()
                || b > capacite || b <= 0
                || bornes[b-1].decrocher() != 0
                || registre.emprunter(a,bornes[b-1],maintenant()) != 0)
        {return null;}

        IVelo v = bornes[b-1];
        bornes[b-1]=null;
        return v;
    }

    /**
     * réalise l'arrimage d'un vélo à la borne b. Si les paramètres sont incorrects (objet null ou numéro de borne en
     * dehors des valeurs attendues), la méthode renvoie -1. Si la borne demandée est déjà occupée ou si le registre
     * n'est pas connecté à la station, la méthode renvoie -2. Si le vélo n'a pas pu être arrimé à la borne, la méthode
     * renvoie -3. Si le registre a produit une erreur à l'enregistrement du retour, la méthode renvoie -4 mais le vélo
     * est arrimé (son retour n'a pas pu être enregistré, mais cela ne l'empêche pas d'être accroché à une borne). Si
     * tout s'est bien passé, la méthode renvoie 0 et le retour du vélo à la borne est transmis au registre.
     * @param v
     * @param b
     * @return
     */
    public int arrimerVelo(IVelo v, int b){
        if(v == null || b > capacite || b <= 0) {return -1;}
        if(registre == null || veloALaBorne(b) != null){return -2;}
        if(v.arrimer() != 0){return -3;}
        bornes[b-1] = v;
        if(registre.retourner(v,maintenant()) != 0){return -4;}

        return 0;
    }

    /**
     * *method qui renvoie le premier vélo trouver dans le camion qui est valide sinon null
     * @param velos CAMION DE VELO
     * @return
     */
    private IVelo valideVelo(Set<IVelo> velos){
        for (IVelo v:velos) {
            if(!v.estAbime() && v.prochaineRevision() > 0){
                return v;
            }
        }
        return null;
    }

    private int nombreVeloNeuf(Set<IVelo> velos){
        int c=0;
        for (IVelo v: velos) {
            if(!v.estAbime() && v.prochaineRevision() >0)
                c++;
        }
        return c;
    }

    private void parcourirCamion(Set<IVelo> velos,int i){
        for (IVelo v : velos) {
            if (!v.estAbime() && v.prochaineRevision() > 0.0) {
                bornes[i] = v;
                v.arrimer();
                velos.remove(v);
                break;
            }
        }
    }
    /**
     * ré-équilibre la station en enlevant/ajoutant des vélos pour faire en sorte d'avoir la moitié des bornes
     * (arrondi à l'entier supérieur) avec des vélos disponibles. Les vélos abîmés sont retirés et remplacés par des
     * vélos en bon état, pris dans l'ensemble velos passé en paramètre.
     *
     * Les vélos nécessitant une révision sont aussi retirés, à moins qu'il n'y ait pas assez de vélos pour les remplacer.
     * On s'autorise à avoir moins de la moitié des bornes équipées en vélos si on a pas assez de stock de vélos en bon
     * état dans l'ensemble velos pour faire les remplacements.
     *
     * Le paramètre velos est donc un ensemble de vélos "de rechange" utilisés pour remplacement. À l'issue de
     * l'exécution de cette méthode, cet ensemble aura été complété par les vélos retirés de la station
     * (abîmés ou à réviser, suivant les critères décrits ci-dessus). Les vélos utilisés pour remplacement sont par
     * contre retirés de cet ensemble pour être mis sur les bornes.
     * @param velos
     */
    public void equilibrer (Set<IVelo> velos) {
        if(velos == null){return;}
        for (int i = 0; i < capacite; i++) {
            if (bornes[i] != null) {
                if (bornes[i].estAbime()) {
                    velos.add(bornes[i]);
                    bornes[i].decrocher();
                    bornes[i] = null;
                    parcourirCamion(velos,i);
                }
            }
        }

        for (int i = 0; i < capacite; i++) {
            if (bornes[i] != null) {
                if (bornes[i].prochaineRevision() <= 0 && nombreVeloNeuf(velos) > 0) {
                    velos.add(bornes[i]);
                    bornes[i].decrocher();
                    bornes[i] = null;
                    parcourirCamion(velos,i);
                }
            }
        }


        for (int i = 0; i < capacite && nbBornesLibres() > Math.floor(capacite() / 2); i++) {
            if (bornes[i] == null) {
                parcourirCamion(velos,i);
            }
        }

        if (nbBornesLibres() == Math.floor(capacite() / 2)) {
            return;
        }

        for (int i = 0; i < bornes.length && nbBornesLibres() < Math.floor(capacite() / 2); i++) {
            if (bornes[i] != null) {
                if (bornes[i].prochaineRevision() <= 0.0) {
                    bornes[i].decrocher();
                    velos.add(bornes[i]);
                    bornes[i] = null;
                }
            }
        }

        if (nbBornesLibres() == Math.floor(capacite() / 2)) {
            return;
        }

        for (int i = 0; i < capacite && nbBornesLibres() < Math.floor(capacite() / 2); i++) {
            if (bornes[i] != null) {
                bornes[i].decrocher();
                velos.add(bornes[i]);
                bornes[i] = null;

            }
        }
    }

    /**
     * permet de calculer la distance en kilomètres entre deux stations. Cette distance est basée sur les coordonnées
     * GPS de chacune des stations. Le calcul applique la formule de Haversine, qui permet de calculer une distance
     * orthodromique
     * @param s station
     * @return distance
     */
    public double distance(Station s){
        double R = 6371e3; // metres
        double φ1 = latitude * Math.PI/180; // φ, λ in radians
        double φ2 = s.latitude * Math.PI/180;
        double Δφ = (s.latitude-latitude) * Math.PI/180;
        double Δλ = (s.longitude-longitude) * Math.PI/180;

        double a = Math.sin(Δφ/2) * Math.sin(Δφ/2) + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ/2) * Math.sin(Δλ/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (R * c)/1000; // in metres
    }

    /**
     * renvoie la date actuelle sous la forme d'un timestamp (entier long représentant le nombre de millisecondes
     * écoulées depuis l'origine du monde informatique le 1er janvier 1970 à minuit.
     * Cette méthode sera utilisée pour déterminer les dates et horaires d'emprunt et de retour du vélo lors des
     * notifications du registre.
     * Indication : cette méthode se contente de renvoyer la valeur System.currentTimeMillis().
     * @return
     */
    public long maintenant(){return System.currentTimeMillis();}



}
