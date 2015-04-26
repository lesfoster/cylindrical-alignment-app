/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.geometry;

/**
 * Delegate normals (perpendicular vectors to surfaces) generation to this class.
 *
 * Created by Leslie L Foster on 12/30/13.
 */
public class TriangleNormalGenerator {
    private MatrixUtils matrixUtils;

    public TriangleNormalGenerator() {
        matrixUtils = new MatrixUtils();
    }

    //public void generateSubHitNormals( Node node ) {
    //    if ( node instanceof Shape3D ) {
    //        generateSubHitNormals( (Shape3D)node );
    //    }
    //    if ( node.getChildren() != null ) {
    //       for ( Node child: node.getChildren() ) {
    //            generateSubHitNormals( child );
    //        }
    //    }
    //}

    /**
     * Normals for all labels in this app, are pointed towards Z.
     */
    public float[] generateLabelNormals( float[] coordinates ) {
        float[] normals = new float[ coordinates.length ];
        for ( int i = 0; i < normals.length; i+=3 ) {
            normals[ i ] = 0.0f;
            normals[ i+1 ] = 0.0f;
            normals[ i+2 ] = 1.0f;
        }
        return normals;
    }

    /**
     * Carry out normal creation (and set) against the triangles making up a cigar band.
     */
    public float[] generateCigarBandNormals( float[] coordinates ) {
        float[] normals = new float[ coordinates.length ];
        for ( int i = 0; i < normals.length; i+= 3 ) {
            normals[ i ] = 1.0f;
            normals[ i + 1 ] = 0.0f;
            normals[ i + 2 ] = 0.0f;
        }
        return normals;
    }

    public float[] generateRuleNormals( float[] coordinates ) {
        return generateGeometryNormals( coordinates );
    }

    /**
     * Uses an algorithm of N = (p2 - p0) X (p1 - p0).
     * @param shape contains/receives geometry.
     */
    public void generateSubHitNormals( float[] coords ) {
        generateGeometryNormals(coords);

    }

    private int getOffset( byte[] indices, int inxOffs ) {
        if ( indices != null ) {
            return indices[ inxOffs ];
        }
        else {
            return inxOffs;
        }
    }

    private float[] generateGeometryNormals(float[] coords) {
        byte[] indices = null; // Do not yet support indices.
        int maxTriple = 0;
        if ( indices != null  &&  indices.length % 3 != 0 ) {
            throw new IllegalArgumentException("Invalid index-count for triangles.");
        }

        if ( indices != null ) {
            maxTriple = indices.length;
        }
        else {
            maxTriple = coords.length / 3;
        }

        float[] normals = new float[ coords.length ];
        for ( int i = 0; i < maxTriple; i+= 3 ) {
            // Iterating over one triangle at a time, using the indices.

            int inxOffs = i;
            int offs0 = getOffset( indices, inxOffs ) * 3;
            int offs1 = getOffset( indices, inxOffs + 1 ) * 3;
            int offs2 = getOffset( indices, inxOffs + 2 ) * 3;

            // Need to build matrices representing the vertices as subtracted.
            float magMult = 100.0f;
            float[] p2_minus_p0 = new float[] {
                    magMult * (coords[ offs2 ] - coords[ offs0 ]),
                    magMult * (coords[ offs2 + 1 ] - coords[ offs0 + 1 ]),
                    magMult * (coords[ offs2 + 2 ] - coords[ offs0 + 2 ])
            };

            float[] p1_minus_p0 = new float[] {
                    magMult * (coords[ offs1 ] - coords[ offs0 ]),
                    magMult * (coords[ offs1 + 1 ] - coords[ offs0 + 1 ]),
                    magMult * (coords[ offs1 + 2 ] - coords[ offs0 + 2 ])
            };

            float[] crossProd = matrixUtils.normalize( matrixUtils.cross(p2_minus_p0, p1_minus_p0) );

            // All of the vertices of the current triangle should be the same.
            normals[ offs0 ] = crossProd[ 0 ];
            normals[ offs0 + 1 ] = crossProd[ 1 ];
            normals[ offs0 + 2 ] = crossProd[ 2 ];

            normals[ offs1 ] = crossProd[ 0 ];
            normals[ offs1 + 1 ] = crossProd[ 1 ];
            normals[ offs1 + 2 ] = crossProd[ 2 ];

            normals[ offs2 ] = crossProd[ 0 ];
            normals[ offs2 + 1 ] = crossProd[ 1 ];
            normals[ offs2 + 2 ] = crossProd[ 2 ];
        }

		/*
        if ( gi.getDebugId().equals( GeometryInfo.MARKER_DEBUG_ID ) ) {
            Log.i("Gen-Hdr", "Normals=======================");
            float[] points = gi.getCoordinates();
            for ( int i = 0; i < normals.length; i += 3 ) {
                Log.i( "Gen-Num"+i, "Number: " + i + ", Point: " + i / 3 );
                Log.i( "Gen-Pt"+i, "POINT: [" + points[(i)] + "],[" + points[(i) + 1] + "],[" + points[(i) + 2] + "]");
                Log.i( "Gen-Norm"+i, "NORMAL: [" + normals[ (i)] + "],[" + normals[ (i)+1 ] + "],[" + normals[ (i) + 2 ] + "]" );
            }
        }
	    */
        return( normals );
    }

}

