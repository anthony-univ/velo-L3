package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

@RunWith(Parameterized.class)
public class AGGVeloTest {

    private IVelo v;
    private int classe;
    private String cadre;
    private char sexe;
    private String [] options;
    private String optionsAttendues = "";
    private static FabriqueVelo fv = FabriqueVelo.getInstance();

    @Before
    public void createBike() {
        switch (this.classe) {
            case 0:
                v = new Velo();
                break;
            case 1:
                v = new Velo(this.sexe);
                break;
            case 2:
                v = fv.construire(this.sexe, options);
                break;
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        String [] options1 = {"CADRE_ALUMINIUM"};
        String [] options2 = {"CADRE_ALUMINIUM", "FREINS_DISQUE"};
        String [] optionsAttendues1 = {"cadre aluminium"};
        String [] optionsAttendues2 = {"cadre aluminium", "freins à disque"};
        return Arrays.asList(new Object[][] {
                {0, "mixte", 'a', null, null},{1, "femme", 'f', null, null},{1, "femme", 'F', null, null},
                {1, "homme", 'h', null, null},{1, "homme", 'h', null, null},{1, "mixte", 'a', null, null},
                {2, "homme", 'h', options1, optionsAttendues1},
                {2, "femme", 'f', options1, optionsAttendues1},
                {2, "homme", 'H', options2, optionsAttendues2},
                {2, "femme", 'F', options2, optionsAttendues2},
                {2, "mixte", 'a', options2, optionsAttendues2},
        });
    }

    public AGGVeloTest(int classe, String cadre, char sexe, String [] options, String [] optionsAttendues) {
        this.classe = classe;
        this.cadre = cadre;
        this.sexe = sexe;
        if(options!=null) {
            this.options = options;
        }
        if(optionsAttendues!=null) {
            for (String opt : optionsAttendues) {
                this.optionsAttendues += (", " + opt);
            }
        }
    }

    /**
     * Test constructeur velo
     */
    @Test
    public void testVelo() {
        String[] opts = optionsAttendues.split(", ");

        for (int i= 1; i < opts.length; ++i) {
            Assert.assertTrue(v.toString().contains(opts[i]));
        }
        Assert.assertTrue(Pattern.matches("^Vélo cadre " + cadre + "(, (cadre aluminium|freins à disque|suspension avant|suspension arrière|assistance électrique))* - 0\\.0 km$", v.toString()));
    }

    /**
     * Test decrocher velo
     */
    @Test
    public void testUnhookBike() {
        Assert.assertEquals(-1, v.decrocher());
        Assert.assertEquals(0, v.arrimer());
        Assert.assertEquals(0, v.decrocher());
    }

    @Test
    public void testUnhookBikeAlreadyUnhook() {
        Assert.assertEquals(-1, v.decrocher());
    }

    @Test
    public void test2UnhookBike() {
        Assert.assertEquals(0, v.arrimer());
        Assert.assertEquals(0, v.decrocher());
        Assert.assertEquals(-1, v.decrocher());
    }


    /**
     * Test accrocher velo
     */
    @Test
    public void testHookBike() {
        Assert.assertEquals(0, v.arrimer());
    }

    @Test
    public void testHookBikeAlreadyHook() {
        Assert.assertEquals(0, v.arrimer());
        Assert.assertEquals(-1, v.arrimer());
    }

    @Test
    public void test2hookBike() {
        Assert.assertEquals(0, v.arrimer());
        Assert.assertEquals(-1, v.arrimer());
    }


    /**
     * Test abimer velo
     */
    @Test
    public void testDamageUnHookBikeNoDamge() {
        v.abimer();
        Assert.assertTrue(v.estAbime());
    }

    @Test
    public void testDamageBikeUnHookAlreadyDamage() {
        v.abimer();
        Assert.assertTrue(v.estAbime());
        v.abimer();
        Assert.assertTrue(v.estAbime());
    }

    @Test
    public void testDamageBikeHookNoDamge() {
        Assert.assertEquals(0, v.arrimer());
        v.abimer();
        Assert.assertTrue(v.estAbime());
    }

    @Test
    public void testDamageBikeHookAlreadyDamage() {
        Assert.assertEquals(0, v.arrimer());
        v.abimer();
        Assert.assertTrue(v.estAbime());
        v.abimer();
        Assert.assertTrue(v.estAbime());
    }

    /**
     * Test reparer velo
     */
    @Test
    public void testRepairUnHookBikeWithDamage() {
        v.abimer();
        Assert.assertEquals(0, v.reparer());
    }

    @Test
    public void testRepairUnHookBikeWithNoDamage() {
        Assert.assertEquals(-2, v.reparer());
    }

    @Test
    public void testRepairHookBikeWithDamage() {
        Assert.assertEquals(0, v.arrimer());
        v.abimer();
        Assert.assertEquals(-1, v.reparer());
    }

    @Test
    public void test2RepairUnHookBikeWithDamage() {
        v.abimer();
        Assert.assertEquals(0, v.reparer());
        Assert.assertEquals(-2, v.reparer());
    }

    /**
     * Test parcourir
     */
    @Test
    public void testTravel_1_kilometers() {
        v.parcourir(1.0);
        Assert.assertEquals(1.0, v.kilometrage(), 0.00001);
    }

    @Test
    public void testTravel_1_point_27_kilometer() {
        v.parcourir(1.27);
        Assert.assertEquals(1.27, v.kilometrage(), 0.00001);
    }

    @Test
    public void testTravel_negative_kilometers() {
        v.parcourir(-1.27);
        Assert.assertEquals(0.0, v.kilometrage(), 0.00001);
    }

    @Test
    public void testTravel_kilometers_hook_bike() {
        v.arrimer();
        v.parcourir(1.27);
        Assert.assertEquals(0.0, v.kilometrage(), 0.00001);
    }
    /*
    @Test
    public void testTravel_positive_infinity_kilometers() {
        v.parcourir(Double.POSITIVE_INFINITY);
        Assert.assertEquals(0.0, v.kilometrage(), 0.00001);
    }

    @Test
    public void testTravel_negative_infinity_kilometers() {
        v.parcourir(Double.NEGATIVE_INFINITY);
        Assert.assertEquals(0.0, v.kilometrage(), 0.00001);
    }*/

    @Test
    public void testTravel_NaN_value_kilometers() {
        v.parcourir(Double.NaN);
        Assert.assertEquals(0.0, v.kilometrage(), 0.00001);
    }

    /**
     * Test reviser
     */
    @Test
    public void testReviewUnHookBike() {
        Assert.assertEquals(0, v.reviser());
        Assert.assertEquals(500.0, v.prochaineRevision(), 0.00001);
        Assert.assertFalse(v.estAbime());
    }

    @Test
    public void testReviewHookBike() {
        v.parcourir(12.2);
        v.arrimer();
        Assert.assertEquals(-1, v.reviser());
        Assert.assertEquals(500.0-12.2, v.prochaineRevision(), 0.00001);
        Assert.assertFalse(v.estAbime());
    }

    @Test
    public void testReviewUnHookDamageBike() {
        v.abimer();
        Assert.assertEquals(0, v.reviser());
        Assert.assertEquals(500.0, v.prochaineRevision(), 0.00001);
        Assert.assertFalse(v.estAbime());
    }

    @Test
    public void testReviewHookDamageBike() {
        v.parcourir(12.2);
        v.abimer();
        v.arrimer();
        Assert.assertEquals(-1, v.reviser());
        Assert.assertEquals(500.0-12.2, v.prochaineRevision(), 0.00001);
        Assert.assertTrue(v.estAbime());
    }

    /**
     * to_String
     */
    @Test
    public void testToString_100() {
        v.parcourir(100);
        String[] opts = optionsAttendues.split(", ");

        for (int i= 1; i < opts.length; ++i) {
            Assert.assertTrue(v.toString().contains(opts[i]));
        }
        Assert.assertTrue(Pattern.matches("^Vélo cadre " + cadre + "(, (cadre aluminium|freins à disque|suspension avant|suspension arrière|assistance électrique))* - 100\\.0 km$", v.toString()));
    }

    @Test
    public void testToString_10_24() {
        v.parcourir(10.24);
        String[] opts = optionsAttendues.split(", ");

        for (int i= 1; i < opts.length; ++i) {
            Assert.assertTrue(v.toString().contains(opts[i]));
        }
        Assert.assertTrue(Pattern.matches("^Vélo cadre " + cadre + "(, (cadre aluminium|freins à disque|suspension avant|suspension arrière|assistance électrique))* - 10\\.2 km$", v.toString()));
    }

    @Test
    public void testToString_10_26() {
        v.parcourir(10.26);
        String[] opts = optionsAttendues.split(", ");

        for (int i= 1; i < opts.length; ++i) {
            Assert.assertTrue(v.toString().contains(opts[i]));
        }
        Assert.assertTrue(Pattern.matches("^Vélo cadre " + cadre + "(, (cadre aluminium|freins à disque|suspension avant|suspension arrière|assistance électrique))* - 10\\.3 km$", v.toString()));
    }

    @Test
    public void testToString_100_249() {
        v.parcourir(100.249);
        String[] opts = optionsAttendues.split(", ");

        for (int i= 1; i < opts.length; ++i) {
            Assert.assertTrue(v.toString().contains(opts[i]));
        }
        Assert.assertTrue(Pattern.matches("^Vélo cadre " + cadre + "(, (cadre aluminium|freins à disque|suspension avant|suspension arrière|assistance électrique))* - 100\\.2 km$", v.toString()));
    }

    @Test
    public void testToString_100_251() {
        v.parcourir(100.251);
        String[] opts = optionsAttendues.split(", ");

        for (int i= 1; i < opts.length; ++i) {
            Assert.assertTrue(v.toString().contains(opts[i]));
        }
        Assert.assertTrue(Pattern.matches("^Vélo cadre " + cadre + "(, (cadre aluminium|freins à disque|suspension avant|suspension arrière|assistance électrique))* - 100\\.3 km$", v.toString()));
    }

    @Test
    public void testToString_499_999_no_Revison() {
        v.parcourir(499.999);
        String[] opts = optionsAttendues.split(", ");

        for (int i= 1; i < opts.length; ++i) {
            Assert.assertTrue(v.toString().contains(opts[i]));
        }
        Assert.assertTrue(Pattern.matches("^Vélo cadre " + cadre + "(, (cadre aluminium|freins à disque|suspension avant|suspension arrière|assistance électrique))* - 500\\.0 km$", v.toString()));
    }

    @Test
    public void testToString_500_0_Revison() {
        v.parcourir(500.0);
        String[] opts = optionsAttendues.split(", ");

        for (int i= 1; i < opts.length; ++i) {
            Assert.assertTrue(v.toString().contains(opts[i]));
        }

        Assert.assertTrue(Pattern.matches("^Vélo cadre " + cadre + "(, (cadre aluminium|freins à disque|suspension avant|suspension arrière|assistance électrique))* - 500\\.0 km \\(révision nécessaire\\)$", v.toString()));
    }

    @Test
    public void testToString_500_001_Revison() {
        v.parcourir(500.001);
        String[] opts = optionsAttendues.split(", ");

        for (int i= 1; i < opts.length; ++i) {
            Assert.assertTrue(v.toString().contains(opts[i]));
        }

        Assert.assertTrue(Pattern.matches("^Vélo cadre " + cadre + "(, (cadre aluminium|freins à disque|suspension avant|suspension arrière|assistance électrique))* - 500\\.0 km \\(révision nécessaire\\)$", v.toString()));
    }

    /**
     * Test tarif
     */
    @Test
    public void testTarif() {
        double t = 0;
        if(optionsAttendues.contains("cadre aluminium")) {
           t += 0.2;
        }
        if(optionsAttendues.contains("suspension avant")) {
            t += 0.5;
        }
        if(optionsAttendues.contains("suspension arrière")) {
            t += 0.5;
        }
        if(optionsAttendues.contains("freins à disque")) {
            t += 0.3;
        }
        if(optionsAttendues.contains("assistance électrique")) {
            t += 2.0;
        }
        Assert.assertEquals(2.0 + t, v.tarif(), 0.00001);
    }


    /**
     * prochaine revison
     */
    @Test
    public void TestProchaineRevisionNegative() {
        v.parcourir(600.0);
        Assert.assertEquals(-100.0, v.prochaineRevision(), 0.00001);
        String[] opts = optionsAttendues.split(", ");

        for (int i= 1; i < opts.length; ++i) {
            Assert.assertTrue(v.toString().contains(opts[i]));
        }

        Assert.assertTrue(Pattern.matches("^Vélo cadre " + cadre + "(, (cadre aluminium|freins à disque|suspension avant|suspension arrière|assistance électrique))* - 600\\.0 km \\(révision nécessaire\\)$", v.toString()));
    }
}
