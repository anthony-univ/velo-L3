package fr.ufc.l3info.oprog;


import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Test unitaire pour les abonnés.
 */
public class AbonneTest {




    @Test (expected = IncorrectNameException.class)
    public void createAbonneName_NameVoid_should_not_be_create() throws IncorrectNameException {final Abonne a = new Abonne("");}

    @Test (expected = IncorrectNameException.class)
    public void createAbonneName_NameNull_should_not_be_create() throws IncorrectNameException {final Abonne a = new Abonne(null);}

    @Test
    public void createAbonneName_NameLigation_should_be_create() throws IncorrectNameException {final Abonne a = new Abonne("cœur");}

    @Test
    public void createAbonneName_NameOnlySingelLetter_should_be_create() throws IncorrectNameException {final Abonne a = new Abonne("p");}

    @Test
    public void createAbonneName_NameValid_should_be_create() throws IncorrectNameException {final Abonne a = new Abonne("leo vandrepol");}

    @Test (expected = IncorrectNameException.class)
    public void createAbonneName_NameWithDoubleSpace_should_not_be_create() throws IncorrectNameException {final Abonne a = new Abonne("leo  vandrepol");}

    @Test (expected = IncorrectNameException.class)
    public void createAbonneName_NameWithDoubleDash_should_not_be_create() throws IncorrectNameException {final Abonne a = new Abonne("leo--vandrepol");
    }

    @Test (expected = IncorrectNameException.class)
    public void createAbonneName_NameWithNumber_should_not_be_create() throws IncorrectNameException {final Abonne a = new Abonne("64");}

    @Test (expected = IncorrectNameException.class)
    public void createAbonneName_NameArabLetter() throws IncorrectNameException {final Abonne a = new Abonne("ث");}

    @Test (expected = IncorrectNameException.class)
    public void createAbonneName_NameCyrillicLetter () throws IncorrectNameException {final Abonne a = new Abonne("д");}



    @Test
    public void createAbonneNameRib_nameVvalid_ribNULL_should_be_create() throws IncorrectNameException {
        final String nom = "leo-vandrepol";
        final Abonne a = new Abonne(nom,null);
        Assert.assertEquals(nom,a.getNom());
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void createAbonneNameRib_nameVvalid_ribInvalid_should_be_create() throws IncorrectNameException {
        final String nom = "leo-vandrepol";
        final Abonne a = new Abonne(nom,"00000-00000-00000000000-97");
        Assert.assertEquals(nom,a.getNom());
        Assert.assertFalse(a.estBloque());
    }

    @Test (expected = IncorrectNameException.class)
    public void createAbonneNameRib_nameInvalid_ribValid_should_not_be_create() throws IncorrectNameException {
        final String nom = "leo--vandrepol";
        final Abonne a = new Abonne(nom,"00000-00000-00000000000-97");
        Assert.assertEquals(nom,a.getNom());
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void createAbonneNameRib_nameValid_ribInvalidPattern_should_be_create_AND_block() throws IncorrectNameException {
        final String nom = "leo-vandrepol";
        final Abonne a = new Abonne(nom,"00000-00000-zerz-97");
        Assert.assertEquals(nom,a.getNom());
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void createAbonneNameRib_nameValid_ribInvalidKey_should_be_create_AND_block() throws IncorrectNameException {
        final String nom = "leo vandrepol";
        final Abonne a = new Abonne(nom,"00000-00000-00000000000-96");
        Assert.assertEquals(nom,a.getNom());
        Assert.assertTrue(a.estBloque());
    }


    @Test
    public void updateRib_invalid_should_not_be_update() throws IncorrectNameException {
        final Abonne a = new Abonne("leo vandrepol","00000-00000-00000000000-96");
        a.miseAJourRIB("00000-00000-00000000000-95");
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void updateRib_valid_should_be_upadte() throws IncorrectNameException {
        final Abonne a = new Abonne("leo vandrepol","00000-00000-00000000000-96");
        a.miseAJourRIB("00000-00000-00000000000-97");
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void updateRib_valid_but_already_valid_should_be_update() throws IncorrectNameException {
        final Abonne a = new Abonne("leo vandrepol","00000-00000-00000000000-97");
        a.miseAJourRIB("00000-00000-00000000000-97");
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void updateRib_block_by_admin_should_not_be_update() throws IncorrectNameException {
        final Abonne a = new Abonne("leo vandrepol","00000-00000-00000000000-97");
        Assert.assertFalse(a.estBloque());
        a.bloquer();
        a.miseAJourRIB("00000-00000-00000000000-97");
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void unblockAbonne_WithNoRib_should_be_false() throws IncorrectNameException {
        final Abonne a = new Abonne("leo vandrepol");
        Assert.assertTrue(a.estBloque());
        a.debloquer();
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void unblockAbonne_VolontaryBlocked_WithNoRib_should_be_false() throws IncorrectNameException {
        final Abonne a = new Abonne("leo vandrepol");
        Assert.assertTrue(a.estBloque());
        a.bloquer();
        Assert.assertTrue(a.estBloque());
        a.debloquer();
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void unblockAbonne_WithRib_should_not_be_false() throws IncorrectNameException {
        Abonne a = new Abonne("leo vandrepol", "00000-00000-00000000000-97");
        Assert.assertFalse(a.estBloque());
        a.bloquer();
        Assert.assertTrue(a.estBloque());
        a.debloquer();
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void equals_abonne_null_should_not_be_equals() throws IncorrectNameException {
        final Abonne a = new Abonne("leo vandrepol");
        final Abonne b = null;
        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void equals_with_bad_object_type_should_not_be_equals() throws IncorrectNameException {
        final Abonne a = new Abonne("leo vandrepol");
        final int b = 2;
        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void equals_should_be_the_same() throws IncorrectNameException {
        final Abonne a = new Abonne("leo vandrepol","00000-00000-00000000000-97");
        Assert.assertTrue(a.equals(a));
    }

    @Test
    public void equals_should_not_be_the_same() throws IncorrectNameException {
        final Abonne a = new Abonne("leo vandrepol","00000-00000-00000000000-97");
        final Abonne b = new Abonne("leo vandrepol","00000-00000-00000000000-97");
        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void id_negatif_should_be_false() throws IncorrectNameException{
        final Abonne a = new Abonne("leo vandrepol");
        Assert.assertTrue(a.getID() >= 0 );
    }

    @Test
    public void id_increment_should_be_true() throws IncorrectNameException{
        Abonne a = new Abonne("leo vandrepol");
        for(int i= 0 ; i <1000;i++){
            Abonne b = new Abonne("leo vandrepol");
            Assert.assertTrue(a.getID() == b.getID()-1);
            a = b;
        }


    }

    @Test
    public void hashCode_should_be_the_same() throws Exception {
        final Abonne a = new Abonne("leo vandrepol");
        Assert.assertTrue(a.hashCode() == a.hashCode());
    }

    @Test
    public void hashCode_should_not_be_the_same() throws Exception {
        final Abonne a = new Abonne("leo vandrepol");
        final Abonne b = new Abonne("leo vandrepol");
        Assert.assertFalse(a.hashCode() == b.hashCode());
    }

}