/* Generated by AN DISI Unibo */ 
package it.unibo.planner

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Planner ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						solve("consult('roomConfiguration.pl')","") //set resVar	
						solve("pantry(PANTRY_X,PANTRY_Y)","") //set resVar	
						itunibo.robot.plannerBhestie.putPantry( getCurSol("PANTRY_X").toString(), getCurSol("PANTRY_Y").toString()  )
						solve("table(TABLE_X,TABLE_Y)","") //set resVar	
						itunibo.robot.plannerBhestie.putTable( getCurSol("TABLE_X").toString(), getCurSol("TABLE_Y").toString()  )
						solve("fridge(FRIDGE_X,FRIDGE_Y)","") //set resVar	
						itunibo.robot.plannerBhestie.putFridge( getCurSol("FRIDGE_X").toString(), getCurSol("FRIDGE_Y").toString()  )
						solve("dishwasher(DISHWASHER_X,DISHWASHER_Y)","") //set resVar	
						itunibo.robot.plannerBhestie.putDishwasher( getCurSol("DISHWASHER_X").toString(), getCurSol("DISHWASHER_Y").toString()  )
						solve("room_size(SIZE)","") //set resVar	
						itunibo.robot.plannerBhestie.setRoomSize( getCurSol("SIZE").toString()  )
						itunibo.robot.plannerBhestie.create(myself)
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
					}
					 transition(edgeName="t026",targetState="doPlan",cond=whenDispatch("executePlanningProject"))
				}	 
				state("doPlan") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("executePlanningProject(X)"), Term.createTerm("executePlanningProject(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								itunibo.robot.plannerBhestie.action( "msg(${payloadArg(0)})"  )
						}
					}
					 transition(edgeName="t027",targetState="doPlan",cond=whenDispatch("executePlanningProject"))
					transition(edgeName="t028",targetState="doingPlan",cond=whenDispatch("plannerCmd"))
					transition(edgeName="t029",targetState="doingPlan",cond=whenDispatch("plannerTask"))
				}	 
				state("doingPlan") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("plannerCmd(X)"), Term.createTerm("plannerCmd(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("plannerCmd", "plannerCmd(${payloadArg(0)})" ,"robotmind" ) 
						}
						if( checkMsgContent( Term.createTerm("plannerTask(X)"), Term.createTerm("plannerTask(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("plannerTask", "plannerTask(${payloadArg(0)})" ,"robotmind" ) 
						}
						if( checkMsgContent( Term.createTerm("registerAck(X)"), Term.createTerm("registerAck(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								itunibo.robot.plannerBhestie.registerAck( "${payloadArg(0)}"  )
								itunibo.robot.plannerBhestie.requestNextMove(  )
						}
						if( checkMsgContent( Term.createTerm("requestNextMove(X)"), Term.createTerm("requestNextMove(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								itunibo.robot.plannerBhestie.requestNextMove(  )
						}
					}
					 transition(edgeName="t030",targetState="doingPlan",cond=whenDispatch("plannerCmd"))
					transition(edgeName="t031",targetState="doingPlan",cond=whenDispatch("plannerTask"))
					transition(edgeName="t032",targetState="doingPlan",cond=whenDispatch("registerAck"))
					transition(edgeName="t033",targetState="endPlan",cond=whenDispatch("endTaskEventCmd"))
					transition(edgeName="t034",targetState="doingPlan",cond=whenDispatch("requestNextMove"))
				}	 
				state("endPlan") { //this:State
					action { //it:State
						forward("endTaskEventCmd", "endTaskEventCmd()" ,"robotmind" ) 
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}
