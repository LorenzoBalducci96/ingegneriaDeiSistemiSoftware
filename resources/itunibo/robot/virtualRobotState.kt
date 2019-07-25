package itunibo.robotMbot
import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.MsgUtil

object virtualRobotState{
	lateinit var actor   : ActorBasic
	var x_position : Int = 0
	var y_position : Int = 0
	var orientation : String = "nord"
				
	fun create( myactor: ActorBasic, port : String ){
		actor = myactor
	}
	
	fun move( cmd : String ){
		println("virtual state robot update state")
		when( cmd ){
			"msg(w)" -> x_position++;
			else -> println("mbotSupport command unknown")
		}
	}
}