%====================================================================================
% robotmind description   
%====================================================================================
mqttBroker("localhost", "1883").
context(ctxrobotmind, "localhost",  "MQTT", "0" ).
context(ctxdummyforbasicrobot, "otherhost",  "MQTT", "0" ).
 qactor( basicrobot, ctxdummyforbasicrobot, "external").
  qactor( resourcemodel, ctxrobotmind, "it.unibo.resourcemodel.Resourcemodel").
  qactor( robotmind, ctxrobotmind, "it.unibo.robotmind.Robotmind").
