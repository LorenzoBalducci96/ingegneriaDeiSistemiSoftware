package IotFridge;

import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ClientTest {
	public static void main(String args[]) throws Exception
	{
		Gson gson = new Gson();
		DatagramSocket clientSocket = new DatagramSocket();
		byte [] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		InetAddress IPAddress = InetAddress.getByName("localhost");
		Comunication_Message message = new Comunication_Message(TYPE.TYPE_REQUEST_FOOD_LIST, "182,1"); //Codice,quanti SENZA SPAZI ne altro
		System.out.println("Converting Comunication_Message into GSON: " + message);
		String finalGson = gson.toJson(message);
		System.out.println("Final converted message: " + finalGson);
		sendData = finalGson.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
		clientSocket.send(sendPacket);
		System.out.println("Packet sent");

		//Preparo la ricezione della risposta
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String responseFromServer = new String(receivePacket.getData());
		System.out.println("RECEIVED: " + responseFromServer);
		//Converto la richiesta da stringa Gson a Comunication_Message
		JsonReader reader = new JsonReader(new StringReader(responseFromServer));
		reader.setLenient(true);
		Comunication_Message messageFromServer = gson.fromJson(reader, Comunication_Message.class);

		switch(messageFromServer.getId()) {
		case TYPE_REQUEST_ID:
			System.out.println("Error, Can't receive REQUEST type messages");
			break;

		case TYPE_REQUEST_FOOD_LIST:
			System.out.println("Error, Can't receive RESPONSE type messages");
			break;

		case TYPE_RESPONSE_ID:
			String payloadMessage = messageFromServer.getPayload();
			String[] splitted = payloadMessage.split(",");
			String foodId = splitted[0].trim();
			int howMuchAvaiable = Integer.parseInt(splitted[1].trim());
			//TODO FARE UN TYPE_RESPONSE_ID_YES o un TYPE_RESPONSE_ID_NO (sarebbe figo)
			System.out.println("Il server mi ha detto che il cibo "+ foodId + " E' presente in quantita' "+ howMuchAvaiable);
			break;

		case TYPE_RESPONSE_FOOD_LIST:
			List<FoodInFridge> oggettiNelFrigo = new ArrayList<>();
			String payloadListMessage = messageFromServer.getPayload();
			StringTokenizer st = new StringTokenizer(payloadListMessage, ";");
			//id1,qt1,descr1;id2,qt2,descr2;id3,qt3,descr3
			while (st.hasMoreTokens()) {  
				String currentFood = st.nextToken();
				String[] splittedFood = currentFood.split(",");
				String idFood = splittedFood[0].trim();
				int howMuch = Integer.parseInt(splittedFood[1].trim());
				String description = splittedFood[2];
				oggettiNelFrigo.add(new FoodInFridge(new Food(idFood, description), howMuch));
			}
			for(FoodInFridge f : oggettiNelFrigo) {
				System.out.println(f.getFood().toString() + " Disponibili : " + f.getQuantity());
			}
			break;

		default :
			System.out.println("Error, Comunication_Message ID not recognized");
			break;
		}
		clientSocket.close();
	}
}
