import fr.ufc.l3info.oprog.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class LVFonctionnelsTests {

    final String vraiPath = "./target/classes/data/";

    @Test
    public void testFonctionnels() throws IOException {
        Ville v = new Ville();
        v.initialiser(new File(vraiPath + "OK1.txt"));

        Abonne a1 = v.creerAbonne("leo","00000-00000-00000000000-97");
        Abonne a2 = v.creerAbonne("le rat","00000-00000-00000000000-97");

        String[] tabe = {};
        FabriqueVelo decathlon = FabriqueVelo.getInstance();

        Exploitant e = new Exploitant();
        for (int i = 0 ; i < 50 ; ++i){
            e.acquerirVelo(decathlon.construire('h',"CADRE_ALUMINIUM"));
        }
        e.ravitailler(v);

        for (Station s:v) {
            System.out.println(s.getNom());
            System.out.println(s.nbBornesLibres()+"/"+s.capacite());
            System.out.println("_____________________");
            Assert.assertEquals(s.capacite()/2,s.nbBornesLibres());
        }

        Station viotte = v.getStation("Gare Viotte");
        IVelo v1 = viotte.emprunterVelo(a1,1);

        Assert.assertEquals(6,viotte.nbBornesLibres());

        v1.parcourir(3000);
        v1.abimer();
        viotte.arrimerVelo(v1,1);

        for (int i = 1 ; i <= viotte.capacite() ; ++i){
            IVelo vv = viotte.veloALaBorne(i);
            System.out.println(vv);
        }

        e.ravitailler(v);
        e.entretenirVelos();






    }


}
