package IotFridge;

public class Comunication_Message{

private TYPE id;
private String payload;

public Comunication_Message(TYPE id, String payload) {
	this.id = id;
	this.payload = payload;
}

public TYPE getId() {
	return id;
}

public void setId(TYPE id) {
	this.id = id;
}

public String getPayload() {
	return payload;
}

public void setPayload(String payload) {
	this.payload = payload;
}

@Override
public String toString() {
	String stringona = "";
	stringona += "Tipo :" + this.id;
	stringona += " Payload :" + this.payload;
	return stringona;
}



}
