/*
Cylindrical Blast Viewer: allows viewing of Biomolecular
alignment data, in 3D, by forming the alignments into a
cylinder, such that the alignments are arranged like the staves
of a barrel. The cylinder can spin, and if it spins, it can
do so at two different speeds.  When stopped, properties can
be seen for the aligned values.
Copyright (C) 2005 Leslie L. Foster

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
package self.lesfoster.cylindrical_alignment.settings;

import self.lesfoster.cylindrical_alignment.utils.GuiUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import self.lesfoster.cylindrical_alignment.affector.SettingsAffector;

/**
 * Created by IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 04/22/2012
 * Time: 11:46 PM
 * The controls on this panel will operate against an affector when the user enters a value.
 */
public class SelectionEnvelopPanel extends JPanel {
    private SettingsAffector settingsAffector;
    public SelectionEnvelopPanel(SettingsAffector settingsAffector) {
        super();
        this.settingsAffector = settingsAffector;
        init();
    }

    private void init() {
        final JTextField envelopDistanceTF = new JTextField();
        envelopDistanceTF.setColumns( 5 );
        envelopDistanceTF.setBorder( new TitledBorder( "Envelope Width" ) );

        JButton goButton = new JButton( "Set" );
        goButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Integer envelopeDistance = Integer.parseInt( envelopDistanceTF.getText() );
                    settingsAffector.setSelectionEnvelope( envelopeDistance );
                } catch ( NumberFormatException nfe ) {
                    // nada
                }
            }
        });
        BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);
        add( envelopDistanceTF, BorderLayout.CENTER );
        add( goButton, BorderLayout.EAST );
    }

}
