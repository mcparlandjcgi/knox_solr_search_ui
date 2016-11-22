/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.knox.solr.ui;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * A SpringConfiguration.
 * 
 * @author John McParland.
 *
 */
@Configuration
@PropertySources({@PropertySource("classpath:application.properties"),
    @PropertySource("file:///usr/knox_solr_search_ui/override.properties")})
public class SpringConfiguration {

  /**
   * Create a SpringConfiguration.
   *
   */
  public SpringConfiguration() {}

  /**
   * @return Property placeholder
   */
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  /**
   * @return
   * @throws KeyManagementException
   * @throws NoSuchAlgorithmException
   * @throws KeyStoreException
   */
  // @Bean
  // public RestTemplate restTemplate()
  // throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
  // TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
  // public boolean isTrusted(X509Certificate[] chain, String authType)
  // throws CertificateException {
  // return true;
  // }
  // };
  //
  // SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
  // .loadTrustMaterial(null, acceptingTrustStrategy).build();
  //
  // SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
  //
  // CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
  //
  // HttpComponentsClientHttpRequestFactory requestFactory =
  // new HttpComponentsClientHttpRequestFactory();
  //
  // requestFactory.setHttpClient(httpClient);
  //
  // RestTemplate restTemplate = new RestTemplate(requestFactory);
  // return restTemplate;
  // }

  // @Bean
  // public RestTemplate restTemplate()
  // throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
  // SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
  // new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build());
  //
  // HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
  //
  // RestTemplate template = new RestTemplate();
  // ((HttpComponentsClientHttpRequestFactory) template.getRequestFactory())
  // .setHttpClient(httpClient);
  // return template;
  // }

  /**
   * @return get a rest template.
   */
  @Bean("sslRestTemplate")
  @Primary
  public RestTemplate restTemplate() {
    CloseableHttpClient httpClient =
        HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
    HttpComponentsClientHttpRequestFactory requestFactory =
        new HttpComponentsClientHttpRequestFactory();
    requestFactory.setHttpClient(httpClient);

    return new RestTemplate(requestFactory);
  }

}
