package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

public class StationIntegrationTest {

    private Abonne a = null;
    private IVelo v = null;
    private IRegistre r = null;
    private Station s = null;

    @Before
    public void before() throws IncorrectNameException {
        a = new Abonne("paul", "11111-11111-11111111111-48");
        v = new Velo();
        r = new JRegistre();
        s = new Station("Gare viotte", 47.246501551427329, 6.022715427111734, 10);
    }

    public void initVeloArrimerEmprunter(IVelo v, int b) {
        Assert.assertEquals(-4, s.arrimerVelo(v,b));
        Assert.assertNotNull(s.emprunterVelo(a, b));
        Assert.assertNull(s.veloALaBorne(b));
    }

    @Test
    public void constructeurTestLatitudeStationIncorrect() {
        Station station = new Station("toto", -90.1, 0, 1); // 0,0
        Assert.assertEquals("toto", station.getNom());
        Assert.assertEquals(1, station.capacite());
    }

    @Test
    public void constructeurTestLongitudeStationIncorrect() {
        Station station = new Station("toto", 0, -90.1, 1); // 0,0
        Assert.assertEquals("toto", station.getNom());
        Assert.assertEquals(1, station.capacite());
        //Assert.assertEquals(5286.005745244741, s.distance(station), 0.0000001);
    }

    @Test
    public void constructeurTestLatitudeLongitudeStationTropPetite() {
        Station station = new Station("toto", -90.1, -90.1, 1); // 0,0
        Assert.assertEquals("toto", station.getNom());
        Assert.assertEquals(1, station.capacite());
        //Assert.assertEquals(5286.005745244741, station.distance(s), 0.0000001);
    }

    @Test
    public void constructeurTestLatitudeLongitudeStationTropGrande() {
        Station station = new Station("toto", 90.1, 90.1, 1); // 0,0
        Assert.assertEquals("toto", station.getNom());
        Assert.assertEquals(1, station.capacite());
        //Assert.assertEquals(5286.005745244741, station.distance(s), 0.0000001);
    }

    @Test
    public void constructeurTestCapaciteNegative() {
        Station station = new Station("toto", 0, 0, -1);
        Assert.assertEquals("toto", station.getNom());
        Assert.assertEquals(0, station.capacite());
    }

    @Test
    public void constructeurTest() {
        Station station = new Station("toto", -50.05414157294687, 87.456224784491237, 5);
        Assert.assertEquals("toto", station.getNom());
        Assert.assertEquals(5, station.capacite());
        Assert.assertEquals(13328.78, s.distance(station), 0.5);
    }

    @Test
    public void arrimerVeloNull() {
        v = null;
        Assert.assertEquals(-1, s.arrimerVelo(v, 1));
        Assert.assertNull(s.veloALaBorne(1));
    }

    @Test
    public void arrimerVeloBorneTropPetite() {
        Assert.assertEquals(-1, s.arrimerVelo(v, 0));
        Assert.assertNull(s.veloALaBorne(1));
    }

    @Test
    public void arrimerVeloBorneTropGrande() {
        Assert.assertEquals(-1, s.arrimerVelo(v, 11));
        Assert.assertNull(s.veloALaBorne(1));
    }

    @Test
    public void arrimerVeloRegistreNull() {
        Assert.assertEquals(-2, s.arrimerVelo(v, 1));
        Assert.assertNull(s.veloALaBorne(1));
    }

    @Test
    public void arrimerVeloSurBornePrise() {
        s.setRegistre(r);
        initVeloArrimerEmprunter(v, 1);
        Assert.assertEquals(0, s.arrimerVelo(v, 1));
        IVelo v2 = new Velo();
        Assert.assertEquals(-2, s.arrimerVelo(v2, 1));
        Assert.assertNotNull(s.veloALaBorne(1));
    }

    @Test
    public void arrimerDeuxFoisVelo() {
        s.setRegistre(r);
        initVeloArrimerEmprunter(v, 1);
        Assert.assertEquals(0, s.arrimerVelo(v, 1));

        Assert.assertEquals(-3, s.arrimerVelo(v, 2));
        Assert.assertNotNull(s.veloALaBorne(1));
        Assert.assertNull(s.veloALaBorne(2));
    }

    @Test
    public void arimerVeloPasEmprunter() {
        s.setRegistre(r);

        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertNotNull(s.veloALaBorne(1));
    }

