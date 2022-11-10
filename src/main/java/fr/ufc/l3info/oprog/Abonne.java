    package fr.ufc.l3info.oprog;

    import java.util.concurrent.atomic.AtomicInteger;
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;
    /**
     * Classe représentant un abonné au service VéloCité.
     */
    public class Abonne {

        /*
        * nom abonné
        * */
        private String nom;
        private String rib;
        private static final AtomicInteger idGenerateur = new AtomicInteger(0);
        private int id;

        /*
        * private boolean[]         bloquer[0] savoir si bloquer |  bloquer[1]  savoir pourquoi si par admin ou mauvais rib (false si bloquer naturellement sinon 1)
        * */
        private boolean[] bloquer = new boolean[2];

        /**
         * Créé un abonné dont le nom est passé en paramètre, sans informations bancaires.
         *  Si le nom de l'abonné n'est pas correct (vide.txt ou ne contenant pas des lettres éventuellement séparées par des espaces ou des traits d'union), le constructeur déclenchera l'exception IncorrectNameException.
         *  On notera que le nom peut contenir des espaces inutiles en début et en fin, mais ceux-ci seront retirés pour enregistrer cette donnée.
         * @param nom le nom du nouvel abonné.
         * @throws IncorrectNameException si le nom de l'abonné n'est pas correct.
         */
        public Abonne(String nom) throws IncorrectNameException {
            boolean var =true;
            if(nom == null){
                var = false;
            }else{
                nom=nom.trim();
            }
            if(var==false || !isValideName(nom)){
                throw new IncorrectNameException();
            }else {
                this.nom = nom;
                id = idGenerateur.getAndIncrement();
                this.bloquer[0] = true;
            }
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
            if(!isValideRib(rib)){
                this.bloquer[0] = true;
                this.bloquer[1] = false;
            }else {
                this.bloquer[0] = false;
                this.rib=rib;
            }
        }

        /**
         * Renvoie l'identifiant de l'abonné, généré autoamtiquement à sa création.
         * @return l'identifiant de l'abonné.
         */
        public int getID() {
            return this.id;
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
            if(isValideRib(rib)){
                this.rib=rib;
                this.bloquer[0]=false;
            }
        }

        /**
         * Permet de bloquer volontairement un abonné.
         */
        public void bloquer() {
            this.bloquer[1] = true;
        }

        /**
         * Permet de débloquer un abonné.
         */
        public void debloquer() {
            this.bloquer[1] = false;
        }

        /**
         * Vérifie si un abonné est bloqué. Celui-ci peut être bloqué volontairement ou parce que ses coordonnées bancaires sont invalides.
         * @return true si l'abonné est considéré comme bloqué, false sinon.
         */
        public boolean estBloque() {
            return this.bloquer[0] || this.bloquer[1];
        }

        /**
         * permet de tester si deux abonnés sont identiques. Pour cela, on vérifiera si leur identifiant est le même.
         * @param a l'abonné avec lequel est comparé l'instance courante.
         * @return true si les deux objets ont le même ID, false sinon.
         */
        public boolean equals(Object a) {
            if(a==null || !(a instanceof Abonne)){return false;}
            return this.getID() == ((Abonne)a).getID();
        }

        /**
         * Utilisée en interne par Java pour obtenir un hash de l'objet. Cette méthode est utilisée pour les structures de collection de type HashSet ou HashMap.
         * @return le hash de l'instance courante.
         */
        public int hashCode() {
            return (this.id * 42) ;
        }

        /**
         * permet de tester si le nom passer en parametre est valide. Ne contient pas de caracteres non valide
         * @param nom de l'abonné qu'on va passer dans la regex regarder s'il est valide
         * @return la valeur du match de la regex
         */
        private Boolean isValideName(String nom){

            Pattern pattern = Pattern.compile("[\\p{IsLatin}]([\\p{IsLatin}]|[ -][\\p{IsLatin}])*");
            Matcher matcher = pattern.matcher(nom);
            boolean result = matcher.matches();
            return result;
        }

        /**
         * permet de tester la validité d'un rib donné en parametre
         * @param rib de l'abboné pour verifier sa validité
         * @return la valeur du match de la regex OU du calcul
         */
        private Boolean isValideRib(String rib){
            if(rib == null){return false;}
            Pattern pattern = Pattern.compile("(\\d{5}-){2}\\d{11}\\-\\d{2}");
            Matcher matcher = pattern.matcher(rib);
            if(!matcher.matches()){return false;};

            int banque = Integer.parseInt(rib.substring(0,5));
            int guichet = Integer.parseInt(rib.substring(6,11));
            int compte = Integer.parseInt(rib.substring(12,23));
            int cle = Integer.parseInt(rib.substring(24,26));

            if(cle != 97-( (banque*89 + guichet*15 + compte*3) % 97) ) {return false;}

            return true;
        }

    }

    class IncorrectNameException extends Exception {
        public IncorrectNameException() {
            super("Le nom fourni n'est pas correct.");
        }
    }
