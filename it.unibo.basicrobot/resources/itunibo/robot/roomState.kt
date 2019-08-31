package itunibo.robot

import it.unibo.kactor.ActorBasic
import java.io.InputStream
import java.io.File
import java.util.StringTokenizer
import itunibo.fridgeIOT.Food
import java.io.IOException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object roomState {
	lateinit var foodOnTable: MutableList<FoodInRoom>
	var cleanDishesInPantry: Int = 0
	var dishwasherDishes: Int = 0
	lateinit var actor: ActorBasic

	fun create( actor: ActorBasic) {
		this.actor = actor
		foodOnTable = ArrayList<FoodInRoom>()
		
		val file = File("C:\\SERVER_DATA\\roomstate.txt")
	    val bufferedReader = file.bufferedReader()
	    val text:List<String> = bufferedReader.readLines()
		var count: Int = 0;
		var iterator: Iterator<String> = text.iterator()
		this.cleanDishesInPantry = Integer.parseInt(iterator.next())
	    this.dishwasherDishes = Integer.parseInt(iterator.next())
		
		while(iterator.hasNext()){
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
}