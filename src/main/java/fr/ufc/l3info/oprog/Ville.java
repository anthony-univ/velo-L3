package fr.ufc.l3info.oprog;

import fr.ufc.l3info.oprog.parser.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Ville implements Iterable<Station>{

    private Station principal;

    private Set<Station> stations;

    private IRegistre r;

    /** Instance singleton du parser de stations */
    final StationParser parser = StationParser.getInstance();

    /**
     * est le constructeur de la ville.
     */
    public Ville(){
        r = new JRegistre();
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
    public void initialiser(File f) throws IOException {

        ASTNode n;
        try{n = parser.parse(f);}
        catch(Exception StationParserException){return ;}

        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        n.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        if(!errors.isEmpty()){
            return ;
        }

        ASTStationBuilder builder = new ASTStationBuilder();
        n.accept(builder);


        stations = builder.getStations();
        principal = stations.iterator().next();
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
            if(s.getNom().equals(st)){
                principal=s;
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
        for (Station s:stations) {
            if(s.getNom().equals(nom)){
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
        Station ss = new Station("t",lat,lon,1);
        Station sss = null;
        Double d = 9999999999999999999999999.0;
        for (Station s : stations) {
            if(s.distance(ss) < d){
                d = s.distance(ss);
                sss=s ;
            }
        }
        return sss;
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
    public Abonne creerAbonne(String nom, String RIB){
        Abonne a = null;
        try{
            a = new Abonne(nom,RIB);
        } catch (Exception IncorrectNameException) {
            return null;
        }
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
    public Iterator<Station> iterator(){

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
    public Map<Abonne, Double> facturation(int mois, int annee){

    }


}
