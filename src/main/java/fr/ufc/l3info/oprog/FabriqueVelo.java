package fr.ufc.l3info.oprog;

public class FabriqueVelo {

    /* Construction de l’instance */
    private static final FabriqueVelo INST = new FabriqueVelo();

    /* Constructeur privé */
    private FabriqueVelo() {}

    /* Retourne l’instance du singleton. */
    public static FabriqueVelo getInstance() {
        return INST;
    }

    public IVelo construire(char t, String... options){

        IVelo v = new Velo(t);

        boolean tabUniqOption[] = {false,false,false,false,false};

        for(int i = 0; i < options.length; ++i){
            if(!tabUniqOption[0] && options[i] == "CADRE_ALUMINIUM" ){
                v = new OptCadreAlu(v);
                tabUniqOption[0] = true;
            }else if( !tabUniqOption[1] && options[i] == "SUSPENSION_AVANT" ){
                v = new OptSuspensionAvant(v);
                tabUniqOption[1] = true;
            }else if( !tabUniqOption[2] && options[i] == "SUSPENSION_ARRIERE"){
                v = new OptSuspensionArriere(v);
                tabUniqOption[2] = true;
            }else if( !tabUniqOption[3] && options[i] == "FREINS_DISQUE" ){
                v = new OptFreinsDisque(v);
                tabUniqOption[3] = true;
            }else if( !tabUniqOption[4] && options[i] == "ASSISTANCE_ELECTRIQUE" ){
                v = new OptAssistanceElectrique(v);
                tabUniqOption[4] = true;
            }
        }


        //v = new OptCadreAlu('H',"CADRE_ALUMINIUM");

        return v;
    }

}
