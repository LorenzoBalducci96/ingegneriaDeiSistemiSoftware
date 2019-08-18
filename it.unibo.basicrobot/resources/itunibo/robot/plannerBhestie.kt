package itunibo.robot
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.ApplMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import it.unibo.exploremap.stella.model.RoomMap
import it.unibo.exploremap.stella.model.Box
import it.unibo.exploremap.program.aiutil
import it.unibo.exploremap.stella.model.RobotState.Direction
import it.unibo.exploremap.stella.model.RobotState.Goal
import aima.core.agent.Action
 

object plannerBhestie{
	lateinit var actor: ActorBasic
	
	fun create( actor: ActorBasic ){
		this.actor = actor
		
		RoomMap.getRoomMap().put(2,2,Box.createTable());
		RoomMap.getRoomMap().put(4,4,Box.createDishwasher());
		RoomMap.getRoomMap().put(0,4,Box.createFridge());
		RoomMap.getRoomMap().put(4,0,Box.createPantry());
	}
	
	fun action (cmd : String){
		GlobalScope.launch {
			if(cmd.equals("msg(c)", true)){//sparecchia
				aiutil.initFromToAI(virtualRobotJavaState.getX(), virtualRobotJavaState.getY(), virtualRobotJavaState.getDirection(), Goal.TABLE);
				var actions = aiutil.doPlan();
				actions.forEach {
					when (it.toString()) {
					    "w" -> actor.autoMsg("plannerCmd", "plannerCmd(i)")
					    "a" -> actor.autoMsg("plannerCmd", "plannerCmd(l)")
						"s" -> actor.autoMsg("plannerCmd", "plannerCmd(h)")
						"d" -> actor.autoMsg("plannerCmd", "plannerCmd(r)")
					}
					virtualRobotJavaState.updateState(cmd);
					aiutil.doMove(it.toString());
				}
				
				println("il robot è arrivato al tavolo e prende i piatti sporchi")
			
				aiutil.initFromToAI(virtualRobotJavaState.getX(), virtualRobotJavaState.getY(), virtualRobotJavaState.getDirection(), Goal.DISHWASHER);
				actions = aiutil.doPlan();
				actions.forEach {
					when (it.toString()) {
					    "w" -> actor.autoMsg("plannerCmd", "plannerCmd(i)")
					    "a" -> actor.autoMsg("plannerCmd", "plannerCmd(l)")
						"s" -> actor.autoMsg("plannerCmd", "plannerCmd(h)")
						"d" -> actor.autoMsg("plannerCmd", "plannerCmd(r)")
					}
					virtualRobotJavaState.updateState(cmd);
					aiutil.doMove(it.toString());
				}
				
			}
			if(cmd.equals("msg(v)", true)){//apparecchia
				aiutil.initFromToAI(virtualRobotJavaState.getX(), virtualRobotJavaState.getY(), virtualRobotJavaState.getDirection(), Goal.PANTRY);
				var actions = aiutil.doPlan();
				actions.forEach {
					when (it.toString()) {
					    "w" -> actor.autoMsg("plannerCmd", "plannerCmd(i)")
					    "a" -> actor.autoMsg("plannerCmd", "plannerCmd(l)")
						"s" -> actor.autoMsg("plannerCmd", "plannerCmd(h)")
						"d" -> actor.autoMsg("plannerCmd", "plannerCmd(r)")
					}
					virtualRobotJavaState.updateState(cmd);
					aiutil.doMove(it.toString());
				}
				
				println("il robot e arrivato alla dispensa, ora prendera i piatti per portarli a tavola")
				
				aiutil.initFromToAI(virtualRobotJavaState.getX(), virtualRobotJavaState.getY(), virtualRobotJavaState.getDirection(), Goal.PANTRY);
				actions = aiutil.doPlan();
				actions.forEach {
					when (it.toString()) {
					    "w" -> actor.autoMsg("plannerCmd", "plannerCmd(i)")
					    "a" -> actor.autoMsg("plannerCmd", "plannerCmd(l)")
						"s" -> actor.autoMsg("plannerCmd", "plannerCmd(h)")
						"d" -> actor.autoMsg("plannerCmd", "plannerCmd(r)")
					}
					virtualRobotJavaState.updateState(cmd);
					aiutil.doMove(it.toString());
				}
			}
			
			/*
			actor.autoMsg("plannerCmd", "plannerCmd(i)")
			delay(600)
			actor.autoMsg("plannerCmd", "plannerCmd(r)")
			delay(600)
			actor.autoMsg("plannerCmd", "plannerCmd(i)")
			delay(600)
			actor.autoMsg("endTaskEventCmd", "endTaskEventCmd(h)")
			 */
			
		}
	}
}