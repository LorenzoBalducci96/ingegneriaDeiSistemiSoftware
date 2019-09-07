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
import java.util.StringTokenizer
import itunibo.fridgeIOT.Food
import itunibo.fridgeIOT.FoodInFridge
import it.unibo.exploremap.stella.model.RobotState.PlannerStep
import it.unibo.exploremap.stella.model.Task
 

object plannerBhestie{
	lateinit var actor: ActorBasic
	lateinit private var actions: MutableList<Action>
	lateinit private var goals_step: MutableList<PlannerStep>
	lateinit private var move_to_register: Action
	lateinit private var foodRequestedList: MutableList<FoodInFridge>
	
	fun setRoomSize(size: String){
		RoomMap.setRoomSize(Integer.parseInt(size));
	}
	
	fun putPantry(x: String, y: String){
		RoomMap.getRoomMap().put(Integer.parseInt(x),Integer.parseInt(y),Box.createPantry());
	}
	
	fun putTable(x: String, y: String){
		RoomMap.getRoomMap().put(Integer.parseInt(x),Integer.parseInt(y),Box.createTable());
	}
	
	fun putDishwasher(x: String, y: String){
		RoomMap.getRoomMap().put(Integer.parseInt(x),Integer.parseInt(y),Box.createDishwasher());
	}
	
	fun putFridge(x: String, y: String){
		RoomMap.getRoomMap().put(Integer.parseInt(x),Integer.parseInt(y),Box.createFridge());
	}
	
	
	fun create( actor: ActorBasic){
		
		foodRequestedList = ArrayList<FoodInFridge>()
		
		this.actor = actor

		/*
		var TABLE_X: String = "2";
		var TABLE_Y: String = "2";
		var DISHWASHER_X: String = "4";
		var DISHWASHER_Y: String = "4";
		var FRIDGE_X: String = "0";
		var FRIDGE_Y: String = "2";
		var PANTRY_X: String = "4";
		var PANTRY_Y: String = "0";
 		*/
		
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
			aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.getDirection(), goals_step.elementAt(0) as Goal);
			var foundPath: Boolean = false;
			aiutil.doPlan()?.let{actions = it; foundPath = true}
			while(!foundPath){
				RoomMap.getRoomMap().resetObstaclesOnMap();
				aiutil.doPlan()?.let{actions = it; foundPath = true}
			}
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
					if(goals_step.elementAt(0) is Goal){
						aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.getDirection(), goals_step.elementAt(0) as Goal);
						
						var foundPath: Boolean = false;
						aiutil.doPlan()?.let{actions = it; foundPath = true}
						while(!foundPath){
							RoomMap.getRoomMap().resetObstaclesOnMap();
							aiutil.doPlan()?.let{actions = it; foundPath = true}
						}
						
