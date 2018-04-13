package test;

import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import org.apache.camel.component.http4.HttpMethods;

import java.util.LinkedHashMap;
import com.google.gson.Gson;

public class LumberjackRouteBuilder extends RouteBuilder {
  private Gson gson = new Gson();
  public void configure(){
    from("lumberjack:localhost")
      .process(new Processor(){
           public void process(Exchange exchange) throws Exception {
             LinkedHashMap<String, String> logLine = exchange.getIn().getBody(LinkedHashMap.class);
             String newLine = gson.toJson(logLine, LinkedHashMap.class);
             exchange.getIn().setBody(newLine);
           }
         })
      .to("jms:test_queue");

    from("jms:test_queue")
      .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http4.HttpMethods.POST))
      .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
      .to("http4://localhost:9200/test_index/doc");
  }
}
