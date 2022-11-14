package fr.ufc.l3info.oprog.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Visiteur réalisant des vérifications sur l'AST du fichier de stations.
 */
public class ASTCheckerVisitor implements ASTNodeVisitor {

    private Map<String, ERROR_KIND> mapError;
    private Set<String> listeStations;

    public ASTCheckerVisitor() {
        mapError = new HashMap<String, ERROR_KIND>();
        listeStations = new HashSet<String>();
    }

    public Map<String, ERROR_KIND> getErrors() {
        return mapError;
    }

    @Override
    public Object visit(ASTNode n) { return null; }

    @Override
    public Object visit(ASTListeStations n) {
        for (ASTNode child : n) {
            child.accept(this);
        }

        if(listeStations.size() == 0) {
            mapError.put("liste station vide dans le fichier : " + n.getLCPrefix(), ERROR_KIND.EMPTY_LIST);
        }
        return null;
    }

    @Override
    public Object visit(ASTStation n) {
        String name = (String) n.getChild(0).accept(this);

        int lon_nb = 0, lat_nb = 0, cap_nb = 0;

        for (int i=1; i < n.getNumChildren(); i++) {
            Object[] decl = (Object[]) n.getChild(i).accept(this);
            switch (((String) decl[0]).toLowerCase()) {
                case "capacite":
                    ++cap_nb;
                    break;
                case "longitude":
                    ++lon_nb;
                    break;
                case "latitude":
                    ++lat_nb;
                    break;
            }
        }

        if(name.trim().isEmpty()) {
            mapError.put("Le nom de station doit pas être vide ou assimilé : "+ n.getLCPrefix(), ERROR_KIND.EMPTY_STATION_NAME);
        }
        if(!listeStations.add(name)) {
            mapError.put("Le noms de station doivent être unique : "+ n.getLCPrefix(), ERROR_KIND.DUPLICATE_STATION_NAME);
        }
        if(lon_nb == 0 || lat_nb == 0 || cap_nb == 0) {
            mapError.put("Manque la déclaration : "+ n.getLCPrefix(), ERROR_KIND.MISSING_DECLARATION);
        }
        if(lon_nb > 1 || lat_nb > 1 || cap_nb > 1) {
            mapError.put("Duplication d'un déclaration : "+ n.getLCPrefix(), ERROR_KIND.DUPLICATE_DECLARATION);
        }

        listeStations.add(name);
        return null;
    }

    @Override
    public Object visit(ASTDeclaration n) {
        String key = (String) n.getChild(0).accept(this);
        String cap = (String) n.getChild(1).accept(this);

        Double value = Double.parseDouble(cap);

        if(key.equals("capacite")) {
            if(value <= 0 || cap.indexOf('.') != -1) {
                mapError.put("Capacité négative ou flottante: "+ n.getLCPrefix(), ERROR_KIND.WRONG_NUMBER_VALUE);
            }
        }
        return new Object[] { key, value };
    }

    @Override
    public Object visit(ASTChaine n) { return n.toString().substring(1, n.toString().length()-1); }

    @Override
    public Object visit(ASTIdentificateur n) { return n.toString(); }

    @Override
    public Object visit(ASTNombre n) {
        return n.value;
    }
}

enum ERROR_KIND {
    EMPTY_LIST,
    EMPTY_STATION_NAME,
    DUPLICATE_STATION_NAME,
    MISSING_DECLARATION,
    DUPLICATE_DECLARATION,
    WRONG_NUMBER_VALUE
}