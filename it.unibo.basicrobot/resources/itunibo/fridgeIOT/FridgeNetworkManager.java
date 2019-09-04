package itunibo.fridgeIOT;

import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class FridgeNetworkManager {

	private FridgeState fridge;
	private int port;

	public FridgeNetworkManager(FridgeState fridge, int port) {
		this.port = port;
		this.fridge = fridge;
	}

	public void startListening() {
		try {
			DatagramSocket serverSocket = new DatagramSocket(port);
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			Gson gson = new Gson();
			while(true)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				System.out.println("Server: waiting for messages");
				serverSocket.receive(receivePacket);
				String requestFromClient = new String(receivePacket.getData());
				System.out.println("RECEIVED: " + requestFromClient);

				//Converto la richiesta da stringa Gson a Comunication_Message
				JsonReader reader = new JsonReader(new StringReader(requestFromClient));
				reader.setLenient(true);
				Comunication_Message message = gson.fromJson(reader, Comunication_Message.class); //safe

				switch(message.getId()) {
				case TYPE_REQUEST_ID:
					String payloadMessage = message.getPayload();
					String[] splitted = payloadMessage.split(",");
					String foodIdRequested = splitted[0].trim();
					int howMuch = Integer.parseInt(splitted[1].trim());

					if(fridge.isFoodAvailable(foodIdRequested, howMuch)) {
						System.out.println("Yes, avaiable in requested quantity");
						InetAddress IPAddress = receivePacket.getAddress();
						int port = receivePacket.getPort();
						Comunication_Message responseMessage = new Comunication_Message(TYPE.TYPE_RESPONSE_ID, "yes");
						String finalGson = gson.toJson(responseMessage);
						sendData = finalGson.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
						serverSocket.send(sendPacket);
						break;
					}
					else {
						System.out.println("No,not avaiable in requested quantity");
						InetAddress IPAddress = receivePacket.getAddress();
						int port = receivePacket.getPort();
						Comunication_Message responseMessage = new Comunication_Message(TYPE.TYPE_RESPONSE_ID, "no");
						String finalGson = gson.toJson(responseMessage);
						sendData = finalGson.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
						serverSocket.send(sendPacket);
						break;
					}
				case TYPE_REQUEST_FOOD_LIST:
					InetAddress IPAddress = receivePacket.getAddress();
					int port = receivePacket.getPort();
					Comunication_Message responseMessage = new Comunication_Message(TYPE.TYPE_RESPONSE_FOOD_LIST, fridge.getFormattedState());
					String finalGson = gson.toJson(responseMessage);
					sendData = finalGson.getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					serverSocket.send(sendPacket);
					System.out.println("Sent fridge state "+ finalGson);
					break;
					
				case TYPE_REMOVE_FOOD:
					payloadMessage = message.getPayload();
					splitted = payloadMessage.split(",");
					foodIdRequested = splitted[0].trim();
					howMuch = Integer.parseInt(splitted[1].trim());
					fridge.removeFood(foodIdRequested, howMuch);
					break;
					
				case TYPE_ADD_FOOD:
					payloadMessage = message.getPayload();
					splitted = payloadMessage.split(",");
					
					foodIdRequested = splitted[0].trim();
					String description = splitted[1].trim();
					howMuch = Integer.parseInt(splitted[2].trim());
					fridge.addFood(foodIdRequested,  description, howMuch);
					break;

				case TYPE_RESPONSE_ID:
					System.out.println("Error, Can't receive RESPONSE type messages");
					break;
				case TYPE_RESPONSE_FOOD_LIST:
					System.out.println("Error, Can't receive RESPONSE type messages");
					break;

				default :
					System.out.println("Error, Comunication_Message ID not recognized");
					break;
				}

			}
		} catch (IOException e) {e.printStackTrace();}
	}

}
