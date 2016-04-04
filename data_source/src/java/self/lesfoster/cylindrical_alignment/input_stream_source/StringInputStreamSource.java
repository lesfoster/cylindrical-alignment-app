/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

