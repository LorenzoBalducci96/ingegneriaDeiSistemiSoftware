package it.unibo.exploremap.stella.model;

import java.util.HashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.ResultFunction;
import aima.core.search.framework.problem.StepCostFunction;
import it.unibo.exploremap.stella.model.RobotState.Direction;
import it.unibo.exploremap.stella.model.RobotState.Goal;

public class Functions implements ActionsFunction, ResultFunction, StepCostFunction, GoalTest {
	public static final double MOVECOST = 1.0;
	public static final double TURNCOST = 1.0;

	@Override
	public double c(Object arg0, Action arg1, Object arg2) {
		RobotAction action = (RobotAction) arg1;
		if (action.getAction() == RobotAction.FORWARD || action.getAction() == RobotAction.BACKWARD)
			return MOVECOST;
		else
			return TURNCOST;
	}

	@Override
	public Object result(Object arg0, Action arg1) {
		RobotState state = (RobotState) arg0;
		RobotAction action = (RobotAction) arg1;
		RobotState result;
		
		switch(action.getAction()) {
		case RobotAction.FORWARD:   result = state.forward(); break;
		case RobotAction.BACKWARD:  result = state.backward(); break;
		case RobotAction.TURNLEFT:  result = state.turnLeft(); break;
		case RobotAction.TURNRIGHT: result = state.turnRight(); break;
		default: throw new IllegalArgumentException("Not a valid RobotAction");
		}
		return result;
	}

	@Override
	public Set<Action> actions(Object arg0) {
		RobotState state = (RobotState) arg0;
		Set<Action> result = new HashSet<>();
		
		result.add(new RobotAction(RobotAction.TURNLEFT));
		result.add(new RobotAction(RobotAction.TURNRIGHT));
		
		if (RoomMap.getRoomMap().canMove(state.getX(), state.getY(), state.getDirection()))
			result.add(new RobotAction(RobotAction.FORWARD));
		
		return result;
	}

	@Override
	public boolean isGoalState(Object arg0) {
		RobotState state = (RobotState) arg0;
		Goal goal = state.getGoal();
		
		switch (goal) {
		case TABLE:
			if((RoomMap.getRoomMap().isSouthToTable(state.getX(), state.getY()) && state.getDirection() == Direction.UP) ||
					(RoomMap.getRoomMap().isNorthToTable(state.getX(), state.getY()) && state.getDirection() == Direction.DOWN) ||
					(RoomMap.getRoomMap().isRightToTable(state.getX(), state.getY()) && state.getDirection() == Direction.LEFT) ||
					(RoomMap.getRoomMap().isLeftToTable(state.getX(), state.getY()) && state.getDirection() == Direction.RIGHT))
				return true;
			break;
		case PANTRY:
			if(RoomMap.getRoomMap().isPantry(state.getX(), state.getY()))
				return true;
			break;
		case DISHWASHER:
			if(RoomMap.getRoomMap().isDishwasher(state.getX(), state.getY()))
				return true;
			break;
		case FRIDGE:
			if(RoomMap.getRoomMap().isFridge(state.getX(), state.getY()))
				return true;
			break;
		case HR:
			if(RoomMap.getRoomMap().isHR(state.getX(), state.getY()))
				return true;
			break;
		default:
			break;
		}

		return false;
		/*if (RoomMap.getRoomMap().isDirty(state.getX(), state.getY()) &&
				!RoomMap.getRoomMap().isObstacle(state.getX(), state.getY()))
			return true;
		else
			return false;*/
	}

}
