package it.unibo.planner;

import java.util.Iterator;
import java.util.List;

import aima.core.agent.Action;
import it.unibo.exploremap.program.aiutil;

public class Main {
	public void demo() {
		System.out.println("===== demo");
		try {
			aiutil.initAI();
			//aiutil.cleanQa();
			System.out.println("===== initial map");
			aiutil.showMap();
			List<Action> actions = aiutil.doPlan();
			System.out.println("===== plan actions: " + actions);
			executeMoves( aiutil.doPlan() );
			System.out.println("===== map after plan");
			aiutil.showMap();
		} catch (Exception e) {
 			e.printStackTrace();
		}		
	}
	
	protected void doSomeMOve() throws Exception {
		aiutil.doMove("w");
		aiutil.doMove("a");
		aiutil.doMove("w");
		aiutil.doMove("w");
		aiutil.doMove("d");
		aiutil.doMove("w");
		aiutil.doMove("a");
		aiutil.doMove("obstacleOnRight");
	}
	
	
	protected void executeMoves(List<Action> actions) throws Exception {
		Iterator<Action> iter = actions.iterator();
		while( iter.hasNext() ) {
			aiutil.doMove(iter.next().toString());
		}
	}
	public static void main(String[] args) {
		new Main().demo();
	}
}
