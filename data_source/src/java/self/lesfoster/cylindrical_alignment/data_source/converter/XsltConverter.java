package self.lesfoster.cylindrical_alignment.data_source.converter;

import self.lesfoster.cylindrical_alignment.data_source.GeneralXsltConverter;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 6/4/13
 * Time: 7:52 AM
 *
 * Given a properly-named file, this will use a general XSLT converter to convert it and save the output.
 */
public class XsltConverter {
    public static void main( String[] args ) throws Exception {
        if ( args.length == 0 ) {
            throw new IllegalArgumentException("Usage: java " + XsltConverter.class.getName() + " <infile>" );
        }
        XsltConverter converter = new XsltConverter();
        converter.exec( args[ 0 ] );
    }

    public XsltConverter() {

    }

    public void exec( String filename ) throws Exception {
        GeneralXsltConverter fileConverter = GeneralXsltConverter.getConverter( filename );
        InputStream is = fileConverter.getStream();

        FileOutputStream fos = new FileOutputStream( filename + ".gff" );
        int inChar = 0;
        while ( -1 != ( inChar = is.read() ) ) {
            fos.write( inChar );
        }

        is.close();
        fos.close();
    }
}
