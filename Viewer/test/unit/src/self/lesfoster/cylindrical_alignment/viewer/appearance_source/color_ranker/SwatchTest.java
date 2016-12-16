/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source.color_ranker;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JFrame;

/**
 * Test that a swatch is being properly created.
 *
 * @author Leslie L Foster
 */
public class SwatchTest extends JFrame {
	public SwatchTest() {
		setTitle("Swatch Test");
		setSize(500,500);
		getRootPane().setBackground(Color.white);
		setLayout(new GridLayout(5, 1));
		Swatch s1 = new Swatch(new RustColorRanker());
		Swatch s2 = new Swatch(new LeadColorRanker());
		Swatch s3 = new Swatch(new TripOrderColorRanker(TripOrderColorRanker.TripOrder.BGR));
		Swatch s4 = new Swatch(new TripOrderColorRanker(TripOrderColorRanker.TripOrder.GBR));
		Swatch s5 = new Swatch(new TripOrderColorRanker(TripOrderColorRanker.TripOrder.RGB));
		add(s1);
		add(s2);
		add(s3);
		add(s4);
		add(s5);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new SwatchTest();
	}
}

