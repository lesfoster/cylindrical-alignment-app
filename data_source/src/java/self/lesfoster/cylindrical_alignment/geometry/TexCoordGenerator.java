/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.geometry;

/**
 * This will create texture coords to simply match the coordinates.
 * @author Leslie L Foster
 */
public class TexCoordGenerator {
	public float[] generateTexCoords( float[] vertexCoords ) {
		// This is how to make dummy texture coordinates.
		return new float[] { 0.0f, 1.0f };
	}
	
}
