package IotFridge;

import java.util.Iterator;
import java.util.List;

public class FridgeState {
	private List<FoodInFridge> foodAvailables;

	public FridgeState(List<FoodInFridge> foodAvailables) {
		this.foodAvailables = foodAvailables;
	}

	public List<FoodInFridge> getFoodAvailables() {
		return foodAvailables;
	}

	public boolean isFoodAvailable(String code, int howMuch) {
		for(FoodInFridge f : foodAvailables) {
			if(f.getFood().getFoodId().equalsIgnoreCase(code) && f.isFoodAvaible(howMuch)) {
				return true;
			}
		}
		return false;
	}
	public int getFoodQuantity(String code) {
		int quantity = -1;
		for(FoodInFridge f : foodAvailables) {
			if(f.getFood().getFoodId().equalsIgnoreCase(code)) {
				return f.getQuantity();
			}
		}
		return quantity;
	}

	public boolean addFood(String code, String description) {
		for(FoodInFridge f : foodAvailables) {
			if(f.getFood().getFoodId().equalsIgnoreCase(code)) {
				f.setQuantity(f.getQuantity()+1);
				return true;
			}
		}
		//Cibo non presente in lista, lo aggiungo e setto la quantita' ad 1
		return foodAvailables.add(new FoodInFridge(new Food(code,description),1));
	}

	public boolean removeFood(String code, int howMuch) {
		for(FoodInFridge f : foodAvailables) {
			if(f.getFood().getFoodId().equalsIgnoreCase(code) && f.isFoodAvaible(howMuch)) {
				f.setQuantity(f.getQuantity()-howMuch);
				return true;
			}
		}
		return false;
	}
	public String getFormattedState() {
		String formattedString = "";
		Iterator <FoodInFridge> iterator = foodAvailables.iterator();
		while(iterator.hasNext()) {
		FoodInFridge current = iterator.next();
		if(iterator.hasNext())
		formattedString+= current.getFood().getFoodId() + "," + current.getQuantity()+ "," + current.getFood().getDescription() +";";
		else
		formattedString+= current.getFood().getFoodId() + "," + current.getQuantity()+ "," + current.getFood().getDescription();
		}
		return formattedString;
	}
}
