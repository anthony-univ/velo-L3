import fr.ufc.l3info.oprog.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AGGTestsFonctionnels {
    Ville ville;
    Station viotte;
    Station mairie;
    Station saintJacques;
    Station granvelle;
    Station vh;
    Station jeanCornet;
    Exploitant exploitant;
    Abonne George;
    Abonne Eva;
    Abonne Chris;
    Abonne Chelsea;
    Abonne Anthony;
    Abonne Bloque;
    Abonne Debloque;
    long mtn = 1669043486238L;  //novembre 21/2022
    long dans4h =  mtn + 240000;
    long dans5h =  mtn + 300000;
    long dans6h =  mtn + 360000;

    @Before
    public  void before() throws IOException {
        ville = new Ville();
        ville.initialiser(new File("./target/classes/data/OK1.txt")); // 6 stations

        viotte = Mockito.spy(ville.getStation("Gare Viotte"));
        Assert.assertNotNull(viotte);
        mairie = Mockito.spy(ville.getStation("Mairie"));
        Assert.assertNotNull(mairie);
        saintJacques = Mockito.spy(ville.getStation("Saint Jacques"));
        Assert.assertNotNull(saintJacques);
        granvelle = Mockito.spy(ville.getStation("Granvelle"));
        Assert.assertNotNull(granvelle);
        vh = Mockito.spy(ville.getStation("Victor Hugo"));
        Assert.assertNotNull(vh);
        jeanCornet = Mockito.spy(ville.getStation("Jean Cornet"));
        Assert.assertNotNull(jeanCornet);

        George = ville.creerAbonne("George", "11111-11111-11111111111-48");
        Assert.assertNotNull(George);
        Eva = ville.creerAbonne("Eva", "11111-11111-11111111111-48");
        Assert.assertNotNull(Eva);
        Chris = ville.creerAbonne("Chris", "11111-11111-11111111111-48");
        Assert.assertNotNull(Chris);
        Chelsea = ville.creerAbonne("Chelsea", "11111-11111-11111111111-48");
        Assert.assertNotNull(Chelsea);
        Anthony = ville.creerAbonne("Anthony", "11111-11111-11111111111-48");
        Assert.assertNotNull(Anthony);
        Bloque = ville.creerAbonne("Bloque", null);
        Assert.assertNotNull(Bloque);
        Debloque = ville.creerAbonne("Debloque", "11111-11111-11111111111-48");
        Assert.assertNotNull(Debloque);


        exploitant = new Exploitant();
        FabriqueVelo fv = FabriqueVelo.getInstance();
        exploitant.acquerirVelo(fv.construire('m'));
        exploitant.acquerirVelo(fv.construire('h'));
        exploitant.acquerirVelo(fv.construire('f'));
        exploitant.acquerirVelo(fv.construire('m'));
        exploitant.acquerirVelo(fv.construire('h'));
        exploitant.acquerirVelo(fv.construire('f'));
        exploitant.acquerirVelo(fv.construire('m'));
        exploitant.acquerirVelo(fv.construire('h'));
        exploitant.acquerirVelo(fv.construire('f'));
        exploitant.acquerirVelo(fv.construire('m'));
        exploitant.acquerirVelo(fv.construire('h'));
        exploitant.acquerirVelo(fv.construire('f'));
        exploitant.acquerirVelo(fv.construire('m'));
        exploitant.acquerirVelo(fv.construire('h'));
        exploitant.acquerirVelo(fv.construire('f'));
        exploitant.acquerirVelo(fv.construire('m'));
        exploitant.acquerirVelo(fv.construire('h'));
        exploitant.acquerirVelo(fv.construire('f'));
        exploitant.acquerirVelo(fv.construire('m'));
        exploitant.acquerirVelo(fv.construire('h'));
        exploitant.acquerirVelo(fv.construire('f'));
        exploitant.acquerirVelo(fv.construire('m'));
        exploitant.acquerirVelo(fv.construire('h'));
        exploitant.acquerirVelo(fv.construire('f'));

        exploitant.ravitailler(ville);

        Assert.assertEquals(5, viotte.nbBornesLibres());
        Assert.assertEquals(5, mairie.nbBornesLibres());
        Assert.assertEquals(6, saintJacques.nbBornesLibres());
        Assert.assertEquals(5, granvelle.nbBornesLibres());
        Assert.assertEquals(5, vh.nbBornesLibres());
        Assert.assertEquals(12, jeanCornet.nbBornesLibres());
    }

    @Test
    public void EmprunterVeloDeposerEmprunterFactures()  {
        //normal Trajet
        Double t =0.0;
        for (int i=1 ; i<=viotte.capacite() ; ++i) {
            IVelo v = viotte.veloALaBorne(i);
            if(v != null) {
                when(viotte.maintenant()).thenReturn(mtn);
                Assert.assertNotNull(viotte.emprunterVelo(Chelsea, i));
                Assert.assertNull(jeanCornet.veloALaBorne(1));
                v.parcourir(4.0);
                when(jeanCornet.maintenant()).thenReturn(dans4h);
                Assert.assertEquals(0, jeanCornet.arrimerVelo(v, 1));
                Assert.assertNotNull(jeanCornet.veloALaBorne(1));
                break;
            }
        }

        when(jeanCornet.maintenant()).thenReturn(dans5h);
        IVelo v = jeanCornet.emprunterVelo(George, 1);
        Assert.assertNull(jeanCornet.veloALaBorne(1));
        v.parcourir(1.2);
        when(jeanCornet.maintenant()).thenReturn(dans6h);
        Assert.assertNotNull(jeanCornet.arrimerVelo(v, 2));
        Assert.assertNotNull(jeanCornet.veloALaBorne(2));

        Map<Abonne, Double> factures = ville.facturation(11, 2022);
        for (Map.Entry f: factures.entrySet()) {
            Abonne a = (Abonne) f.getKey();
            if(a.getNom() == "George") {
                Assert.assertEquals(0.033, (Double) f.getValue(), 0.001);
            } else if(a.getNom() == "Chelsea") {
                Assert.assertEquals(0.133, (Double) f.getValue(), 0.001);
            }else {
                Assert.assertEquals(0.0, (Double) f.getValue(), 0.001);
            }
        }
    }

    @Test
    public void EmprunterVeloDeposerAbimerEmprunter()  {
        //normal Trajet
        for (int i=1 ; i<=viotte.capacite() ; ++i) {
            IVelo v = viotte.veloALaBorne(i);
            if(v != null) {
                when(viotte.maintenant()).thenReturn(dans4h);
                Assert.assertNotNull(viotte.emprunterVelo(Chelsea, i));
                Assert.assertNull(jeanCornet.veloALaBorne(1));
                v.parcourir(4.0);
                v.abimer();
                when(jeanCornet.maintenant()).thenReturn(dans5h);
                Assert.assertEquals(0, jeanCornet.arrimerVelo(v, 1));
                Assert.assertTrue(Chelsea.estBloque());
                Assert.assertNotNull(jeanCornet.veloALaBorne(1));
                break;
            }
        }

        Assert.assertNull(mairie.emprunterVelo(Chelsea, 5)); //bloquer
        Assert.assertNull(jeanCornet.emprunterVelo(Anthony, 1)); //velo abime
    }

    @Test
    public void ReparerVelo()  {
        //normal Trajet
        Abonne [] abonnes = {Chelsea, Chris, George, Anthony, Eva};
        int j = 0;
        for (int i=1 ; i<=viotte.capacite() ; ++i) {
            IVelo v = viotte.veloALaBorne(i);
            if(v != null) {
                when(viotte.maintenant()).thenReturn(dans4h);
                Assert.assertNotNull(viotte.emprunterVelo(abonnes[j], i));
                v.parcourir(4.0);
                v.abimer();
                when(jeanCornet.maintenant()).thenReturn(dans5h);
                Assert.assertEquals(0, jeanCornet.arrimerVelo(v, i));
                Assert.assertNull(jeanCornet.emprunterVelo(Debloque, i)); //velo abime -> impossible emprunter
                Assert.assertNotNull(jeanCornet.veloALaBorne(i));
                Assert.assertTrue(abonnes[j].estBloque());
                j++;
            }
        }
        Assert.assertEquals(10, viotte.nbBornesLibres());
        Assert.assertEquals(7, jeanCornet.nbBornesLibres());

        exploitant.ravitailler(ville);
        Assert.assertEquals(12, jeanCornet.nbBornesLibres());

        exploitant.entretenirVelos();

        exploitant.ravitailler(ville);
        Assert.assertEquals(5, viotte.nbBornesLibres());
        /*
        for (int i=1 ; i < viotte.capacite() ; ++i) {
            System.out.println(viotte.veloALaBorne(i));
            if(viotte.veloALaBorne(i) != null) {
                Assert.assertNotNull(viotte.emprunterVelo(Debloque, i)); //velo pas abime -> possible emprunter
                break;
            }
        }*/
    }

    @Test
    public void ReviserVelo()  {
        //normal Trajet
        Abonne [] abonnes = {Chelsea, Chris, George, Anthony, Eva};
        int j = 0;
        for (int i=1 ; i<=viotte.capacite() ; ++i) {
            IVelo v = viotte.veloALaBorne(i);
            if(v != null) {
                when(viotte.maintenant()).thenReturn(dans4h);
                Assert.assertNotNull(viotte.emprunterVelo(abonnes[j], i));
                v.parcourir(400.0);
                when(jeanCornet.maintenant()).thenReturn(dans5h);
                Assert.assertEquals(0, jeanCornet.arrimerVelo(v, i));
                Assert.assertNotNull(jeanCornet.veloALaBorne(i));
                j++;
            }
        }
        j=0;
        for (int i=1 ; i<=mairie.capacite() ; ++i) {
            IVelo v = mairie.veloALaBorne(i);
            if(v != null) {
                when(mairie.maintenant()).thenReturn(dans4h);
                Assert.assertNotNull(mairie.emprunterVelo(abonnes[j], i));
                v.parcourir(400.0);
                when(jeanCornet.maintenant()).thenReturn(dans5h);
                Assert.assertEquals(0, jeanCornet.arrimerVelo(v, i+5));

                Assert.assertNotNull(jeanCornet.veloALaBorne(i+5));
                j++;
            }
        }

        Assert.assertEquals(10, viotte.nbBornesLibres());
        Assert.assertEquals(10, mairie.nbBornesLibres());
        Assert.assertEquals(2, jeanCornet.nbBornesLibres());

        exploitant.ravitailler(ville); // 4 velo pris jeanCornet
        Assert.assertEquals(6, jeanCornet.nbBornesLibres());

        exploitant.entretenirVelos();

        exploitant.ravitailler(ville); // 4 velos mis vioote
        Assert.assertEquals(6, viotte.nbBornesLibres());

        /*
        for (int i=1 ; i < viotte.capacite() ; ++i) {
            if(viotte.veloALaBorne(i) != null) {
                Assert.assertNotNull(viotte.emprunterVelo(Debloque, i)); //velo reviser -> possible emprunter
                break;
            }
        }*/
    }
}
