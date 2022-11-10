package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StationWithMocksTest {

    private Abonne mockAbonne = null;
    private IVelo mockBike = null;
    private IRegistre mockRegister = null;
    private Station s = null;

    @Before
    public void before() {
        mockAbonne = Mockito.mock(Abonne.class);
        mockBike = Mockito.mock(IVelo.class);
        mockRegister = Mockito.mock(IRegistre.class);
        s = new Station("Gare viotte", 47.246501551427329, 6.022715427111734, 10);
    }

    @Test
    public void constructeurTestLatitudeStationIncorrect() {
        Station station = new Station("toto", -90.1,0,1); // 0,0
        Assert.assertEquals("toto", station.getNom());
        Assert.assertEquals(1, station.capacite());
        //Assert.assertEquals(5286.005745244741, station.distance(s), 0.0000001);
    }

    @Test
    public void constructeurTestLongitudeStationIncorrect() {
        Station station = new Station("toto", 0,-90.1,1); // 0,0
        Assert.assertEquals("toto", station.getNom());
        Assert.assertEquals(1, station.capacite());
        //Assert.assertEquals(5286.005745244741, s.distance(station), 0.0000001);
    }

    @Test
    public void constructeurTestLatitudeLongitudeStationTropPetite() {
        Station station = new Station("toto", -90.1,-90.1,1); // 0,0
        Assert.assertEquals("toto", station.getNom());
        Assert.assertEquals(1, station.capacite());
        //Assert.assertEquals(5286.005745244741, station.distance(s), 0.0000001);
    }

    @Test
    public void constructeurTestLatitudeLongitudeStationTropGrande() {
        Station station = new Station("toto", 90.1,90.1,1); // 0,0
        Assert.assertEquals("toto", station.getNom());
        Assert.assertEquals(1, station.capacite());
        //Assert.assertEquals(5286.005745244741, station.distance(s), 0.0000001);
    }

    @Test
    public void constructeurTestCapaciteNegative() {
        Station station = new Station("toto", 0,0,-1);
        Assert.assertEquals("toto", station.getNom());
        Assert.assertEquals(0, station.capacite());
    }

    @Test
    public void constructeurTest() {
        Station station = new Station("toto", -50.05414157294687,87.456224784491237,5);
        Assert.assertEquals("toto", station.getNom());
        Assert.assertEquals(5, station.capacite());
        Assert.assertEquals(13328.78, s.distance(station), 0.5);
    }

    @Test
    public void arrimerVeloNull() {
        mockBike = null;
        Assert.assertEquals(-1, s.arrimerVelo(mockBike, 1));
        Assert.assertNull(s.veloALaBorne(1));
    }

    @Test
    public void arrimerVeloBorneTropPetite() {
        Assert.assertEquals(-1, s.arrimerVelo(mockBike, 0));
        Assert.assertNull(s.veloALaBorne(1));
    }

    @Test
    public void arrimerVeloBorneTropGrande() {
        Assert.assertEquals(-1, s.arrimerVelo(mockBike, 11));
        Assert.assertNull(s.veloALaBorne(1));
    }

    @Test
    public void arrimerVeloRegistreNull() {
        Assert.assertEquals(-2, s.arrimerVelo(mockBike, 1));
        Assert.assertNull(s.veloALaBorne(1));
    }

    @Test
    public void arrimerVeloSurBornePrise() {
        s.setRegistre(mockRegister);
        Assert.assertEquals(0, s.arrimerVelo(mockBike, 1));
        IVelo mockBike2 = Mockito.mock(IVelo.class);
        Assert.assertEquals(-2, s.arrimerVelo(mockBike2, 1));
        Assert.assertNotNull(s.veloALaBorne(1));
    }

    @Test
    public void arrimerDeuxFoisVelo() {
        s.setRegistre(mockRegister);
        Mockito.when(mockBike.arrimer()).thenReturn(0);
        Assert.assertEquals(0, s.arrimerVelo(mockBike, 1));
        Mockito.when(mockBike.arrimer()).thenReturn(-1);
        Assert.assertEquals(-3, s.arrimerVelo(mockBike, 2));
        Assert.assertNotNull(s.veloALaBorne(1));
        Assert.assertNull(s.veloALaBorne(2));
    }

    @Test
    public void arimerVeloPasEmprunter() {
        s.setRegistre(mockRegister);
        Mockito.when(mockBike.arrimer()).thenReturn(0);
        Mockito.when(mockRegister.retourner(Mockito.any(IVelo.class), anyLong())).thenReturn(-2);
        Assert.assertEquals(-4, s.arrimerVelo(mockBike, 1));
        Assert.assertNotNull(s.veloALaBorne(1));
    }

    @Test
    public void arimerVeloMauvaiseDateRetour() {
        s.setRegistre(mockRegister);
        Mockito.when(mockBike.arrimer()).thenReturn(0);
        Mockito.when(mockRegister.retourner(Mockito.any(IVelo.class), anyLong())).thenReturn(-3);
        Assert.assertEquals(-4, s.arrimerVelo(mockBike, 1));
        Assert.assertNotNull(s.veloALaBorne(1));
    }

    @Test
    public void arrimerVeloCorrect() {
        IVelo mockBike = Mockito.mock(IVelo.class);
        s.setRegistre(mockRegister);
        Mockito.when(mockBike.arrimer()).thenReturn(0);
        Mockito.when(mockRegister.retourner(Mockito.any(IVelo.class), anyLong())).thenReturn(0);
        Assert.assertEquals(0, s.arrimerVelo(mockBike, 1));
        Assert.assertNotNull(s.veloALaBorne(1));
    }

    @Test
    public void emprunterVeloRegistreNull() {
        Assert.assertNull(s.emprunterVelo(mockAbonne, 1));
    }

    @Test
    public void emprunterVeloAbonneNull() {
        s.setRegistre(mockRegister);
        mockAbonne = null;
        Assert.assertNull(s.emprunterVelo(mockAbonne, 1));
    }

    @Test
    public void emprunterVeloBornevide() {
        s.setRegistre(mockRegister);
        Assert.assertNull(s.emprunterVelo(mockAbonne, 1));
    }

    @Test
    public void emprunterVeloBorneTropPetite() {
        s.setRegistre(mockRegister);
        Assert.assertNull(s.emprunterVelo(mockAbonne, 0));
    }

    @Test
    public void emprunterVeloBorneTropGrande() {
        s.setRegistre(mockRegister);
        Assert.assertNull(s.emprunterVelo(mockAbonne, 11));
    }

    @Test
    public void emprunterVeloAbonneBloque() {
        s.setRegistre(mockRegister);
        Assert.assertEquals(0, s.arrimerVelo(mockBike, 1));
        Assert.assertNotNull(s.veloALaBorne(1));
        Mockito.when(mockAbonne.estBloque()).thenReturn(true);
        Assert.assertNull(s.emprunterVelo(mockAbonne, 1));
    }

    @Test
    public void emprunterVeloDejaEmprunter() {
        s.setRegistre(mockRegister);
        Assert.assertEquals(0, s.arrimerVelo(mockBike, 1));
        Assert.assertNotNull(s.veloALaBorne(1));
        Mockito.when(mockAbonne.estBloque()).thenReturn(false);
        Mockito.when(mockRegister.emprunter(Mockito.any(Abonne.class),Mockito.any(IVelo.class), anyLong())).thenReturn(-2);
        Assert.assertNull(s.emprunterVelo(mockAbonne, 1));
    }

    @Test
    public void emprunterVeloCorrect() {
        s.setRegistre(mockRegister);
        Assert.assertEquals(0, s.arrimerVelo(mockBike, 1));
        Assert.assertNotNull(s.veloALaBorne(1));
        Mockito.when(mockAbonne.estBloque()).thenReturn(false);
        Mockito.when(mockRegister.nbEmpruntsEnCours(Mockito.any(Abonne.class))).thenReturn(0);
        Mockito.when(mockRegister.emprunter(Mockito.any(Abonne.class),Mockito.any(IVelo.class), anyLong())).thenReturn(0);
        Assert.assertNotNull(s.emprunterVelo(mockAbonne, 1));
    }

    @Test
    public void emprunterVeloAbonneDejaEmprunt() {
        s.setRegistre(mockRegister);
        Assert.assertEquals(0, s.arrimerVelo(mockBike, 1));
        Assert.assertNotNull(s.veloALaBorne(1));
        Mockito.when(mockAbonne.estBloque()).thenReturn(false);
        Mockito.when(mockRegister.nbEmpruntsEnCours(Mockito.any(Abonne.class))).thenReturn(1);
        Mockito.when(mockRegister.emprunter(Mockito.any(Abonne.class),Mockito.any(IVelo.class), anyLong())).thenReturn(0);
        Assert.assertNull(s.emprunterVelo(mockAbonne, 1));
    }

    public IVelo createMockVelosArrimer(int b) {
        IVelo v = Mockito.mock(IVelo.class);
        Mockito.when(v.arrimer()).thenReturn(0);
        Assert.assertEquals(0, s.arrimerVelo(v, b));
        Assert.assertNotNull(s.veloALaBorne(b));
        return v;
    }

    @Test
    public void equilibrer() {
        for(int j = 0; j < 2500; ++j) {
            int capaciteStation = (int) (Math.random() * 20);

            s = new Station("Gare viotte", 47.246501551427329, 6.022715427111734, capaciteStation);
            s.setRegistre(mockRegister);
            Mockito.when(mockRegister.retourner(Mockito.any(IVelo.class), anyLong())).thenReturn(0);

            Set<IVelo> velos = new HashSet<>();

            int nombreVeloStation = (int) (Math.random() * (capaciteStation));
            int nbVeloBonEtat = 0;
            int nbVeloAbime = 0;
            int nbVeloReviser = 0;
            int nbVeloAbimeReviser = 0;

            for (int i = 0; i < nombreVeloStation; ++i) {
                IVelo v = createMockVelosArrimer(i + 1);
                int choix = (int) (Math.random() * 4);

                switch (choix) {
                    case 0: // velo sain
                        Mockito.when(v.estAbime()).thenReturn(false);
                        Mockito.when(v.prochaineRevision()).thenReturn(200.0);
                        ++nbVeloBonEtat;
                        break;
                    case 1: // velo abime
                        Mockito.when(v.estAbime()).thenReturn(true);
                        Mockito.when(v.prochaineRevision()).thenReturn(200.0);
                        ++nbVeloAbime;
                        break;
                    case 2: // velo a reviser
                        Mockito.when(v.estAbime()).thenReturn(false);
                        Mockito.when(v.prochaineRevision()).thenReturn(0.0);
                        ++nbVeloReviser;
                        break;
                    case 3: // velo abime et a reviser
                        Mockito.when(v.estAbime()).thenReturn(true);
                        Mockito.when(v.prochaineRevision()).thenReturn(0.0);
                        ++nbVeloAbimeReviser;
                        break;
                }
            }

            int nombreVeloCamion = (int) (Math.random() * (30));
            int nbVeloBonEtatCamion = 0;
            int nbVeloAbimeCamion = 0;
            int nbVeloReviserCamion = 0;
            int nbVeloAbimeReviserCamion = 0;

            for (int i = 0; i < nombreVeloCamion; ++i) {
                IVelo v = Mockito.mock(IVelo.class);
                velos.add(v);
                int choix = (int) (Math.random() * 4);
                switch (choix) {
                    case 0: // velo sain
                        Mockito.when(v.estAbime()).thenReturn(false);
                        Mockito.when(v.prochaineRevision()).thenReturn(200.0);
                        ++nbVeloBonEtatCamion;
                        break;
                    case 1: // velo abime
                        Mockito.when(v.estAbime()).thenReturn(true);
                        Mockito.when(v.prochaineRevision()).thenReturn(200.0);
                        ++nbVeloAbimeCamion;
                        break;
                    case 2: // velo a reviser
                        Mockito.when(v.estAbime()).thenReturn(false);
                        Mockito.when(v.prochaineRevision()).thenReturn(0.0);
                        ++nbVeloReviserCamion;
                        break;
                    case 3: // velo abime et a reviser
                        Mockito.when(v.estAbime()).thenReturn(true);
                        Mockito.when(v.prochaineRevision()).thenReturn(0.0);
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
            System.out.println("|\t\t" + "Nombre totale de velo : " + (capaciteStation -s.nbBornesLibres()) + " (" + (size_borne_vide_attendue+size_borne_pleine_attendue) + ")" + "\t\t|");
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
    public void testParis() {
        Station station = new Station("Gare Viotte", 48.8566,2.3522,1); // 0,0
        Assert.assertEquals(326.268, station.distance(s), 0.5);
    }

    /*léo
    private Set<IVelo> createCamion(int nbVeloBien, int nbVeloAbiber, int nbVeloAreviser,int nbVeloAbimerEtAreviser){
        Set<IVelo> camion = new HashSet<>();

        for (int i = 0; i < nbVeloBien; i++) {
            IVelo mockVelo1 = Mockito.mock(IVelo.class);
            Mockito.when(mockVelo1.estAbime()).thenReturn(false);
            Mockito.when(mockVelo1.prochaineRevision()).thenReturn(500.0);
            camion.add(mockVelo1);
        }

        for (int i = 0; i < nbVeloAbimerEtAreviser; i++) {
            IVelo mockVelo1 = Mockito.mock(IVelo.class);
            Mockito.when(mockVelo1.estAbime()).thenReturn(true);
            Mockito.when(mockVelo1.prochaineRevision()).thenReturn(-500.0);
            camion.add(mockVelo1);
        }

        for (int i = 0; i < nbVeloAreviser; i++) {
            IVelo mockVelo1 = Mockito.mock(IVelo.class);
            Mockito.when(mockVelo1.prochaineRevision()).thenReturn(-500.0);
            Mockito.when(mockVelo1.estAbime()).thenReturn(false);
            camion.add(mockVelo1);
        }

        for (int i = 0; i < nbVeloAbiber; i++) {
            IVelo mockVelo1 = Mockito.mock(IVelo.class);
            Mockito.when(mockVelo1.estAbime()).thenReturn(true);
            Mockito.when(mockVelo1.prochaineRevision()).thenReturn(500.0);
            camion.add(mockVelo1);
        }


        return camion;
    }

    private void createStation(Station s,int nbVeloBien, int nbVeloAbiber, int nbVeloAreviser,int nbVeloAbimerEtAreviser){
        int index = 1;

        for (int i = 0; i < nbVeloBien; i++) {
            IVelo mockVelo1 = Mockito.mock(IVelo.class);
            Mockito.when(mockVelo1.estAbime()).thenReturn(false);
            Mockito.when(mockVelo1.prochaineRevision()).thenReturn(500.0);
            s.arrimerVelo(mockVelo1,index);
            index++;
        }

        for (int i = 0; i < nbVeloAbimerEtAreviser; i++) {
            IVelo mockVelo1 = Mockito.mock(IVelo.class);
            Mockito.when(mockVelo1.estAbime()).thenReturn(true);
            Mockito.when(mockVelo1.prochaineRevision()).thenReturn(-500.0);
            s.arrimerVelo(mockVelo1,index);
            index++;
        }

        for (int i = 0; i < nbVeloAreviser; i++) {
            IVelo mockVelo1 = Mockito.mock(IVelo.class);
            Mockito.when(mockVelo1.prochaineRevision()).thenReturn(-500.0);
            Mockito.when(mockVelo1.estAbime()).thenReturn(false);
            Assert.assertEquals(0, s.arrimerVelo(mockVelo1,index));
            index++;
        }

        for (int i = 0; i < nbVeloAbiber; i++) {
            IVelo mockVelo1 = Mockito.mock(IVelo.class);
            Mockito.when(mockVelo1.estAbime()).thenReturn(true);
            Mockito.when(mockVelo1.prochaineRevision()).thenReturn(500.0);
            s.arrimerVelo(mockVelo1,index);
            index++;
        }
    }

    @Test
    public void testCamionVide() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,9);
        s.setRegistre(mockRegister);
        Mockito.when(mockBike.estAbime()).thenReturn(false);

        createStation(s,2,4,2,0);

        Set<IVelo> velos = createCamion(5,3,1,1);

        int nbVeloAbiberStation=0;
        int nbVeloAreviserStation=0;
        int nbVeloAbimerEtAreviserStation=0;
        int nbVeloBienStation=0;

        for (int i = 0 ; i < s.capacite(); ++i) {
            if(s.veloALaBorne(i) != null) {

                if(s.veloALaBorne(i).estAbime() && s.veloALaBorne(i).prochaineRevision() <= 0){
                    nbVeloAbimerEtAreviserStation++;
                }else if (s.veloALaBorne(i).estAbime()) {
                    nbVeloAbiberStation++;
                }else if (s.veloALaBorne(i).prochaineRevision() <= 0) {
                    nbVeloAreviserStation++;
                }else {
                    nbVeloBienStation++;
                }
            }
        }

        int nbVeloAbiberCamion=0;
        int nbVeloAreviserCamion=0;
        int nbVeloAbimerEtAreviserCamion=0;
        int nbVeloBienCamion=0;

        for (IVelo vv: velos) {
            if(vv.estAbime() && vv.prochaineRevision() <= 0){
                nbVeloAbimerEtAreviserCamion++;
                continue;
            }
            if (vv.estAbime()) {
                nbVeloAbiberCamion++;
                continue;
            }
            if (vv.prochaineRevision() <= 0) {
                nbVeloAreviserCamion++;
                continue;
            }
            nbVeloBienCamion++;
        }


        System.out.println("nombre total de velo dans la station :"+(s.capacite()-s.nbBornesLibres()));
        System.out.println("nombre velo bien      :"+nbVeloBienStation);
        System.out.println("nombre velo abimer    :"+nbVeloAbiberStation);
        System.out.println("nombre velo à réviser :"+nbVeloAreviserStation);
        System.out.println("nombre velo abimer àre:"+nbVeloAbimerEtAreviserStation);

        System.out.println("------------------------");

        System.out.println("nombre total de velo dans le camion  :"+velos.size());
        System.out.println("nombre velo bien      :"+nbVeloBienCamion);
        System.out.println("nombre velo abimer    :"+nbVeloAbiberCamion);
        System.out.println("nombre velo à réviser :"+nbVeloAreviserCamion);
        System.out.println("nombre velo abimer àre:"+nbVeloAbimerEtAreviserCamion);

        s.equilibrer(velos);
        System.out.println("------------------------");
        System.out.println("------------------------");
        nbVeloAbiberStation=0;
        nbVeloAreviserStation=0;
        nbVeloAbimerEtAreviserStation=0;
        nbVeloBienStation=0;

        for (int i = 0 ; i < s.capacite(); ++i) {
            if(s.veloALaBorne(i) != null) {

                if(s.veloALaBorne(i).estAbime() && s.veloALaBorne(i).prochaineRevision() <= 0){
                    nbVeloAbimerEtAreviserStation++;
                    continue;
                }
                if (s.veloALaBorne(i).estAbime()) {
                    nbVeloAbiberStation++;
                    continue;
                }
                if (s.veloALaBorne(i).prochaineRevision() <= 0) {
                    nbVeloAreviserStation++;
                    continue;
                }
                nbVeloBienStation++;
            }
        }

        nbVeloAbiberCamion=0;
        nbVeloAreviserCamion=0;
        nbVeloAbimerEtAreviserCamion=0;
        nbVeloBienCamion=0;

        for (IVelo vv: velos) {
            if(vv.estAbime() && vv.prochaineRevision() <= 0){
                nbVeloAbimerEtAreviserCamion++;
                continue;
            }
            if (vv.estAbime()) {
                nbVeloAbiberCamion++;
                continue;
            }
            if (vv.prochaineRevision() <= 0) {
                nbVeloAreviserCamion++;
                continue;
            }
            nbVeloBienCamion++;
        }


        System.out.println("nombre total de velo dans la station :"+(s.capacite()-s.nbBornesLibres()));
        System.out.println("nombre velo bien      :"+nbVeloBienStation);
        System.out.println("nombre velo abimer    :"+nbVeloAbiberStation);
        System.out.println("nombre velo à réviser :"+nbVeloAreviserStation);
        System.out.println("nombre velo abimer àre:"+nbVeloAbimerEtAreviserStation);

        System.out.println("------------------------");

        System.out.println("nombre total de velo dans le camion  :"+velos.size());
        System.out.println("nombre velo bien      :"+nbVeloBienCamion);
        System.out.println("nombre velo abimer    :"+nbVeloAbiberCamion);
        System.out.println("nombre velo à réviser :"+nbVeloAreviserCamion);
        System.out.println("nombre velo abimer àre:"+nbVeloAbimerEtAreviserCamion);

        Assert.assertFalse(false);
    }*/
}
