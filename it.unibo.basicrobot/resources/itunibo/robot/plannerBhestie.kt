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
import it.unibo.exploremap.stella.model.RobotState
 

object plannerBhestie{
	lateinit var actor: ActorBasic
	lateinit private var actions: MutableList<Action>
	lateinit private var goals_step: MutableList<Goal>
	lateinit private var move_to_register: Action
	
	fun create( actor: ActorBasic ){
		this.actor = actor
		
		RoomMap.getRoomMap().put(2,2,Box.createTable());
		RoomMap.getRoomMap().put(4,4,Box.createDishwasher());
		RoomMap.getRoomMap().put(0,4,Box.createFridge());
		RoomMap.getRoomMap().put(4,0,Box.createPantry());
		
		aiutil.initAI();
	}
	
	fun registerAck(cmd : String){
		if(cmd.equals("ok", true)){
			aiutil.doMove(move_to_register.toString())
			aiutil.showMap();
		}
		if(cmd.equals("fail", true)){
			when(aiutil.initialState.getDirection()){
				Direction.UP -> RoomMap.getRoomMap().put(aiutil.initialState.getX(), aiutil.initialState.getY() - 1, Box.createObstacle())
				Direction.DOWN -> RoomMap.getRoomMap().put(aiutil.initialState.getX(), aiutil.initialState.getY() + 1, Box.createObstacle())
				Direction.RIGHT -> RoomMap.getRoomMap().put(aiutil.initialState.getX() + 1, aiutil.initialState.getY(), Box.createObstacle())
				Direction.LEFT -> RoomMap.getRoomMap().put(aiutil.initialState.getX() - 1, aiutil.initialState.getY(), Box.createObstacle())
			}
			aiutil.showMap();
			aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.getDirection(), goals_step.elementAt(0));
			actions = aiutil.doPlan()
		}
	}
	
	fun requestNextMove(){
		GlobalScope.launch {
			if(actions.isEmpty()){
				goals_step.removeAt(0);
				if(goals_step.isEmpty()){
					actor.autoMsg("endTaskEventCmd", "endTaskEventCmd(GOAL)")
				}
				else{
					aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.getDirection(), goals_step.elementAt(0));
					actions = aiutil.doPlan()
					move_to_register = actions.removeAt(0);
					when (move_to_register.toString()) {
					    "w" -> actor.autoMsg("plannerCmd", "plannerCmd(i)")
					    "a" -> actor.autoMsg("plannerCmd", "plannerCmd(l)")
						"s" -> actor.autoMsg("plannerCmd", "plannerCmd(h)")
						"d" -> actor.autoMsg("plannerCmd", "plannerCmd(r)")
					}
				}
			}
			else{//TODO fattorizzare codice
				move_to_register = actions.removeAt(0);
					when (move_to_register.toString()) {
					    "w" -> actor.autoMsg("plannerCmd", "plannerCmd(i)")
					    "a" -> actor.autoMsg("plannerCmd", "plannerCmd(l)")
						"s" -> actor.autoMsg("plannerCmd", "plannerCmd(h)")
						"d" -> actor.autoMsg("plannerCmd", "plannerCmd(r)")
					}
			}
		}	
	}
	
	
	fun action (cmd : String){
		if(cmd.equals("msg(c)", true)){//sparecchia
			goals_step = mutableListOf(Goal.TABLE, Goal.DISHWASHER)
			aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.getDirection(), goals_step.elementAt(0));//prendo i piatti
			actions = aiutil.doPlan()
			aiutil.showMap();
			
			//----------------------------------
		}
	}
	
	
	/*
	fun action (cmd : String){
		GlobalScope.launch {
			if(cmd.equals("msg(c)", true)){//sparecchia
				aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.getDirection(), Goal.TABLE);//prendo i piatti
				var actions = aiutil.doPlan();
				
				aiutil.showMap();
				
				actions.forEach {
					aiutil.doMove(it.toString());
					aiutil.showMap();
					when (it.toString()) {
					    "w" -> actor.autoMsg("plannerCmd", "plannerCmd(i)")
					    "a" -> actor.autoMsg("plannerCmd", "plannerCmd(l)")
						"s" -> actor.autoMsg("plannerCmd", "plannerCmd(h)")
						"d" -> actor.autoMsg("plannerCmd", "plannerCmd(r)")
					}
				}
				
				println("il robot è arrivato al tavolo e prende i piatti sporchi")
			
				aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.direction, Goal.DISHWASHER);//porto i piatti in lavastoviglie
				actions = aiutil.doPlan();
				actions.forEach {
					aiutil.doMove(it.toString());
					when (it.toString()) {
					    "w" -> actor.autoMsg("plannerCmd", "plannerCmd(i)")
					    "a" -> actor.autoMsg("plannerCmd", "plannerCmd(l)")
						"s" -> actor.autoMsg("plannerCmd", "plannerCmd(h)")
						"d" -> actor.autoMsg("plannerCmd", "plannerCmd(r)")
					}
					aiutil.showMap();
				}
				
			}
			if(cmd.equals("msg(v)", true)){//apparecchia
				aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.direction, Goal.PANTRY);
				var actions = aiutil.doPlan();
				
				aiutil.showMap();
				
				actions.forEach {
					aiutil.doMove(it.toString());
					when (it.toString()) {
					    "w" -> actor.autoMsg("plannerCmd", "plannerCmd(i)")
					    "a" -> actor.autoMsg("plannerCmd", "plannerCmd(l)")
						"s" -> actor.autoMsg("plannerCmd", "plannerCmd(h)")
						"d" -> actor.autoMsg("plannerCmd", "plannerCmd(r)")
					}
					aiutil.showMap();
				}
				
				println("il robot e arrivato alla dispensa, ora prendera i piatti per portarli a tavola")
				
				aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.direction, Goal.PANTRY);
				actions = aiutil.doPlan();
				actions.forEach {
					aiutil.doMove(it.toString());
					when (it.toString()) {
					    "w" -> actor.autoMsg("plannerCmd", "plannerCmd(i)")
					    "a" -> actor.autoMsg("plannerCmd", "plannerCmd(l)")
						"s" -> actor.autoMsg("plannerCmd", "plannerCmd(h)")
						"d" -> actor.autoMsg("plannerCmd", "plannerCmd(r)")
					}
					aiutil.showMap();
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
 */
}