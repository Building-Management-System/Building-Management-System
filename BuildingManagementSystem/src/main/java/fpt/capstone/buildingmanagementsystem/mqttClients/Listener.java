package fpt.capstone.buildingmanagementsystem.mqttClients;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class Listener implements ApplicationListener<CustomSpringEvent> {
    @Autowired
    Mqtt mqtt;

    @Override
    public void onApplicationEvent(CustomSpringEvent event) {
        final String topic = "mqtt/face/2032105/Rec";
        try {
            mqtt.subscribe(topic);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
