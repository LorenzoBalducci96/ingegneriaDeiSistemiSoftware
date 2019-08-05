package IotFridge;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
	List<FoodInFridge> listaCibi = new ArrayList<>();
	listaCibi.add(new FoodInFridge(new Food("123","Osso di Pollo"),5));
	listaCibi.add(new FoodInFridge(new Food("182","Carne putrefatta"),1));
	listaCibi.add(new FoodInFridge(new Food("164","Arancia fritta"),2));
	listaCibi.add(new FoodInFridge(new Food("170","Pesce gatto"),4));
	listaCibi.add(new FoodInFridge(new Food("192","Fusilloni"),2));
	listaCibi.add(new FoodInFridge(new Food("152","Raguttino"),3));
	
	FridgeState contenuto = new FridgeState(listaCibi);
	contenuto.addFood("104","Potente concentrato");
	contenuto.addFood("104","Potente concentrato");
	contenuto.removeFood("164", 1);
	FridgeNetworkManager fridge = new FridgeNetworkManager(contenuto, 9876); 
	fridge.startListening();
	}

}
