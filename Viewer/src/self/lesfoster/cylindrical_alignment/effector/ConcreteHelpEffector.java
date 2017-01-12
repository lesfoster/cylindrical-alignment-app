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


package self.lesfoster.cylindrical_alignment.effector;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import self.lesfoster.cylindrical_alignment.constants.Constants;

/** Provides a response for user help-clicks. */
public class ConcreteHelpEffector implements HelpEffector {
	private Component parentComponent;
	private HelpEffectorTarget target;

	public ConcreteHelpEffector( Component component, HelpEffectorTarget target ) {
		this.parentComponent = component;
		this.target = target;
	}

	@Override
	public void showAbout() {
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
                JOptionPane.showMessageDialog(
                		parentComponent, 
                		Constants.ABOUT_MESSAGE, 
                		"About", 
                		JOptionPane.INFORMATION_MESSAGE );            			
    		}
    	});

	}

	@Override
	public void showInputData() {
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
                JOptionPane.showMessageDialog(
                		parentComponent, 
                		"Now Viewing File: " + target.getInputFile(),
                		"Input",
                		JOptionPane.INFORMATION_MESSAGE );
    		}
    	});

	}

    @Override
    public void showApplicationHelp() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(
                		parentComponent,
                        Constants.APPLICATION_HELP_MSG,
                		"Application Help",
                		JOptionPane.INFORMATION_MESSAGE );
    		}
        });

    }

    @Override
    public void showUrlPingAdvisory() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(
                		parentComponent,
                        Constants.URL_PING_ADVISORY,
                		"URL Ping",
                		JOptionPane.INFORMATION_MESSAGE );

            }
        });
    }

}
