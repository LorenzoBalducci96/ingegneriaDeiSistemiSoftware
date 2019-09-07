package itunibo.comunicationMessageClient
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket
import java.net.SocketException
import java.net.UnknownHostException

import com.google.common.net.InetAddresses

import it.unibo.kactor.ActorBasic
import com.google.gson.Gson

import itunibo.fridgeIOT.Comunication_Message;
import itunibo.fridgeIOT.TYPE;
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.google.gson.stream.JsonReader
import java.io.StringReader

object comunicationMessageClient {
    private var clientSocket: DatagramSocket? = null
    private val singletonClient: comunicationMessageClient? = null
    private var sendData: ByteArray? = null
    private var recvData: ByteArray? = null
    private var ipAddr: InetAddress? = null
    private var port: Int = 0
    private var actor: ActorBasic? = null
	//private var receivedComunicationMessage: Comunication_Message? = null


    fun init(actor: ActorBasic) {
        comunicationMessageClient.actor = actor
        try {
            comunicationMessageClient.clientSocket = DatagramSocket()
            comunicationMessageClient.sendData = ByteArray(1024)
            comunicationMessageClient.recvData = ByteArray(1024)

            ipAddr = InetAddress.getByName("localhost")
            port = 9876

        } catch (e: SocketException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }

    }


    fun requestFoodList(cmd: String) {
		GlobalScope.launch {
			println(cmd)
	        if (cmd.equals("msg(l)", ignoreCase = true)) {
	            var gson: Gson = Gson();
	            var message: Comunication_Message = Comunication_Message(TYPE.TYPE_REQUEST_FOOD_LIST, "");
	            val messageGson = gson.toJson(message);
	            sendData = messageGson.toByteArray()
	            try {
	                clientSocket!!.send(DatagramPacket(sendData!!, sendData!!.size, ipAddr, port))
	            } catch (e: IOException) {
	                // TODO Auto-generated catch block
	                e.printStackTrace()
	            }
				var ricezione: DatagramPacket = DatagramPacket(recvData, recvData!!.size)
				clientSocket!!.receive(ricezione);
				
				var receivedMessage: String = String(ricezione.getData())
				var reader: JsonReader = JsonReader(StringReader(receivedMessage))
				reader.setLenient(true)
				
				
				var comunicationMessage: Comunication_Message =
					gson.fromJson(reader, Comunication_Message::class.java)
				
				//var formatted: String = comunicationMessage.toString().replace(":","")
				//formatted = formatted.replace(" ", "-")
				//formatted = formatted.replace(",", "_")
				//formatted = formatted.replace(";", "_")
				//receivedComunicationMessage = comunicationMessage
				var msg: String = "recvFoodMsgEvent(" + comunicationMessage.toString() + ")";
				println(msg)
				
				//actor!!.autoMsg("recvFoodMsgEvent", "recvFoodMsgEvent(ok)")
				actor!!.autoMsg("recvFoodMsgEvent", "recvFoodMsgEvent(\"" + msg + "\")")     
	        }
			if (cmd.startsWith("msg(a", ignoreCase = true)) {
				//ricevo msg(r_123_2)
				var payload:String = cmd.substringAfter("_")
				payload = payload.substringBefore(")")
				var foodCode: String = payload.substringBefore("_")
				var foodQt: String = payload.substringAfter("_")
	            var gson: Gson = Gson();
	            var message: Comunication_Message = Comunication_Message(TYPE.TYPE_REQUEST_ID, foodCode + "," + foodQt);
				
	            val messageGson = gson.toJson(message);
	            sendData = messageGson.toByteArray()
	            try {
	                clientSocket!!.send(DatagramPacket(sendData!!, sendData!!.size, ipAddr, port))
	            } catch (e: IOException) {
	                // TODO Auto-generated catch block
	                e.printStackTrace()
	            }
				var ricezione: DatagramPacket = DatagramPacket(recvData, recvData!!.size)
				clientSocket!!.receive(ricezione);
				
				var receivedMessage: String = String(ricezione.getData())
				var reader: JsonReader = JsonReader(StringReader(receivedMessage))
				reader.setLenient(true)
				
				
				var comunicationMessage: Comunication_Message =
					gson.fromJson(reader, Comunication_Message::class.java)
				
				println(comunicationMessage.toString())
				
				if(comunicationMessage.toString().substringAfter("Payload").substringAfter(":").trim().equals("yes")){
					actor!!.autoMsg("foodAvailable", "foodAvailable(yes)")  
				}else{
					actor!!.autoMsg("foodUnavailable", "foodUnavailable(food_unavailable)")  
				}
	        }
		}
    }
	
	fun removeFromFridge(foodCode: String, foodQt: Int) {
		var message: Comunication_Message = Comunication_Message(TYPE.TYPE_REMOVE_FOOD, foodCode + "," + foodQt);
	    var gson: Gson = Gson();        
		val messageGson = gson.toJson(message);
        sendData = messageGson.toByteArray()
        try {
            clientSocket!!.send(DatagramPacket(sendData!!, sendData!!.size, ipAddr, port))
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
	}
	
	fun removeFromFridge(foodCode: String, foodQtStr: String) {
		removeFromFridge(foodCode, Integer.parseInt(foodQtStr))
	}
	
	fun addToFridge(foodCode: String, foodDescription: String, foodQt: Int) {
		var message: Comunication_Message = Comunication_Message(TYPE.TYPE_ADD_FOOD, foodCode + "," + foodDescription + "," + foodQt);
	    var gson: Gson = Gson();        
		val messageGson = gson.toJson(message);
        sendData = messageGson.toByteArray()
        try {
            clientSocket!!.send(DatagramPacket(sendData!!, sendData!!.size, ipAddr, port))
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
	}
	
	fun addToFridge(foodCode: String, foodDescription: String, foodQtString: String) {
		addToFridge(foodCode, foodDescription, Integer.parseInt(foodQtString))
	}
}







