package fr.ufc.l3info.oprog.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class ASTCheckerVisitorTest {

    /** Chemin vers les fichiers de test */
    final String path = "./target/classes/data/";

    /** Instance singleton du parser de stations */
    final StationParser parser = StationParser.getInstance();


    @Test
    public void test() throws IOException, StationParserException{
        ASTNode n = parser.parse(new File("./target/classes/data/double.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        n.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertFalse(errors.isEmpty());

    }

    @Test
    public void vide() throws IOException, StationParserException{
        ASTNode n = parser.parse(new File("./target/classes/data/vide.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        n.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        Assert.assertFalse(errors.isEmpty());

    }

    @Test
    public void capacite0() throws IOException, StationParserException{
        ASTNode n = parser.parse(new File("./target/classes/data/capacite1.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        n.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        System.out.println(errors.values());
        Assert.assertFalse(errors.isEmpty());

    }

    @Test
    public void capaciteDouble() throws IOException, StationParserException{
        ASTNode n = parser.parse(new File("./target/classes/data/capacite2.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        n.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        System.out.println(errors.values());
        Assert.assertFalse(errors.isEmpty());

    }

    @Test
    public void capaciteNegatif() throws IOException, StationParserException{
        ASTNode n = parser.parse(new File("./target/classes/data/capacite4.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        n.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        System.out.println(errors.values());
        Assert.assertFalse(errors.isEmpty());

    }

    @Test
    public void zzzzzzzzzz() throws IOException, StationParserException{
        ASTNode n = parser.parse(new File("./target/classes/data/capacite3.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        n.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        System.out.println(errors.values());
        Assert.assertFalse(errors.isEmpty());

    }

    @Test
    public void empty() throws IOException, StationParserException{
        ASTNode n = parser.parse(new File("./target/classes/data/empty.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        n.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        System.out.println(errors.values());
        Assert.assertFalse(errors.isEmpty());

    }

    @Test
    public void duplicateCdelca() throws IOException, StationParserException{
        ASTNode n = parser.parse(new File("./target/classes/data/duplicatedecla.txt"));
        ASTCheckerVisitor visitor = new ASTCheckerVisitor();
        n.accept(visitor);

        Map<String, ERROR_KIND> errors = visitor.getErrors();
        System.out.println(errors.values());
        Assert.assertFalse(errors.isEmpty());

    }

}
