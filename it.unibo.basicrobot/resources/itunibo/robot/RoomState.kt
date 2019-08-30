package itunibo.robot

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.ArrayList
import java.util.StringTokenizer
import itunibo.fridgeIOT.Food
import java.io.InputStream
import java.io.File

class RoomState {
	val foodOnTable: MutableList<FoodInRoom>? = ArrayList<FoodInRoom>()
	var cleanDishes: Int = 0
	var dishwasherDishes: Int = 0

			init {
			try {
				val inputStream : InputStream = File("C:\\Users\\Davide\\git\\ingegneriaDeiSistemiSoftware\\it.unibo.basicrobot\\roomstate.txt").inputStream()
						var line: String? = null

						val list : List<String> = inputStream.bufferedReader().useLines {
					lines: Sequence<String> -> lines.take(2).toList()
				}

				this.cleanDishes = Integer.parseInt(list.get(0))
						this.dishwasherDishes = Integer.parseInt(list.get(1))

						val lineList = mutableListOf<String>()

						inputStream.bufferedReader().useLines {
								lines -> lines.forEach {
							var st : StringTokenizer = StringTokenizer(it, ";")
							var id: String = st.nextToken()
							var description : String = st.nextToken()
							var quantity : Int = Integer.parseInt(st.nextToken())
							this.foodOnTable.add(FoodInRoom(Food(id, description), quantity))
								}
						}
				lineList.forEach{ println(">  " + it) }

			} catch (e: IOException) {
				e.printStackTrace()
			}

}


fun getFoodOnTable(): List<FoodInRoom>? {
	return foodOnTable
}


fun setFoodOnTable(foodOnTable: MutableList<FoodInRoom>) {
	this.foodOnTable = foodOnTable
}

fun updateRoomState(msg: String) {

}
}
