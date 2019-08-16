package it.unibo.planner;

import java.util.Iterator;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.SearchAgent;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import it.unibo.exploremap.program.aiutil;
import it.unibo.exploremap.stella.model.Functions;
import it.unibo.exploremap.stella.model.RobotState;
import it.unibo.exploremap.stella.model.RobotState.Direction;
import it.unibo.exploremap.stella.model.RobotState.Goal;

public class Main {

	private static RobotState initialState;
	private static BreadthFirstSearch search ;

	public void demo() {
		System.out.println("===== demo");
		try {
//			aiutil.initFromToAI(3, 2, Direction.LEFT, Goal.HR);
			aiutil.initFromToAI(2, 3, Direction.DOWN, Goal.TABLE);
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