						move_to_register = actions.removeAt(0)
						when (move_to_register.toString()) {
						    "w" -> actor.autoMsg("plannerCmd", "plannerCmd(i)")
						    "a" -> actor.autoMsg("plannerCmd", "plannerCmd(l)")
							"s" -> actor.autoMsg("plannerCmd", "plannerCmd(h)")
							"d" -> actor.autoMsg("plannerCmd", "plannerCmd(r)")
						}
					}else if(goals_step.elementAt(0) is Task){
						var action: String = (goals_step.elementAt(0) as Task).action
						actor.autoMsg("plannerTask", "plannerTask(\"" + action + "\")")
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
		GlobalScope.launch(){
			if(cmd.equals("msg(v)", true)){//apparecchia...(prepare)
				goals_step = mutableListOf(Goal.PANTRY, Task("take_default_initialization_dish"),
					 Goal.TABLE, Task("put_food_on_robot_hands_on_table"),
					 Goal.FRIDGE, Task("take_default_initialization_food"),
					 Goal.TABLE, Task("put_food_on_robot_hands_on_table"),
					 Goal.HR)
				aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.getDirection(), goals_step.elementAt(0) as Goal);//prendo i piatti
				
				var foundPath: Boolean = false;
				aiutil.doPlan()?.let{actions = it; foundPath = true}
				while(!foundPath){
					RoomMap.getRoomMap().resetObstaclesOnMap();
					aiutil.doPlan()?.let{actions = it; foundPath = true}
				}
				aiutil.showMap();
				requestNextMove();
			}
			else if(cmd.startsWith("msg(a_", true)){//add food
				
				var payload: String = cmd.substringAfter("_")
				payload = payload.substringBefore(")")
				println(payload)
				var foodCode: String = payload.substringBefore("_")
				var foodQt: String = payload.substringAfter("_")
				
				goals_step = mutableListOf(Goal.FRIDGE, Task("get_food_from_fridge_" + payload),
					 Goal.TABLE, Task("put_food_on_robot_hands_on_table"),
					 Goal.HR)
				aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.getDirection(), goals_step.elementAt(0) as Goal);//prendo i piatti
				var foundPath: Boolean = false;
				aiutil.doPlan()?.let{actions = it; foundPath = true}
				while(!foundPath){
					RoomMap.getRoomMap().resetObstaclesOnMap();
					aiutil.doPlan()?.let{actions = it; foundPath = true}
				}
				aiutil.showMap();
				requestNextMove();
			}
			if(cmd.equals("msg(c)", true)){//sparecchia
				goals_step = mutableListOf(Goal.TABLE, Task("take_all_dishes_from_table"),
					Goal.DISHWASHER, Task("put_all_dishes_on_dishwasher"),
					Goal.TABLE, Task("take_all_food_from_table"),
					Goal.FRIDGE, Task("put_all_food_on_fridge"),
					Goal.HR)
				aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.getDirection(), goals_step.elementAt(0) as Goal);//prendo i piatti
				
				var foundPath: Boolean = false;
				aiutil.doPlan()?.let{actions = it; foundPath = true}
				while(!foundPath){
					RoomMap.getRoomMap().resetObstaclesOnMap();
					aiutil.doPlan()?.let{actions = it; foundPath = true}
				}
				aiutil.showMap();
				requestNextMove();
			}
		}
	}
	
	
	/*OLD FUNCTION USED WHEN WAS THE PLANNER TO DECIDE IF START OR NOT...
 	THAN WE CHANGED THE LOGIC....
	fun evaluate_food_availability(food : String){
		GlobalScope.launch(){
			if(food.substringAfter("Tipo :").substringBefore(" Payload").trim().equals("TYPE_RESPONSE_ID")){
				var payload: String = food.split("(?<=Payload)".toRegex())[1];
				payload = payload.substringAfter(":")
				payload = payload.substringBefore(")")
				if(payload.equals("yes"))
					actor.emit("foodAvailable", "foodAvailable(ok)")
				else
					actor.emit("foodUnavailable", "foodUnavailable(\"cibo richiesto non presente nel frigo\")")
			}
			else if(food.substringAfter("Tipo :").substringBefore(" Payload").trim().equals("TYPE_RESPONSE_FOOD_LIST")){
				//decide, in base al parametro String e alla richiesta del food fatta, se sparare un
				//foodAvailable o un
				//foodUnavailable (tutti unavailable quindi non si procede con le mosse)
				var payload: String = food.split("(?<=Payload)".toRegex())[1];
		        var token: StringTokenizer = StringTokenizer(payload);
		        payload = token.nextToken(")");
		        token = StringTokenizer(payload);
		
				var availableFood: MutableList<FoodInFridge> = ArrayList<FoodInFridge>()
				
		        var internalTokenizer: StringTokenizer = StringTokenizer((token.nextToken(";")));
		        var foodCode: String = internalTokenizer.nextToken(",");
		        var qt: String = internalTokenizer.nextToken(",");
		        var description: String = internalTokenizer.nextToken(",");
				var food: FoodInFridge = FoodInFridge(Food(foodCode, description), Integer.parseInt(qt))
				availableFood.add(food)
		        
		
		        var actualToken: String = "";
		        while(token.hasMoreTokens()){
		            internalTokenizer = StringTokenizer(token.nextToken(";"));
		            foodCode = internalTokenizer.nextToken(",");
		            qt = internalTokenizer.nextToken(",");
		            description = internalTokenizer.nextToken(",");
		            var food: FoodInFridge = FoodInFridge(Food(foodCode, description), Integer.parseInt(qt))
		            availableFood.add(food)
		        }
				
				var foundAlmenoOneFood = false
				var foundFood = false
				
				if(foodRequestedList.isEmpty())
					foundAlmenoOneFood = true
				
				foodRequestedList.iterator().forEach{ foodRequested ->
					foundFood = false
					availableFood.iterator().forEach{ foodAvailable ->
						if(foodAvailable.getFood().getFoodId().equals(foodRequested.getFood().getFoodId(), true)
						&& foodAvailable.getQuantity() >= foodRequested.getQuantity())
							foundFood = true
					}
					if(foundFood)
						foundAlmenoOneFood = true
				}
				
				if(foundAlmenoOneFood){
					actor!!.emit("foodAvailable", "foodAvailable(ok)")
				}else{
					actor.emit("foodUnavailable", "foodUnavailable(ok)")
				}
			}
		}
	}
 	*/
}