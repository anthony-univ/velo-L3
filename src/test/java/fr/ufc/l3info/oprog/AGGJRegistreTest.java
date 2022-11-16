package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AGGJRegistreTest {

    private JRegistre r;
    private Abonne a;
    private IVelo v;

    @Before
    public void before() throws IncorrectNameException {
        r = new JRegistre();
        a = new Abonne("paul", "11111-11111-11111111111-48");
        v = new Velo();
    }

    @Test
    public void testEmprunterVeloNull() {
        v = null;
        Assert.assertEquals(-1, r.emprunter(a, v, 0));
        Assert.assertEquals(0, r.nbEmpruntsEnCours(a));
    }

    @Test
    public void testEmprunterAbonneNull() {
        a = null;
        Assert.assertEquals(-1, r.emprunter(a, v, 0));
    }

    @Test
    public void testEmprunterMemeVelo() throws IncorrectNameException {
        Assert.assertEquals(0, r.emprunter(a, v, 0));
        Assert.assertEquals(1, r.nbEmpruntsEnCours(a));
        Abonne b = new Abonne("Paul");
        Assert.assertEquals(-2, r.emprunter(b, v, 1));
        Assert.assertEquals(0, r.nbEmpruntsEnCours(b));
    }

    @Test
    public void testEmprunterVeloChevauchant() throws IncorrectNameException {
        Assert.assertEquals(0, r.emprunter(a, v, -2));
        Assert.assertEquals(1, r.nbEmpruntsEnCours(a));
        Assert.assertEquals(0, r.retourner(v,0));
        Assert.assertEquals(0, r.nbEmpruntsEnCours(a));
        Abonne b = new Abonne("Paul");
        Assert.assertEquals(-2, r.emprunter(b, v, -1));
        Assert.assertEquals(0, r.nbEmpruntsEnCours(b));
    }

    @Test
    public void plusVitequeSonOmbre() {
        Assert.assertEquals(0, r.emprunter(a, v, 0));
        Assert.assertEquals(0, r.retourner(v,0));
        Assert.assertEquals(0, r.nbEmpruntsEnCours(a));
    }

    @Test
    public void testEmprunterDeuxVeloAbonne()  {
        Assert.assertEquals(0, r.emprunter(a, v, 0));
        Velo v2 = new Velo();
        Assert.assertEquals(0, r.emprunter(a, v2, 0));
        Assert.assertEquals(2, r.nbEmpruntsEnCours(a));
    }

    @Test
    public void testEmprunterVelo()  {
        Assert.assertEquals(0, r.emprunter(a, v, 0));
        Assert.assertEquals(1, r.nbEmpruntsEnCours(a));

        Velo v2 = new Velo();
        Assert.assertEquals(0, r.emprunter(a, v2, 0));
        Assert.assertEquals(2, r.nbEmpruntsEnCours(a));

        Velo v3 = new Velo();
        Assert.assertEquals(0, r.emprunter(a, v3, 0));
        Assert.assertEquals(3, r.nbEmpruntsEnCours(a));
    }

    @Test
    public void testRetournerVeloNull()  {
        v = null;
        Assert.assertEquals(-1, r.retourner(v, 0));
        Assert.assertEquals(0, r.nbEmpruntsEnCours(a));
    }

    @Test
    public void testRetournerVeloPasEmprunter()  {
        Assert.assertEquals(-2, r.retourner(v, 0));
        Assert.assertEquals(0, r.nbEmpruntsEnCours(a));
    }

    @Test
    public void testRetournerVeloDateAnterieur()  {
        Assert.assertEquals(0, r.emprunter(a, v, 0));
        Assert.assertEquals(1, r.nbEmpruntsEnCours(a));
        Assert.assertEquals(-3, r.retourner(v, -30000));
        Assert.assertEquals(1, r.nbEmpruntsEnCours(a));
    }

    @Test
    public void testRetournerVeloChevauchant() throws IncorrectNameException {
        Assert.assertEquals(0, r.emprunter(a, v, -30000));
        Assert.assertEquals(1, r.nbEmpruntsEnCours(a));
        Assert.assertEquals(0, r.retourner(v,0));
        Assert.assertEquals(0, r.nbEmpruntsEnCours(a));
        Abonne b = new Abonne("Paul");
        Assert.assertEquals(0, r.emprunter(b, v, -60000));
        Assert.assertEquals(1, r.nbEmpruntsEnCours(b));
        // devrait retourner -3
        Assert.assertEquals(-3, r.retourner(v, 60000));
    }

    @Test
    public void testFacturationHb() {
        Assert.assertEquals(0, r.emprunter(a, v, 0));
        Assert.assertEquals(0, r.retourner(v, 60000));
        Assert.assertEquals(0, r.facturation(a, 0, 50000), 0.001);
    }

    @Test
    public void testFacturation() {
        Assert.assertEquals(0, r.emprunter(a, v, 0));
        Assert.assertEquals(0, r.retourner(v, 60000));
        Assert.assertEquals(0, r.emprunter(a, v, 60001));
        Assert.assertEquals(0, r.retourner(v, 180000));
        Assert.assertEquals(0, r.emprunter(a, v, 180001));
        Assert.assertEquals(0, r.retourner(v, 300000));
        Assert.assertEquals(0, r.emprunter(a, v, 300001));
        Assert.assertEquals(0, r.retourner(v, 400000));

        Assert.assertEquals(0.133, r.facturation(a, 0, 600000), 0.001);
    }

    @Test
    public void testFacturationMinutePresDessous() {
        Assert.assertEquals(0, r.emprunter(a, v, 0));
        Assert.assertEquals(0, r.retourner(v, 59999));
        Assert.assertEquals(0.0, r.facturation(a, 0, 600000), 0.001);
    }

    @Test
    public void testFacturationMinutePresEqual() {
        Assert.assertEquals(0, r.emprunter(a, v, 0));
        Assert.assertEquals(0, r.retourner(v, 60000));
        Assert.assertEquals(0.033, r.facturation(a, 0, 600000), 0.001);
    }

    @Test
    public void testFacturationMinutePresDessus() {
        Assert.assertEquals(0, r.emprunter(a, v, 0));
        Assert.assertEquals(0, r.retourner(v, 120001));
        Assert.assertEquals(0.066, r.facturation(a, 0, 600000), 0.001);
    }

    @Test
    public void testJRegistrefaux() {
        Assert.assertEquals(0, r.emprunter(a, v, 30000));
        Assert.assertEquals(0, r.retourner(v, 60000));
        Assert.assertEquals(0, r.emprunter(a, v, 0));
        //devrait retourner -3
        Assert.assertEquals(-3, r.retourner(v, 90000));
    }
}
