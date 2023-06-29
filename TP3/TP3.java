package org.example;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.commons.syntax.Predicate;
import org.tweetyproject.logics.commons.syntax.RelationalFormula;
import org.tweetyproject.logics.fol.syntax.FolFormula;
import org.tweetyproject.logics.fol.syntax.FolSignature;
import org.tweetyproject.logics.ml.parser.MlParser;
import org.tweetyproject.logics.ml.reasoner.SimpleMlReasoner;
import org.tweetyproject.logics.ml.syntax.MlBeliefSet;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws ParserException, IOException {
        MlBeliefSet bs = new MlBeliefSet(); // Mlbeliefset est utilise pour stocker les formules modales
        MlParser parser = new MlParser(); // Mlparser est utilise pour parser les formules modales
        FolSignature sig = new FolSignature();  // FolSignature est utilise pour stocker les symboles de la logique du premier ordre
        // On ajoute les symboles de la logique du premier ordre dans la signature de la logique modale
        sig.add(new Predicate("p", 0));
        sig.add(new Predicate("q", 0));
        // On ajoute la signature de la logique du premier ordre dans le parser de la logique modale
        parser.setSignature(sig);
        // On ajoute les formules modales dans la base de connaissances modale
        bs.add((RelationalFormula)parser.parseFormula("<>(p && q)"));
        bs.add((RelationalFormula)parser.parseFormula("[](!(p) || q)"));
        bs.add((RelationalFormula)parser.parseFormula("[](q && <>(!(q)))"));
        // On affiche la base de connaissances modale
        System.out.println("Modal knowledge base: " + bs);
        //reasoner est utilise pour repondre aux requetes sur la base de connaissances modale par la methode query
        SimpleMlReasoner reasoner = new SimpleMlReasoner();
        System.out.println("[](!p)      " + reasoner.query(bs, (FolFormula)parser.parseFormula("[](!p)")) + "\n");
        System.out.println("<>(p && q)  " + reasoner.query(bs, (FolFormula)parser.parseFormula("<>(p && q)")) + "\n");
        System.out.println("!(q)  " + reasoner.query(bs, (FolFormula)parser.parseFormula("!(q)")) + "\n");
    }
}