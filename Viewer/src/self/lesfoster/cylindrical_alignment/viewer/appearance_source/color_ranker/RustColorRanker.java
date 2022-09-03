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
 * Gives the appearance of advancing rust as the score decreases.
 *
 * @author Leslie L Foster
 */
public class RustColorRanker implements ColorRanker {

    public static final float STEEL_LEVEL = 240.0f / 256.0f;

    private final static double SCORE_RESOLUTION = 0.01 * 0.75;
    private final static double BLUE_RESOLUTION = 0.01 * 0.5;
    private final static int[] RUSTCOLORS = new int[]{183, 65, 14};

    // Start out silvery.
    private float scoreRed = STEEL_LEVEL;
    private float scoreBlue = STEEL_LEVEL;
    private float scoreGreen = STEEL_LEVEL;

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
    public float[] getSelectColor() {
        return MOLTEN_SELECT_COLOR;
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

    @Override
    public void reset() {
        scoreRed = STEEL_LEVEL;
        scoreBlue = STEEL_LEVEL;
        scoreGreen = STEEL_LEVEL;
    }

    @Override
    public String toString() {
        return "Rust";
    }
}
