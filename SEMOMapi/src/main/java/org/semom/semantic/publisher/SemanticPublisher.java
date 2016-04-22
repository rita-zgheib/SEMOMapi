package org.semom.semantic.publisher;

import java.util.Set;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import usedClasses.OntologyGenerator;



public class SemanticPublisher implements MqttCallback {
	
	  private MqttClient myClient;
	  private MqttConnectOptions connectOptions;

	  private static final String BROKER_URL = "tcp://m2m.eclipse.org:1883";
	 // private static final String BROKER_URL = "192.168.10.8:1883";
	  private static final String MY_MQTT_CLIENT_ID = "Rita-SemanticMQTT-pub";
	  
	  private static final String DEFAULT_TOPIC = "Bedsore/room2/bed1/moisture";
	  private static final Logger LOG = LoggerFactory.getLogger(SemanticPublisher.class);
	  private static final boolean PUBLISHER = true;
	  private static final boolean SUBSCRIBER = false;

	  private static final int RETRIES = 3;
	  
	  public SemanticPublisher() throws MqttException {
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
		// System.out.println("Response :: " + token.getResponse().toString());
		try {
			System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
		} catch (MqttException e) {
		// TODO Auto-generated catch block
				e.printStackTrace();
		}
		
	 }

	 public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		    System.out.println("Recieved Message :: -----------------------------");
		    System.out.println("| Topic:" + arg0);
		    System.out.println("| Message: " + new String(arg1.getPayload()));
		    System.out.println("End ---------------------------------------------");
		  }
	 
	  public void runClient() {

		    connectOptions = new MqttConnectOptions();
		    connectOptions.setCleanSession(true);
		    connectOptions.setKeepAliveInterval(100);

		    try {

		      System.out.println("Attempting Connection to " + BROKER_URL);
		      myClient.connect(connectOptions);
		      System.out.println("Connected to " + BROKER_URL);
		      

		    } catch (MqttException me) {

		      System.err.println(me.getMessage());
		      System.err.println(me.getStackTrace());
		      System.exit(-1);
		    }

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
		  
	 public void stopClient() throws MqttException {
			System.out.println("Trying to disconnect from " + BROKER_URL);  
		    myClient.disconnect();
		    System.out.println("Disonnected from " + BROKER_URL);
		    System.exit(0);
		  }

	 public static void main(String[] args) throws Exception {

			  	SemanticPublisher app = new SemanticPublisher();
			  	//String message = "Hello From My MQTT APP";
			  	//MqttMessage msg = new MqttMessage(message.getBytes());
			  	OntologyGenerator semMsg = new OntologyGenerator("BedsoreObservation1", "Bedsoresensor", 
						"BackElder", "bedsoreRisk", 3, "RH", 2, 3);
			  	OWLOntology o = semMsg.getOntology();
			  	//Set<OWLNamedIndividual> ind = o.getIndividualsInSignature();
			  	MqttMessage mqttSemMsg = new MqttMessage(o.toString().getBytes());
			    app.runClient();
			    for (int i = 0; i < 110; i++){
			    	app.sendMessage(DEFAULT_TOPIC, mqttSemMsg.toString());
			    	Thread.sleep(200);
			    }
			   // app.messageArrived(DEFAULT_TOPIC, mqttSemMsg);
			    
			    app.stopClient();

	 }
}
