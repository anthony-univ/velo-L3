package fr.ufc.l3info.oprog.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class AGGASTCheckerVisitorTest {

    /** Chemin vers les fichiers de test */
    final String path = "./target/classes/data/AGG/";

    /** Instance singleton du parser de stations */
    final StationParser parser = StationParser.getInstance();

    @Test
    public void testStationListeVide() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File(path + "checkedStationsVide.txt"));
        ASTCheckerVisitor builder = new ASTCheckerVisitor();
        n.accept(builder);
        Map<String, ERROR_KIND> mapError =  builder.getErrors();
        Assert.assertTrue(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
        Assert.assertEquals(1, mapError.size());
    }

    @Test
    public void testStationNomUnique() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File(path + "checkedStationsDouble.txt"));
        ASTCheckerVisitor builder = new ASTCheckerVisitor();
        n.accept(builder);
        Map<String, ERROR_KIND> mapError =  builder.getErrors();
        Assert.assertFalse(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
        Assert.assertTrue(mapError.containsValue(ERROR_KIND.DUPLICATE_STATION_NAME));
        Assert.assertEquals(2, mapError.size());
    }

    @Test
    public void testStationNomVide() throws IOException, StationParserException {
    ASTNode n = parser.parse(new File(path + "checkedStationsNomVide.txt"));
    ASTCheckerVisitor builder = new ASTCheckerVisitor();
    n.accept(builder);
    Map<String, ERROR_KIND> mapError =  builder.getErrors();
    Assert.assertFalse(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
    Assert.assertTrue(mapError.containsValue(ERROR_KIND.EMPTY_STATION_NAME));
    Assert.assertEquals(1, mapError.size());
    }

    @Test
    public void testStationNomEspaceTabulation() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File(path + "checkedStationsNomEspaceTabulation.txt"));
        ASTCheckerVisitor builder = new ASTCheckerVisitor();
        n.accept(builder);
        Map<String, ERROR_KIND> mapError =  builder.getErrors();
        Assert.assertFalse(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
        Assert.assertTrue(mapError.containsValue(ERROR_KIND.EMPTY_STATION_NAME));
        Assert.assertEquals(1, mapError.size());

    }

    @Test
    public void testStationManqueDeclaration() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File(path + "checkedStationsManqueDeclaration.txt"));
        ASTCheckerVisitor builder = new ASTCheckerVisitor();
        n.accept(builder);
        Map<String, ERROR_KIND> mapError =  builder.getErrors();
        Assert.assertFalse(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
        Assert.assertTrue(mapError.containsValue(ERROR_KIND.MISSING_DECLARATION));
        Assert.assertEquals(2, mapError.size());
    }

    @Test
    public void testStationDoubleDeclaration() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File(path + "checkedStationsDoubleDeclaration.txt"));
        ASTCheckerVisitor builder = new ASTCheckerVisitor();
        n.accept(builder);
        Map<String, ERROR_KIND> mapError =  builder.getErrors();
        Assert.assertFalse(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
        Assert.assertTrue(mapError.containsValue(ERROR_KIND.DUPLICATE_DECLARATION));
        Assert.assertEquals(2, mapError.size());
    }

    @Test
    public void testStationNombre() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File(path + "checkedStationsNombre.txt"));
        ASTCheckerVisitor builder = new ASTCheckerVisitor();
        n.accept(builder);
        Map<String, ERROR_KIND> mapError =  builder.getErrors();
        Assert.assertFalse(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
        Assert.assertTrue(mapError.containsValue(ERROR_KIND.WRONG_NUMBER_VALUE));
        Assert.assertEquals(6, mapError.size());
    }
}
