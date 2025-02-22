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
package org.apache.camel.example.cxf.jaxrs;

import org.apache.camel.example.cxf.jaxrs.resources.Book;
import org.apache.camel.example.cxf.jaxrs.resources.BookNotFoundFault;
import org.apache.camel.example.cxf.jaxrs.resources.BookStore;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.camel.test.spring.junit5.CamelSpringTestSupport;
import org.apache.cxf.BusFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class JAXRSClientServerTest extends CamelSpringTestSupport {
    
    @BeforeAll
    public static void setupPorts() {
        System.setProperty("soapEndpointPort", String.valueOf(AvailablePortFinder.getNextAvailable()));
        System.setProperty("restEndpointPort", String.valueOf(AvailablePortFinder.getNextAvailable()));
    }
    
    @Test
    void testJAXWSClient() throws BookNotFoundFault {
        JAXWSClient jaxwsClient = new JAXWSClient();
        BookStore bookStore = jaxwsClient.getBookStore();
        
        bookStore.addBook(new Book("Camel User Guide", 123L));
        Book book = bookStore.getBook(123L);
        assertNotNull(book, "We should find the book here");
      
        try {
            book = bookStore.getBook(124L);
            fail("We should not have this book");
        } catch (Exception exception) {
            assertTrue(exception instanceof BookNotFoundFault, "The exception should be BookNotFoundFault");
        }
    }
    
    @Test
    void testJAXRSClient() throws BookNotFoundFault {
        // JAXRSClient invocation
        JAXRSClient jaxrsClient = new JAXRSClient();
        BookStore bookStore =  jaxrsClient.getBookStore();
        
        bookStore.addBook(new Book("Camel User Guide", 124L));
        Book book = bookStore.getBook(124L);
        assertNotNull(book, "We should find the book here");
        
        try {
            book = bookStore.getBook(126L);
            fail("We should not have this book");
        } catch (Exception exception) {
            assertTrue(exception instanceof BookNotFoundFault, "The exception should be BookNotFoundFault");
        }
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext(new String[]{"/META-INF/spring/JAXRSCamelContext.xml"});
    }
    
    @Override
    @AfterEach
    public void tearDown() throws Exception {
        super.tearDown();
        BusFactory.setDefaultBus(null);
        BusFactory.setThreadDefaultBus(null);
    }

}
