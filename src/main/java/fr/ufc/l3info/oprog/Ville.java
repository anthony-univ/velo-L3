package fr.ufc.l3info.oprog;

import fr.ufc.l3info.oprog.parser.*;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Ville implements Iterable<Station>{

    private Station stationPrincipal;

    private Set<Station> stations;

    private IRegistre r;

    private Set<Abonne> setDabo;

    /** Instance singleton du parser de stations */
    final StationParser parser = StationParser.getInstance();

    /**
     * est le constructeur de la ville.
     */
    public Ville(){
        this.setDabo = new HashSet<>();
        this.r = new JRegistre();
        this.stationPrincipal = null;
        this.stations = new HashSet<>();
    }

    /**
     * permet d'initialiser l'ensemble des stations de la ville à partir du
     * fichier de description passé en paramètre. Si le fichier est incorrectement
     * formatté ou s'il contient des erreurs, aucune station n'est associée à la ville.
     * Cette méthode peut déclencher une exception de type IOException issue du parser
     * qui est utilisé pour réaliser l'analyse syntaxique (cf. TP6).
     * On notera que chaque appel à cette méthode aura pour effet de réinitialiser
     * l'ensemble des stations existantes.
     *
     *
     * @param f fichier de description
     * @throws IOException issue du parser
     */
    public void initialiser(File f) throws IOException{

        ASTNode n = null;
        try{
            n = parser.parse(f);
        }
        catch(Exception StationParserException){
            //throw new IOException(); ///////////////:voir avec léo
            return;
        }

        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        n.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        if(!errors.isEmpty()){
            return ;
        }

        ASTStationBuilder builder = new ASTStationBuilder();
        n.accept(builder);

        this.stations = builder.getStations();
        String name = n.getChild(0).getChild(0).toString().replace("\"", "");


        for (Station s : this.stations) {
            //System.out.println("initialiser;" + s.getNom()+ ";principale defaut;" + name +";");
            s.setRegistre(this.r);
            if(name.equals(s.getNom())) {
                this.stationPrincipal = s;
            }
        }
        //System.out.println("station  principale ->" + stationPrincipal.getNom());
    }

    /**
     * permet de définir la station principale de la ville en donnant son nom sous
     * forme d'une chaîne de caractères. Tant que cette méthode n'a pas été appelée,
     * la première station du fichier d'initialisation est considérée comme étant la
     * station principale.
     *
     * @param st nom de la station
     */
    public void setStationPrincipale(String st){
        for (Station s:stations) {
            if(s !=null && s.getNom().equals(st)){
                stationPrincipal = s;
            }
        }
    }

    /**
     *
     * permet de récupérer la station dont le nom est passé en paramètre, ou null si
     * la station n'existe pas.
     *
     * @param nom station rechercher
     * @return station ou null si nexiste pas
     */
    public Station getStation(String nom){
        if(nom == null) {return null;}

        for (Station s:stations) {
            if(s != null && s.getNom().equals(nom)){
                return s;
            }
        }
        return null;
    }

    /**
     * renvoie la station qui est la plus proche du point dont les coordonnées GPS
     * (latitude, longitude)sont passés en paramètre.
     *
     * @param lat
     * @param lon
     * @return
     */
    public Station getStationPlusProche(double lat, double lon){
        Station stationRef = new Station("t",lat,lon,1);
        Station stationPlusProche = null;
        Double d = 9999999999999999999999999.0;
        for (Station s : this.stations) {
            double distance = s.distance(stationRef);
            //System.out.println(s.getNom());
            //System.out.println(stationRef.getNom());
            //System.out.println(distance);
            if(s != null && distance < d){
                d = distance;
                stationPlusProche = s ;
            }
        }
        return stationPlusProche;
    }

    /**
     *
     * permet de créer un abonné avec les caractéristiques passées en paramètre.
     * La méthode renvoie null en cas d'erreur.
     *
     * @param nom
     * @param RIB
     * @return
     */
    public Abonne creerAbonne(String nom, String RIB) {

        Abonne a = null;
        try{
            a = new Abonne(nom,RIB);
        } catch (Exception IncorrectNameException) {
            return null;
        }
        setDabo.add(a);
        return a;
    }

    /**
     *
     * permet de créer un itérateur permettant de parcourir les stations de la ville
     * à partir de la station principale. Cette méthode instanciera un itérateur de
     *      type ClosestStationIterator décrit ci-après.
     *
     * @return
     */
    public Iterator<Station> iterator() {
        ClosestStationIterator c = new ClosestStationIterator( this.stations, this.stationPrincipal);
        return c;
    }

    /**
     *
     * calcule la facturation mensuelle de chaque abonné pour le mois et l'année passés
     * en paramètre (du 1er jour du mois à minuit, au 1er jour du mois suivant juste avant
     * minuit).
     *
     * @param mois
     * @param annee
     * @return
     */
    public Map<Abonne, Double> facturation(int mois, int annee)   {
        Map<Abonne,Double> facturation = new HashMap<Abonne,Double>();
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, mois-1);
        cal.set(Calendar.YEAR, annee);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND,0);
        long debut  = cal.getTimeInMillis();
        System.out.println("debut facture "  + cal.getTimeInMillis());
        System.out.println("debut facture "  + new Date(cal.getTimeInMillis()));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, mois);
        cal.set(Calendar.YEAR, annee);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        //cal.set(Calendar.MILLISECOND, 59999);
        long fin  = cal.getTimeInMillis()-1;
        System.out.println("fin facture "  + (cal.getTimeInMillis()-1));
        System.out.println("fin facture "  + new Date(cal.getTimeInMillis()-1));

        for (Abonne a: this.setDabo) {
            //System.out.println("la:::::::: " + " " + this.r.facturation(a, debut, fin));
            facturation.put(a, this.r.facturation(a, debut, fin));
        }
        return facturation;
    }

}
