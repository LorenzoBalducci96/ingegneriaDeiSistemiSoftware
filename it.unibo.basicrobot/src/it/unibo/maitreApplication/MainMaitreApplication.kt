/* Generated by AN DISI Unibo */ 
package it.unibo.maitreApplication
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "basicrobot.pl", "sysRules.pl"
	)
}