    @Test
    public void arimerVeloMauvaiseDateRetour() {
        s = Mockito.spy(s);
        s.setRegistre(r);
        initVeloArrimerEmprunter(v, 1);
        Mockito.when(s.maintenant()).thenReturn(System.currentTimeMillis() - 10 * 60 * 1000);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertNotNull(s.veloALaBorne(1));
    }

    @Test
    public void arrimerVeloCorrect() {
        s.setRegistre(r);
        initVeloArrimerEmprunter(v, 1);
        Assert.assertEquals(0, s.arrimerVelo(v, 1));
        Assert.assertNotNull(s.veloALaBorne(1));
    }

    @Test
    public void emprunterVeloRegistreNull() {
        s.setRegistre(r);
        Assert.assertEquals(-4, s.arrimerVelo(v,1));
        s.setRegistre(null);
        Assert.assertNull(s.emprunterVelo(a, 1));
    }

    @Test
    public void emprunterVeloAbonneNull() {
        s.setRegistre(r);
        a = null;
        Assert.assertNull(s.emprunterVelo(a, 1));
    }

    @Test
    public void emprunterVeloBornevide() {
        s.setRegistre(r);
        Assert.assertNull(s.emprunterVelo(a, 1));
    }

    @Test
    public void emprunterVeloBorneTropPetite() {
        s.setRegistre(r);
        Assert.assertNull(s.emprunterVelo(a, 0));
    }

    @Test
    public void emprunterVeloBorneTropGrande() {
        s.setRegistre(r);
        Assert.assertNull(s.emprunterVelo(a, 11));
    }

    @Test
    public void emprunterVeloAbonneBloque() throws IncorrectNameException {
        s.setRegistre(r);
        Abonne b = new Abonne("bob");
        Assert.assertEquals(-4, s.arrimerVelo(v,1));
        Assert.assertNull(s.emprunterVelo(b, 1));
    }


    @Test
    public void emprunterVeloDejaEmprunter() throws IncorrectNameException {
        s.setRegistre(r);
        initVeloArrimerEmprunter(v, 1);
        Abonne b = new Abonne("bob", "11111-11111-11111111111-48");
        Assert.assertNull(s.emprunterVelo(b, 1));
    }

    @Test
    public void emprunterVeloCorrect() {
        s.setRegistre(r);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertNotNull(s.veloALaBorne(1));

        Assert.assertNotNull(s.emprunterVelo(a, 1));
    }

    @Test
    public void emprunterVeloAbonneDejaEmprunt() {
        s.setRegistre(r);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertEquals(-4, s.arrimerVelo(new Velo(), 2));
        Assert.assertNotNull(s.emprunterVelo(a, 1));
        Assert.assertEquals(1, r.nbEmpruntsEnCours(a));
        Assert.assertNull(s.emprunterVelo(a, 2));
        Assert.assertEquals(1, r.nbEmpruntsEnCours(a));
    }

    @Test
    public void avance() {
        s = Mockito.spy(s);
        s.setRegistre(r);
        long dateEmprunt = s.maintenant();
        initVeloArrimerEmprunter(v,1 );
        v.parcourir(42.0);
        Assert.assertEquals(500.0 - 42.0, v.prochaineRevision(), 0.001);
        Mockito.when(s.maintenant()).thenReturn(System.currentTimeMillis() + 2 * 10 * 60 * 1000);
        Assert.assertEquals(0, s.arrimerVelo(v, 1));

        Assert.assertEquals(0.666 , r.facturation(a, dateEmprunt, s.maintenant()), 0.001);

        v.parcourir(42.0);
        Assert.assertEquals(500.0 - 42.0, v.prochaineRevision(), 0.001);
    }

    @Test
    public void testParis() {
        Station station = new Station("Gare Viotte", 48.8566,2.3522,1); // 0,0
        Assert.assertEquals(326.268, station.distance(s), 0.5);
    }

