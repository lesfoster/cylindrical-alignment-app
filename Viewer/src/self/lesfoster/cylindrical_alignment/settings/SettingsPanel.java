package self.lesfoster.cylindrical_alignment.settings;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import self.lesfoster.cylindrical_alignment.effector.CylinderPositioningEffector;
import self.lesfoster.cylindrical_alignment.effector.SettingsEffector;
import self.lesfoster.cylindrical_alignment.effector.SpeedEffector;

/**
 * Created by IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 4/22/12
 * Time: 12:58 AM
 *
 * Dialog to unify the settings.
 */
public class SettingsPanel extends JPanel {
    private SpeedEffector speedAffector;
    private SettingsEffector settingsAffector;
    private CylinderPositioningEffector positioningAffector;

    // These are the controls for the panel.

    /**
     * Takes the affectors: these can simply have messages called upon them to make things happen.
     *
     * @param speedAffector controller for speed of cylinder spin.
     * @param settingsAffector various simple settings.
     * @param positioningAffector how the rotation-by-mouse takes place, etc.
     */
    public SettingsPanel(
            SpeedEffector speedAffector,
            SettingsEffector settingsAffector,
            CylinderPositioningEffector positioningAffector) {
        this.speedAffector = speedAffector;
        this.settingsAffector = settingsAffector;
        this.positioningAffector = positioningAffector;
    }

    //todo look at invoke and wait: could that be done better by caller?
    /** Sets up the GUI. Callable here from client, to allow the setup burden to be lost only at use-time. */
    public void initGui() throws InterruptedException, InvocationTargetException{
        SwingUtilities.invokeAndWait( new Runnable() {
            public void run() {

            }
        });
    }
}
