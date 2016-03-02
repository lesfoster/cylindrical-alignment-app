package self.lesfoster.cylindrical_alignment.settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import self.lesfoster.cylindrical_alignment.effector.SettingsEffector;

/**
 * Created by IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 4/27/12
 * Time: 12:57 AM
 *
 * Controller class to link up affector with gesture to turn off 2ndary directional light source.
 */
public class SecondDirectionalLightActionListener implements ActionListener {
    private SettingsEffector settingsAffector;
    public SecondDirectionalLightActionListener( SettingsEffector settingsAffector ) {
        this.settingsAffector = settingsAffector;
    }
    public void actionPerformed(ActionEvent ae) {
        settingsAffector.setSecondLightSource(((AbstractButton)ae.getSource()).isSelected());
    }
}
