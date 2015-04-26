/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.geometry;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created with IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 6/18/13
 * Time: 12:14 AM
 *
 * Use this to carry out matrix operations on things.
 */
public class MatrixUtils {

    public static final float RADIAN_CONV = (float) (Math.PI / 180.0);
    public static final float DEGREE_CONV = (float) (180.0 / Math.PI);
    static {
        MatrixUtils u = new MatrixUtils();
        u.setSystemConv(RADIAN_CONV);
        float[] val = new float[] { 0.0f, 1.0f, 0.0f };
        u.matrixMult( val, u.createRotationX( 90.0f ) );
        //Log.d("MatrixUtils-DEBUG", "After rot-X, value is " + val[0]+","+val[1]+","+val[2]);
        u.matrixMult( val, u.createRotationY( 90.0f ) );
        //Log.d("MatrixUtils-DEBUG", "After rot-Y, value is " + val[0]+","+val[1]+","+val[2]);
        u.matrixMult( val, u.createRotationZ( 90.0f ) );
        //Log.d("MatrixUtils-DEBUG", "After rot-Z, value is " + val[0]+","+val[1]+","+val[2]);
    }
    private static int X_INX = 0;
    private static int Y_INX = 1;
    private static int Z_INX = 2;

    private float systemConv = RADIAN_CONV;

    public void setSystemConv( float conversion ) {
        this.systemConv = conversion;    
    }
    
    /*
     Rotations:
  Rotation about the X axis by an angle a:
  |1       0        0    0|
  |0  cos(a)  -sin(a)    0|
  |0  sin(a)   cos(a)    0|
  |0       0        0    1|

  Rotation about the Y axis by an angle a:
  | cos(a)  0  sin(a)    0|
  |      0  1       0    0|
  |-sin(a)  0  cos(a)    0|
  |      0  0       0    1|

  Rotation about the Z axis by an angle a:
  |cos(a)  -sin(a)  0   0|
  |sin(a)   cos(a)  0   0|
  |     0        0  1   0|
  |     0        0  0   1|
     */

    // NEXT: Multiply all by the matrix.
//    public void rotateGeometryAboutX( GeometryInfo gi, float phi ) {
//        float[][] rotX = createRotationX( phi );
//        float[] coords = gi.getCoordinates();
//        //NOTE: assumption is, no need to re-set coords, because reference has been handed back above.
//        matrixMult( coords, rotX );
//    }
//
//    public void rotateGeometryAboutY( GeometryInfo gi, float theta ) {
//        float[][] rotY = createRotationY(theta);
//        float[] coords = gi.getCoordinates();
//        //NOTE: assumption is, no need to re-set coords, because reference has been handed back above.
//        matrixMult( coords, rotY );
//    }
//
//    public void rotateGeometryAboutZ( GeometryInfo gi, float psi ) {
//        float[][] rotZ = createRotationZ(psi);
//        float[] coords = gi.getCoordinates();
//        //NOTE: assumption is, no need to re-set coords, because reference has been handed back above.
//        matrixMult( coords, rotZ );
//    }

    public float[] makeColMajor( float[][] twoDMatrix ) {
        float[] rtnVal = new float[ twoDMatrix.length * twoDMatrix[ 0 ].length ];
        for ( int rowInx = 0; rowInx < twoDMatrix.length; rowInx ++ ) {
            for ( int colInx = 0; colInx < twoDMatrix[ 0 ].length; colInx ++ ) {
                rtnVal[ colInx * twoDMatrix[ 0 ].length + rowInx ] = twoDMatrix[ rowInx ][ colInx ];
            }
        }

        return rtnVal;
    }

    public float[][] fromColMajor( float[] oneDMatrix ) {
        float[][] rtnVal = null;
        if ( oneDMatrix.length == 16 ) {
            rtnVal = new float[][] {
                new float[ 4 ], new float[ 4 ], new float[ 4 ], new float[ 4 ]
            };

            int oneDInx = 0;
            for ( int i = 0; i < 4; i++ ) {
                for ( int j = 0; j < 4; j++ ) {
                    rtnVal[ j ][ i ] = oneDMatrix[ oneDInx ++ ];
                }
            }
        }

        return rtnVal;
    }

