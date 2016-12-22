/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source;

import javafx.scene.paint.Color;

/**
 * Implement this to provide a color ranking scheme.
 *
 * @author Leslie L Foster
 */
public interface ColorRanker {
	public static final float[] GOLDEN_SELECT_COLOR = new float[] { (float)Color.GOLD.getRed(), (float)Color.GOLD.getGreen(), (float)Color.GOLD.getBlue() };
	public static final float[] MOLTEN_SELECT_COLOR = new float[] { 1.0f, 165f/256f, 0.0f }; //16f*12f / 256.0f, 35.0f/255.0f, 42.0f/256.0f }; //#c0232a
	
	float getScoreRed();
	float getScoreGreen();
	float getScoreBlue();
	float[] getSelectColor();
	void decrementRank();	
	void reset();
}
