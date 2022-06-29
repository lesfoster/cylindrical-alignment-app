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



/*
 * Settings Affector
 * Created on Mar 23, 2005
 */
package self.lesfoster.cylindrical_alignment.effector;

/**
 * Allows external manipulation of settings.
 * @author FosterLL
 */
public interface SettingsEffector extends Effector {
	void setAntialias(boolean isAntialias);
	void setAmbientLightSource(boolean isAmbientOn);
	void setDark(boolean isDark);
    void setSelectionEnvelope(int envelopeDistance);
}
