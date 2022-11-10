package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RunWith(Parameterized.class)
public class VeloTest {

    private static final String ALU = "CADRE_ALUMINIUM";
    private static final String S_AVANT = "SUSPENSION_AVANT";
    private static final String S_ARRIERE = "SUSPENSION_ARRIERE";
    private static final String F_DISQUE = "FREINS_DISQUE";
    private static final String A_ELEC = "ASSISTANCE_ELECTRIQUE";
    private static FabriqueVelo decathlon = FabriqueVelo.getInstance();


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        String[] tab={ALU,S_AVANT};
        String[] tabe={S_ARRIERE,ALU,S_AVANT};
        String[] tabee={S_ARRIERE,ALU,S_AVANT,F_DISQUE,A_ELEC};
        String[] tabeee={S_ARRIERE,ALU,F_DISQUE};
        String[] tabeeee={S_ARRIERE,ALU};

        String[] e = {};
        return Arrays.asList(new Object[][] {
                {false,"mixte",e},
                {false,"homme",e},
                {false,"femme",e},
                {true,"homme",tab},
                {true,"mixte",tabe},
                {true,"F",tabee},
                {true,"H",tabeee},
                {true,"f",tabee},
                {true,"h",tabeee},
                {true,"d",tabeeee},
                {true,"void",tabeeee},

        });
    }

    private IVelo v;
    private String type;
    private boolean instanceType;
    private String[] options={};

    public VeloTest(boolean instance,String t,String[] poptions) {
        instanceType=instance;

        char typechar = t.charAt(0);
        if(typechar == 'h' || typechar == 'H'){
            type="homme";
        }else if(typechar == 'f' || typechar == 'F'){
            type="femme";
        }else{
            type="mixte";
        }

        options=poptions;

    }

    @Before
    public void resetVelo(){
        if(instanceType){
            v = decathlon.construire(type.charAt(0),options);
            return ;
        }
        if(type.charAt(0) != 'h' && type.charAt(0) != 'H' && type.charAt(0) != 'f' && type.charAt(0) != 'F'){
            v = new Velo();
            return;
        }
        v = new Velo(type.charAt(0));
    }

    @Test
    public void createVelo_should_be_mixteType() {
        Assert.assertTrue(v.toString().contains(type));
    }

    @Test
    public void createVelo_WithTypeHommeUppercase_should_be_hommeType() {
        Assert.assertTrue(v.toString().contains(type));
    }

    @Test
    public void createVelo_WithTypeHommeLowercase_should_be_hommeType() {
        Assert.assertTrue(v.toString().contains(type));
    }

    @Test
    public void createVelo_WithTypeFemmeUppercase_should_be_femmeType() {
        Assert.assertTrue(v.toString().contains(type));
    }

    @Test
    public void createVelo_WithTypeFemmeLowercase_should_be_femmeType() {
        Assert.assertTrue(v.toString().contains(type));
    }

    @Test
    public void createVelo_WithInvalidType_should_be_mixteType() {
        Assert.assertTrue(v.toString().contains(type));
    }

    @Test
    public void decrocher_AND_reDecrocher_should_be_trueFalseFalse() {

        Assert.assertEquals(0,v.arrimer());
        Assert.assertEquals(0,v.decrocher());
        Assert.assertEquals(-1,v.decrocher());
        Assert.assertEquals(-1,v.decrocher());
    }

    @Test
    public void arrimer_AND_reArrimer_should_be_trueFalse() {

        Assert.assertEquals(0,v.arrimer());
        Assert.assertEquals(-1,v.arrimer());
        Assert.assertEquals(0,v.decrocher());
    }

    @Test
    public void abimer_veloNeuf_should_be_abimer() {

        Assert.assertFalse(v.estAbime());
        v.abimer();
        Assert.assertTrue(v.estAbime());
        v.abimer();
        Assert.assertTrue(v.estAbime());
    }

    @Test
    public void reparer_decrocherAbimerReparer_should_be_reparer() {

        v.abimer();
        Assert.assertTrue(v.estAbime());
        Assert.assertEquals(0,v.reparer());
    }

    @Test
    public void reparer_accrocherAbimerReparer_should_not_be_reparer() {

        v.abimer();
        Assert.assertTrue(v.estAbime());
        Assert.assertEquals(0,v.arrimer());
        Assert.assertEquals(-1,v.reparer());
    }

    @Test
    public void reparer_decrocherNonAbimerReparer_should_not_be_reparer() {
        Assert.assertEquals(-2,v.reparer());
    }

    @Test
    public void reparer_accrocherNonAbimerReparer_should_not_be_reparer() {

        Assert.assertEquals(0,v.arrimer());
        Assert.assertEquals(-1,v.reparer());
    }

    @Test
    public void parcourir_accrocher_moreThan500km_shoul_not_be_parourue() {

        Assert.assertEquals(0,v.arrimer());
        v.parcourir(1000);
        Assert.assertFalse(v.toString().contains("(révision nécessaire)"));
    }

    @Test
    public void parcourir_NegatifNumber_shoul_not_be_parourue() {

        v.parcourir(-1000);
        Assert.assertFalse(v.toString().contains("(révision nécessaire)"));
    }

    @Test
    public void toString_true() {

        v.parcourir(421);
        String str = v.toString();
        if(instanceType){
            Assert.assertEquals("Vélo cadre "+type+str.substring(16,str.indexOf('-')-1)+" - 421.0 km",str);
            v.parcourir(50);
            Assert.assertEquals("Vélo cadre "+type+str.substring(16,str.indexOf('-')-1)+" - 471.0 km",v.toString());

            return ;
        }
        Assert.assertEquals("Vélo cadre "+type+" - 421.0 km",str);
        v.parcourir(50);
        Assert.assertEquals("Vélo cadre "+type+" - 471.0 km",v.toString());

    }

    @Test
    public void toString_false() {

        v.parcourir(421);
        Assert.assertNotEquals("Vélo cadre "+type+"- 421.0 km",v.toString());
    }

    @Test
    public void parcourir_moreThan500km() {

        v.parcourir(500.0);
        Assert.assertTrue(v.toString().contains("(révision nécessaire)"));
    }

    @Test
    public void parcourir_lessThan500km() {

        v.parcourir(499.899);
        Assert.assertFalse(v.toString().contains("(révision nécessaire)"));
    }

    @Test
    public void reviser_normal() {

        v.parcourir(500.0);
        Assert.assertTrue(v.toString().contains("(révision nécessaire)"));
        Assert.assertEquals(0,v.reviser());
        Assert.assertFalse(v.toString().contains("(révision nécessaire)"));

    }

    @Test
    public void reviser_under500km() {

        v.parcourir(499.899);
        Assert.assertFalse(v.toString().contains("(révision nécessaire)"));
        Assert.assertEquals(0,v.reviser());
        Assert.assertFalse(v.toString().contains("(révision nécessaire)"));
    }

    @Test
    public void reviser_accrocher() {

        Assert.assertEquals(0,v.arrimer());
        Assert.assertEquals(-1,v.reviser());
    }

    @Test
    public void reviser_moreThan500km_abimer() {

        v.parcourir(500.0);
        v.abimer();
        Assert.assertEquals(0,v.reviser());
        Assert.assertFalse(v.estAbime());
    }

    @Test
    public void tarif() {
        double tarif=2.0;

        String str = v.toString();
        if(str.contains("cadre aluminium")){tarif+=0.2;}
        if(str.contains("freins à disque")){tarif+=0.3;}
        if(str.contains("assistance électrique")){tarif+=2;}
        if(str.contains("suspension arrière")){tarif+=0.5;}
        if(str.contains("suspension avant")){tarif+=0.5;}



        Assert.assertEquals(tarif,v.tarif(),0.01);

    }












}
