package org.semom.semantic.subscriber;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.semom.semantic.publisher.SemanticPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SemanticSubscriber implements MqttCallback {
			
	    MqttClient client;
	    private String DEFAULT_TOPIC;
	    private MqttConnectOptions connectOptions;
	    
	    private static final String BROKER_URL = "tcp://m2m.eclipse.org:1883";
		 // private static final String BROKER_URL = "192.168.10.8:1883";
		private static final String MY_MQTT_CLIENT_ID = "Rita-SemanticMQTT-sub";
		private static final Logger LOG = LoggerFactory.getLogger(SemanticPublisher.class);
		private static final boolean PUBLISHER = false;
		private static final boolean SUBSCRIBER = true;
		
		private static final int RETRIES = 3;

		public SemanticSubscriber(String topic) throws MqttException {
			client = new MqttClient(BROKER_URL, MY_MQTT_CLIENT_ID);
		    client.setCallback(this);
			DEFAULT_TOPIC = topic;		
		}

		  public void runClient() {

			    connectOptions = new MqttConnectOptions();
			    connectOptions.setCleanSession(true);
			    connectOptions.setKeepAliveInterval(100);

			    try {

			      System.out.println("Attempting Connection to " + BROKER_URL);
			      client.connect(connectOptions);
			      System.out.println("Connected to " + BROKER_URL);
			      client.subscribe(DEFAULT_TOPIC);
			      MqttMessage m = connectOptions.getWillMessage();
			      System.out.println("message is " + m.toString());

			    } catch (MqttException me) {

			      System.err.println(me.getMessage());
			      System.err.println(me.getStackTrace());
			      System.exit(-1);
			    }

			  }
		
		public void connectionLost(Throwable cause) {
		    // TODO Auto-generated method stub
			 System.out.println("Connection lost!");	
		}

		
		public void messageArrived(String topic, MqttMessage message)
		        throws Exception {
			LOG.info("New message on topic '{}': {}", topic, message);
			System.out.println("-------------------------------------------------");
			System.out.println("| Topic:" + topic.getBytes());
			System.out.println("| Message: " + new String(message.getPayload()));
			System.out.println("-------------------------------------------------");   
		}

		public void deliveryComplete(IMqttDeliveryToken token) {
		    // TODO Auto-generated method stub
		}

		public static void main(String[] args) throws MqttException {
			SemanticSubscriber app =  new SemanticSubscriber("Bedsore/room2/bed1/moisture");
			app.runClient();
		}



}
