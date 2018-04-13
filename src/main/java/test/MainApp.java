package test;

import org.apache.camel.main.Main;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MainApp {

  public static void main(String... args) throws Exception {
    
    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
    Main main = new Main();
    main.bind("jms", JmsComponent.jmsComponentAutoAcknowledge(factory));
    main.addRouteBuilder(new LumberjackRouteBuilder());
    main.run(args);
  }
}

