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


package self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker;

import self.lesfoster.cylindrical_alignment.viewer.appearance_source.ColorRanker;

/**
 * Runs down each primary color in the order given.
 *
 * @author Leslie L Foster
 */
public class TripOrderColorRanker implements ColorRanker {
	public enum TripOrder {		
		BRG(new float[] {1.0f, 0.0f,  1.0f}),
		BGR(new float[] {0,    1.0f,  1.0f}),
		RGB(new float[] {1.0f, 1.0f,  0f}),
		RBG(new float[] {1.0f, 0,     1.0f}),
		GRB(new float[] {1.0f, 1.0f,  0.0f}),
		GBR(new float[] {0,    1.0f,  1.0f});				
		
		private float[] baseColor;
		
		TripOrder(float[] baseColor) {
			this.baseColor = baseColor;
		}
		
		public float[] getBaseColor() { return baseColor; }
	}

	private static final double MIN_SCORE = 0.2;
	private static final double SCORE_RESOLUTION = 0.003;
	
	private float scoreRed;
	private float scoreGreen;
	private float scoreBlue;
	
	private TripOrder tripOrder = TripOrder.RGB;
	
	public TripOrderColorRanker(TripOrder tripOrder) {
		this.tripOrder = tripOrder;
		scoreRed = tripOrder.getBaseColor()[0];
		scoreGreen = tripOrder.getBaseColor()[1];
		scoreBlue = tripOrder.getBaseColor()[2];
	}

	@Override
	public float getScoreRed() {
		return scoreRed;
	}

	@Override
	public float getScoreGreen() {
		return scoreGreen;
	}

	@Override
	public float getScoreBlue() {
		return scoreBlue;
	}

	@Override
	public void decrementRank() {
		switch(tripOrder) {
			case BRG: brgDimOrder();
			          break;
			case BGR: bgrDimOrder();
			          break;
			case GBR: gbrDimOrder();
			          break;
			case GRB: grbDimOrder();
			          break;
			case RBG: rbgDimOrder();
			          break;
			case RGB: rgbDimOrder();
			          break;
		}
	}
	
	@Override
	public void reset() {
		scoreRed = tripOrder.getBaseColor()[0];
		scoreGreen = tripOrder.getBaseColor()[1];
		scoreBlue = tripOrder.getBaseColor()[2];		
	}
	
	@Override
	public String toString() {
		return tripOrder.toString();
	}
	
	@Override
	public float[] getSelectColor() {
		return GOLDEN_SELECT_COLOR;
	}
	
	private void brgDimOrder() {
		if (scoreBlue >= MIN_SCORE) {
			scoreBlue -= SCORE_RESOLUTION;
		}
		else if (scoreRed >= MIN_SCORE) {
			scoreRed -= SCORE_RESOLUTION;
		}
		else if (scoreGreen >= MIN_SCORE) {
			scoreGreen -= SCORE_RESOLUTION;
		}
	}
	
	private void bgrDimOrder() {
		if (scoreBlue >= MIN_SCORE) {
			scoreBlue -= SCORE_RESOLUTION;
		}
		else if (scoreGreen >= MIN_SCORE) {
			scoreGreen -= SCORE_RESOLUTION;
		}
		else if (scoreRed >= MIN_SCORE) {
			scoreRed -= SCORE_RESOLUTION;
		}
	}
	
	private void gbrDimOrder() {
		if (scoreGreen >= MIN_SCORE) {
			scoreGreen -= SCORE_RESOLUTION;
		}
		else if (scoreBlue >= MIN_SCORE) {
			scoreBlue -= SCORE_RESOLUTION;
		}
		else if (scoreRed >= MIN_SCORE) {
			scoreRed -= SCORE_RESOLUTION;
		}
	}

	private void grbDimOrder() {
		if (scoreGreen >= MIN_SCORE) {
			scoreGreen -= SCORE_RESOLUTION;
		}
		else if (scoreRed >= MIN_SCORE) {
			scoreRed -= SCORE_RESOLUTION;
		}
		else if (scoreBlue >= MIN_SCORE) {
			scoreBlue -= SCORE_RESOLUTION;
		}
	}

	private void rbgDimOrder() {
		if (scoreRed >= MIN_SCORE) {
			scoreRed -= SCORE_RESOLUTION;
		}
		else if (scoreBlue >= MIN_SCORE) {
			scoreBlue -= SCORE_RESOLUTION;
		}
		else if (scoreGreen >= MIN_SCORE) {
			scoreGreen -= SCORE_RESOLUTION;
		}
	}

	private void rgbDimOrder() {
		if (scoreRed >= MIN_SCORE) {
			scoreRed -= SCORE_RESOLUTION;
		}
		else if (scoreGreen >= MIN_SCORE) {
			scoreGreen -= SCORE_RESOLUTION;
		}
		else if (scoreBlue >= MIN_SCORE) {
			scoreBlue -= SCORE_RESOLUTION;
		}
	}

}
