package tuProlog

import alice.tuprolog.Prolog
import alice.tuprolog.SolveInfo
import alice.tuprolog.Theory
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class PrologSolver {
    private val engine : Prolog = Prolog()

    init{
        try {
            val t = Theory(FileInputStream("foods.pl"))
            engine.theory = t
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun solve(prologQuery: String): SolveInfo {
        return engine.solve(prologQuery)
    }

    fun hasOpenAlternatives(): Boolean {
        if(engine.hasOpenAlternatives())
            return true
        return false
    }

    fun solveNext(): SolveInfo {
        return engine.solveNext()
    }

    fun aggiungiABaseDiConoscenza(prologQuery: String): SolveInfo {
        return engine.solve("assert($prologQuery)")
    }

    fun rimuoviDaBaseDiConoscenza(prologQuery: String): SolveInfo {
        return engine.solve("retract($prologQuery)")
    }
}