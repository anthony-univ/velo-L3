package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class VilleTest {

        final String path = "./target/classes/data/AGG/";

        private Ville v = new Ville();


        private void after(int nbvoulue) {
                int nb = 0;
                Iterator<Station> it = v.iterator();
                while(it.hasNext()) {
                        Station s = it.next();
                        if(s != null) {
                                ++nb;
                        }
                        it.next();
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

        @Test
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

                Assert.assertEquals(new Station("21 - Avenue Fontaine Argent, Boulevard Diderot", 47.2477615, 5.98359951, 12), v.getStationPlusProche(38.47, 10.2));
        }

}
