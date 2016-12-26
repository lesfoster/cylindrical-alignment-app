/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker;

import java.awt.image.BufferedImage;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.ColorRanker;

/**
 * Special image extension, that renders the image to graphics, using a
 * Color Ranker.
 *
 * @author Leslie L Foster
 */
public class SwatchImage extends BufferedImage {
	
	public static final int SWATCH_HEIGHT = 20;
	public static final int SWATCH_WIDTH = 20;
	public static final String COLOR_RANKER_KEY = "ColorRanker";

	private ColorRanker colorRanker;
	
	private int[][] colors = new int[SWATCH_WIDTH][SWATCH_HEIGHT];
	
	public SwatchImage(ColorRanker colorRanker) {
		super(SWATCH_WIDTH, SWATCH_HEIGHT, TYPE_INT_RGB);
		this.colorRanker = colorRanker;		
		colorize();
	}

	@Override
	public int getRGB(int x, int y) {
        return colors[x][y];
    }
	
	private void colorize() {
		colorRanker.reset();
		for (int i = 0; i < SWATCH_HEIGHT; i++) {
			for (int j = 0; j < SWATCH_WIDTH; j++) {
				// Colors must be moved into integer 0-255 range first.  Then,
				// there is a type of "shift" to get the byte into the correct
				// part of the int's 4-byte.
				colors[i][j] = (int)(colorRanker.getScoreRed() * 256) +
				               (int)(colorRanker.getScoreGreen() * 256) * 256 +
				               (int)(colorRanker.getScoreBlue() * 256) * 256 * 256;
			}
			colorRanker.decrementRank();
		}
	}
		
}
