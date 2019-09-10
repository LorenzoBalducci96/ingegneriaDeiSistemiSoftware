const { WebpageServer, isWebpageRead } = require('./WebpageServer')
const TCPServer = require('./TCPServer')

const portNumber = readPortNumberFromArguments()

const webpageCallbacks = {
    onWebpageReady: () => server.send( { type: 'webpage-ready', arg: {} } ),
    onSonarActivated: object => server.send( { type: 'sonar-activated', arg: object } ),
    onCollision: objectName => server.send( { type: 'collision', arg: { objectName } } ),
	onMovement_succeeded: ack => server.send( { type: 'onMovement_succeded', arg: {ack} } ),
	onAction: action => beep()
}

const webpageServer = new WebpageServer(webpageCallbacks)
const server = new TCPServer( {
    port: portNumber,
    onClientConnected: () => { if(isWebpageRead()) webpageCallbacks.onWebpageReady() },
    onMessage: command => webpageServer[command.type](command.arg) 
} )

function readPortNumberFromArguments() {
    const port = Number(process.argv[2])
    if(!port || port < 0 || port >= 65536) {
        console.error("This script expects a valid port number (>= 0 and < 65536) as argument.")
        process.exit()
    }

    return port
}

function beep() {
	alert("execute action")
}
