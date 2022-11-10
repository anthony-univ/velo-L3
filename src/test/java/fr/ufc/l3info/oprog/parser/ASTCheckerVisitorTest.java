package fr.ufc.l3info.oprog.parser;

import fr.ufc.l3info.oprog.Station;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ASTCheckerVisitorTest {

    /** Chemin vers les fichiers de test */
    final String path = "./target/classes/data/";

    /** Instance singleton du parser de stations */
    final StationParser parser = StationParser.getInstance();

    @Test
    public void testStationListeVide() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File("./target/classes/data/checkedStationsVide.txt"));
        ASTCheckerVisitor builder = new ASTCheckerVisitor();
        n.accept(builder);
        Map<String, ERROR_KIND> mapError =  builder.getErrors();
        Assert.assertTrue(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
        Assert.assertEquals(1, mapError.size());
    }

    @Test
    public void testStationNomUnique() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File("./target/classes/data/checkedStationsDouble.txt"));
        ASTCheckerVisitor builder = new ASTCheckerVisitor();
        n.accept(builder);
        Map<String, ERROR_KIND> mapError =  builder.getErrors();
        Assert.assertFalse(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
        Assert.assertTrue(mapError.containsValue(ERROR_KIND.DUPLICATE_STATION_NAME));
        Assert.assertEquals(2, mapError.size());
    }

    @Test
    public void testStationNomVide() throws IOException, StationParserException {
    ASTNode n = parser.parse(new File("./target/classes/data/checkedStationsNomVide.txt"));
    ASTCheckerVisitor builder = new ASTCheckerVisitor();
    n.accept(builder);
    Map<String, ERROR_KIND> mapError =  builder.getErrors();
    Assert.assertFalse(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
    Assert.assertTrue(mapError.containsValue(ERROR_KIND.EMPTY_STATION_NAME));
    Assert.assertEquals(1, mapError.size());
    }

    @Test
    public void testStationNomEspaceTabulation() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File("./target/classes/data/checkedStationsNomEspaceTabulation.txt"));
        ASTCheckerVisitor builder = new ASTCheckerVisitor();
        n.accept(builder);
        Map<String, ERROR_KIND> mapError =  builder.getErrors();
        Assert.assertFalse(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
        Assert.assertTrue(mapError.containsValue(ERROR_KIND.EMPTY_STATION_NAME));
        Assert.assertEquals(1, mapError.size());

    }

    @Test
    public void testStationManqueDeclaration() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File("./target/classes/data/checkedStationsManqueDeclaration.txt"));
        ASTCheckerVisitor builder = new ASTCheckerVisitor();
        n.accept(builder);
        Map<String, ERROR_KIND> mapError =  builder.getErrors();
        Assert.assertFalse(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
        Assert.assertTrue(mapError.containsValue(ERROR_KIND.MISSING_DECLARATION));
        Assert.assertEquals(2, mapError.size());
    }

    @Test
    public void testStationDoubleDeclaration() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File("./target/classes/data/checkedStationsDoubleDeclaration.txt"));
        ASTCheckerVisitor builder = new ASTCheckerVisitor();
        n.accept(builder);
        Map<String, ERROR_KIND> mapError =  builder.getErrors();
        Assert.assertFalse(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
        Assert.assertTrue(mapError.containsValue(ERROR_KIND.DUPLICATE_DECLARATION));
        Assert.assertEquals(2, mapError.size());
    }

    @Test
    public void testStationNombre() throws IOException, StationParserException {
        ASTNode n = parser.parse(new File("./target/classes/data/checkedStationsNombre.txt"));
        ASTCheckerVisitor builder = new ASTCheckerVisitor();
        n.accept(builder);
        Map<String, ERROR_KIND> mapError =  builder.getErrors();
        Assert.assertFalse(mapError.containsValue(ERROR_KIND.EMPTY_LIST));
        Assert.assertTrue(mapError.containsValue(ERROR_KIND.WRONG_NUMBER_VALUE));
        Assert.assertEquals(6, mapError.size());
    }
}
