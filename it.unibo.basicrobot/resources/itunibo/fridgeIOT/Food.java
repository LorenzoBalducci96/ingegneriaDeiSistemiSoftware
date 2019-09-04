package itunibo.fridgeIOT;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Food {
private String foodId;
private String description;

public Food(String foodId, String description) {
	this.foodId = foodId;
	this.description = description;
}

public Food(String foodId) {
	this.foodId = foodId;
	this.description = "";
	String line = null;
	try {
		BufferedReader reader = new BufferedReader(new FileReader("C:\\SERVER_DATA\\knoledgebase_descriptions.txt"));
		while((line = reader.readLine()) != null) {
			String[] pezzi = line.split(",");
			if(pezzi[0].equalsIgnoreCase(foodId))
				description = pezzi[1];
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
