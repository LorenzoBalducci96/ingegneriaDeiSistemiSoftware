/* Generated by AN DISI Unibo */ 
package it.unibo.viewmodel

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Viewmodel ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						itunibo.console.outguiSupport.create(myself ,"" )
						itunibo.console.outguiSupport.output( "WELCOME to viewmodel"  )
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
					}
					 transition(edgeName="t00",targetState="handleModelContent",cond=whenEvent("modelContent"))
				}	 
				state("handleModelContent") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("content(STATE)"), Term.createTerm("content(STATE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								itunibo.console.outguiSupport.output( payloadArg(0)  )
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}
