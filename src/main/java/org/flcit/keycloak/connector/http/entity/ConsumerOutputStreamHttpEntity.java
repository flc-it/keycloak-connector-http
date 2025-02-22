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

package org.flcit.keycloak.connector.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.AbstractHttpEntity;

import org.flcit.commons.core.functional.consumer.ConsumerIOException;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public class ConsumerOutputStreamHttpEntity extends AbstractHttpEntity {

    private final ConsumerIOException<OutputStream> consumer;

    /**
     * @param consumer
     */
    public ConsumerOutputStreamHttpEntity(ConsumerIOException<OutputStream> consumer) {
        this.consumer = consumer;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public long getContentLength() {
        return -1;
    }

    @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeTo(OutputStream outStream) throws IOException {
        consumer.accept(outStream);
    }

    @Override
    public boolean isStreaming() {
        return true;
    }

}
