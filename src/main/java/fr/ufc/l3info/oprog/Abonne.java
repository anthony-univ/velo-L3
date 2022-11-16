package fr.ufc.l3info.oprog;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Classe représentant un abonné au service VéloCité.
 */
public class Abonne {

    private String nom;
    private String rib;
    private int ID;
    private boolean estBloque;
    private boolean estBloqueVolontairement;
    private static final AtomicInteger generatorId = new  AtomicInteger(0);

    /**
     * Verifie la conformité du rib.
     * @param rib le rib à verifier.
     * @return true si la rib est valide, false sinon.
     */
    private boolean ribEstValide(String rib) {
        if(rib == null || !Pattern.matches("^([\\d]{5}-){2}[\\d]{11}-[\\d]{2}$", rib)) {
            return false;
        }

        int codeBanque = Integer.parseInt(rib.substring(0, 5));
        int codeGuichet = Integer.parseInt(rib.substring(6, 11));
        long numCompte = Long.parseLong(rib.substring(12, 23));
        int cle = Integer.parseInt(rib.substring(24, 26));

        if(cle != 97-((89*codeBanque + 15*codeGuichet + 3*numCompte)%97)) {
            return false;
        }

        return true;
    }

    /**
     * Créé un abonné dont le nom est passé en paramètre, sans informations bancaires.
     *  Si le nom de l'abonné n'est pas correct (vide ou ne contenant pas des lettres éventuellementséparées par des espaces ou des traits d'union), le constructeur déclenchera l'exception IncorrectNameException.
     *  On notera que le nom peut contenir des espaces inutiles en début et en fin, mais ceux-ci seront retirés pour enregistrer cette donnée.
     * @param nom le nom du nouvel abonné.
     * @throws IncorrectNameException si le nom de l'abonné n'est pas correct.
     */
    public Abonne(String nom) throws IncorrectNameException {
        if(nom == null || !Pattern.matches("^[ ]*[\\p{IsLatin}]([ -]?[\\p{IsLatin}]+)*[ ]*$", nom)) {
            throw new IncorrectNameException();
        }

        this.nom = nom.trim();
        this.rib = "";
        this.ID = Abonne.generatorId.getAndIncrement();
        this.estBloque = true;
        this.estBloqueVolontairement = false;
    }

    /**
     * Créé un abonné dont le nom est passé en paramètre, avec les informations bancaires spécifiées dans le second paramètre.
     *  Le comportement attendu est le même que celui du constructeur précédent. Le RIB n'est enregistré que si celui-ci est valide.
     * @param nom le nom du nouvel abonné.
     * @param rib le RIB
     * @throws IncorrectNameException si le nom de l'abonné n'est pas correct.
     */
    public Abonne(String nom, String rib) throws IncorrectNameException {
        this(nom);

        if(this.ribEstValide(rib)) {
            this.rib = rib;
            this.estBloque = false;
        }
    }

    /**
     * Renvoie l'identifiant de l'abonné, généré autoamtiquement à sa création.
     * @return l'identifiant de l'abonné.
     */
    public int getID() {
        return this.ID;
    }

    /**
     * Renvoie le nom de l'abonné.
     * @return le nom de l'abonné, sans les éventuels espace en début et en fin de chaîne.
     */
    public String getNom() {
        return this.nom;
    }

    /**
     * Met à jour l'ancien RIB pour un nouveau. Si le nouveau RIB n'est pas valide, l'abonné conserve ses anciennes coordonnées bancaires.
     * @param rib nouveau RIB pour la mise à jour.
     */
    public void miseAJourRIB(String rib) {
        if(this.ribEstValide(rib)) {
            this.rib = rib;
            this.estBloque = false;
        }
    }

    /**
     * Permet de bloquer volontairement un abonné.
     */
    public void bloquer() {
        this.estBloqueVolontairement = true;
    }

    /**
     * Permet de débloquer un abonné.
     */
    public void debloquer() {
        this.estBloqueVolontairement = false;
    }

    /**
     * Vérifie si un abonné est bloqué. Celui-ci peut être bloqué volontairement ou parce que ses coordonnées bancaires sont invalides.
     * @return true si l'abonné est considéré comme bloqué, false sinon.
     */
    public boolean estBloque() {
        return this.estBloque || this.estBloqueVolontairement;
    }

    /**
     * permet de tester si deux abonnés sont identiques. Pour cela, on vérifiera si leur identifiant est le même.
     * @param a l'abonné avec lequel est comparé l'instance courante.
     * @return true si les deux objets ont le même ID, false sinon.
     */
    public boolean equals(Object a) {
        if(a == null || !(a instanceof Abonne)) {
            return false;
        }

        return this.getID() == ((Abonne)a).getID();
    }

    /**
     * Utilisée en interne par Java pour obtenir un hash de l'objet. Cette méthode est utilisée pour les structures de collection de type HashSet ou HashMap.
     * @return le hash de l'instance courante.
     */
    public int hashCode() {
        return this.ID;
    }

}

class IncorrectNameException extends Exception {
    public IncorrectNameException() {
        super("Le nom fourni n'est pas correct.");
    }
}