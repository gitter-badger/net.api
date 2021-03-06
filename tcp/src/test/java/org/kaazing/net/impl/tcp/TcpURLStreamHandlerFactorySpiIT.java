/*
 * Copyright 2014, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaazing.net.impl.tcp;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Rule;
import org.junit.Test;
import org.kaazing.net.URLFactory;
import org.kaazing.robot.junit.annotation.Robotic;
import org.kaazing.robot.junit.rules.RobotRule;

public class TcpURLStreamHandlerFactorySpiIT {

    @Rule
    public RobotRule robot = new RobotRule();

    @Robotic(script = "echo.then.closed")
    @Test(timeout = 1000)
    public void shouldEcho() throws Exception {
        URL location = URLFactory.createURL("tcp://localhost:61234");

        URLConnection connection = location.openConnection();
        connection.connect();
        OutputStream out = connection.getOutputStream();
        InputStream in = connection.getInputStream();

        out.write("Hello, world".getBytes(UTF_8));
        byte[] buf = new byte[32];
        int len = in.read(buf);

        out.close();
        in.close();

        robot.join();

        assertEquals(12, len);
        assertEquals("Hello, world", new String(buf, 0, 12, UTF_8));
    }

}
