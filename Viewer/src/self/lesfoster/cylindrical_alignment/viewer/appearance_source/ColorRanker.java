/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source;

/**
 * Implement this to provide a color ranking scheme.
 *
 * @author Leslie L Foster
 */
public interface ColorRanker {
	float getScoreRed();
	float getScoreGreen();
	float getScoreBlue();
	void decrementRank();
}
