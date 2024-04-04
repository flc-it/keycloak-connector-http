/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flcit.keycloak.connector.http.util;

import java.io.IOException;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.flcit.keycloak.connector.http.entity.ConsumerOutputStreamHttpEntity;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class HttpClientUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().setSerializationInclusion(Include.NON_EMPTY);

    private HttpClientUtils() { }

    /**
     * @param httpClient
     * @param uri
     * @param body
     * @throws IOException
     */
    public static void postJsonNoResponse(final HttpClient httpClient, final String uri, Object body) throws IOException {
        org.apache.http.client.utils.HttpClientUtils.closeQuietly(postJson(httpClient, uri, body));
    }

    /**
     * @param httpClient
     * @param uri
     * @param body
     * @return
     * @throws IOException
     */
    public static HttpResponse postJson(final HttpClient httpClient, final String uri, Object body) throws IOException {
        return httpClient.execute(RequestBuilder.post(uri)
                .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
                .setVersion(HttpVersion.HTTP_1_1)
                .setEntity(new ConsumerOutputStreamHttpEntity(os -> OBJECT_MAPPER.writeValue(os, body)))
                .build());
    }

    /**
     * @param httpResponse
     * @return
     */
    public static boolean is2xx(final HttpResponse httpResponse) {
        return is2xx(httpResponse.getStatusLine().getStatusCode());
    }

    private static boolean is2xx(final int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

}
