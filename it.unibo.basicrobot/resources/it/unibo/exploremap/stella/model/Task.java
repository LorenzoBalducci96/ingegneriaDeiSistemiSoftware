package it.unibo.exploremap.stella.model;

import it.unibo.exploremap.stella.model.RobotState.PlannerStep;

public class Task implements PlannerStep{
	public String action = "";//takeFood(melanzane, 5)
	public Task(String action) {
		this.action = action;
	}
}