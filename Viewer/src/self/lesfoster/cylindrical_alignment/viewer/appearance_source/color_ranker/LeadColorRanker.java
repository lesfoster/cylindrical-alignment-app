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
 * Gives the appearance of dulling into lead as score decreases.
 *
 * @author Leslie L Foster
 */
public class LeadColorRanker implements ColorRanker {

    public static final float SILVER_LEVEL = 240.0f / 256.0f;
    private static final double MIN_SCORE = 0.0025 * 5;
    private static final double SCORE_RESOLUTION = 0.0075 * 0.75;

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
    public float[] getSelectColor() {
        return MOLTEN_SELECT_COLOR;
    }

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
