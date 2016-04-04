/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.input_stream_source;
import java.io.InputStream;

/**
 * Implement one of these to supply the raw data feed for various types of parsers in use here.
 * Created by Leslie L Foster on 11/23/2015.
 */
public interface InputStreamSource {
    InputStream getInputStream();
}
