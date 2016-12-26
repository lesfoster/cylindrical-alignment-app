/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.ColorRanker;
import static self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.Swatch.SWATCH_HEIGHT;
import static self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.Swatch.SWATCH_WIDTH;

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
		Graphics2D g2d = (Graphics2D)getGraphics();
		for (int i = 0; i < SWATCH_HEIGHT; i++) {
			g2d.setColor(new Color(colorRanker.getScoreRed(), colorRanker.getScoreGreen(), colorRanker.getScoreBlue()));
			g2d.drawRect(0, i, SWATCH_WIDTH, 1);
			for (int j = 0; j < 360 / SWATCH_HEIGHT; j++) {		
				colorRanker.decrementRank();
			}
		}
	}
		
}
