/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.lifecycle;

import java.awt.Color;
import java.io.InputStream;
import javax.swing.UIManager;
import javax.swing.plaf.synth.SynthLookAndFeel;
import org.openide.windows.OnShowing;

/**
 * Startup hook to set the look and feel.
 *
 * @author Leslie L Foster
 */
//@OnShowing
public class AppInitializer implements Runnable {
	public void run() {
		try {
			//UIManager.setLookAndFeel( "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			/*
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					UIManager.put("Menu.foreground", Color.CYAN);
					//UIManager.put("MenuUI", javax.swing.JComponent.class.getName());
					UIManager.put("nimbusBase", new Color(0, 20, 20));
					UIManager.put("nimbusBlueGrey", new Color(0, 120, 120));
					UIManager.put("control", new Color(0, 120, 120));
					break;
				}
			}
		    */
			SynthLookAndFeel laf = new SynthLookAndFeel();
			final InputStream lafStream = AppInitializer.class.getResourceAsStream("/laf.xml");
			if (lafStream == null) {
				throw new RuntimeException("Cannot locate the XML file.");
			}
			laf.load(lafStream, AppInitializer.class);
			UIManager.setLookAndFeel(laf);
		} catch (Exception ex) {
			System.err.println("Low priority LaF operation has failed.  Proceeding.");
			ex.printStackTrace();
		}
	}
	
}
