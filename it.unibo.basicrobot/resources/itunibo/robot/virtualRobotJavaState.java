package itunibo.robot;

public class virtualRobotJavaState {
	private static String orientation = "nord";
	private static int x = 0;
	private static int y = 0;
	
	public static int getX() {
		return x;
	}
	
	public static int getY() {
		return y;
	}
	
	public static String getOrientation() {
		return orientation;
	}
	
	public static void updateState(String cmd) {
		if(cmd.equalsIgnoreCase("msg(i)")) {
			if(orientation.equalsIgnoreCase("nord")) {
				y++;
			}
			if(orientation.equalsIgnoreCase("est")) {
				x++;
			}
			if(orientation.equalsIgnoreCase("ovest")) {
				x--;
			}
			if(orientation.equalsIgnoreCase("sud")) {
				y--;
			}
		}
		if(cmd.equalsIgnoreCase("msg(r)")) {
			if(orientation.equalsIgnoreCase("nord")) {
				orientation="est";
			}
			if(orientation.equalsIgnoreCase("est")) {
				orientation="sud";
			}
			if(orientation.equalsIgnoreCase("ovest")) {
				orientation="nord";
			}
			if(orientation.equalsIgnoreCase("sud")) {
				orientation="ovest";
			}
		}
		if(cmd.equalsIgnoreCase("msg(l)")) {
			if(orientation.equalsIgnoreCase("nord")) {
				orientation="ovest";
			}
			if(orientation.equalsIgnoreCase("est")) {
				orientation="nord";
			}
			if(orientation.equalsIgnoreCase("ovest")) {
				orientation="sud";
			}
			if(orientation.equalsIgnoreCase("sud")) {
				orientation="est";
			}
		}
		if(cmd.equalsIgnoreCase("msg(k)")) {
			if(orientation.equalsIgnoreCase("nord")) {
				y--;
			}
			if(orientation.equalsIgnoreCase("est")) {
				x--;
			}
			if(orientation.equalsIgnoreCase("ovest")) {
				x++;
			}
			if(orientation.equalsIgnoreCase("sud")) {
				y++;
			}
		}
	}
	
	public static void printState() {
		System.out.println("x: " + x);
		System.out.println("y: " + y);
	}
}
