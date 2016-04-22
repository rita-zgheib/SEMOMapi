package org.semom.semantic.publisher;

import org.semom.semantic.message.SemanticObservationMessage;

public interface SemanticPublisherInterface {
	
	public void connect();

	void disconnectFromBroker();

	void publish(SemanticObservationMessage publication);

	void unpublish(SemanticObservationMessage publication);

	public void reconnect();

	public void reconnect(String brokerIP, int brokerPort);

}
