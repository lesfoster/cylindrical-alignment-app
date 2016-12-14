/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker;

import self.lesfoster.cylindrical_alignment.viewer.appearance_source.ColorRanker;

/**
 * Gives the appearance of advancing rust as the score decreases.
 *
 * @author Leslie L Foster
 */
public class RustColorRanker implements ColorRanker {
	private final static double SCORE_RESOLUTION = 0.0025 * 0.75;
	private final static double BLUE_RESOLUTION = 0.0025 * 0.5;
	private final static int[] RUSTCOLORS = new int[] { 183, 65, 14 };
		
	// Start out silvery.
	private float scoreRed = 240.0f / 256.0f;
	private float scoreBlue = 240.0f / 256.0f;
	private float scoreGreen = 240.0f / 256.0f;

	@Override
	public float getScoreRed() {
		return scoreRed;
	}

	@Override
	public float getScoreGreen() {
		return scoreBlue;
	}

	@Override
	public float getScoreBlue() {
		return scoreGreen;
	}

	@Override
	public void decrementRank() {
		if (scoreRed >= RUSTCOLORS[0] / 256.0) {
			scoreRed -= SCORE_RESOLUTION;
		}
		if (scoreGreen >= RUSTCOLORS[1] / 256.0) {
			scoreGreen -= SCORE_RESOLUTION;
		}
		if (scoreBlue >= RUSTCOLORS[2] / 256.0) {
			scoreBlue -= BLUE_RESOLUTION;
		}
	}	
}
