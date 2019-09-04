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
	
	lateinit var actor: ActorBasic

	fun create( actor: ActorBasic) {
		this.actor = actor
		foodOnTable = ArrayList<FoodInRoom>()
		foodInRobotHands = ArrayList<FoodInRoom>()
		
		val file = File("C:\\SERVER_DATA\\roomstate.txt")
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
		
		/*
		try {
			val inputStream : InputStream = File("C:\\SERVER_DATA\\roomstate.txt").inputStream()
					var line: String? = null

					val list : List<String> = inputStream.bufferedReader().useLines {
				lines: Sequence<String> -> lines.take(2).toList()
			}
			
			this.cleanDishesInPantry = Integer.parseInt(list.get(0))
					this.dishwasherDishes = Integer.parseInt(list.get(1))

					val lineList = mutableListOf<String>()

					inputStream.bufferedReader().useLines {
							lines -> lines.forEach {
						var st : StringTokenizer = StringTokenizer(it, ";")
						var id: String = st.nextToken()
						var description : String = st.nextToken()
						var quantity : Int = Integer.parseInt(st.nextToken())
						this.foodOnTable!!.add(FoodInRoom(Food(id, description), quantity))
							}
					}
			lineList.forEach{ println(">  " + it) }

		} catch (e: IOException) {
			e.printStackTrace()
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
	
	fun addFoodOnTable(food: String){
		
	}
	
	fun emitRoomState(){
		GlobalScope.launch{
		var msgPayload: String = ""
		msgPayload += "" + cleanDishesInPantry + ";"
		msgPayload += "" + dishwasherDishes + ";"
		foodOnTable.iterator().forEach {
			println("sono dentro il ciclo")
			msgPayload += it.getFood().foodId + "," + it.getFood().description + "," + it.quantity + ";";
		}
		actor.autoMsg("roomStateEvent", "roomStateEvent(\"" + msgPayload + "\")")     
		}
	}
	
	fun updateState(cmd: String){
		if(cmd.startsWith("take_default_initialization_dish")){
			var file = File("C:\\SERVER_DATA\\default_room_take.txt")
			var bufferedReader = file.bufferedReader()
			var text:String = bufferedReader.readLine()
			var dishesNumToTake: Int = Integer.parseInt(text)
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
			var file = File("C:\\SERVER_DATA\\default_room_take.txt")
			var bufferedReader = file.bufferedReader()
			bufferedReader.readLine()//leggo l'int iniziale che codifica il numero di piatti da prendere
			var text:List<String> = bufferedReader.readLines()//leggo tutti i food da prendere
			var iterator: Iterator<String> = text.iterator()
			
			while(iterator.hasNext()){
				var pezzi: List<String> = iterator.next().split(",")
				var id: String = pezzi[0]
				var description : String = pezzi[1]
				var quantity : Int = Integer.parseInt(pezzi[2])
				this.foodInRobotHands!!.add(FoodInRoom(Food(id, description), quantity))
			}
		}
		else if(cmd.startsWith("put_default_initialization_food")){
			foodOnTable = foodInRobotHands
			foodInRobotHands.clear()
		}else if(cmd.startsWith("get_food_from_fridge")){
			//get_food_from_fridge(123,2)
			var foodCode: String = cmd.substringAfter("get_food_from_fridge(").substringBefore(",")
			var foodQt: Int = Integer.parseInt(cmd.substringAfter(",").substringBefore(")"))
			foodInRobotHands.add(FoodInRoom(Food(foodCode), foodQt))
			itunibo.comunicationMessageClient.comunicationMessageClient.removeFromFridge(foodCode, foodQt)
		}
	}
}


