# SEMOMapi
SEmantic Message Oriented Middleware api

This is the first version of the semantic MOM api using MQTT as a message oriented communication protocol
and OWLAPI for owl representations.

To resume, we have 3 actors: 

1- programmer1BasicPublisher
      - he will be able to create new semanticSensor (which has these attributes: semantic property, semantic featureOfInterest, Many semanticOutputs and then many semanticObservations)
      - he will add sensorsOutput and create semantic Messages
      
N.B till now these functionalities are what our api offer to the programmer. Version0
      
2- programmer2SemanticEnrichment

3 programmer3Subscriber
