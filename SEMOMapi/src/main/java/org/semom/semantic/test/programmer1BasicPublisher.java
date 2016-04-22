package org.semom.semantic.test;

import org.semom.semantic.message.SemanticObservationMessage;
import org.semom.semantic.publisher.SemanticPublisherInterface;
import org.semom.semantic.sensor.SemanticSensor;

public class programmer1BasicPublisher implements SemanticPublisherInterface {
	
	public static void main(String[] args){
		//SemanticSensor s = new SemanticSensor(new SemanticProperty("Humidity"), new SemanticFeatureOfInterest());
		SemanticSensor s = new SemanticSensor("Humidity", "bed", "RH");
		// faire une boucle... tant que je reçois de nouvelles valeurs faire s.addSensorOutput et créer un nouveau message
		String semMessage = s.addSensorOutput(30);
		System.out.println("semMessage is "+ semMessage);
		
		//SemanticPublisher pub = new SemanticPublisher();
		//pub.sendMessage(s.getProp().toString(), sem.toString());
	}

	public void connect() {
		// TODO Auto-generated method stub
		
	}

	public void disconnectFromBroker() {
		// TODO Auto-generated method stub
		
	}

	public void publish(SemanticObservationMessage publication) {
		// TODO Auto-generated method stub
		
	}

	public void unpublish(SemanticObservationMessage publication) {
		// TODO Auto-generated method stub
		
	}

	public void reconnect() {
		// TODO Auto-generated method stub
		
	}

	public void reconnect(String brokerIP, int brokerPort) {
		// TODO Auto-generated method stub
		
	}

}
