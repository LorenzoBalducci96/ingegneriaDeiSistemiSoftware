%====================================================================================
% system description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxrobotmind, "localhost",  "MQTT", "0" ).
context(ctxmaitre, "localhost",  "MQTT", "0" ).
 qactor( frontend, ctxmaitre, "external").
  qactor( externalcomunicator, ctxrobotmind, "it.unibo.externalcomunicator.Externalcomunicator").
  qactor( robotmind, ctxrobotmind, "it.unibo.robotmind.Robotmind").
  qactor( planner, ctxrobotmind, "it.unibo.planner.Planner").
  qactor( basicrobot, ctxrobotmind, "it.unibo.basicrobot.Basicrobot").
