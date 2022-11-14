package fr.ufc.l3info.oprog;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LVJRegistreTest {

    private IVelo v;
    private Abonne a;
    private JRegistre j;


    @Before
    public void init() throws IncorrectNameException{
        j = new JRegistre();
        a = new Abonne("Hanniebal Tringue");
        v = new Velo();
    }

    @Test
    public void emprunterValide(){
        Assert.assertEquals(0,j.emprunter(a,v,10));
    }

    @Test
    public void emprunterNonValideAbonneNull(){
        Assert.assertEquals(-1,j.emprunter(null,v,10));
    }

    @Test
    public void emprunterNonValideVeloNull(){
        Assert.assertEquals(-1,j.emprunter(a,null,10));
    }

    @Test
    public void emprunterNonValideDejaEmprunter(){
        Assert.assertEquals(0,j.emprunter(a,v,10));
        Assert.assertEquals(-2,j.emprunter(a,v,10));
    }

    @Test
    public void emprunterNonValideDejaEmprunterz() throws IncorrectNameException {
        Abonne a = new Abonne("Maude Cologne");
        Assert.assertEquals(0,j.emprunter(a,v,0));
        Assert.assertEquals(0,j.retourner(v,10));
        Assert.assertEquals(-2,j.emprunter(a,v,0));
    }

    @Test
    public void retournerValide(){
        Assert.assertEquals(0,j.emprunter(a,v,10));
        Assert.assertEquals(0,j.retourner(v,10));
    }

    @Test
    public void retournerNonValideVeloNUll(){
        Assert.assertEquals(0,j.emprunter(a,v,10));
        Assert.assertEquals(-1,j.retourner(null,10));
    }

    @Test
    public void retournerNonValideVeloNOnEmprunter(){
        Assert.assertEquals(-2,j.retourner(v,10));
    }

    @Test
    public void retournerNonValideDateAntérieur(){
        Assert.assertEquals(0,j.emprunter(a,v,10));
        Assert.assertEquals(-3,j.retourner(v,-10));
    }

    @Test
    public void retournerNonValideChevaucher(){
        Assert.assertEquals(0,j.emprunter(a,v,10));
        Assert.assertEquals(0,j.retourner(v,20));
        Assert.assertEquals(0,j.emprunter(a,v,5));
        Assert.assertEquals(-3,j.retourner(v,15));
    }

    @Test
    public void retournerNopkoehjoergnioergui(){
        Assert.assertEquals(0,j.emprunter(a,v,10));

    }




    @Test
    public void nbEmpruntsEnCoursValide0(){
        Assert.assertEquals(0,j.nbEmpruntsEnCours(a));
    }

    @Test
    public void nbEmpruntsEnCoursValideMANY() throws IncorrectNameException {
        Assert.assertEquals(0,j.emprunter(a,v,10));
        for (int i = 0; i < 100; i++) {
            Velo VV = new Velo();
            Assert.assertEquals(0,j.emprunter(a,VV,10));
        }
        Assert.assertEquals(0,j.retourner(v,20));
        Assert.assertEquals(100,j.nbEmpruntsEnCours(a));
    }

    @Test
    public void facturationValide4(){
        long now=System.currentTimeMillis();
        long dans10minutes=now+10 * 60 * 1000;
        Assert.assertEquals(0,j.emprunter(a,v,now));
        Assert.assertEquals(0,j.retourner(v,dans10minutes));
        Assert.assertEquals(v.tarif()/6,j.facturation(a,now,dans10minutes),0);
    }

    @Test
    public void facturationValide(){
        Assert.assertEquals(0,j.emprunter(a,v,0));
        Assert.assertEquals(0,j.retourner(v,60));
        Assert.assertEquals(0,j.facturation(a,0,60),0.00001);
    }

    @Test
    public void facturationNonValideAbonneNull(){
        Assert.assertEquals(0,j.facturation(null,0,60),0.00001);
    }




}
