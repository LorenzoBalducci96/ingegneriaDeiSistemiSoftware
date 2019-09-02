package tuProlog

import java.util.ArrayList
import java.util.regex.Pattern

import alice.tuprolog.SolveInfo
import alice.tuprolog.exceptions.MalformedGoalException
import alice.tuprolog.exceptions.NoMoreSolutionException
import alice.tuprolog.exceptions.NoSolutionException

object Program {

    @JvmStatic
    fun main(args: Array<String>) {
        val risolutore: PrologSolver = PrologSolver() //Custom prolog solver

        //Test 1 => L'elemento appartiene alla KB?
        println("Running Test 1 -------------------------")
        var result1: SolveInfo
        try {
            result1 = risolutore.solve("cibo(banana)")
            println("cibo(banana) appartiene alla KB? [PRIMO RISULTATO] $result1")
            result1 = risolutore.solve("cibo(pokemon)")
            println("cibo(pokemon) appartiene alla KB? [SECONDO RISULTATO] $result1")
        } catch (e1: MalformedGoalException) {}


        //Test 2 => Uso 2p al contrario, mi chiedo se esiste nella KB un X tale che cibo(X) e' vero
        println("Running Test 2 -------------------------")
        val result2: SolveInfo
        val moreResults = ArrayList<SolveInfo>()
        try {
            result2 = risolutore.solve("cibo(X)")
            if (result2.isSuccess()) {
                System.out.println("Esiste nella KB un X tale che cibo(X) e' vero? [PRIMO RISULTATO] " + result2.getSolution())
                //Se ho altri risultati
                while (risolutore.hasOpenAlternatives()) {
                    try {
                        moreResults.add(risolutore.solveNext())
                    } catch (e: NoMoreSolutionException) {
                        e.printStackTrace()
                    }

                }
            }
            if (moreResults.isEmpty() == false) {
                println("E ho anche altri risultati!")
                for (s in moreResults) {
                    System.out.println(s.getSolution())
                }
            }
        }
        catch (e1: MalformedGoalException) {}
        catch (e1: NoSolutionException) {}

        //Test 3 => Ottenere i risultati INTERNI al predicato usando le Regexp
        println("Running Test 3 -------------------------")
        val `in` = "cibo(mela),cibo(pesca),cibo(pesce)"
        val p = Pattern.compile("\\((.*?)\\)")
        val m = p.matcher(`in`)
        println("Estrazione di elemento da inner parentesi")
        //Stampa in ordine mela, banana e pesce
        while (m.find()) println(m.group(1))

        //Test 4 => Modificare la base di conoscenza e fare query a runtime
        println("Running Test 4 -------------------------")
        try {
            //Modifica la base di conoscenza e poi effettua diverse query
            risolutore.aggiungiABaseDiConoscenza("cibo(cane)")
            risolutore.rimuoviDaBaseDiConoscenza("cibo(pokemon)")
            var result4 = risolutore.solve("cibo(cane)")
            var result5 = risolutore.solve("cibo(pokemon)")
            System.out.println(result4)//Si!
            System.out.println(result5)//No!
        } catch (e: MalformedGoalException) {}

    } //Main

}//Class

