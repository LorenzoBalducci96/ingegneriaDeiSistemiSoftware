%====================================================================================
% robotmind description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxrobotmind, "localhost",  "MQTT", "0" ).
context(ctxbasicrobot, "localhost",  "MQTT", "0" ).
 qactor( robotmind, ctxrobotmind, "it.unibo.robotmind.Robotmind").
  qactor( basicrobot, ctxbasicrobot, "it.unibo.basicrobot.Basicrobot").
