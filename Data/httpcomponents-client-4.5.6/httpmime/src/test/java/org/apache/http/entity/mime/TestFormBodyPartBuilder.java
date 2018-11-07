/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.entity.mime;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.junit.Assert;
import org.junit.Test;

public class TestFormBodyPartBuilder {

    @Test
    public void testBuildBodyPartBasics() throws Exception {
        final StringBody stringBody = new StringBody("stuff", ContentType.TEXT_PLAIN);
        final FormBodyPart bodyPart = FormBodyPartBuilder.create()
                .setName("blah")
                .setBody(stringBody)
                .build();
        Assert.assertNotNull(bodyPart);
        Assert.assertEquals("blah", bodyPart.getName());
        Assert.assertEquals(stringBody, bodyPart.getBody());
        final Header header = bodyPart.getHeader();
        Assert.assertNotNull(header);
        assertFields(Arrays.asList(
                        new MinimalField("Content-Disposition", "form-data; name=\"blah\""),
                        new MinimalField("Content-Type", "text/plain; charset=ISO-8859-1"),
                        new MinimalField("Content-Transfer-Encoding", "8bit")),
                header.getFields());
    }

    @Test
    public void testCharacterStuffing() throws Exception {
        final FormBodyPartBuilder builder = FormBodyPartBuilder.create();
        final InputStreamBody fileBody = new InputStreamBody(new ByteArrayInputStream(
                "hello world".getBytes("UTF-8")), "stuff_with \"quotes\" and \\slashes\\.bin");
        final FormBodyPart bodyPart2 = builder
                .setName("yada_with \"quotes\" and \\slashes\\")
                .setBody(fileBody)
                .build();

        Assert.assertNotNull(bodyPart2);
        Assert.assertEquals("yada_with \"quotes\" and \\slashes\\", bodyPart2.getName());
        Assert.assertEquals(fileBody, bodyPart2.getBody());
        final Header header2 = bodyPart2.getHeader();
        Assert.assertNotNull(header2);
        assertFields(Arrays.asList(
                        new MinimalField("Content-Disposition", "form-data; name=\"yada_with \\\"quotes\\\" " +
                                "and \\\\slashes\\\\\"; filename=\"stuff_with \\\"quotes\\\" and \\\\slashes\\\\.bin\""),
                        new MinimalField("Content-Type", "application/octet-stream"),
                        new MinimalField("Content-Transfer-Encoding", "binary")),
                header2.getFields());
    }

    @Test
    public void testBuildBodyPartMultipleBuilds() throws Exception {
        final StringBody stringBody = new StringBody("stuff", ContentType.TEXT_PLAIN);
        final FormBodyPartBuilder builder = FormBodyPartBuilder.create();
        final FormBodyPart bodyPart1 = builder
                .setName("blah")
                .setBody(stringBody)
                .build();
        Assert.assertNotNull(bodyPart1);
        Assert.assertEquals("blah", bodyPart1.getName());
        Assert.assertEquals(stringBody, bodyPart1.getBody());
        final Header header1 = bodyPart1.getHeader();
        Assert.assertNotNull(header1);
        assertFields(Arrays.asList(
                        new MinimalField("Content-Disposition", "form-data; name=\"blah\""),
                        new MinimalField("Content-Type", "text/plain; charset=ISO-8859-1"),
                        new MinimalField("Content-Transfer-Encoding", "8bit")),
                header1.getFields());
        final FileBody fileBody = new FileBody(new File("/path/stuff.bin"), ContentType.DEFAULT_BINARY);
        final FormBodyPart bodyPart2 = builder
                .setName("yada")
                .setBody(fileBody)
                .build();

        Assert.assertNotNull(bodyPart2);
        Assert.assertEquals("yada", bodyPart2.getName());
        Assert.assertEquals(fileBody, bodyPart2.getBody());
        final Header header2 = bodyPart2.getHeader();
        Assert.assertNotNull(header2);
        assertFields(Arrays.asList(
                        new MinimalField("Content-Disposition", "form-data; name=\"yada\"; filename=\"stuff.bin\""),
                        new MinimalField("Content-Type", "application/octet-stream"),
                        new MinimalField("Content-Transfer-Encoding", "binary")),
                header2.getFields());
    }

    @Test
    public void testBuildBodyPartCustomHeaders() throws Exception {
        final StringBody stringBody = new StringBody("stuff", ContentType.TEXT_PLAIN);
        final FormBodyPartBuilder builder = FormBodyPartBuilder.create("blah", stringBody);
        final FormBodyPart bodyPart1 = builder
                .addField("header1", "blah")
                .addField("header3", "blah")
                .addField("header3", "blah")
                .addField("header3", "blah")
                .addField("header3", "blah")
                .addField("header3", "blah")
                .build();

        Assert.assertNotNull(bodyPart1);
        final Header header1 = bodyPart1.getHeader();
        Assert.assertNotNull(header1);

        assertFields(Arrays.asList(
                new MinimalField("header1", "blah"),
                new MinimalField("header3", "blah"),
                new MinimalField("header3", "blah"),
                new MinimalField("header3", "blah"),
                new MinimalField("header3", "blah"),
                new MinimalField("header3", "blah"),
                new MinimalField("Content-Disposition", "form-data; name=\"blah\""),
                new MinimalField("Content-Type", "text/plain; charset=ISO-8859-1"),
                new MinimalField("Content-Transfer-Encoding", "8bit")),
                header1.getFields());

        final FormBodyPart bodyPart2 = builder
                .setField("header2", "yada")
                .removeFields("header3")
                .build();

        Assert.assertNotNull(bodyPart2);
        final Header header2 = bodyPart2.getHeader();
        Assert.assertNotNull(header2);

        assertFields(Arrays.asList(
                        new MinimalField("header1", "blah"),
                        new MinimalField("header2", "yada"),
                        new MinimalField("Content-Disposition", "form-data; name=\"blah\""),
                        new MinimalField("Content-Type", "text/plain; charset=ISO-8859-1"),
                        new MinimalField("Content-Transfer-Encoding", "8bit")),
                header2.getFields());

        final FormBodyPart bodyPart3 = builder
                .addField("Content-Disposition", "disposition stuff")
                .addField("Content-Type", "type stuff")
                .addField("Content-Transfer-Encoding", "encoding stuff")
                .build();

        Assert.assertNotNull(bodyPart3);
        final Header header3 = bodyPart3.getHeader();
        Assert.assertNotNull(header3);

        assertFields(Arrays.asList(
                        new MinimalField("header1", "blah"),
                        new MinimalField("header2", "yada"),
                        new MinimalField("Content-Disposition", "disposition stuff"),
                        new MinimalField("Content-Type", "type stuff"),
                        new MinimalField("Content-Transfer-Encoding", "encoding stuff")),
                header3.getFields());

    }

    private static void assertFields(final List<MinimalField> expected, final List<MinimalField> result) {
        Assert.assertNotNull(result);
        Assert.assertEquals(expected.size(), result.size());
        for (int i = 0; i < expected.size(); i++) {
            final MinimalField expectedField = expected.get(i);
            final MinimalField resultField = result.get(i);
            Assert.assertNotNull(resultField);
            Assert.assertEquals(expectedField.getName(), resultField.getName());
            Assert.assertEquals(expectedField.getBody(), resultField.getBody());
        }
    }

}
