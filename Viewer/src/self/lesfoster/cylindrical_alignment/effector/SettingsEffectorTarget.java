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

import javafx.scene.Scene;

/**
 * Implement this to provide a settings affector with the hooks it needs to
 * perform its task.
 *
 * @author Leslie L Foster
 */
public interface SettingsEffectorTarget {

    Scene getUniverse();

    void setAmbient(boolean isAmbient);

    void setDark(boolean dark);

    void setEnvelopeDistance(int envelopeDistance);
}
