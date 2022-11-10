package fr.ufc.l3info.oprog.parser;

import fr.ufc.l3info.oprog.Station;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Visiteur réalisant des vérifications sur l'AST du fichier de stations.
 */
public class ASTCheckerVisitor implements ASTNodeVisitor {

    private HashMap<String,ERROR_KIND> error = new HashMap<String,ERROR_KIND>() ;
    private Set<String> listStation =  new HashSet<String>();

    public ASTCheckerVisitor() {
        error = new HashMap<>();
    }

    public Map<String, ERROR_KIND> getErrors() {
        return error;
    }

    @Override
    public Object visit(ASTNode n) {return null;}

    @Override
    public Object visit(ASTListeStations n) {
        for (ASTNode child : n) {
            child.accept(this);
        }
        if(listStation.isEmpty()){
            error.put("void", ERROR_KIND.EMPTY_LIST);
        }
        return null;
    }

    @Override
    public Object visit(ASTStation n) {
        String name = (String) n.getChild(0).accept(this);
        double latitude = 0.0;
        double longitude = 0.0;
        int capacite = 0;

        int a =0 ; int b = 0; int c = 0;
        for (int i=1; i < n.getNumChildren(); i++) {

            Object[] decl = (Object[]) n.getChild(i).accept(this);
            switch (((String) decl[0]).toLowerCase()) {
                case "capacite":
                    ++a;
                    break;
                case "longitude":
                    ++b;
                    break;
                case "latitude":
                    ++c;
                    break;
            }

        }
        if(a < 1 || b < 1 || c < 1){
            error.put("MISSING_DECLARATION capacite longitude latitude", ERROR_KIND.MISSING_DECLARATION);
        }
        if(a > 1 || b > 1 || c > 1){
            error.put("DUPLICATE_DECLARATION capacite longitude latitude", ERROR_KIND.DUPLICATE_DECLARATION);
        }


        Station s = new Station(name,latitude,longitude,capacite);
        if(!listStation.add(s.getNom())){
            error.put("erreur doublons", ERROR_KIND.DUPLICATE_STATION_NAME);
        }
        if(name.trim().equals("\"\"")){
            error.put("erreur nom vide.txt", ERROR_KIND.EMPTY_STATION_NAME);
        }





        return null;
    }

    @Override
    public Object visit(ASTDeclaration n) {
        String key = (String) n.getChild(0).accept(this);
        String value = (String) n.getChild(1).accept(this);
        Double valuee= Double.parseDouble(value);
        if(key.equals("capacite")  && value.indexOf('.') != -1){
            error.put("mauvais nombre", ERROR_KIND.WRONG_NUMBER_VALUE);
        }
        if(key.equals("capacite") && valuee <= 0){
            error.put("mauvais nombre", ERROR_KIND.WRONG_NUMBER_VALUE);
        }


        return new Object[] { key, valuee };
    }

    @Override
    public Object visit(ASTChaine n) {
        return n.value;
    }

    @Override
    public Object visit(ASTIdentificateur n) { return n.toString(); }

    @Override
    public Object visit(ASTNombre n){return n.value;}


}

enum ERROR_KIND {
    EMPTY_LIST,
    EMPTY_STATION_NAME,
    DUPLICATE_STATION_NAME,
    MISSING_DECLARATION,
    DUPLICATE_DECLARATION,
    WRONG_NUMBER_VALUE
}