package it.unibo.exploremap.stella.model;

public class Box {
	private boolean isObstacle;
	private boolean isTable;
	private boolean isPantry;
	private boolean isFridge;
	private boolean isDishwaser;
	private boolean isRobot;
	
	//Costruttore completo
	public Box(boolean isObstacle, boolean isTable, boolean isPantry, boolean isFridge, boolean isDishwaser,
			boolean isRobot) {
		this.isObstacle = isObstacle;
		this.isTable = isTable;
		this.isPantry = isPantry;
		this.isFridge = isFridge;
		this.isDishwaser = isDishwaser;
		this.isRobot = isRobot;
	}
	
	//Casella ostacolo
	public Box(boolean isObstacle) {
		this(isObstacle, false, false, false, false, false);
	}
	
	//Casella normale
	public Box() {
		this(false, false, false, false, false, false);
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