    public static float[][] times( float[][] coords, float[][] multBy ) {
        if ( coords == null  ||  multBy == null  ||  coords.length < 1 )
            throw new IllegalArgumentException( "Empty matrix" );

        if ( coords[0].length != coords.length  ||  multBy.length != multBy[ 0 ].length  ||  coords.length != multBy.length ) {
            throw new IllegalArgumentException( "All array lengths of both matrices must be equal." );
        }

        int size = coords.length;

        // No assumptions about matrix size or content.  Just multiply all by all.
        float[][] temp = new float[coords[0].length][coords.length];
        for ( int nRow = 0; nRow < size; nRow++ ) {
            for ( int nCol = 0; nCol < size; nCol ++ ) {
                for ( int oRow = 0; oRow < size; oRow++ ) {
                    temp[ oRow ][ nCol ] += coords[ nRow ][ nCol ] * multBy[ oRow ][ nCol ];
                }
            }
        }

        return temp;
    }

    /**
     * Ax = ByCz - BzCy
     * Ay = BzCx - BxCz
     * Az = BxCy - ByCx
     */
    public float[] cross(float[] b, float[] c) {
        return new float[] {
                b[ Y_INX ]*c[ Z_INX ] - b[ Z_INX ]*c[ Y_INX ],
                b[ Z_INX ]*c[ X_INX ] - b[ X_INX ]*c[ Z_INX ],
                b[ X_INX ]*c[ Y_INX ] - b[ Y_INX ]*c[ X_INX ]
        };
    }

    public float[] normalize( float[] vector ) {
        float[] rtnVal = new float[ vector.length ];
        double sumSquares = 0.0f;
        for ( int i = 0; i < vector.length; i++ ) {
            sumSquares += vector[ i ] * vector[ i ];
        }

        Double magnitude = Math.sqrt( sumSquares );

        if ( magnitude == 0 ) {
            //Log.e("MU-NaN", "Seeing zero-divide for normalize of " + vector[0] + "," + vector[1] + "," + vector[2]);
        }
        for ( int i = 0; i < vector.length; i++ ) {
            rtnVal[ i ] = vector[ i ] / magnitude.floatValue();
        }

        return rtnVal;
    }

    public void matrixMult( float[] coords, float[][] multBy ) {
        // Coords are organized into triplets per point.
        for ( int i = 0; i < coords.length; i+= 3 ) {   // Coordinate triplet iterator.
            float[] temp = new float[ 3 ];
            for ( int k = 0; k < 3; k++ ) {      // Multby's col iterator.
                for ( int j = 0; j < 3; j++ ) {  // Multby's row iterator.
                    temp[ k ] += coords[ i+j ] * multBy[ j ][ k ];
                }
            }
            // Store info back into coordinates.
            for ( int j = 0; j < 3; j++ ) {
                coords[ i+j ] = temp[ j ];
            }
        }
    }

    // http://www.cprogramming.com/tutorial/3d/rotationMatrices.html
    public float[][] createRotationX( float phi ) {
        float adjustedAngle = systemConv * phi;
        float[][] rotationMatrix = new float[][] {
                { 1.0f,  0.0f,                       0.0f,                      },
                { 0.0f, (float)cos(adjustedAngle),  (float)-sin(adjustedAngle), },
                { 0.0f, (float)sin(adjustedAngle),  (float)cos(adjustedAngle),  },
        };
        return rotationMatrix;
    }

    public float[][] createRotationY( float theta ) {
        float adjustedAngle = systemConv * theta;
        float[][] rotationMatrix = new float[][] {
                { (float)cos(adjustedAngle),  0.0f,  (float)sin(adjustedAngle), },
                { 0.0f,                       1.0f,  0.0f,                      },
                { (float)-sin(adjustedAngle), 0.0f,  (float)cos(adjustedAngle), },
        };
        return rotationMatrix;
    }

