package self.lesfoster.cylindrical_alignment.data_source.converter;

import self.lesfoster.cylindrical_alignment.data_source.GeneralXsltConverter;
import java.io.*;

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
