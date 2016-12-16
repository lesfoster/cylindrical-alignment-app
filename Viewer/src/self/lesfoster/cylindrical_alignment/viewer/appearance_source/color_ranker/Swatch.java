/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import org.openide.util.NbPreferences;
import self.lesfoster.cylindrical_alignment.model.action.FileOpener;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.ColorRanker;

/**
 * This swatch can give a quick overview of what a ranker does.
 * @author Leslie L Foster
 */
public class Swatch extends JPanel {
	public static final String COLOR_RANKER_KEY = "ColorRanker";
	public static final int SWATCH_HEIGHT = 20;
	public static final int SWATCH_WIDTH = 20;
	
	private ColorRanker colorRanker;
	public Swatch(ColorRanker colorRanker) {
		this.setBackground(Color.white);
		this.colorRanker = colorRanker;
		this.setToolTipText(colorRanker.toString());
		this.addMouseListener(new SwatchMouseListener());
	}
	
	@Override
	public void paint(Graphics g) {
		colorRanker.reset();
		Graphics2D g2d = (Graphics2D)g;
		for (int i = 0; i < SWATCH_HEIGHT; i++) {
			g2d.setColor(new Color(colorRanker.getScoreRed(), colorRanker.getScoreGreen(), colorRanker.getScoreBlue()));
			g2d.drawRect(0, i, SWATCH_WIDTH, 1);
			for (int j = 0; j < 360 / SWATCH_HEIGHT; j++) {		
				colorRanker.decrementRank();
			}
		}
	}
	
	private class SwatchMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent me) {
			NbPreferences.forModule(FileOpener.class).put(COLOR_RANKER_KEY, colorRanker.getClass().getName());			
		}
	}
}
