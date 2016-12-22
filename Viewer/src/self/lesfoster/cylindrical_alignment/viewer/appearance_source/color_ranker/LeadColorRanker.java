/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker;

import self.lesfoster.cylindrical_alignment.viewer.appearance_source.ColorRanker;

/**
 * Gives the appearance of dulling into lead as score decreases.
 *
 * @author Leslie L Foster
 */
public class LeadColorRanker implements ColorRanker {
	public static final float SILVER_LEVEL = 240.0f / 256.0f;
	private static final double MIN_SCORE = 0.0025 * 5;
	private static final double SCORE_RESOLUTION = 0.0025 * 0.75;
	private static final int[] RUSTCOLORS = new int[] { 183, 65, 14 };
		
	// Start out silvery
	private float scoreRed = SILVER_LEVEL;
	private float scoreBlue = SILVER_LEVEL;
	private float scoreGreen = SILVER_LEVEL;

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
		if (scoreRed >= MIN_SCORE) {
			scoreRed -= SCORE_RESOLUTION;
		}
		if (scoreGreen >= MIN_SCORE) {
			scoreGreen -= SCORE_RESOLUTION;
		}
		if (scoreBlue >= MIN_SCORE) {
			scoreBlue -= SCORE_RESOLUTION;
		}
	}
	
	@Override
	public float[] getSelectColor() { return MOLTEN_SELECT_COLOR; }

	@Override
	public void reset() {
		scoreRed = SILVER_LEVEL;
		scoreGreen = SILVER_LEVEL;
		scoreBlue = SILVER_LEVEL;
	}
	
	@Override
	public String toString() {
		return "Leaden";
	}
}
