/* Generated by AN DISI Unibo */ 
package it.unibo.sonarhandler

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Sonarhandler ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "init"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						println("sonarhandler STARTS ... ")
					}
					 transition( edgeName="goto",targetState="waitForEvents", cond=doswitch() )
				}	 
				state("waitForEvents") { //this:State
					action { //it:State
					}
					 transition(edgeName="t09",targetState="handleSonar",cond=whenEvent("sonar"))
					transition(edgeName="t010",targetState="handleSonar",cond=whenEvent("sonarRobot"))
				}	 
				state("handleSonar") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("sonar(SONAR,DISTANCE)"), Term.createTerm("sonar(SONAR,DISTANCE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val D = Integer.parseInt( payloadArg(1) ) * 5
								emit("polar", "p($D,90)" ) 
						}
						if( checkMsgContent( Term.createTerm("sonar(DISTANCE)"), Term.createTerm("sonar(DISTANCE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val D = Integer.parseInt( payloadArg(0) ) * 5
								emit("polar", "p($D,180)" ) 
						}
					}
					 transition( edgeName="goto",targetState="waitForEvents", cond=doswitch() )
				}	 
			}
		}
}
