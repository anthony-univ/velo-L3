package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Test unitaire pour la FabriqueVeloTest.
 */
public class LVStationIntegrationTest {

    private IVelo v;
    private IRegistre r;
    private Abonne a;

    @Before
    public void init() throws IncorrectNameException {
        r = new JRegistre();
        v = new Velo();
        a = new Abonne("zergzegzegf","00000-00000-00000000000-97");
    }

    @Test
    public void testArrimerVeloValid() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));

        System.out.println(s.capacite());
        System.out.println(s.nbBornesLibres());
        System.out.println(s.veloALaBorne(1));
        IVelo vv = s.emprunterVelo(a,1);

    }

    @Test
    public void testDecrocherNonValid0() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(-1, s.arrimerVelo(v, 0));

    }

    @Test
    public void testArrimerVeloNull() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(-1, s.arrimerVelo(null, 0));

    }

    @Test
    public void testArrimerBornesTooMany() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(-1, s.arrimerVelo(v, 11));

    }

    @Test
    public void testArrimerRegistreNull() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        Assert.assertEquals(-1, s.arrimerVelo(v, -2));
    }

    @Test
    public void testArrimerVeloBorneNull() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        IVelo vBis = new Velo();
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertEquals(-2, s.arrimerVelo(v, 1));
    }

    @Test
    public void testArrimerVeloBorneNullRegisterNull() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        IVelo vBis = new Velo();
        Assert.assertEquals(-2, s.arrimerVelo(v, 1));
        Assert.assertEquals(-2, s.arrimerVelo(v, 1));
    }

    @Test
    public void testArrimerDejaArrimer() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        v.arrimer();
        Assert.assertEquals(-3, s.arrimerVelo(v, 1));
    }

    @Test
    public void testArrimerRetournerNotWork() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        r.retourner(v,1);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
    }


    @Test
    public void testGetNom() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        Assert.assertEquals("Gare Viotte", s.getNom());
    }

    @Test
    public void testCapacitee() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        Assert.assertEquals(10, s.capacite());
    }

    @Test
    public void testVeloAlaBorneMinus() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(null,s.veloALaBorne(-10));

    }

    @Test
    public void testVeloAlaBorneMaaxus() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(null,s.veloALaBorne(6548));

    }

    @Test
    public void testNombreBorneLibres() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(-4,s.arrimerVelo(v,1));
        Assert.assertEquals(9,s.nbBornesLibres());

    }

    @Test
    public void testDistance() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        Station se = new Station("Isembart",47.243093093172654,6.025615409841949,8);
        Assert.assertEquals(0.5196,s.distance(se),0.0001);
    }


    @Test
    public void testEmprunterValid() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertEquals(v, s.emprunterVelo(a, 1));

    }

    @Test
    public void testEmprunterRegisterNull() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        s.setRegistre(null);
        Assert.assertEquals(null, s.emprunterVelo(a, 0));

    }

    @Test
    public void testEmprunterAbonneNull() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertEquals(null, s.emprunterVelo(null, 0));

    }

    @Test
    public void testEmprunterAbonneEmpruntEnCour() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Velo vv = new Velo();
        s.arrimerVelo(vv,2);
        s.emprunterVelo(a,2);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertEquals(null, s.emprunterVelo(a, 0));

    }

    @Test
    public void testEmprunterAbonneBloquer() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        a.bloquer();
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertEquals(null, s.emprunterVelo(a, 0));

    }

    @Test
    public void testEmprunterIndexUnderCpacity() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertEquals(null, s.emprunterVelo(a, -9));

    }

    @Test
    public void testEmprunterIndexUpperCpacity() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertEquals(null, s.emprunterVelo(a, 6465));

    }

    @Test
    public void testEmprunterAllreadyDecrocher() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertEquals(null, s.emprunterVelo(a, 0));

    }

    @Test
    public void testEmprunterRegisterEmprunter() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertEquals(null, s.emprunterVelo(a, 0));

    }

    @Test
    public void testEmprunterNoVelo() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,10);
        s.setRegistre(r);
        Assert.assertEquals(null, s.emprunterVelo(a, 0));

    }

    private IVelo addToSet(Set<IVelo> velos){
        IVelo o = new Velo();
        velos.add(o);
        return o;
    }
    private Set<IVelo> createCamion(int nbVeloBien, int nbVeloAbiber, int nbVeloAreviser,int nbVeloAbimerEtAreviser){
        Set<IVelo> camion = new HashSet<>();

        for (int i = 0; i < nbVeloBien; i++) {
            IVelo v1 = new Velo();
            camion.add(v1);
        }

        for (int i = 0; i < nbVeloAbimerEtAreviser; i++) {
            IVelo v1 = new Velo();
            v1.abimer();
            v1.parcourir(1000);
            camion.add(v1);
        }

        for (int i = 0; i < nbVeloAreviser; i++) {
            IVelo v1 = new Velo();
            v1.parcourir(1000);
            camion.add(v1);
        }

        for (int i = 0; i < nbVeloAbiber; i++) {
            IVelo v1 = new Velo();
            v1.abimer();
            camion.add(v1);
        }


        return camion;
    }

    private void createStation(Station s,int nbVeloBien, int nbVeloAbiber, int nbVeloAreviser,int nbVeloAbimerEtAreviser){
        int index = 1;

        for (int i = 0; i < nbVeloBien; i++) {
            IVelo v1 = new Velo();
            s.arrimerVelo(v1,index);
            index++;
        }

        for (int i = 0; i < nbVeloAbimerEtAreviser; i++) {
            IVelo v1 = new Velo();
            v1.abimer();
            v1.parcourir(1000);
            s.arrimerVelo(v1,index);
            index++;
        }

        for (int i = 0; i < nbVeloAreviser; i++) {
            IVelo v1 = new Velo();
            v1.parcourir(1000);
            s.arrimerVelo(v1,index);
            index++;
        }

        for (int i = 0; i < nbVeloAbiber; i++) {
            IVelo v1 = new Velo();
            v1.abimer();
            s.arrimerVelo(v1,index);
            index++;
        }
    }

    @Test
    public void testCamionNULL() {
        Station s = new Station("Gare Viotte", 47.24705, 6.0219527, 9);
        s.setRegistre(r);
        v.abimer();
        s.arrimerVelo(v,1);
        s.equilibrer(null);
    }

        @Test
    public void testCamionVide() {
        Station s = new Station("Gare Viotte",47.24705,6.0219527,9);
        s.setRegistre(r);

        createStation(s,2,0,2,4);

        Set<IVelo> velos = createCamion(5,0,1,4);

        int nbVeloAbiberStation=0;
        int nbVeloAreviserStation=0;
        int nbVeloAbimerEtAreviserStation=0;
        int nbVeloBienStation=0;

        for (int i = 1 ; i < s.capacite(); ++i) {
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

        for (int i = 1 ; i < s.capacite(); ++i) {
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

        Assert.assertEquals(5,(s.capacite()-s.nbBornesLibres()));
        Assert.assertEquals(5,nbVeloBienStation);
        Assert.assertEquals(0,nbVeloAbiberStation);
        Assert.assertEquals(0,nbVeloAreviserStation);
        Assert.assertEquals(0,nbVeloAbimerEtAreviserStation);

        Assert.assertEquals(13,velos.size());
        Assert.assertEquals(2,nbVeloBienCamion);
        Assert.assertEquals(0,nbVeloAbiberCamion);
        Assert.assertEquals(3,nbVeloAreviserCamion);
        Assert.assertEquals(8,nbVeloAbimerEtAreviserCamion);
    }

    @Test
    public void testConstructeurCapaciteNegative () {
        Station q = new Station("Test", 0, 0, -1);
        Assert.assertNotEquals(q.capacite(), -1);
    }



    @Test
    public void facturationValide4(){
        Station s = new Station("Test", 0, 0, 9);
        s.setRegistre(r);
        long now=System.currentTimeMillis();
        long dans10minutes=now+10 * 60 * 1000;
        Assert.assertEquals(-4, s.arrimerVelo(v, 1));
        Assert.assertEquals(0,r.emprunter(a,v,now));
        Assert.assertEquals(0,r.retourner(v,dans10minutes));
        Assert.assertEquals(v.tarif()/6,r.facturation(a,now,dans10minutes),0);
    }








}