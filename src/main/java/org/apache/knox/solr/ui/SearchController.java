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

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * A SearchController.
 * 
 * @author John McParland.
 *
 */
@RestController
public class SearchController {

  /**
   * The logger.
   */
  private final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * The knoxSolrConfig.
   */
  @Autowired
  private KnoxSolrConfig knoxSolrConfig = null;

  /**
   * The restTemplate.
   */
  @Qualifier("sslRestTemplate")
  @Autowired
  private RestTemplate restTemplate = null;


  /**
   * Create a SearchController.
   *
   */
  public SearchController() {
    // Nothing needed
  }

  /**
   * Get the knoxSolrConfig.
   * 
   * @return the knoxSolrConfig.
   */
  public KnoxSolrConfig getKnoxSolrConfig() {
    return knoxSolrConfig;
  }

  /**
   * Set the knoxSolrConfig.
   * 
   * @param knoxSolrConfig the knoxSolrConfig to set.
   */
  public void setKnoxSolrConfig(KnoxSolrConfig knoxSolrConfig) {
    this.knoxSolrConfig = knoxSolrConfig;
  }

  /**
   * Get the restTemplate.
   * 
   * @return the restTemplate.
   */
  public RestTemplate getRestTemplate() {
    return restTemplate;
  }

  /**
   * Set the restTemplate.
   * 
   * @param restTemplate the restTemplate to set.
   */
  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * Get the logger.
   * 
   * @return the logger.
   */
  public Logger getLogger() {
    return logger;
  }

  /**
   * Do a solr search
   * 
   * @param solrQuery the Solr query
   * @return the response
   */
  @CrossOrigin(origins = "*")
  @RequestMapping("/search")
  public @ResponseBody String search(@RequestParam("solrQuery") String solrQuery) {
    logger.debug("Received Solr Query: {}", solrQuery);

    // Build the URL and arguments
    final String solrUrl = buildSolrUrl();
    final MultiValueMap<String, String> solrQueryArgs = extractSolrQueryArgs(solrQuery);
    addMandatoryArgs(solrQueryArgs);

    // Create a URI
    final UriComponents uri =
        UriComponentsBuilder.fromHttpUrl(solrUrl).queryParams(solrQueryArgs).build();
    logger.debug("Executing with URL: {} and Args: {}", solrUrl, solrQueryArgs);

    // Build the headers
    final HttpHeaders headers = buildHttpHeaders();
    HttpEntity<?> reqentity = new HttpEntity<Object>(headers);

    // Do the request
    final ResponseEntity<String> solrResult =
        restTemplate.exchange(uri.toUri(), HttpMethod.GET, reqentity, String.class);
    logger.debug("Solr Result: {}", solrResult);

    // Return something!
    if (HttpStatus.OK.equals(solrResult.getStatusCode())) {
      return solrResult.getBody();
    } else {
      logger.error("Error getting Solr Result - http status: {}", solrResult.getStatusCodeValue());
      return "";
    }
  }

  /**
   * Extract the solr query arguments.
   * 
   * @param solrQuery the solr query the solr query args.
   * @return a multi value map of query arguments.
   */
  protected MultiValueMap<String, String> extractSolrQueryArgs(String solrQuery) {
    if (StringUtils.isEmpty(solrQuery)) {
      return null;
    }
    // 1. Split mutiple args around ","
    String[] allArgs = solrQuery.split("&");
    MultiValueMap<String, String> args = new LinkedMultiValueMap<String, String>();
    for (final String arg : allArgs) {
      // 2. Split single arg around "="
      final String[] splitArgPair = arg.split("=");
      args.add(splitArgPair[0], splitArgPair[1]);
    }
    return args;
  }

  /**
   * Build the full Solr Request URL.
   * 
   * @return the URL to execute against Knox, to do a Solr request.
   */
  protected String buildSolrUrl() {
    final StringBuilder sb = new StringBuilder();
    sb.append(knoxSolrConfig.getProtocol());
    sb.append("://");
    sb.append(knoxSolrConfig.getIpaddres());
    sb.append(":");
    sb.append(knoxSolrConfig.getPort());
    sb.append("/");
    sb.append(knoxSolrConfig.getKnoxContext());
    sb.append("/");
    sb.append(knoxSolrConfig.getKnoxTopology());
    sb.append("/");
    sb.append(knoxSolrConfig.getSolrContext());
    sb.append("/");
    sb.append(knoxSolrConfig.getSolrCollection());
    sb.append("/");
    sb.append(knoxSolrConfig.getSolrSearchCommand());
    return sb.toString();
  }

  /**
   * Add mandatory arguments
   * 
   * @param args the arguments
   */
  protected void addMandatoryArgs(MultiValueMap<String, String> args) {
    if (!args.containsKey("wt")) {
      args.add("wt", knoxSolrConfig.getSolrFormat());
    }
  }

  /**
   * Build up the http headers
   * 
   * @return the Http Headers.
   */
  protected HttpHeaders buildHttpHeaders() {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML,
        MediaType.APPLICATION_XHTML_XML, MediaType.APPLICATION_XML));
    final String authnString = knoxSolrConfig.getUsername() + ":" + knoxSolrConfig.getPassword();
    headers.add("Authorization", "Basic "
        + Base64.getUrlEncoder().encodeToString(authnString.getBytes(Charset.forName("UTF-8"))));
    return headers;
  }

}
