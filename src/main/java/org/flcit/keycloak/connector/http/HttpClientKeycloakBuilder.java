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

package org.flcit.keycloak.connector.http;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.keycloak.Config;
import org.keycloak.connections.httpclient.HttpClientBuilder;

import org.flcit.commons.core.util.PropertyUtils;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class HttpClientKeycloakBuilder {

    private static final Long MAX_CONNECTION_IDLE_TIME_DEFAULT = 60000L;
    private static final Long CONNECTION_TTL_DEFAULT = 60000L;
    private static final Long ESTABLISH_CONNECTION_TIMEOUT_DEFAULT = 3000L;
    private static final Long SOCKET_TIMEOUT_DEFAULT = 5000L;
    private static final int CONNECTION_POOL_SIZE_DEFAULT = 32;
    private static final int MAX_POOLED_PER_ROUTE_DEFAULT = 12;
    private static final String CONNECTION_TTL = "connection-ttl";
    private static final String ESTABLISH_CONNECTION_TIMEOUT = "establish-connection-timeout";
    private static final String MAX_CONNECTION_IDLE_TIME = "max-connection-idle-time";
    private static final String SOCKET_TIMEOUT = "socket-timeout";

    private HttpClientKeycloakBuilder() { }

    /**
     * @param properties
     * @param prefix
     * @return
     */
    public static HttpClient build(Properties properties, String prefix) {
        final HttpClientBuilder builder = new HttpClientBuilder();
        builder.connectionTTL(PropertyUtils.getNumber(properties, prefix + CONNECTION_TTL, CONNECTION_TTL_DEFAULT, Long.class), getTimeUnit(properties, prefix, CONNECTION_TTL));
        builder.connectionPoolSize(PropertyUtils.getNumber(properties, prefix + "connection-pool-size", CONNECTION_POOL_SIZE_DEFAULT, Integer.class));
        builder.disableCookies(PropertyUtils.getBoolean(properties, prefix + "disable-cookies", false));
        if (Boolean.TRUE.equals(PropertyUtils.getBoolean(properties, prefix + "disable-trust-manager", false))) {
            builder.disableTrustManager();
        }
        builder.establishConnectionTimeout(PropertyUtils.getNumber(properties, prefix + ESTABLISH_CONNECTION_TIMEOUT, ESTABLISH_CONNECTION_TIMEOUT_DEFAULT, Long.class), getTimeUnit(properties, prefix, ESTABLISH_CONNECTION_TIMEOUT));
        builder.expectContinueEnabled(PropertyUtils.getBoolean(properties, prefix + "expect-continue-enabled", false));
        builder.hostnameVerification(HttpClientBuilder.HostnameVerificationPolicy.valueOf((String) properties.getOrDefault(prefix + "hostname-verification", HttpClientBuilder.HostnameVerificationPolicy.WILDCARD.name())));
        builder.maxConnectionIdleTime(PropertyUtils.getNumber(properties, prefix + MAX_CONNECTION_IDLE_TIME, MAX_CONNECTION_IDLE_TIME_DEFAULT, Long.class), getTimeUnit(properties, prefix, MAX_CONNECTION_IDLE_TIME));
        builder.maxPooledPerRoute(PropertyUtils.getNumber(properties, prefix + "max-pooled-per-route", MAX_POOLED_PER_ROUTE_DEFAULT, Integer.class));
        builder.reuseConnections(PropertyUtils.getBoolean(properties, prefix + "resuse-connections", true));
        builder.socketTimeout(PropertyUtils.getNumber(properties, prefix + SOCKET_TIMEOUT, SOCKET_TIMEOUT_DEFAULT, Long.class), getTimeUnit(properties, prefix, SOCKET_TIMEOUT));
        return builder.build();
    }

    /**
     * @param config
     * @param prefix
     * @return
     */
    public static HttpClient build(Config.Scope config, String prefix) {
        final HttpClientBuilder builder = new HttpClientBuilder();
        builder.connectionTTL(config.getLong(prefix + CONNECTION_TTL, CONNECTION_TTL_DEFAULT), getTimeUnit(config, prefix, CONNECTION_TTL));
        builder.connectionPoolSize(config.getInt(prefix + "connection-pool-size", CONNECTION_POOL_SIZE_DEFAULT));
        builder.disableCookies(config.getBoolean(prefix + "disable-cookies", false));
        if (Boolean.TRUE.equals(config.getBoolean(prefix + "disable-trust-manager", false))) {
            builder.disableTrustManager();
        }
        builder.establishConnectionTimeout(config.getLong(prefix + ESTABLISH_CONNECTION_TIMEOUT, ESTABLISH_CONNECTION_TIMEOUT_DEFAULT), getTimeUnit(config, prefix, ESTABLISH_CONNECTION_TIMEOUT));
        builder.expectContinueEnabled(config.getBoolean(prefix + "expect-continue-enabled", false));
        builder.hostnameVerification(HttpClientBuilder.HostnameVerificationPolicy.valueOf(config.get(prefix + "hostname-verification", HttpClientBuilder.HostnameVerificationPolicy.WILDCARD.name())));
        builder.maxConnectionIdleTime(config.getLong(prefix + MAX_CONNECTION_IDLE_TIME, MAX_CONNECTION_IDLE_TIME_DEFAULT), getTimeUnit(config, prefix, MAX_CONNECTION_IDLE_TIME));
        builder.maxPooledPerRoute(config.getInt(prefix + "max-pooled-per-route", MAX_POOLED_PER_ROUTE_DEFAULT));
        builder.reuseConnections(config.getBoolean(prefix + "resuse-connections", true));
        builder.socketTimeout(config.getLong(prefix + SOCKET_TIMEOUT, SOCKET_TIMEOUT_DEFAULT), getTimeUnit(config, prefix, SOCKET_TIMEOUT));
        return builder.build();
    }

    private static TimeUnit getTimeUnit(Properties properties, String prefix, String property) {
        if (!properties.containsKey(prefix + property)) {
            return TimeUnit.MILLISECONDS;
        }
        final String value = properties.getProperty(prefix + property + "-unit");
        return value == null ? TimeUnit.MILLISECONDS : TimeUnit.valueOf(value);
    }

    private static TimeUnit getTimeUnit(Config.Scope config, String prefix, String property) {
        if (config.get(prefix + property) == null) {
            return TimeUnit.MILLISECONDS;
        }
        final String value = config.get(prefix + property + "-unit");
        return value == null ? TimeUnit.MILLISECONDS : TimeUnit.valueOf(value);
    }

}
