package IotFridge;

public class Food {
private String foodId;
private String description;

public Food(String foodId, String description) {
	this.foodId = foodId;
	this.description = description;
}


public String getFoodId() {
	return foodId;
}


public void setFoodId(String foodId) {
	this.foodId = foodId;
}


public String getDescription() {
	return description;
}


public void setDescription(String description) {
	this.description = description;
}


@Override
public String toString() {
	String food = "";
	food += "Id :" + foodId + " Description " + description;
	return food;
}



}
