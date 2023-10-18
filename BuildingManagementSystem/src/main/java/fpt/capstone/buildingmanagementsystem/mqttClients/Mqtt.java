package fpt.capstone.buildingmanagementsystem.mqttClients;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

@Component
public class Mqtt {
    private static IMqttClient instance;

    public IMqttClient getInstance() {
        try {
            if (instance == null) {
                instance = new MqttClient("tcp://127.0.0.1:61613", "mqttx_e9b67c51");
            }
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setUserName("admin");
            options.setPassword("admin".toCharArray());
            if (!instance.isConnected()) {
                instance.connect(options);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return instance;
    }
    private Mqtt() {
    }
    public void subscribe(final String topic) throws MqttException, InterruptedException {
        Mqtt mqtt= new Mqtt();
        mqtt.getInstance().subscribeWithResponse(topic, (tpic, msg) -> {
            System.out.println("Messages received:");
            System.out.println(msg.getId() + " -> " + new String(msg.getPayload()));
        });
    }
}
