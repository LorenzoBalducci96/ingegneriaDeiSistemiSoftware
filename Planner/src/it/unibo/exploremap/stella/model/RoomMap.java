package it.unibo.exploremap.stella.model;

public class RoomMap {
	private final static int ROOMSIZE = 5;
	private static RoomMap singletonRoomMap;
	
	public static RoomMap getRoomMap() {
		if (singletonRoomMap == null)
			singletonRoomMap = new RoomMap();
		return singletonRoomMap;
	}
	
	
	
	//private List<ArrayList<Box>> roomMap = new ArrayList<ArrayList<Box>>();
	private Box[][] roomMap;
	
	private RoomMap() {
		roomMap = new Box[ROOMSIZE][ROOMSIZE];
		
		for(int i = 0; i < ROOMSIZE; i++) {
			for(int j = 0; j < ROOMSIZE; j++) {
				roomMap[i][j] = Box.createNormalBox();
			}
		}
		
		roomMap[0][0] = Box.createRobot();
		roomMap[2][2] = Box.createTable();
		roomMap[4][4] = Box.createDishwasher();
		roomMap[0][4] = Box.createFridge();
		roomMap[4][0] = Box.createPantry();
	}
	
	public void put(int x, int y, Box box) {
		/*try {
			roomMap.get(y);
		} catch (IndexOutOfBoundsException e) {
			roomMap.get(y).add(x, box);
		} catch (IndexOutOfBoundsException e) {
			for (int j=roomMap.get(y).size(); j<x; j++) {
				roomMap.get(y).add(new Box(false, true, false));
				roomMap.get(y).add(Box.createNormalBox());
			}
			roomMap.get(y).add(x, box);
		}*/
		
		if(x < 0 || x > ROOMSIZE || y < 0 || y > ROOMSIZE)
			return;
		
		roomMap[x][y] = box;
	}
	
	public boolean isObstacle(int x, int y) {
		try {
			Box box = roomMap[x][y];
			//System.out.println(" ... RoomMap  isObstacle " + box.isObstacle());
			if  (box == null)
				return false;
			if (box.isObstacle())
				return true;
			else
				return false;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public boolean isTable(int x, int y) {
		try {
			Box box = roomMap[x][y];
			if  (box == null)
				return true;
			if (box.isTable())
				return true;
			else
				return false;
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}
	
	public boolean isFridge(int x, int y) {
		try {
			Box box = roomMap[x][y];
			if  (box == null)
				return true;
			if (box.isFridge())
				return true;
			else
				return false;
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}
	
	public boolean isDishwasher(int x, int y) {
		try {
			Box box = roomMap[x][y];
			if  (box == null)
				return true;
			if (box.isDishwaser())
				return true;
			else
				return false;
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}
	
	public boolean isPantry(int x, int y) {
		try {
			Box box = roomMap[x][y];
			if  (box == null)
				return true;
			if (box.isPantry())
				return true;
			else
				return false;
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}

	public boolean isHR(int x, int y) {
		try {
			Box box = roomMap[x][y];
			if  (box == null)
				return true;
			if (x == 0 && y == 0)
				return true;
			else
				return false;
		} catch (IndexOutOfBoundsException e) {
			return true;
		}
	}
	
	public boolean canMove(int x, int y, RobotState.Direction direction) {
		switch (direction) {
		case UP: return canMoveUp(x, y);
		case RIGHT: return canMoveRight(x, y);
		case DOWN: return canMoveDown(x, y);
		case LEFT: return canMoveLeft(x, y);
		default: throw new IllegalArgumentException("Not a valid direction");
		}
	}
	
	public boolean canMoveUp(int x, int y) {
		if (y<=0)
			return false;
		try {
			Box box = roomMap[x][y-1];
			if  (box == null)
				return true;
			if (box.isObstacle())
				return false;
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public boolean canMoveRight(int x, int y) {
		try {
			Box box = roomMap[x+1][y];
			if  (box == null)
				return true;
			if (box.isObstacle())
				return false;
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public boolean canMoveDown(int x, int y) {
		try {
			Box box = roomMap[x][y+1];
			if  (box == null)
				return true;
			if (box.isObstacle())
				return false;
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	public boolean canMoveLeft(int x, int y) {
		if (x<=0)
			return false;
		try {
			Box box = roomMap[x-1][y];
			if  (box == null)
				return true;
			if (box.isObstacle())
				return false;
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Box box;
		
		for(int i = 0; i < ROOMSIZE; i++) {
			for(int j = 0; j < ROOMSIZE; j++) {
				box = roomMap[i][j];
				if (box == null)
					break;
				if (box.isRobot())
					builder.append("r, ");
				else if(box.isTable())
					builder.append("T, ");
				else if (box.isObstacle())
					builder.append("X, ");
				else if(box.isDishwaser())
					builder.append("D, ");
				else if(box.isPantry())
					builder.append("P, ");
				else if(box.isFridge())
					builder.append("F, ");
				else
					builder.append("0, ");
			}
			builder.append("\n");
		}
		return builder.toString();
	}
	
	public int getDimX() {
		int result=0;
		for (int i=0; i < ROOMSIZE; i++) {
			if (result < roomMap.length)
				result = roomMap[i].length;
		}
		return result;
	}
	
	public int getDimY() {
		return roomMap.length;
	}
	
}