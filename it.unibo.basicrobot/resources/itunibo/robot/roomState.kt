package itunibo.robot

import it.unibo.kactor.ActorBasic
import java.io.InputStream
import java.io.File
import java.util.StringTokenizer
import itunibo.fridgeIOT.Food
import java.io.IOException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import itunibo.fridgeIOT.Comunication_Message

object roomState {
	lateinit var foodOnTable: MutableList<FoodInRoom>
	lateinit var foodInRobotHands: MutableList<FoodInRoom>
	
	var cleanDishesInPantry: Int = 0
	var dishwasherDishes: Int = 0
	
	var dishesInRobotHands = 0
	var dishesInTable = 0 //not updated...
	
	var defaultDishesInDishwasherToTake = 0;
	lateinit var defaultFoodToTakeFromFridge: MutableList<FoodInRoom>
	
	lateinit var actor: ActorBasic
	
	fun adddefaultFoodToTakeFromFridge(foodId: String, food_description: String, food_qt: String){
		this.defaultFoodToTakeFromFridge.add(FoodInRoom(Food(foodId, food_description.replace("'", "")),
			Integer.parseInt(food_qt)))
	}
	
	
	fun setdefaultDishwasherDishesToTake(defaultDishesInDishwasherToTake: String){
		this.defaultDishesInDishwasherToTake = Integer.parseInt(defaultDishesInDishwasherToTake)
	}
	
	fun setcleanDishesInPantry(cleanDishesInPantry: String){
		this.cleanDishesInPantry = Integer.parseInt(cleanDishesInPantry)
	}
	
	fun setdishwasherDishes(dishwasherDishes: String){
		this.dishwasherDishes = Integer.parseInt(dishwasherDishes)
	}

	fun create( actor: ActorBasic) {
		this.actor = actor
		foodOnTable = ArrayList<FoodInRoom>()
		foodInRobotHands = ArrayList<FoodInRoom>()
		defaultFoodToTakeFromFridge = ArrayList<FoodInRoom>()
		
		/*
 		use prolog in qak system!
		val file = File("/SERVER_DATA/roomstate.txt")
		//val file = File("C:\\SERVER_DATA\\roomstate.txt")
	    val bufferedReader = file.bufferedReader()
	    val text:List<String> = bufferedReader.readLines()
		var count: Int = 0;
		var iterator: Iterator<String> = text.iterator()
		this.cleanDishesInPantry = Integer.parseInt(iterator.next())
	    this.dishwasherDishes = Integer.parseInt(iterator.next())
		
		while(iterator.hasNext()){//da requisiti non sembra che il tavolo inizialmente sia riempito
			var pezzi: List<String> = iterator.next().split(",")
			
			var id: String = pezzi[0]
			var description : String = pezzi[1]
			var quantity : Int = Integer.parseInt(pezzi[2])
			this.foodOnTable!!.add(FoodInRoom(Food(id, description), quantity))
		}
	    */
	}
	
	fun addDish(where: String){
		when(where){
			"pantry" -> cleanDishesInPantry = cleanDishesInPantry + 1;
			"dishwasher" -> dishwasherDishes = dishwasherDishes + 1;
		}
	}
		
	fun removeDish(where: String){
		when(where){
			"pantry" -> cleanDishesInPantry = cleanDishesInPantry - 1;
			"dishwasher" -> dishwasherDishes = dishwasherDishes - 1;
		}
	}
	
	fun addFoodOnTable(foodId: String, food_description: String, food_qt: String){
		this.foodOnTable.add(FoodInRoom(Food(foodId, food_description.replace("'", "")),
			Integer.parseInt(food_qt)))
	}
	
	fun emitRoomState(){
		GlobalScope.launch{
		var msgPayload: String = ""
		msgPayload += "" + cleanDishesInPantry + ";"
		msgPayload += "" + dishwasherDishes + ";"
		foodOnTable.iterator().forEach {
			msgPayload += it.getFood().foodId + "," + it.getFood().description + "," + it.quantity + ";";
		}
		actor.autoMsg("roomStateEvent", "roomStateEvent(\"" + msgPayload + "\")")     
		}
	}
	
	fun updateState(cmd: String){
		GlobalScope.launch(){
			if(cmd.startsWith("take_default_initialization_dish")){
				var dishesNumToTake: Int = defaultDishesInDishwasherToTake
				if(dishwasherDishes >= dishesNumToTake){
					dishwasherDishes -= dishesNumToTake
					dishesInRobotHands = dishesNumToTake
				}else{
					dishesInRobotHands = dishwasherDishes
					dishwasherDishes = 0
				}
			}
			else if(cmd.startsWith("put_food_on_robot_hands_on_table")){
				dishesInTable += dishesInRobotHands;
				
				foodInRobotHands.iterator().forEach { theFoodInRobotHands ->
					var found: Boolean = false
					foodOnTable.iterator().forEach { theFoodOnTable ->
						if(theFoodOnTable.food.foodId.equals(theFoodInRobotHands.food.foodId)){
							found = true
							theFoodOnTable.quantity += theFoodInRobotHands.quantity
						}
					}
					if(!found){
						foodOnTable.add(theFoodInRobotHands)
					}
				}
			}
			else if(cmd.startsWith("take_default_initialization_food")){
				defaultFoodToTakeFromFridge.iterator().forEach {
					foodInRobotHands.add(it)
					actor.autoMsg("removeFromFridge",
						"removeFromFridge(" + it.food.getFoodId() + "," + it.quantity.toString() + ")")
				}
			}
			else if(cmd.startsWith("put_default_initialization_food")){
				println("ERROR: NO PREVISED STATE!")
				foodOnTable = foodInRobotHands
				foodInRobotHands.clear()
			}else if(cmd.startsWith("get_food_from_fridge")){
				//get_food_from_fridge(123,2)
				var foodCode: String = cmd.substringAfter("get_food_from_fridge_").substringBefore("_")
				var foodQt: Int = Integer.parseInt(cmd.substringAfter("get_food_from_fridge_").substringAfter("_"))
				foodInRobotHands.add(FoodInRoom(Food(foodCode), foodQt))
				actor.autoMsg("removeFromFridge",
						"removeFromFridge(\"" + foodCode + "\", \"" + foodQt.toString() + "\")")
			}else if(cmd.startsWith("take_all_dishes_from_table")){
				dishesInRobotHands = dishesInTable;
				dishesInTable = 0;
			}else if(cmd.startsWith("put_all_dishes_on_dishwasher")){
				dishwasherDishes = dishesInRobotHands
				dishesInRobotHands = 0;
			}else if(cmd.startsWith("take_all_food_from_table")){
				foodInRobotHands = foodOnTable.toMutableList();
				foodOnTable.clear();
			}else if(cmd.startsWith("put_all_food_on_fridge")){
				foodInRobotHands.iterator().forEach {
					actor.autoMsg("addToFridge",
						"addToFridge(\"" + it.food.foodId + "\",\"" + it.food.description + "\",\"" +  it.quantity.toString() + "\")")
				}
			}
		}
	}
}


