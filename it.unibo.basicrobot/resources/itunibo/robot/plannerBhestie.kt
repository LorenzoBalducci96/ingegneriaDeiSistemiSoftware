package itunibo.robot
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
 

object plannerBhestie{
	lateinit var actor: ActorBasic
	
	fun create( actor: ActorBasic ){
		this.actor = actor
	}
	
	fun action (cmd : String){
		GlobalScope.launch {
			println("ricevuto comando maitre, l'incredibile strada che il robot deve fare sono un passo avanti, girarsi a destra e un passo avanti")
		//consult dello stato attuale virtualRobotJavaState.getX();
			actor.autoMsg("plannerCmd", "plannerCmd(i)")
			delay(600)
			actor.autoMsg("plannerCmd", "plannerCmd(r)")
			delay(600)
			actor.autoMsg("plannerCmd", "plannerCmd(i)")
			delay(600)
			actor.autoMsg("endTaskEventCmd", "endTaskEventCmd(h)")
			
		}
	}
}