//package fpt.capstone.buildingmanagementsystem.mqttClients;
//
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.core.MessageProducer;
//import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
//import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
//import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
//import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
//import org.springframework.integration.mqtt.support.MqttHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHandler;
//import org.springframework.messaging.MessagingException;
//
//@Configuration
//public class Mqtt {
//    String uri="tcp://127.0.0.1:61613";
//    String username="admin";
//    String pass = "password";
//    String clientId="mqttx_e9b67c51";
//    @Bean
//    public MqttPahoClientFactory mqttPahoClientFactory() {
//        DefaultMqttPahoClientFactory factory= new DefaultMqttPahoClientFactory();
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setServerURIs(new String[] {uri});
//        options.setUserName(username);
//        options.setPassword(pass.toCharArray());
//        options.setCleanSession(true);
//        factory.setConnectionOptions(options);
//        return factory;
//    }
//    @Bean
//    public MessageChannel mqttInputChannel(){
//        return new DirectChannel();
//    }
//    @Bean
//    public MessageProducer inbound(){
//        MqttPahoMessageDrivenChannelAdapter adapter= new MqttPahoMessageDrivenChannelAdapter(clientId,mqttPahoClientFactory(),"#");
//        adapter.setCompletionTimeout(5000);
//        adapter.setConverter(new DefaultPahoMessageConverter());
//        adapter.setQos(2);
//        adapter.setOutputChannel(mqttInputChannel());
//        return adapter;
//    }
//    @Bean
//    @ServiceActivator(inputChannel = "mqttInputChannel")
//    public MessageHandler handler(){
//        return new MessageHandler() {
//            @Override
//            public void handleMessage(Message<?> message) throws MessagingException {
//                String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
//                if(topic.equals("mqtt/face/2032105/Rec")){
//                    System.out.println("This is out of topic");
//                }
//                System.out.println(message.getPayload());
//            }
//        };
//    }
//}
