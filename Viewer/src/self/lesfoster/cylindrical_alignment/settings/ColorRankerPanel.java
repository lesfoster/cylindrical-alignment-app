/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.settings;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.openide.util.NbPreferences;
import self.lesfoster.cylindrical_alignment.model.action.FileOpener;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.ColorRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.StandardAppearanceSource;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.LeadColorRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.RGBRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.RustColorRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.SwatchImage;
import static self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.SwatchImage.COLOR_RANKER_KEY;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.TripOrderColorRanker;

/**
 * Width to display a group of color-rank-progression swatches for chosing.
 *
 * @author Leslie L Foster
 */
public class ColorRankerPanel extends JPanel {
	private int width;
	public ColorRankerPanel(int width) {
		this.width = width;
		init();
	}
	
	private void init() {
		setLayout(new GridLayout(1, 5));
		setSize(new Dimension(width, 20));
		
		JButton rustCRButton = new JButton();
		rustCRButton.setIcon(new ImageIcon(new SwatchImage(new RustColorRanker())));
		rustCRButton.addActionListener(new SwatchActionListener(new RustColorRanker()));
		rustCRButton.setToolTipText("Color changes from sliver to rust; must restart viewer.");
		
		JButton leadCRButton = new JButton();
		leadCRButton.setIcon(new ImageIcon(new SwatchImage(new LeadColorRanker())));
		leadCRButton.addActionListener(new SwatchActionListener(new LeadColorRanker()));		
		leadCRButton.setToolTipText("Color changes from sliver to leaden; must restart viewer.");
		
		JButton rgbCRButton = new JButton();
		rgbCRButton.setIcon(new ImageIcon(new SwatchImage(new RGBRanker())));
		rgbCRButton.addActionListener(new SwatchActionListener(new RGBRanker()));
		rgbCRButton.setToolTipText("Color goes from to yellow to green; must restart viewer.");
		
		JButton gbrCRButton = new JButton();
		gbrCRButton.setIcon(new ImageIcon(new SwatchImage(new TripOrderColorRanker(TripOrderColorRanker.TripOrder.GBR))));
		
		JButton brgCRButton = new JButton();
		brgCRButton.setIcon(new ImageIcon(new SwatchImage(new TripOrderColorRanker(TripOrderColorRanker.TripOrder.BRG))));
		
		add(rustCRButton);
		add(leadCRButton);
		add(rgbCRButton);
		add(gbrCRButton);
		add(brgCRButton);
		
		setBorder(new TitledBorder("Save Color Ranker Setting"));
	}

	private class SwatchActionListener implements ActionListener {
		private ColorRanker colorRanker;
		public SwatchActionListener(ColorRanker colorRanker) {
			this.colorRanker = colorRanker;
		}
		@Override
		public void actionPerformed(ActionEvent me) {
			NbPreferences.forModule(StandardAppearanceSource.class).put(COLOR_RANKER_KEY, colorRanker.getClass().getName());			
		}
	}	
	
}
