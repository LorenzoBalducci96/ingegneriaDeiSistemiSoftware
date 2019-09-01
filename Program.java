package tuProlog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import alice.tuprolog.exceptions.MalformedGoalException;
import alice.tuprolog.exceptions.NoMoreSolutionException;
import alice.tuprolog.exceptions.NoSolutionException;

public class Program {

	public static void main(String[] args) {
		Prolog engine = new Prolog();
		try {
			Theory t = new Theory(new FileInputStream("foods.pl"));
			engine.setTheory(t);
		}
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException e) {e.printStackTrace();} 

		//Test 1 => L'elemento appartiene alla KB?
		System.out.println("Running Test 1 -------------------------");
		SolveInfo result1;
		try {
			result1 = engine.solve("cibo(banana)");
			System.out.println("cibo(banana) appartiene alla KB? [PRIMO RISULTATO] " +result1);
		} catch (MalformedGoalException e1) {}


		//Test 2 => Uso 2p al contrario, mi chiedo se esiste nella KB un X tale che cibo(X) e' vero
		System.out.println("Running Test 2 -------------------------");
		SolveInfo result2;
		List<SolveInfo> moreResults = new ArrayList<>();
		try {
			result2 = engine.solve("cibo(X)");
			if(result2.isSuccess()) {
				System.out.println("Esiste nella KB un X tale che cibo(X) e' vero? [PRIMO RISULTATO] "+ result2.getSolution());
				//Se ho altri risultati
				while(engine.hasOpenAlternatives()) {
					try {
						moreResults.add(engine.solveNext());
					} catch (NoMoreSolutionException e) {e.printStackTrace();}
				}
			}
			if(moreResults.isEmpty() == false) {
				System.out.println("E ho anche altri risultati!");
				for(SolveInfo s : moreResults) {
					System.out.println(s.getSolution());
				}
			}
		} catch (MalformedGoalException e1) {} catch (NoSolutionException e1) {}
		
		//Test 3 => Ottenere i risultati INTERNI al predicato usando le Regexp
		System.out.println("Running Test 3 -------------------------");
		String in = "cibo(mela),cibo(banana),cibo(pesce)";
		Pattern p = Pattern.compile("\\((.*?)\\)");
		Matcher m = p.matcher(in);
		System.out.println("Estrazione di elemento da inner parentesi");
		while(m.find()) {
		    System.out.println(m.group(1)); //Stampa in ordine mela, banana e pesce
		}
		
		//Test 4 => Modificare la base di conoscenza e fare query a runtime
		System.out.println("Running Test 4 -------------------------");
		try {
			SolveInfo result4 = engine.solve("retract(cibo(pesca))");
			SolveInfo result5 = engine.solve("assert(cibo(uovo))");
			result4 = engine.solve("cibo(pesca)");
			result5 = engine.solve("cibo(uovo)");
			System.out.println(result4);//No!
			System.out.println(result5);//Si!
		} catch (MalformedGoalException e) {}

	} //Main

}//Class

