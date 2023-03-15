package com.studerw.tda.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

import java.util.Properties;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Check the constructors
 */
public class HttpTdaClientTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpTdaClientTest.class);

  @Test(expected = IllegalArgumentException.class)
  public void testNoProps() {
    new HttpTdaClient(new Properties());
    Fail.fail("should not get here");
  }

  @Test
  public void testDefaultPropsInResources() {
    HttpTdaClient client = new HttpTdaClient();
    Assertions.assertThat(client.tdaProps.getProperty("tda.token.refresh")).isEqualTo("<REFRESH_TOKEN>");
    Assertions.assertThat(client.tdaProps.getProperty("tda.client_id")).isEqualTo("<CLIENT_ID>");
    Assertions.assertThat(client.tdaProps.getProperty("tda.url")).isEqualTo(HttpTdaClient.DEFAULT_PATH);
    Assertions.assertThat(client.tdaProps.getProperty("tda.debug.bytes.length")).isEqualTo("-1");
  }

  @Test
  public void testProps() {
    Properties props = new Properties();
    props.setProperty("tda.token.refresh", "abd");
    props.setProperty("tda.client_id", "abd");
    LOGGER.debug("Set valid props, others using defaults");
    HttpTdaClient client = new HttpTdaClient(props);
    Assertions.assertThat(client.tdaProps.getProperty("tda.token.refresh")).isEqualTo("abd");
    Assertions.assertThat(client.tdaProps.getProperty("tda.client_id")).isEqualTo("abd");
    Assertions.assertThat(client.tdaProps.getProperty("tda.url")).isEqualTo(HttpTdaClient.DEFAULT_PATH);
    Assertions.assertThat(client.tdaProps.getProperty("tda.debug.bytes.length")).isEqualTo("-1");
  }
}
