package fr.ufc.l3info.oprog;


import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test unitaire pour les abonnés.
 */
public class AbonneTest {

    /**
     * Test constructeur avec nom
     */
    @Test(expected=IncorrectNameException.class)
    public void testNomVide() throws IncorrectNameException {
        Abonne a = new Abonne("");
    }

    @Test
    public void testNom1lettre() throws IncorrectNameException {
        Abonne a = new Abonne("a");
        Assert.assertEquals("a", a.getNom());
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testNomBasique() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony");
        Assert.assertEquals("Anthony", a.getNom());
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testNomsAvecEspacesAvantApres() throws IncorrectNameException {
        String [] nomAvecEspacesAvantApres = {" Anthony", "Anthony ", " Anthony ", "  Anthony", "Anthony  ", "  Anthony  "};
        for(int i = 0; i < nomAvecEspacesAvantApres.length; ++i) {
            Abonne a = new Abonne(nomAvecEspacesAvantApres[i]);
            Assert.assertEquals(nomAvecEspacesAvantApres[i].trim(), a.getNom());
            Assert.assertTrue(a.estBloque());
        }
    }

    @Test
    public void testNomsComposes() throws IncorrectNameException {
        String [] nomsComposes = {"Anthony gasca", "Anthony-gasca", "Anthony gasca gimeno", "Anthony-gasca-gimeno"};
        for(int i = 0; i < nomsComposes.length; ++i) {
            Abonne a = new Abonne(nomsComposes[i]);
            Assert.assertEquals(nomsComposes[i], a.getNom());
            Assert.assertTrue(a.estBloque());
        }
    }

    @Test(expected=IncorrectNameException.class)
    public void testNomComposesAvecDeuxEspacesConsecutif() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony  gasca");
    }

