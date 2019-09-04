package it.unibo.exploremap.stella.model;

public class Box {
	private boolean isObstacle;
	private boolean isTable;
	private boolean isPantry;
	private boolean isFridge;
	private boolean isDishwaser;
	private boolean isRobot;
	private boolean isNormalBox;
	
	//Costruttore completo
	private Box(boolean isObstacle, boolean isTable, boolean isPantry, boolean isFridge, boolean isDishwaser,
			boolean isRobot) {
		this.isObstacle = isObstacle;
		this.isTable = isTable;
		this.isPantry = isPantry;
		this.isFridge = isFridge;
		this.isDishwaser = isDishwaser;
		this.isRobot = isRobot;
		if(!isObstacle && !isTable && !isPantry && ! isFridge && !isDishwaser && !isRobot)
			this.isNormalBox = true;
	}

	//Casella dishwasher
	public static Box createDishwasher() {
		return new Box(false, false, false, false, true, false);
	}

	//Casella fridge
	public static Box createFridge() {
		return new Box(false, false, false, true, false, false);
	}

	//Casella pantry
	public static Box createPantry() {
		return new Box(false, false, true, false, false, false);
	}

	//Casella table
	public static Box createTable() {
		return new Box(true, true, false, false, false, false);
	}
	
	//Casella robot
	public static Box createRobot() {
		return new Box(false, false, false, false, false, true);
	}
	
	//Casella ostacolo
	public static Box createObstacle() {
		return new Box(true, false, false, false, false, false);
	}
	
	//Casella normale
	public static Box createNormalBox() {
		return new Box(false, false, false, false, false, false);
	}
	
	public boolean isNormalBox() {
		return this.isNormalBox;
	}
	
	public void setRobot(boolean isRobot) {
		this.isRobot = isRobot;
	}
	
	public boolean isRobot() {
		return this.isRobot;
	}
	
	public boolean isObstacle() {
		return this.isObstacle;
	}
	
	public void setObstacle(boolean isObstacle) {
		this.isObstacle = isObstacle;
	}

	public boolean isTable() {
		return isTable;
	}

	public void setTable(boolean isTable) {
		this.isTable = isTable;
	}

	public boolean isPantry() {
		return isPantry;
	}

	public void setPantry(boolean isPantry) {
		this.isPantry = isPantry;
	}

	public boolean isFridge() {
		return isFridge;
	}

	public void setFridge(boolean isFridge) {
		this.isFridge = isFridge;
	}

	public boolean isDishwaser() {
		return isDishwaser;
	}

	public void setDishwaser(boolean isDishwaser) {
		this.isDishwaser = isDishwaser;
	}
}
