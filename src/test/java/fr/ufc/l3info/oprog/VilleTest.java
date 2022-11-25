package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VilleTest {

        final String path = "./target/classes/data/AGG/";
        final String vraiPath = "./target/classes/data/";

        private Ville v = new Ville();


        private void after(int nbvoulue) {
                int nb = 0;
                Iterator<Station> it = v.iterator();

                while(it.hasNext()) {
                        Station s = it.next();
                        System.out.println("parcours " + s.getNom());
                        if(s != null) {
                                ++nb;
                        }
                }
                Assert.assertEquals(nbvoulue, nb);
        }

        @Test
        public void TestInitOK() throws IOException {
                v.initialiser(new File(path + "stationsOK.txt"));
                Assert.assertNotNull(v.getStation("21 - Avenue Fontaine Argent, Boulevard Diderot"));
                Assert.assertNotNull(v.getStation("Avenue du Maréchal Foch"));

                after(2);
        }

        @Test//(expected=IOException.class)
        public void TestInitKO() throws IOException {
                v.initialiser(new File(path + "stationsKO.txt"));
                Assert.assertNull(v.getStation("21 - Avenue Fontaine Argent, Boulevard Diderot"));
                Assert.assertNull(v.getStation("Avenue du Maréchal Foch"));

                after(0);
        }

        @Test
        public void TestPasDeStation() throws IOException {
                v.initialiser(new File(path + "checkedStationsVide.txt"));
                Assert.assertNull(v.getStation("21 - Avenue Fontaine Argent, Boulevard Diderot"));
                Assert.assertNull(v.getStation("Avenue du Maréchal Foch"));

                after(0);
        }

        @Test
        public void TestGetStationPlusprocheOK() throws IOException {
                v.initialiser(new File(path + "stationsOK2.txt"));
                Assert.assertNotNull(v.getStation("21 - Avenue Fontaine Argent, Boulevard Diderot"));
                Assert.assertNotNull(v.getStation("Avenue du Maréchal Foch"));

                after(2);
                System.out.println("laaaaaaaaaaaaaaaaa"  + v.getStationPlusProche(38.47, 10.2).getNom());
                Assert.assertEquals(v.getStation("Avenue du Maréchal Foch"), v.getStationPlusProche(38.47, 10.2));
        }





        @Test
        public void setStationPrincipaleValide() throws IOException {
                v.initialiser(new File(vraiPath + "OK1.txt"));
                Station principale = v.iterator().next();
                v.setStationPrincipale("Mairie");
                Station bis = v.iterator().next();
                Assert.assertEquals("Mairie",bis.getNom());
        }

        @Test
        public void setStationPrincipaleNoValide() throws IOException {
                v.initialiser(new File(vraiPath + "OK1.txt"));
                Station principale = v.iterator().next();
                v.setStationPrincipale("Mairiee");
                Station bis = v.iterator().next();
                Assert.assertEquals("Gare Viotte",bis.getNom());
        }

        @Test
        public void setStationPrincipaleNull() throws IOException {
                v.initialiser(new File(vraiPath + "OK1.txt"));
                Station principale = v.iterator().next();
                v.setStationPrincipale(null);
                Station bis = v.iterator().next();
                Assert.assertEquals("Gare Viotte",bis.getNom());
        }



        @Test
        public void getStationTestValide() throws IOException {
            v.initialiser(new File(vraiPath + "OK1.txt"));
            Assert.assertEquals("Mairie",v.getStation("Mairie").getNom());
        }

        @Test
        public void getStationTestNoValideNull() throws IOException {
                v.initialiser(new File(vraiPath + "OK1.txt"));
                Assert.assertEquals(null,v.getStation(null));
        }

        @Test
        public void getStationTestNoValideBadName() throws IOException {
                v.initialiser(new File(vraiPath + "OK1.txt"));
                Assert.assertEquals(null,v.getStation("zeghergerhee"));
        }


        @Test
        public void getStationPlusProche() throws IOException {
                v.initialiser(new File(vraiPath + "OK1.txt"));
                Assert.assertEquals("Gare Viotte",v.getStationPlusProche(47.24559926269805, 6.021859188084817).getNom());
        }


        @Test
        public void creerAbonneValide() throws IOException {
                v.initialiser(new File(vraiPath + "OK1.txt"));
                Abonne a = v.creerAbonne("anthoonyzefg","11111-11111-11111111111-48");
                Assert.assertTrue(a != null);
        }

        @Test
        public void iterator() throws IOException {
                v.initialiser(new File(vraiPath + "OK1.txt"));

                Iterator<Station> it = v.iterator();

                Station current = it.next();
                double d = 0.0;

                while(it.hasNext()){
                        Station TempCurrent = it.next();
                        System.out.println(TempCurrent.getNom());
                        double dPrime = current.distance(TempCurrent);
                        if(dPrime > d){
                                Assert.assertTrue(true);
                        }
                        d = dPrime;
                        current = TempCurrent;
                }
        }



        @Test
        public void TestFacturation() throws IOException {
                v.initialiser(new File(path + "stationsOK2.txt"));
                Assert.assertNotNull(v.getStation("21 - Avenue Fontaine Argent, Boulevard Diderot"));
                Assert.assertNotNull(v.getStation("Avenue du Maréchal Foch"));

                after(2);

                Abonne aa = v.creerAbonne("Léo", "11111-11111-11111111111-48");
                Abonne a = v.creerAbonne("Anthony", "11111-11111-11111111111-48");
                IVelo velo = new Velo();
                IVelo veloo = new Velo();

                Station s = Mockito.spy( v.getStation("Avenue du Maréchal Foch"));

                s.arrimerVelo(velo, 1);
                s.arrimerVelo(veloo, 3);
                long mtn = 1669043486238L;  //novembre 21/2022
                long dans2h =  mtn + 120000;

                Mockito.when(s.maintenant()).thenReturn(mtn);
                s.emprunterVelo(a, 1);
                Mockito.when(s.maintenant()).thenReturn(dans2h);
                s.arrimerVelo(velo, 2);

                Mockito.when(s.maintenant()).thenReturn(mtn);
                s.emprunterVelo(aa, 3);
                Mockito.when(s.maintenant()).thenReturn(dans2h);
                s.arrimerVelo(veloo, 4);

                Map<Abonne,Double> facturations = v.facturation(11, 2022);
                for (Map.Entry m: facturations.entrySet()) {
                        System.out.println(m.getValue());
                        //Assert.assertEquals("Anthony", m.getKey());
                        Assert.assertEquals(0.0667, (Double) m.getValue(), 0.01);
                }
        }







}
