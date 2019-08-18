package itunibo.robot;

import it.unibo.exploremap.stella.model.RobotState.Direction;

public class virtualRobotJavaState {
	private static Direction direction = Direction.UP;
	private static int x = 0;
	private static int y = 0;
	
	public static int getX() {
		return x;
	}
	
	public static int getY() {
		return y;
	}
	
	public static Direction getDirection() {
		return direction;
	}
	
	public static void updateState(String cmd) {
		if(cmd.equalsIgnoreCase("msg(i)")) {
			if(direction.equals(Direction.UP)) {
				y++;
			}
			if(direction.equals(Direction.RIGHT)) {
				x++;
			}
			if(direction.equals(Direction.LEFT)) {
				x--;
			}
			if(direction.equals(Direction.DOWN)) {
				y--;
			}
		}
		if(cmd.equalsIgnoreCase("msg(r)")) {
			if(direction.equals(Direction.UP)) {
				direction = Direction.RIGHT;
			}
			if(direction.equals(Direction.RIGHT)) {
				direction = Direction.DOWN;
			}
			if(direction.equals(Direction.LEFT)) {
				direction = Direction.UP;
			}
			if(direction.equals(Direction.DOWN)) {
				direction = Direction.LEFT ;
			}
		}
		if(cmd.equalsIgnoreCase("msg(l)")) {
			if(direction.equals(Direction.UP)) {
				direction = Direction.LEFT;
			}
			if(direction.equals(Direction.RIGHT)) {
				direction = Direction.UP;
			}
			if(direction.equals(Direction.LEFT)) {
				direction = Direction.DOWN;
			}
			if(direction.equals(Direction.DOWN)) {
				direction = direction.RIGHT;
			}
		}
		if(cmd.equalsIgnoreCase("msg(k)")) {
			if(direction.equals(Direction.UP)) {
				y--;
			}
			if(direction.equals(Direction.RIGHT)) {
				x--;
			}
			if(direction.equals(Direction.LEFT)) {
				x++;
			}
			if(direction.equals(Direction.DOWN)) {
				y++;
			}
		}
	}
	
	public static void printState() {
		System.out.println("x: " + x);
		System.out.println("y: " + y);
	}
}
