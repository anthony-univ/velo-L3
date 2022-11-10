package fr.ufc.l3info.oprog;

public class FabriqueVelo {

    private static FabriqueVelo INSTANCE = null;

    private FabriqueVelo() { }

    public static FabriqueVelo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FabriqueVelo();
        }
        return INSTANCE;
    }

    public IVelo construire(char t, String... options) {
        IVelo v = new Velo(t);
        boolean [] booleans = {false, false, false, false, false};

        for (String option : options) {
            if (!booleans[0] && option == "CADRE_ALUMINIUM") {
                v = new OptCadreAlu(v);
                booleans[0] = true;
            }
            if (!booleans[1] && option == "SUSPENSION_AVANT") {
                v = new OptSuspensionAvant(v);
                booleans[1] = true;
            }
            if (!booleans[2] && option == "SUSPENSION_ARRIERE") {
                v = new OptSuspensionArriere(v);
                booleans[2] = true;
            }
            if (!booleans[3] && option == "FREINS_DISQUE") {
                v = new OptFreinsDisque(v);
                booleans[3] = true;
            }
            if (!booleans[4] && option == "ASSISTANCE_ELECTRIQUE") {
                v = new OptAssistanceElectrique(v);
                booleans[4] = true;
            }
        }
        return v;
    }
}
