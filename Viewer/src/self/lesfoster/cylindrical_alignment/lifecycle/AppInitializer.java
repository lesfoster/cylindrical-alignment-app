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
