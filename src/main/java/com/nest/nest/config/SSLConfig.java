package com.nest.nest.config;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSLConfig implements InitializingBean {

  @Value("${server.ssl.trust-store:#{null}}")
  private String trustStore;

  @Value("${server.ssl.trust-store-password:#{null}}")
  private String trustStorePassword;

  @Override
  public void afterPropertiesSet() throws Exception {
    if (trustStore != null && trustStorePassword != null) {
      System.setProperty("jdk.tls.client.protocols", "TLSv1.2,TLSv1.3");

      System.setProperty("javax.net.ssl.trustStore", trustStore);
      System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
    }
  }

}
