package itunibo.robot;

import itunibo.fridgeIOT.Food;

public class FoodInRoom {

	private Food food;
	private int quantity;

	public FoodInRoom(Food food, int quantity) {
		this.food = food;
		this.quantity = quantity;
	}

	public boolean isFoodAvaible(int howMuch) {
		return quantity >= howMuch ? true : false;
	}

	public Food getFood() {
		return food;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


}
