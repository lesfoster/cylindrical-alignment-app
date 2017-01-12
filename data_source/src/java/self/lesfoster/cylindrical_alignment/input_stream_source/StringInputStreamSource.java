/*
 CDDL HEADER START

 The contents of this file are subject to the terms of the
 Common Development and Distribution License (the "License").
 You may not use this file except in compliance with the License.

 You can obtain a copy of the license at
   https://opensource.org/licenses/CDDL-1.0.
 See the License for the specific language governing permissions
 and limitations under the License.

 When distributing Covered Code, include this CDDL HEADER in each
 file and include the License file at
    https://opensource.org/licenses/CDDL-1.0.
 If applicable, add the following below this CDDL HEADER, with the
 fields enclosed by brackets "[]" replaced with your own identifying
 information: Portions Copyright [yyyy] [name of copyright owner]

 CDDL HEADER END
*/


package self.lesfoster.cylindrical_alignment.input_stream_source;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Simplistic input stream source, making a stream from a string.
 * Created by Leslie L Foster on 11/23/2015.
 */
public class StringInputStreamSource implements InputStreamSource {
    private final String inputString;
    public StringInputStreamSource(String inputString) {
        this.inputString = inputString;
    }
    public InputStream getInputStream() {
        return new ByteArrayInputStream(inputString.getBytes());
    }
}

