package itunibo.fridgeIOT;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
	List<FoodInFridge> listaCibi = new ArrayList<>();
	String line = null;
	try {
		BufferedReader reader = new BufferedReader(new FileReader("C:\\SERVER_DATA\\cibo.txt"));
		while((line = reader.readLine()) != null) {
			/**/
			String[] pezzi = line.split(",");
			Food f = new Food(pezzi[0], pezzi[1]);
			FoodInFridge fInFridge = new FoodInFridge(f, Integer.parseInt(pezzi[2]));
			listaCibi.add(fInFridge);
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	/*
	listaCibi.add(new FoodInFridge(new Food("123","Osso di Pollo"),5));
	listaCibi.add(new FoodInFridge(new Food("182","Carne putrefatta"),1));
	listaCibi.add(new FoodInFridge(new Food("164","Arancia fritta"),2));
	listaCibi.add(new FoodInFridge(new Food("170","Pesce gatto"),4));
	listaCibi.add(new FoodInFridge(new Food("192","Fusilloni"),2));
	listaCibi.add(new FoodInFridge(new Food("152","Raguttino"),3));
	*/
	
	
	FridgeState contenuto = new FridgeState(listaCibi);
	//contenuto.addFood("104","Potente concentrato");
	//contenuto.addFood("104","Potente concentrato");
	//contenuto.removeFood("164", 1);
	System.out.println(contenuto.getFormattedState());
	FridgeNetworkManager fridge = new FridgeNetworkManager(contenuto, 9876); 
	fridge.startListening();
	}

}
