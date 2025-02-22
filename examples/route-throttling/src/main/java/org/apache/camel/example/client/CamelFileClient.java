/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.example.client;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Client that uses the {@link org.apache.camel.ProducerTemplate} to easily exchange messages with the Server.
 */
public final class CamelFileClient {

    private static final int SIZE = 5000;

    private CamelFileClient() {
        // Helper class
    }

    public static void main(final String[] args) throws Exception {
        try (ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("camel-file-client.xml")) {
            // get the camel template for Spring template style sending of messages (= producer)
            try (final ProducerTemplate producer = context.getBean("camelTemplate", ProducerTemplate.class)) {

                // now send a lot of messages
                System.out.println("Writing files ...");

                for (int i = 0; i < SIZE; i++) {
                    producer.sendBodyAndHeader("file:target//inbox", "File " + i, Exchange.FILE_NAME, i + ".txt");
                }

                System.out.println("... Wrote " + SIZE + " files");
            }
        }
    }

}