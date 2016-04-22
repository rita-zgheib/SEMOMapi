package usedClasses;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class Pubsubclass implements MqttCallback {
	
	  private MqttClient myClient;
	  private MqttConnectOptions connectOptions;

	  private static final String BROKER_URL = "tcp://m2m.eclipse.org:1883";
	 // private static final String BROKER_URL = "192.168.10.8:1883";
	  private static final String MY_MQTT_CLIENT_ID = "Rita-SemanticMQTT";
	  private static final String DEFAULT_TOPIC = "BedsoreRisk";
	  //private static final String DEFAULT_TOPIC = "<file:/C:/Users/rzgheib/AppData/Local/Temp/saved_Topic8710371318580728126owl;BedsoreRisk>";
	  //private static final String DEFAULT_TOPIC = "<file:/C:/Users/rzgheib/AppData/Local/Temp/saved_Topic8710371318580728126owl#bedsoreRisk>";
	  
	  private static final boolean PUBLISHER = true;
	  private static final boolean SUBSCRIBER = true;

	  private static final int RETRIES = 3;

	  public Pubsubclass() throws MqttException{
		 myClient = new MqttClient(BROKER_URL, MY_MQTT_CLIENT_ID);
		 myClient.setCallback(this);
	  }
	  
	  public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		System.out.println("Connection lost!");
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		System.out.println("Devliery completed with token ::");
		System.out.println("Message Id :: " + token.getMessageId());
		try {
			System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Recieved Message :: -----------------------------");
	    System.out.println("| Topic:" + arg0);
	    System.out.println("| Message: " + new String(arg1.getPayload()));
	    System.out.println("End ---------------------------------------------");	
	}
	
	public void sendMessage(String topic, String message) throws InterruptedException {

	    System.out.println("Building message with " + message.getBytes().length + " bytes of payload");
	    MqttMessage mqttMessage = new MqttMessage(message.getBytes());
	    mqttMessage.setQos(0);
	    mqttMessage.setRetained(false);

	    MqttTopic mqttTopic = myClient.getTopic(topic);

	    MqttDeliveryToken token = null;

	    try {
	      token = mqttTopic.publish(mqttMessage);
	      Thread.sleep(100);
	      token.waitForCompletion();
	    } catch (MqttException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	    }
	    if (null != token) {
		      System.out.println("Published with Token :: " +token);
		      System.out.println("Message id is: "+token.getMessageId());
	    }
	}
	
	public void runClient() throws OWLOntologyStorageException, OWLOntologyCreationException, IOException {
	
		// Connect to Broker
		 connectOptions = new MqttConnectOptions();
		 connectOptions.setCleanSession(true);
		 connectOptions.setKeepAliveInterval(100);
		 
		 OntologyGenerator semMsg = new OntologyGenerator("BedsoreObservation1", "Bedsoresensor", 
					"BackElder", "bedsoreRisk", 3, "RH", 2, 3);
		String semTopic = semMsg.toSemanticTopic(DEFAULT_TOPIC).toString();
		OWLOntology o = semMsg.getOntology();
		MqttMessage mqttSemMsg = new MqttMessage(o.toString().getBytes());

		 try {
		      System.out.println("Attempting Connection to " + BROKER_URL);
		      myClient.connect(connectOptions);
		 } catch (MqttException me) {
		      System.err.println(me.getMessage());
		      System.err.println(me.getStackTrace());
		      System.exit(-1);
		    }
		    
		  System.out.println("Connected to " + BROKER_URL);
		      
	      MqttTopic topic = myClient.getTopic(DEFAULT_TOPIC);
		      
	      if (SUBSCRIBER) {
				try {
					int subQoS = 0;
					myClient.subscribe(semTopic, subQoS);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		      
	      if (PUBLISHER) {
				for (int i=1; i<=10; i++) {
			   		String pubMsg = "{\"pubmsg\":" + i + "}";
			   		int pubQoS = 0;
					MqttMessage message = new MqttMessage(pubMsg.getBytes());
			    	message.setQos(pubQoS);
			    	message.setRetained(false);

			    	// Publish the message
			    	System.out.println("Publishing to topic \"" + topic + "\" qos " + pubQoS);
			    	MqttDeliveryToken token = null;
			    	try {
			    		// publish message to broker
						token = topic.publish(message);
						sendMessage(semTopic, mqttSemMsg.toString());
				    	// Wait until the message has been delivered to the broker
						token.waitForCompletion();
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}			
			}	
		}
	 public void stopClient() throws MqttException {
			System.out.println("Trying to disconnect from " + BROKER_URL);  
		    myClient.disconnect();
		    System.out.println("Disonnected from " + BROKER_URL);
		    System.exit(0);
		  }
	 
	public static void main(String[] args) throws MqttException, OWLOntologyStorageException, 
	OWLOntologyCreationException, IOException, InterruptedException{
		Pubsubclass app = new Pubsubclass();	
/*		SemanticMessage semMsg = new SemanticMessage("BedsoreObservation1", "Bedsoresensor", 
					"BackElder", "bedsoreRisk", 3, "RH", 2, 3);
		//String semTopic = semMsg.toSemanticTopic(DEFAULT_TOPIC).toString();
		OWLOntology o = semMsg.getOntology();
		MqttMessage mqttSemMsg = new MqttMessage(o.toString().getBytes());*/
		app.runClient();
		//app.sendMessage(DEFAULT_TOPIC, mqttSemMsg.toString());
    	Thread.sleep(200);
    	app.stopClient();
	
	}

}
