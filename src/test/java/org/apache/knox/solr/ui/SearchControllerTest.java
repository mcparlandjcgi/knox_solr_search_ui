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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

/**
 * A SearchControllerTest.
 * 
 * @author John McParland.
 *
 */
public class SearchControllerTest {

  /**
   * Test method for {@link org.apache.knox.solr.ui.SearchController#buildSolrUrl()}.
   */
  @Test
  public void testBuildSolrUrl() {
    // Expected Result
    final String expectedUrl =
        "http://localhost:8443/gateway/sandbox/solr/KnoxSolrIntegration/search";

    // Mocks
    final KnoxSolrConfig solrConfig = mock(KnoxSolrConfig.class);
    when(solrConfig.getIpaddres()).thenReturn("localhost");
    when(solrConfig.getProtocol()).thenReturn("http");
    when(solrConfig.getPort()).thenReturn("8443");
    when(solrConfig.getKnoxContext()).thenReturn("gateway");
    when(solrConfig.getKnoxTopology()).thenReturn("sandbox");
    when(solrConfig.getSolrContext()).thenReturn("solr");
    when(solrConfig.getUsername()).thenReturn("guest");
    when(solrConfig.getPassword()).thenReturn("NOT_A_REAL_PASSWORD");
    when(solrConfig.getSolrSearchCommand()).thenReturn("search");
    when(solrConfig.getSolrCollection()).thenReturn("KnoxSolrIntegration");
    when(solrConfig.getSolrFormat()).thenReturn("json");

    final SearchController controller = new SearchController();
    controller.setKnoxSolrConfig(solrConfig);

    final String obtainedUrl = controller.buildSolrUrl();
    assertEquals(expectedUrl, obtainedUrl);
  }

  /**
   * Test method for {@link SearchController#createSolrQueryArgs(String)}.
   */
  @Test
  public void testCreateSolrQueryArgs() {
    final String solrQuery = "lorem";
    final String solrFormat = "json";

    final KnoxSolrConfig knoxSolrConfig = mock(KnoxSolrConfig.class);
    when(knoxSolrConfig.getSolrFormat()).thenReturn(solrFormat);

    final SearchController controller = new SearchController();
    controller.setKnoxSolrConfig(knoxSolrConfig);

    final MultiValueMap<String, String> args = controller.createSolrQueryArgs(solrQuery);
    assertEquals(2, args.size());
    assertEquals(solrQuery, args.get("q").iterator().next());
    assertEquals(solrFormat, args.get("wt").iterator().next());
  }

  /**
   * Test method for {@link SearchController#buildHttpHeaders()}.
   */
  @Test
  public void testBuildHttpHeaders() {
    final String username = "knox";
    final String password = "solr";

    final KnoxSolrConfig knoxSolrConfig = mock(KnoxSolrConfig.class);
    when(knoxSolrConfig.getUsername()).thenReturn(username);
    when(knoxSolrConfig.getPassword()).thenReturn(password);

    final SearchController controller = new SearchController();
    controller.setKnoxSolrConfig(knoxSolrConfig);

    final HttpHeaders headers = controller.buildHttpHeaders();
    // Check what's accepted
    final List<MediaType> acceptedMediaTypes = headers.getAccept();
    assertTrue(acceptedMediaTypes.containsAll(Arrays.asList(MediaType.APPLICATION_JSON,
        MediaType.TEXT_HTML, MediaType.APPLICATION_XHTML_XML, MediaType.APPLICATION_XML)));
    // Check what's sent
    assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());

    // Authorization header is key!
    assertTrue(headers.containsKey("Authorization"));
    final String authValue = headers.getFirst("Authorization");

    // Check the value is "Basic blahblahblah" where blahblahblah is a UTF-8 encoded
    // username:password
    // Base64.getUrlEncoder().encodeToString(authnString.getBytes(Charset.forName("UTF-8"))
    final String authCreds = authValue.split(" ")[1];
    final byte[] authBytes = Base64.getUrlDecoder().decode(authCreds);
    String usernamePassword = new String(authBytes, Charset.forName("UTF-8"));
    assertEquals(username, usernamePassword.split(":")[0]);
    assertEquals(password, usernamePassword.split(":")[1]);
  }

}
