import * as THREE from '../../node_modules/three/build/three.module.js'
import eventBus from '../eventBus/EventBus.js'
import eventBusEvents from '../eventBus/events.js'

export default (mesh, camera, config, collisionManager) => {
	
	const keycodes = {
        W: 87,
        A: 65,
        S: 83,
        D: 68,
        R: 82,
        F: 70
    }
	
    let forward = false
    let backward = false
    let rotating = false
	let rotating_orientation = 'A'
	
	let movement_correct_completed = true
	var init_movement;
	var millis_to_undo_move = 0;
	
    setCameraPositionRelativeToMesh(camera, mesh)
	
	function onKeyDown(keyCode, duration) {
        if(keyCode === keycodes.W){
			//alert("inizio forward"); ci va!
			console.log("inizio forward")
            forward = true
			init_movement = Date.now()
			movement_correct_completed = true
		}
        else if(keyCode === keycodes.S){
            backward = true
			init_movement = Date.now()
			movement_correct_completed = true
		}
        
		//always correct completed in this virtual wnvironment
        else if(keyCode === keycodes.D){
			rotate(-Math.PI/2, duration)
			rotating_orientation = 'D'
			init_movement = Date.now()
			movement_correct_completed = true
		}
        else if(keyCode === keycodes.A){
            rotate(Math.PI/2, duration)
			rotating_orientation = 'A'
			init_movement = Date.now()
			movement_correct_completed = true
		}
    }

    function onKeyUp(keyCode) {
        if(keyCode === keycodes.W){
            forward = false
			if(movement_correct_completed){
				eventBus.post(eventBusEvents.movement_succeeded, "ok")
			}else{
				movement_correct_completed = true
				backward = true;
				setTimeout(function(){
					alert("step indietro")
					backward = false; 
					eventBus.post(eventBusEvents.movement_succeeded, "fail");
				},millis_to_undo_move);
				
			}
		}
		if(keyCode === keycodes.D){
			if(movement_correct_completed){
				eventBus.post(eventBusEvents.movement_succeeded, "ok")
			}else{
				eventBus.post(eventBusEvents.movement_succeeded, "fail")
			}
		}
		if(keyCode === keycodes.A){
			if(movement_correct_completed){
				eventBus.post(eventBusEvents.movement_succeeded, "ok")
			}else{
				eventBus.post(eventBusEvents.movement_succeeded, "fail")
			}
		}
        else if(keyCode === keycodes.S){
            backward = false;
			if(movement_correct_completed){
				if(movement_correct_completed){
					eventBus.post(eventBusEvents.movement_succeeded, "ok")
				}else{
					movement_correct_completed = true
					forward = true;
					setTimeout(function(){
						alert("step indietro")
						forward = false; 
						eventBus.post(eventBusEvents.movement_succeeded, "fail");
					},millis_to_undo_move);
				}
			}
		}
    }

    function rotate(angle, duration = 300) {
        duration -= 50
        if(rotating)
            return

        const finalAngle = mesh.rotation.y + angle

        rotating = true
        new TWEEN.Tween(mesh.rotation)
            .to({ y: finalAngle }, duration)
            .easing(TWEEN.Easing.Quadratic.InOut)
            .onComplete( () => {rotating = false; onKeyUp(keycodes.D) } )
            .start()
    }    

    function update(time) {
        const matrix = new THREE.Matrix4()
        matrix.extractRotation( mesh.matrix )

        const directionVector = new THREE.Vector3( 0, 0, -1 )
        directionVector.applyMatrix4(matrix)
    
		if(forward || backward) {
            const direction = backward ? 1 : -1
            const stepVector = directionVector.multiplyScalar( config.speed * direction )
            const tPosition = mesh.position.clone().add(stepVector)
            
            const collision = collisionManager.checkCollision(tPosition)
			if(collision && movement_correct_completed){
				movement_correct_completed = false
				millis_to_undo_move = Date.now() - init_movement;
			}

            if(!collision) {
                mesh.position.add(stepVector)
                camera.position.add(stepVector)
             }            
        } else 
            collisionManager.checkCollision(mesh.position)
    }
    
    function resetPosition() {
        mesh.position.x = config.position.x
        mesh.position.z = config.position.y

        setCameraPositionRelativeToMesh(camera, mesh)
    }

    function setCameraPositionRelativeToMesh(camera, mesh) {
        camera.position.x = mesh.position.x
        camera.position.z = mesh.position.z + 20

        camera.lookAt(new THREE.Vector3(mesh.position.x, 0, mesh.position.z))
    }
	
	return {
        resetPosition,
		onKeyDown,
		onKeyUp,
		update
	}
}