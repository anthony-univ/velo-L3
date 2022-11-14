package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test unitaire pour la FabriqueVeloTest.
 */
public class LVFabriqueVeloTest {


    private final String ALU = "CADRE_ALUMINIUM";
    private final String S_AVANT = "SUSPENSION_AVANT";
    private final String S_ARRIERE = "SUSPENSION_ARRIERE";
    private final String F_DISQUE = "FREINS_DISQUE";
    private final String A_ELEC = "ASSISTANCE_ELECTRIQUE";

    private FabriqueVelo decathlon = FabriqueVelo.getInstance();


    @Test
    public void FabriqueVeloTest() {
        IVelo iv =  decathlon.construire('h',ALU,S_AVANT,S_ARRIERE,F_DISQUE,A_ELEC,ALU);
        String str = iv.toString();

        Assert.assertTrue(str.contains(", cadre aluminium"));

    }

    @Test
    public void DoubleOptAlu() {
        IVelo iv =  decathlon.construire('h',S_AVANT,S_AVANT);
        int str = iv.toString().split("suspension avant").length-1;

        Assert.assertEquals(1,str);
    }

    @Test
    public void DoublSusArr() {
        IVelo iv =  decathlon.construire('h',S_ARRIERE,S_ARRIERE);
        int str = iv.toString().split("suspension arrière").length-1;

        Assert.assertEquals(1,str);
    }

    @Test
    public void Doubldisuqe() {
        IVelo iv =  decathlon.construire('h',F_DISQUE,F_DISQUE);
        int str = iv.toString().split("freins à disque").length-1;

        Assert.assertEquals(1,str);
    }

    @Test
    public void DoublassiElec() {
        IVelo iv =  decathlon.construire('h',A_ELEC,A_ELEC);
        int str = iv.toString().split("assistance électrique").length-1;

        Assert.assertEquals(1,str);
    }

    @Test
    public void DoubleSusAvant() {
        IVelo iv =  decathlon.construire('h',ALU,ALU);
        int str = iv.toString().split("cadre aluminium").length-1;

        Assert.assertEquals(1,str);
    }


    @Test
    public void invalidOption() {
        IVelo iv =  decathlon.construire('h',"invalidOption");
        String str = iv.toString();
        Assert.assertEquals("Vélo cadre homme - 0.0 km",str);
    }



}