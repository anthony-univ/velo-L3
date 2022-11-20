package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class VilleTest {

        final String path = "./target/classes/data/AGG/";

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
        public void TestFacturation() throws IOException, IncorrectNameException {
                v.initialiser(new File(path + "stationsOK2.txt"));
                Assert.assertNotNull(v.getStation("21 - Avenue Fontaine Argent, Boulevard Diderot"));
                Assert.assertNotNull(v.getStation("Avenue du Maréchal Foch"));

                after(2);

                Abonne a = v.creerAbonne("Anthony", "11111-11111-11111111111-48");
                IVelo velo = new Velo();

                Station s = Mockito.spy( v.getStation("Avenue du Maréchal Foch"));

                s.arrimerVelo(velo, 1);
                long mtn = System.currentTimeMillis();
                long dans2h =  System.currentTimeMillis() + 120000;

                Mockito.when(s.maintenant()).thenReturn(mtn);
                s.emprunterVelo(a, 1);
                Mockito.when(s.maintenant()).thenReturn(dans2h);
                s.arrimerVelo(velo, 2);

                Map<Abonne,Double> facturations = v.facturation(11, 2022);
                for (Map.Entry m: facturations.entrySet()) {
                        System.out.println(m.getValue());
                        //Assert.assertEquals("Anthony", m.getKey());
                        Assert.assertEquals(0.0667, (Double) m.getValue(), 0.01);
                }
        }


}
