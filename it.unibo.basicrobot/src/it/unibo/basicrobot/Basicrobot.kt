/* Generated by AN DISI Unibo */ 
package it.unibo.basicrobot

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Basicrobot ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						solve("consult('basicRobotConfig.pl')","") //set resVar	
						solve("robot(R,PORT)","") //set resVar	
						if(currentSolution.isSuccess()) { println("USING ROBOT : ${getCurSol("R")},  port= ${getCurSol("PORT")} ")
						 }
						else
						{ println("no robot")
						 }
						if(currentSolution.isSuccess()) { itunibo.robot.robotSupport.create(myself ,getCurSol("R").toString(), getCurSol("PORT").toString() )
						 }
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
					}
					 transition(edgeName="t035",targetState="handleRobotCmd",cond=whenDispatch("robotCmd"))
					transition(edgeName="t036",targetState="handleRobotAction",cond=whenDispatch("robotAction"))
				}	 
				state("handleRobotAction") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("robotAction(ACTION)"), Term.createTerm("robotAction(ACTION)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								itunibo.robot.robotSupport.executeAction(  )
						}
					}
					 transition(edgeName="t037",targetState="handleRobotCmd",cond=whenDispatch("robotCmd"))
					transition(edgeName="t038",targetState="handleRobotAction",cond=whenDispatch("robotAction"))
				}	 
				state("handleRobotCmd") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("robotCmd(CMD)"), Term.createTerm("robotCmd(MOVE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								itunibo.robot.robotSupport.move( "msg(${payloadArg(0)})"  )
								itunibo.robot.robotSupport.waitAck(  )
						}
					}
					 transition(edgeName="t039",targetState="waitAck",cond=whenDispatch("ackMsg"))
					transition(edgeName="t040",targetState="handleRobotCmd",cond=whenDispatch("robotCmd"))
				}	 
				state("waitAck") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("ackMsg(X)"), Term.createTerm("ackMsg(X)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("ackMsg", "ackMsg(${payloadArg(0)})" ,"robotmind" ) 
						}
					}
					 transition(edgeName="t041",targetState="waitAck",cond=whenDispatch("ackMsg"))
					transition(edgeName="t042",targetState="handleRobotCmd",cond=whenDispatch("robotCmd"))
					transition(edgeName="t043",targetState="handleRobotAction",cond=whenDispatch("robotAction"))
				}	 
			}
		}
}
