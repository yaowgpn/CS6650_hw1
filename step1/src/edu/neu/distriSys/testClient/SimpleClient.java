package edu.neu.distriSys.testClient;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by wangy on 9/27/2017.
 */
public class SimpleClient {
  private final Client client;
  private WebTarget webTarget;

  public SimpleClient(String url) {
    client = ClientBuilder.newClient();
    //webTarget = client.target("http://" + url).path("webapi/hello");//local tomcat8 server;//local
    webTarget = client.target("http://" + url+"/local_server").path("webapi/hello");
  }

  public <T> T sendPost(Object requestEntity, Class<T> responseType) throws ClientErrorException {
    return
        webTarget.request(MediaType.TEXT_PLAIN).
            post(Entity.entity(requestEntity,MediaType.TEXT_PLAIN), responseType);
  }

  public Response sendGet() throws ClientErrorException {
    return webTarget.request(MediaType.TEXT_PLAIN_TYPE).get();
  }

  public void closeClient() {
    client.close();
  }

}
