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

import self.lesfoster.cylindrical_alignment.viewer.appearance_source.ColorRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.StandardAppearanceSource;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.BGRRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.BRGRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.GBRRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.GRBRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.LeadColorRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.RBGRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.RGBRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.RustColorRanker;
import self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.SwatchImage;
import static self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker.SwatchImage.COLOR_RANKER_KEY;

/**
 * Width to display a group of color-rank-progression swatches for chosing.
 *
 * @author Leslie L Foster
 */
public class ColorRankerPanel extends JPanel {
	public static final String STD_SWATCH_TOOLTIP_TEXT = "Must restart application.";

	private int width;
	public ColorRankerPanel(int width) {
		this.width = width;
		init();
	}
	
	private void init() {
		setLayout(new GridLayout(1, 8));
		setSize(new Dimension(width, 20));
		
		JButton rustCRButton = new JButton();
		rustCRButton.setIcon(new ImageIcon(new SwatchImage(new RustColorRanker())));
		rustCRButton.addActionListener(new SwatchActionListener(new RustColorRanker()));
		rustCRButton.setToolTipText("Color changes from sliver to rust; must restart viewer.");
		
		JButton leadCRButton = new JButton();
		leadCRButton.setIcon(new ImageIcon(new SwatchImage(new LeadColorRanker())));
		leadCRButton.addActionListener(new SwatchActionListener(new LeadColorRanker()));		
		leadCRButton.setToolTipText("Color changes from sliver to leaden; must restart viewer.");
		
		add(rustCRButton);
		add(leadCRButton);
		add(makeRankerButton(new BRGRanker(), STD_SWATCH_TOOLTIP_TEXT));
		add(makeRankerButton(new BGRRanker(), STD_SWATCH_TOOLTIP_TEXT));
		add(makeRankerButton(new GBRRanker(), STD_SWATCH_TOOLTIP_TEXT));
		add(makeRankerButton(new GRBRanker(), STD_SWATCH_TOOLTIP_TEXT));
		add(makeRankerButton(new RBGRanker(), STD_SWATCH_TOOLTIP_TEXT));
		add(makeRankerButton(new RGBRanker(), STD_SWATCH_TOOLTIP_TEXT));
		
		setBorder(new TitledBorder("Save Color Ranker Setting"));
	}
	
	private JButton makeRankerButton(ColorRanker colorRanker, String toolText) {
		JButton rtnVal = new JButton();
		rtnVal.setIcon(new ImageIcon(new SwatchImage(colorRanker)));
		rtnVal.addActionListener(new SwatchActionListener(colorRanker));
		rtnVal.setToolTipText(toolText);
		return rtnVal;
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
