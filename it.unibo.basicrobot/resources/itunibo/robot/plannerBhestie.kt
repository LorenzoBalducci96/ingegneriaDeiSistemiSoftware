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
	
	fun create( actor: ActorBasic/*, PANTRY_X: String, PANTRY_Y: String,
   				TABLE_X: String, TABLE_Y: String,
   				DISHWASHER_X: String, DISHWASHER_Y: String,
   				FRIDGE_X: String, FRIDGE_Y: String*/){
		
		foodRequestedList = ArrayList<FoodInFridge>()
		
		this.actor = actor

		var TABLE_X: String = "2";
		var TABLE_Y: String = "2";
		var DISHWASHER_X: String = "4";
		var DISHWASHER_Y: String = "4";
		var FRIDGE_X: String = "0";
		var FRIDGE_Y: String = "2";
		var PANTRY_X: String = "4";
		var PANTRY_Y: String = "0";
		
		RoomMap.getRoomMap().put(Integer.parseInt(TABLE_X),Integer.parseInt(TABLE_Y),Box.createTable());
		RoomMap.getRoomMap().put(Integer.parseInt(DISHWASHER_X),Integer.parseInt(DISHWASHER_Y),Box.createDishwasher());
		RoomMap.getRoomMap().put(Integer.parseInt(FRIDGE_X),Integer.parseInt(FRIDGE_Y),Box.createFridge());
		RoomMap.getRoomMap().put(Integer.parseInt(PANTRY_X),Integer.parseInt(PANTRY_Y),Box.createPantry());
		
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
					if(goals_step.elementAt(0) is Goal){
						aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.getDirection(), goals_step.elementAt(0) as Goal);
						actions = aiutil.doPlan()
						move_to_register = actions.removeAt(0)
						when (move_to_register.toString()) {
						    "w" -> actor.autoMsg("plannerCmd", "plannerCmd(i)")
						    "a" -> actor.autoMsg("plannerCmd", "plannerCmd(l)")
							"s" -> actor.autoMsg("plannerCmd", "plannerCmd(h)")
							"d" -> actor.autoMsg("plannerCmd", "plannerCmd(r)")
						}
					}else if(goals_step.elementAt(0) is Task){
						var action: String = (goals_step.elementAt(0) as Task).action
						actor.autoMsg("plannerTask", "plannerTask(" + action + ")")
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
				actions = aiutil.doPlan()
				aiutil.showMap();
				
				actor.autoMsg("ackMsg", "ackMsg(ok)")
				
				
				//opzione 1: il planning deve vedere se il fridge ha o meno il food
				//actor.autoMsg("fridgeRequest", "fridgeRequest(l)")
				
				//opzione 2: il planning non richiede di passare dal fridge
				//actor!!.autoMsg("foodAvailable", "foodAvailable(ok)")
				
				//Da inserire negli stati che richiedono cibo da fridge
				//----------------------------------
			}
			else if(cmd.startsWith("msg(a:", true)){//add food
				
				var payload: String = cmd.substringAfter(":")
				payload = payload.substringBefore(")")
				println(payload)
				var foodCode: String = payload.substringBefore(",")
				var foodQt: String = payload.substringAfter(",")
				
				goals_step = mutableListOf(Goal.FRIDGE, Task("get_food_from_fridge(" + payload + ")"),
					 Goal.TABLE, Task("put_food_on_robot_hands_on_table"),
					 Goal.HR)
				aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.getDirection(), goals_step.elementAt(0) as Goal);//prendo i piatti
				actions = aiutil.doPlan()
				aiutil.showMap();
				
				actor.autoMsg("fridgeRequest", "fridgeRequest(\"r:" + foodCode + "," + foodQt + "\")")
				
				
				//opzione 1: il planning deve vedere se il fridge ha o meno il food
				//actor.autoMsg("fridgeRequest", "fridgeRequest(l)")
				
				//opzione 2: il planning non richiede di passare dal fridge
				//actor!!.autoMsg("foodAvailable", "foodAvailable(ok)")
				
				//Da inserire negli stati che richiedono cibo da fridge
				//----------------------------------
			}
			if(cmd.equals("msg(c)", true)){//sparecchia
				goals_step = mutableListOf(Goal.TABLE, Goal.DISHWASHER)
				aiutil.initFromToAI(aiutil.initialState.getX(), aiutil.initialState.getY(), aiutil.initialState.getDirection(), goals_step.elementAt(0) as Goal);//prendo i piatti
				actions = aiutil.doPlan()
				aiutil.showMap();
				
				//opzione 1: il planning deve vedere se il fridge ha o meno il food
				actor.autoMsg("fridgeRequest", "fridgeRequest(l)")
				
				//opzione 2: il planning non richiede di passare dal fridge
				//actor!!.autoMsg("foodAvailable", "foodAvailable(ok)")
				
				//Da inserire negli stati che richiedono cibo da fridge
				//----------------------------------
			}
		}
	}
	
	fun evaluate_food_availability(food : String){
		GlobalScope.launch(){
			if(food.substringAfter("Tipo :").substringBefore(" Payload").trim().equals("TYPE_RESPONSE_ID")){
				var payload: String = food.split("(?<=Payload)".toRegex())[1];
				payload = payload.substringAfter(":")
				payload = payload.substringBefore(")")
				if(payload.equals("yes"))
					actor.emit("foodAvailable", "foodAvailable(ok)")
				else
					actor.emit("foodUnavailable", "foodUnavailable(ok)")
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
}