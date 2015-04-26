/*
Cylindrical Blast Viewer: allows viewing of Biomolecular
alignment data, in 3D, by forming the alignments into a
cylinder, such that the alignments are arranged like the staves
of a barrel. The cylinder can spin, and if it spins, it can
do so at two different speeds.  When stopped, properties can
be seen for the aligned values.
Copyright (C) 2011 Leslie L. Foster

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
package self.lesfoster.cylindrical_alignment.affector;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import self.lesfoster.cylindrical_alignment.constants.Constants;

/** Provides a response for user help-clicks. */
public class ConcreteHelpAffector implements HelpAffector {
	private Component parentComponent;
	private HelpAffectorTarget target;

	public ConcreteHelpAffector( Component component, HelpAffectorTarget target ) {
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
