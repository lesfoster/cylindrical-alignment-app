package self.lesfoster.cylindrical_alignment.settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import self.lesfoster.cylindrical_alignment.affector.SettingsAffector;

/**
 * Created by IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 4/27/12
 * Time: 12:43 AM
 *
 * Controller to handoff click result to a settings affector.
 */
public class AntialiasActionListener implements ActionListener {
    private SettingsAffector settingsAffector;
    public AntialiasActionListener( SettingsAffector settingsAffector ) {
        this.settingsAffector = settingsAffector;
    }
    public void actionPerformed(ActionEvent ae) {
        settingsAffector.setAntialias(((AbstractButton)ae.getSource()).isSelected());
    }
}