    @Test(expected=IncorrectNameException.class)
    public void testNomComposesAvecDeuxTiretsConsecutif() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony--gasca");
    }

    @Test(expected=IncorrectNameException.class)
    public void testNomComposesAvecUnTiretEtEspaceConsecutif() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony- gasca");
    }

    @Test(expected=IncorrectNameException.class)
    public void testNomAvecChiffre() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony 2");
    }

    @Test
    public void testNomsAvecLettresAccentuées() throws IncorrectNameException {
        String [] nomsAccentues = {"Lemaître", "PRÉVÔT", "Jean pöl", "besançon"};
        for(int i = 0; i < nomsAccentues.length; ++i) {
            Abonne a = new Abonne(nomsAccentues[i]);
            Assert.assertEquals(nomsAccentues[i], a.getNom());
            Assert.assertTrue(a.estBloque());
        }
    }

    @Test
    public void testNomsAvecLigatures() throws IncorrectNameException {
        String [] nomsLigatures = {"cœur", "kris KÆR"};
        for(int i = 0; i < nomsLigatures.length; ++i) {
            Abonne a = new Abonne(nomsLigatures[i]);
            Assert.assertEquals(nomsLigatures[i], a.getNom());
            Assert.assertTrue(a.estBloque());
        }
    }

    @Test(expected=IncorrectNameException.class)
    public void testNomNull() throws IncorrectNameException {
        Abonne a = new Abonne(null);
    }

    /**
     * Test constructeur avec nom et rib
     */
    @Test
    public void testNomCorrectRibCorrect() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony", "11111-11111-11111111111-48"); //->rib correct
        Assert.assertEquals("Anthony", a.getNom());
        Assert.assertFalse(a.estBloque());
    }

    @Test(expected=IncorrectNameException.class)
    public void testNomIncorrectRibQuelqueSoit() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony 2", "11111-11111-11111111111-48");
    }

    @Test
    public void testNomCorrectFormatRibIncorrect() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony", "11111 11111-11111111111 48"); //->rib incorrect car présence de lettres
        Assert.assertEquals("Anthony", a.getNom());
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testNomCorrectCléRibIncorrect() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony", "11111-11111-11111111111-47"); //->rib incorrect car clé incorrect
        Assert.assertEquals("Anthony", a.getNom());
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testNomCorrectRibTropLongDebut() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony", "FR11111-11111-11111111111-48"); //->rib incorrect car présence de lettres
        Assert.assertEquals("Anthony", a.getNom());
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testNomCorrectRibTropLongFin() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony", "11111-11111-11111111111-48FR"); //->rib incorrect car présence de lettres
        Assert.assertEquals("Anthony", a.getNom());
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testRibNull() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony", null);
    }

    /**
     * Test mise a jour RIB
     */
    @Test
    public void testMajRibPersonneAvecRibCorrect() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony", "11111-11111-11111111111-47"); //->personne bloquée car rib incorrect
        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB("11111-11111-11111111111-48"); //->personne débloquée car rib correct
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void testMajRibPersonneAvecFormatRibIncorrect() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony", "FR111-11111-11111111111-48"); //->personne bloquée car format rib incorrect
        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB("EN111-11111-11111111111-48"); //->personne toujours bloquée car rib incorrect
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testMajRibPersonneAvecCleRibIncorrect() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony", "11111-11111-11111111111-47"); //->personne bloquée car clé rib incorrect
        Assert.assertTrue(a.estBloque());
        a.miseAJourRIB("11111-11111-11111111111-47"); //->personne toujours bloquée car clé rib incorrect
        Assert.assertTrue(a.estBloque());
    }

    @Test
    public void testMajRibPersonneInterditBancaireAvecRibCorrect() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony", "11111-11111-11111111111-48"); //->personne débloquée car rib correct
        Assert.assertFalse(a.estBloque());
        a.bloquer();
        Assert.assertTrue(a.estBloque()); //-> personne Insterdit Bancaire
        a.miseAJourRIB("11111-11111-11111111111-48"); //->personne toujours bloquée car l'abonne est interdit bancaire
        Assert.assertTrue(a.estBloque());
    }

    /**
     * Test debloquer/bloquer
     */
    @Test
    public void testDebloquerPersonneAvecCoordonneesBancairesRenseignées() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony", "11111-11111-11111111111-48"); //->personne non bloquée car rib correct
        Assert.assertFalse(a.estBloque());
        a.bloquer(); //->abonne interdit bancaire
        Assert.assertTrue(a.estBloque());
        a.debloquer();
        Assert.assertFalse(a.estBloque());
    }

    @Test
    public void testDebloquerPersonneAvecCoordonneesBancairesNonrenseignées() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony", ""); //->personne bloquée car rib incorrect
        Assert.assertTrue(a.estBloque());
        a.debloquer(); //->personne toujours bloquée car rib non renseigné(rib="")
        Assert.assertTrue(a.estBloque());
    }

    /**
     * equals
     */
    @Test
    public void testNotEquals2Abonnes() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony");
        Abonne b = new Abonne("Léa");
        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void testEqualsAbonnesIdentique() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony");
        Assert.assertTrue(a.equals(a));
    }

    @Test
    public void testNotEqualsBetweenAbonneAndString() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony");
        String b = new String("Léa");
        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void testNotEqualsAbonneNull() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony");
        Abonne b = null;
        Assert.assertFalse(a.equals(b));
    }

    /**
     * hashCode
     */

    @Test
    public void testHashCodeEquals() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony");

        Assert.assertEquals(a.hashCode(), a.hashCode());
    }

    @Test
    public void testHashCodeNotEquals() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony");
        Abonne b = new Abonne("Anthony");

        Assert.assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void TestHashSet() throws IncorrectNameException {
        HashSet<Abonne> abonnes = new HashSet<Abonne>();
        Abonne a = new Abonne("Anthony");
        abonnes.add(a);

        Assert.assertTrue(abonnes.contains(a));
    }

    /**
     * testID
     */
    @Test
    public void TestIdIncrement() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony");
        Abonne b = new Abonne("Anthony");

        Assert.assertEquals(a.getID()+1, b.getID());
    }

    @Test
    public void TestIdNegative() throws IncorrectNameException {
        Abonne a = new Abonne("Anthony");

        Assert.assertTrue(a.getID()>=0);
    }
}