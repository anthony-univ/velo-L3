package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Pattern;

public class AGGFabriqueVeloTest {

    private FabriqueVelo fv = null;

    @Before
    public void createFabriqueVelo() {
        fv = FabriqueVelo.getInstance();
    }

    @Test
    public void testNoneOption() {
        IVelo v = fv.construire('m');
        Assert.assertEquals("Vélo cadre mixte - 0.0 km", v.toString());
    }

    @Test
    public void testOneOption() {
        IVelo v = fv.construire('m', "SUSPENSION_ARRIERE");
        Assert.assertEquals("Vélo cadre mixte, suspension arrière - 0.0 km", v.toString());
    }

    @Test
    public void testOptionNull() {
        IVelo v = fv.construire('m', null, "SUSPENSION_ARRIERE");
        Assert.assertEquals("Vélo cadre mixte, suspension arrière - 0.0 km", v.toString());
    }

    @Test
    public void testMultiOptions() {
        IVelo v = fv.construire('m', "CADRE_ALUMINIUM", "SUSPENSION_AVANT", "SUSPENSION_ARRIERE", "FREINS_DISQUE", "ASSISTANCE_ELECTRIQUE");
        Assert.assertTrue(v.toString().contains("cadre aluminium"));
        Assert.assertTrue(v.toString().contains("suspension avant"));
        Assert.assertTrue(v.toString().contains("suspension arrière"));
        Assert.assertTrue(v.toString().contains("freins à disque"));
        Assert.assertTrue(v.toString().contains("assistance électrique"));
        Assert.assertTrue(Pattern.matches("^Vélo cadre (mixte|homme|femme)(, (cadre aluminium|freins à disque|suspension avant|suspension arrière|assistance électrique))* - [\\d]+\\.[\\d] km$", v.toString()));
    }

    @Test
    public void testWrongOption() {
        IVelo v = fv.construire('m', "INVALIDE_OPTION");
        Assert.assertEquals("Vélo cadre mixte - 0.0 km", v.toString());
    }

    @Test
    public void testSameOption() {
        String [] options = {"CADRE_ALUMINIUM", "SUSPENSION_AVANT", "SUSPENSION_ARRIERE", "FREINS_DISQUE", "ASSISTANCE_ELECTRIQUE"};
        String [] tab = {"cadre aluminium", "suspension avant", "suspension arrière", "freins à disque", "assistance électrique"};
        int i =0 ;
        for (String opt: options) {
            IVelo v = fv.construire('m', opt, opt);
            Assert.assertTrue(v.toString().contains(tab[i]));
            Assert.assertEquals("Vélo cadre mixte, " + tab[i] + " - 0.0 km", v.toString());
            ++i;
        }

    }

    @Test
    public void test2InstancesFabriques() {
        FabriqueVelo fv2 = FabriqueVelo.getInstance();
        Assert.assertEquals(fv, fv2);
    }
}
