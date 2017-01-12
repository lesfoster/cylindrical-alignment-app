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
