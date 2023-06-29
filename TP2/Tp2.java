package org.example;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.commons.syntax.Constant;
import org.tweetyproject.logics.commons.syntax.Functor;
import org.tweetyproject.logics.commons.syntax.Predicate;
import org.tweetyproject.logics.commons.syntax.Sort;
import org.tweetyproject.logics.fol.parser.FolParser;
import org.tweetyproject.logics.fol.syntax.FolBeliefSet;
import org.tweetyproject.logics.fol.syntax.FolSignature;

public class Tp2 {
    /**
     *
     * @param args arguments
     * @throws ParserException ParserException
     * @throws IOException IOException
     */
    public static void main(String[] args) throws ParserException, IOException {

        FolSignature sig = new FolSignature();
        Sort animal = new Sort("animal");
        sig.add(animal);
        Constant anna = new Constant("titi",animal); //titi est un animal
        sig.add(anna);
        Constant bob = new Constant("bob",animal); //bob est un animal
        sig.add(bob);
        Sort plante = new Sort("plante");
        sig.add(plante);
        Constant emma = new Constant("emma", plante); //emma est une plante
        sig.add(emma);

        List<Sort> l = new ArrayList<Sort>();
        l.add(animal);
        Predicate animals = new Predicate("Animal", l);//creation du prédicat Animal d'arité 1
        sig.add(animals);
        l = new ArrayList<Sort>();
        l.add(animal);
        l.add(plante);
        Predicate eats = new Predicate("Mange", l); //creation du prédicat Mange qui est d'arité 2
        sig.add(eats);
        l = new ArrayList<Sort>();
        l.add(animal);
        // pereDe est une fonction qui prend un terme qui est de type animal
        Functor fatherOf = new Functor("pereDe",l,animal);
        sig.add(fatherOf);

        FolBeliefSet b = new FolBeliefSet();
        b.setSignature(sig);
        FolParser parser = new FolParser();
        parser.setSignature(sig);

        b.add(parser.parseFormula("forall X:(Animal(X) => (exists Y:(Mange(X,Y))))"));
        //si X est un animal alors fatherOf(X) est un animal
        b.add(parser.parseFormula("forall X:(Animal(X) => Animal(pereDe(X)))"));
        b.add(parser.parseFormula("Animal(titi)")); //titi est un animal
        b.add(parser.parseFormula("Mange(titi,emma)")); //bob mange emma

        //print to console
        System.out.println(b.toArray()[0]);
        System.out.println(b.toArray()[1]);
        System.out.println(b.toArray()[2]);
        System.out.println(b.toArray()[3]);
    }
}