    @Test
    public void equilibrer() {
        for(int j = 0; j < 200000; ++j) {
            int capaciteStation = (int) (Math.random() * 20);

            s = new Station("Gare viotte", 47.246501551427329, 6.022715427111734, capaciteStation);
            s.setRegistre(r);

            Set<IVelo> velos = new HashSet<>();

            int nombreVeloStation = (int) (Math.random() * (capaciteStation));
            int nbVeloBonEtat = 0;
            int nbVeloAbime = 0;
            int nbVeloReviser = 0;
            int nbVeloAbimeReviser = 0;

            for (int i = 0; i < nombreVeloStation; ++i) {
                IVelo v = new Velo();
                int choix = (int) (Math.random() * 4);

                switch (choix) {
                    case 0: // velo sain
                        ++nbVeloBonEtat;
                        break;
                    case 1: // velo abime
                        v.abimer();
                        ++nbVeloAbime;
                        break;
                    case 2: // velo a reviser
                        v.parcourir(1000.0);
                        ++nbVeloReviser;
                        break;
                    case 3: // velo abime et a reviser
                        v.abimer();
                        v.parcourir(1000.0);
                        ++nbVeloAbimeReviser;
                        break;
                }
                Assert.assertEquals(-4, s.arrimerVelo(v, i+1));
                Assert.assertNotNull(s.veloALaBorne(i+1));
            }

            int nombreVeloCamion = (int) (Math.random() * (30));
            int nbVeloBonEtatCamion = 0;
            int nbVeloAbimeCamion = 0;
            int nbVeloReviserCamion = 0;
            int nbVeloAbimeReviserCamion = 0;

            for (int i = 0; i < nombreVeloCamion; ++i) {
                IVelo v = new Velo();
                velos.add(v);
                int choix = (int) (Math.random() * 4);
                switch (choix) {
                    case 0: // velo sain
                        ++nbVeloBonEtatCamion;
                        break;
                    case 1: // velo abime
                        v.abimer();
                        ++nbVeloAbimeCamion;
                        break;
                    case 2: // velo a reviser
                        v.parcourir(1000.0);
                        ++nbVeloReviserCamion;
                        break;
                    case 3: // velo abime et a reviser
                        v.abimer();
                        v.parcourir(1000.0);
                        ++nbVeloAbimeReviserCamion;
                        break;
                }
            }
            /*
            System.out.println("\t<<<<<<<<AVANT EQUILIBRAGE>>>>>>>>>");
            System.out.println("\t\t\t\t\t" + "STATION");
            System.out.println("---------------------------------------------");
            System.out.println("|\t\t\t " + "Capacite station: " + capaciteStation + "\t\t\t|");
            System.out.println("|\t\t" + "Nombre totale de velo : " + nombreVeloStation + "\t\t\t|");
            System.out.println("|\t\t" + "Nombre velo bon etat : " + nbVeloBonEtat + "\t\t\t|");
            System.out.println("|\t\t" + "Nombre velo abime : " + nbVeloAbime + "\t\t\t\t|");
            System.out.println("|\t\t" + "Nombre velo a reviser : " + nbVeloReviser + "\t\t\t|");
            System.out.println("|\t\t" + "Nombre velo abime/reviser : " + nbVeloAbimeReviser + "\t\t|");
            System.out.println("---------------------------------------------");
            System.out.println("\t\t\t\t\t" + "CAMION");
            System.out.println("---------------------------------------------");
            System.out.println("|\t\t" + "Nombre totale de velo : " + nombreVeloCamion + "\t\t\t|");
            System.out.println("|\t\t" + "Nombre velo bon etat : " + nbVeloBonEtatCamion + "\t\t\t|");
            System.out.println("|\t\t" + "Nombre velo abime : " + nbVeloAbimeCamion + "\t\t\t\t|");
            System.out.println("|\t\t" + "Nombre velo a reviser : " + nbVeloReviserCamion + "\t\t\t|");
            System.out.println("|\t\t" + "Nombre velo abime/reviser : " + nbVeloAbimeReviserCamion + "\t\t|");
            System.out.println("---------------------------------------------");
            */

            if (nbVeloAbime + nbVeloAbimeReviser < nbVeloBonEtatCamion) {
                nbVeloBonEtatCamion -= (nbVeloAbime + nbVeloAbimeReviser);
                nbVeloBonEtat += nbVeloAbime + nbVeloAbimeReviser;
            } else {
                nbVeloBonEtat += nbVeloBonEtatCamion;
                nbVeloBonEtatCamion = 0;
            }

            nbVeloAbimeCamion += nbVeloAbime;
            nbVeloAbimeReviserCamion += nbVeloAbimeReviser;
            nbVeloAbime = nbVeloAbimeReviser = 0;

            if (nbVeloReviser > 0 && nbVeloBonEtatCamion>0) {
                if (nbVeloBonEtatCamion >= nbVeloReviser) { //si reste velo bon etat camion
                    nbVeloReviserCamion += nbVeloReviser;
                    nbVeloBonEtatCamion -= nbVeloReviser;
                    nbVeloBonEtat += nbVeloReviser;
                    nbVeloReviser = 0;
                } else {
                    nbVeloReviserCamion += nbVeloBonEtatCamion;
                    nbVeloReviser -= nbVeloBonEtatCamion;
                    nbVeloBonEtat += nbVeloBonEtatCamion;
                    nbVeloBonEtatCamion = 0;
                }
            }

            int size = nbVeloBonEtat + nbVeloReviser + nbVeloAbimeReviser + nbVeloAbime;
            //pas assez
            if ((size) < Math.ceil(s.capacite() / 2.0)) {
                int nbVeloManquant = (int) (Math.ceil(s.capacite() / 2.0) - (size));
                if (nbVeloBonEtatCamion > nbVeloManquant) {
                    nbVeloBonEtat += nbVeloManquant;
                    nbVeloBonEtatCamion -= nbVeloManquant;
                } else {
                    nbVeloBonEtat += nbVeloBonEtatCamion;
                    nbVeloBonEtatCamion = 0;
                }
            }

            size = nbVeloBonEtat + nbVeloReviser + nbVeloAbimeReviser + nbVeloAbime;
            //en trop
            if ((size) > Math.ceil(s.capacite() / 2.0)) {
                int nbVeloEntrop =  (size) - (int)(Math.ceil(s.capacite() / 2.0));
                if(nbVeloReviser > nbVeloEntrop) {
                    nbVeloReviser -= nbVeloEntrop;
                    nbVeloReviserCamion += nbVeloEntrop;
                }else {
                    nbVeloBonEtatCamion += nbVeloEntrop-nbVeloReviser;
                    nbVeloReviserCamion += nbVeloReviser;
                    nbVeloBonEtat -= (nbVeloEntrop-nbVeloReviser);
                    nbVeloReviser=0;
                }
            }

            s.equilibrer(velos);

            int nbVeloBonEtatEquilibrageCamion = 0;
            int nbVeloAbimeEquilibrageCamion = 0;
            int nbVeloReviserEquilibrageCamion = 0;
            int nbVeloAbimeReviserEquilibrageCamion = 0;
            for (IVelo v: velos) {
                if(v.estAbime() && v.prochaineRevision() > 0) {
                    nbVeloAbimeEquilibrageCamion++;
                }else if(v.estAbime() && v.prochaineRevision() <=0) {
                    ++nbVeloAbimeReviserEquilibrageCamion;
                }else if(!v.estAbime() && v.prochaineRevision() <= 0){
                    ++nbVeloReviserEquilibrageCamion;
                }else{
                    ++nbVeloBonEtatEquilibrageCamion;
                }
            }

            int nbVeloBonEtatEquilibrage = 0;
            int nbVeloAbimeEquilibrage = 0;
            int nbVeloReviserEquilibrage = 0;
            int nbVeloAbimeReviserEquilibrage = 0;
            for (int i = 0; i < s.capacite(); ++i) {
                IVelo v = s.veloALaBorne(i+1);
                if(v == null) continue;
                if(v.estAbime() && v.prochaineRevision() > 0) {
                    nbVeloAbimeEquilibrage++;
                }else if(v.estAbime() && v.prochaineRevision() <=0) {
                    ++nbVeloAbimeReviserEquilibrage;
                }else if(!v.estAbime() && v.prochaineRevision() > 0){
                    ++nbVeloBonEtatEquilibrage;
                }else {
                    ++nbVeloReviserEquilibrage;
                }
            }

            int size_set_rechange_attendue = nbVeloBonEtatCamion + nbVeloAbimeCamion + nbVeloReviserCamion + nbVeloAbimeReviserCamion;
            int size_borne_pleine_attendue = nbVeloBonEtat + nbVeloAbime + nbVeloReviser + nbVeloAbimeReviser;
            int size_borne_vide_attendue = capaciteStation - size_borne_pleine_attendue;
            /*
            System.out.println("\t<<<<<<<<<<<<EQUILIBRAGE>>>>>>>>>>>>");
            System.out.println("\t\t\t\t\t" + "STATION");
            System.out.println("---------------------------------------------");
            System.out.println("|\t\t\t " + "Capacite station: " + capaciteStation + "\t\t\t|");
            System.out.println("|\t\t" + "Nombre totale de velo : " + (capaciteStation -s.nbBornesLibres()) + " (" + (size_borne_pleine_attendue) + ")" + "\t\t|");
            System.out.println("|\t\t" + "Nombre velo bon etat : " + nbVeloBonEtatEquilibrage + " (" + nbVeloBonEtat + ")" + "\t\t|");
            System.out.println("|\t\t" + "Nombre velo abime : " + nbVeloAbimeEquilibrage + " (" + nbVeloAbime + ")" + "\t\t\t|");
            System.out.println("|\t\t" + "Nombre velo a reviser : " + nbVeloReviserEquilibrage + " (" + nbVeloReviser + ")" + "\t\t|");
            System.out.println("|\t\t" + "Nombre velo abime/reviser : " + nbVeloAbimeReviserEquilibrage + " (" + nbVeloAbimeReviser + ")" + "\t|");
            System.out.println("---------------------------------------------");
            System.out.println("\t\t\t\t\t" + "CAMION");
            System.out.println("---------------------------------------------");
            System.out.println("|\t\t" + "Nombre totale de velo : " + velos.size() + " (" + size_set_rechange_attendue + ")" + "\t\t|");
            System.out.println("|\t\t" + "Nombre velo bon etat : " + nbVeloBonEtatEquilibrageCamion + " (" + nbVeloBonEtatCamion + ")" + "\t\t|");
            System.out.println("|\t\t" + "Nombre velo abime : " + nbVeloAbimeEquilibrageCamion + " (" + (nbVeloAbimeCamion) + ")" + "\t\t\t|");
            System.out.println("|\t\t" + "Nombre velo a reviser : " + nbVeloReviserEquilibrageCamion + " (" +  nbVeloReviserCamion + ")" + "\t\t|");
            System.out.println("|\t\t" + "Nombre velo abime/reviser : " + nbVeloAbimeReviserEquilibrageCamion + " (" +  (nbVeloAbimeReviserCamion) + ")" + "\t|");
            System.out.println("---------------------------------------------");
            */
            //verifie nombre de velos a pas changer
            Assert.assertEquals(nombreVeloStation + nombreVeloCamion,s.capacite() - s.nbBornesLibres() + velos.size());
            //verifie de la taille du set de rechange
            Assert.assertEquals(size_set_rechange_attendue, velos.size());
            //verfie le nombre de borne pleine
            Assert.assertEquals(size_borne_pleine_attendue, s.capacite()-s.nbBornesLibres());
            //verfie le nombre de borne vide
            Assert.assertEquals(size_borne_vide_attendue, s.nbBornesLibres());
            //verfie la config set de velo
            Assert.assertEquals(nbVeloAbimeEquilibrageCamion, nbVeloAbimeCamion);
            Assert.assertEquals(nbVeloAbimeReviserEquilibrageCamion, nbVeloAbimeReviserCamion);
            Assert.assertEquals(nbVeloReviserEquilibrageCamion, nbVeloReviserCamion);
            Assert.assertEquals(nbVeloBonEtatEquilibrageCamion, nbVeloBonEtatCamion);
            //verif la config des bornes
            Assert.assertEquals(0, nbVeloAbime);
            Assert.assertEquals(0, nbVeloAbimeReviser);
            Assert.assertEquals(nbVeloReviserEquilibrage, nbVeloReviser);
            Assert.assertEquals(nbVeloBonEtatEquilibrage, nbVeloBonEtat);
        }
    }

    @Test
    public void CamionNull() {
        s.setRegistre(r);
        IVelo v = new Velo();
        v.abimer();
        s.arrimerVelo(v, 1);
        s.arrimerVelo(new Velo(), 3);
        s.arrimerVelo(new Velo(), 5);
        s.equilibrer(null);
        Assert.assertEquals(3, s.capacite()- s.nbBornesLibres());
    }

    @Test
    public void CamionVeloNull() {
        s.setRegistre(r);
        IVelo v = new Velo();
        v.abimer();
        s.arrimerVelo(v, 1);
        s.arrimerVelo(new Velo(), 3);
        s.arrimerVelo(new Velo(), 5);
        Set<IVelo> camions = new HashSet<IVelo>();
        camions.add(new Velo());
        camions.add(null);
        camions.add(new Velo());
        s.equilibrer(camions);
        Assert.assertEquals(4, s.capacite()- s.nbBornesLibres());
    }
}