    public float[][] pad3to4( float[][] threeDimMatrix ) {
        float[][] rtnVal = new float[][] {
            new float[ 4 ], new float[ 4 ], new float[ 4 ], new float[ 4 ]
        };
        for ( int i = 0; i < 3; i ++ ) {
            for ( int j = 0; j < 3; j++ ) {
                rtnVal[ i ][ j ] = threeDimMatrix[ i ][ j ];
            }
        }
        rtnVal[ 3 ][ 3 ] = 1.0f;
        return rtnVal;
    }


    public float[][] createRotationZ( float psi ) {
        float adjustedAngle = systemConv * psi;
        float[][] rotationMatrix = new float[][] {
                { (float)cos(adjustedAngle),    (float)-sin(adjustedAngle),  0.0f, },
                { (float)sin(adjustedAngle),    (float)cos(adjustedAngle),   0.0f, },
                { 0.0f,                         0.0f,                        1.0f, },
        };
        return rotationMatrix;
    }

    // computes inverse of affine matrix. assumes last row is [0,0,0,1]
    public float[][] inv(float[][] m) {
        float[][] r = new float[4][4];            // default constructor initializes it to identity
        //assert(isAffine(m));
        float det = m[0][0]*(m[1][1]*m[2][2] - m[1][2]*m[2][1]) +
                m[0][1]*(m[1][2]*m[2][0] - m[1][0]*m[2][2]) +
                m[0][2]*(m[1][0]*m[2][1] - m[1][1]*m[2][0]);

        // check non-singular matrix
        //assert(std::abs(det) > CS175_EPS3);

        // "rotation part"
        r[0][0] =  (m[1][1] * m[2][2] - m[1][2] * m[2][1]) / det;
        r[1][0] = -(m[1][0] * m[2][2] - m[1][2] * m[2][0]) / det;
        r[2][0] =  (m[1][0] * m[2][1] - m[1][1] * m[2][0]) / det;
        r[0][1] = -(m[0][1] * m[2][2] - m[0][2] * m[2][1]) / det;
        r[1][1] =  (m[0][0] * m[2][2] - m[0][2] * m[2][0]) / det;
        r[2][1] = -(m[0][0] * m[2][1] - m[0][1] * m[2][0]) / det;
        r[0][2] =  (m[0][0] * m[1][2] - m[0][2] * m[1][1]) / det;
        r[1][2] = -(m[0][0] * m[1][2] - m[0][2] * m[1][0]) / det;
        r[2][2] =  (m[0][0] * m[1][1] - m[0][1] * m[0][1]) / det;

        // "translation part" - multiply the translation (on the left) by the inverse linear part
        r[0][3] = -(m[0][3] * r[0][0] + m[1][3] * r[0][1] + m[2][3] * r[0][2]);
        r[1][3] = -(m[0][3] * r[1][0] + m[1][3] * r[1][1] + m[2][3] * r[1][2]);
        r[2][3] = -(m[0][3] * r[2][0] + m[1][3] * r[2][1] + m[2][3] * r[2][2]);
        //assert(isAffine(r) && norm2(Matrix4() - m*r) < CS175_EPS2);
        return r;
    }

    float[][] transpose(float[][] m) {
        float[][] r = new float[4][4];
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                r[i][j] = m[j][i];
            }
        }
        return r;
    }

    float[][] normalMatrix(float[][] m) {
        float[][] invm = inv(m);
        invm[0][3] = invm[1][3] = invm[2][3] = 0;
        return transpose(invm);
    }

    public float[][] newIdentity( int dim ) {
        float[][] rtnVal = new float[ dim ][];
        for ( int i = 0; i < dim; i++ ) {
            rtnVal[ i ] = new float[ dim ];
        }

        for ( int i = 0; i < dim; i++ ) {
            rtnVal[ i ][ i ] = 1.0f;
        }

        return rtnVal;
    }
}

