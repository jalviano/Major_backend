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
package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests {@link org.apache.commons.lang3.SerializationUtils}.
 */
public class SerializationUtilsTest {

  static final String CLASS_NOT_FOUND_MESSAGE = "ClassNotFoundSerialization.readObject fake exception";
    protected static final String SERIALIZE_IO_EXCEPTION_MESSAGE = "Anonymous OutputStream I/O exception";

    private String iString;
    private Integer iInteger;
    private HashMap<Object, Object> iMap;

    @Before
    public void setUp() {
        iString = "foo";
        iInteger = Integer.valueOf(7);
        iMap = new HashMap<>();
        iMap.put("FOO", iString);
        iMap.put("BAR", iInteger);
    }

    //-----------------------------------------------------------------------

    @Test
    public void testConstructor() {
        assertNotNull(new SerializationUtils());
        final Constructor<?>[] cons = SerializationUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertTrue(Modifier.isPublic(cons[0].getModifiers()));
        assertTrue(Modifier.isPublic(SerializationUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(SerializationUtils.class.getModifiers()));
    }

    @Test
    public void testException() {
        SerializationException serEx;
        final Exception ex = new Exception();

        serEx = new SerializationException();
        assertSame(null, serEx.getMessage());
        assertSame(null, serEx.getCause());

        serEx = new SerializationException("Message");
        assertSame("Message", serEx.getMessage());
        assertSame(null, serEx.getCause());

        serEx = new SerializationException(ex);
        assertEquals("java.lang.Exception", serEx.getMessage());
        assertSame(ex, serEx.getCause());

        serEx = new SerializationException("Message", ex);
        assertSame("Message", serEx.getMessage());
        assertSame(ex, serEx.getCause());
    }

    //-----------------------------------------------------------------------

    @Test
    public void testSerializeStreamObjNull() throws Exception {
        try {
            SerializationUtils.serialize(iMap, null);
        } catch (final IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    @Test
    public void testSerializeStreamNullNull() throws Exception {
        try {
            SerializationUtils.serialize(null, null);
        } catch (final IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    //-----------------------------------------------------------------------

    @Test
    public void testDeserializeStreamNull() throws Exception {
        try {
            SerializationUtils.deserialize((InputStream) null);
        } catch (final IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    //-----------------------------------------------------------------------

    @Test
    public void testDeserializeBytesNull() throws Exception {
        try {
            SerializationUtils.deserialize((byte[]) null);
        } catch (final IllegalArgumentException ex) {
            return;
        }
        fail();
    }

    //-----------------------------------------------------------------------

    @Test
    public void testCloneNull() throws Exception {
        final Object test = SerializationUtils.clone(null);
        assertNull(test);
    }

}

class ClassNotFoundSerialization implements Serializable {

    private static final long serialVersionUID = 1L;

    private void readObject(final ObjectInputStream in) throws ClassNotFoundException    {
        throw new ClassNotFoundException(SerializationUtilsTest.CLASS_NOT_FOUND_MESSAGE);
    }
}
