package itunibo.robot
import it.unibo.kactor.ActorBasic
 

object robotSupport{
	lateinit var robotKind : String
	
	fun create( actor: ActorBasic, robot : String, port: String ){
		robotKind = robot
		when( robotKind ){
			"virtual"    ->  { itunibo.robotVirtual.clientWenvObjTcp.initClientConn( actor, "localhost" ) }
			"realmbot"   ->  { itunibo.robotMbot.mbotSupport.create( actor, port ) }  //port="/dev/ttyUSB0"   "COM6"
			"realnano" ->    { it.unibo.robotRaspOnly.nanoSupport.create(actor, true ) } //false=NO SONAR SUPPORT!!!
			else -> println( "robot unknown" )
		}
	}
	
	fun move( cmd : String ){ //cmd = msg(M) M=w | a | s | d | h
		println("robotSupport move cmd=$cmd robotKind=$robotKind" )
		when( robotKind ){
			"virtual"  -> { itunibo.robotVirtual.clientWenvObjTcp.sendMsg(  cmd ) }	
			"realmbot" -> { itunibo.robotMbot.mbotSupport.move( cmd ) }
			"realnano" -> { it.unibo.robotRaspOnly.nanoSupport.move( cmd ) }
			else       -> println( "robot unknown" )
		}
	}
	
	fun executeAction( action: String ){ //cmd = msg(M) M=w | a | s | d | h
		println("robotSupport execute action mock")
		when( robotKind ){
			"virtual"  -> { itunibo.robotVirtual.clientWenvObjTcp.executeAction(action) }	
			"realmbot" -> { itunibo.robotMbot.mbotSupport.executeAction() }
			"realnano" -> { println("not implemented on virtual realnano ")}
			else       -> println( "robot unknown" )
		}
	}
	
	fun waitAck(){ //cmd = msg(M) M=w | a | s | d | h
		println("robotSupport waitAck" )
		when( robotKind ){
			"virtual"  -> { itunibo.robotVirtual.clientWenvObjTcp.waitAck( )  }
			"realmbot" -> { itunibo.robotMbot.mbotSupport.waitAck( ) }
			"realnano" -> { println("not implemented yet, program interrupted" ) }
			else       -> println( "robot unknown" )
		}
	}
	
}