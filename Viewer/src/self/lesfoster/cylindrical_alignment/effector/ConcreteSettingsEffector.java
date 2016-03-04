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
package self.lesfoster.cylindrical_alignment.effector;

/**
 * Will carry out settings modifications in response to user click.
 * 
 * @author Leslie L Foster
 *
 */
public class ConcreteSettingsEffector implements SettingsEffector {
	private SettingsEffectorTarget target;
	
	/**
	 * Construct with the target.
	 */
	public ConcreteSettingsEffector( SettingsEffectorTarget target ) {
		this.target = target;
	}

	@Override
	public void setAntialias(boolean isAntialias) {
//		target.getUniverse().getViewer().getView().setSceneAntialiasingEnable(isAntialias);
		System.out.println("setAntialias has no effect");
	}

	@Override
	public void setSecondLightSource(boolean isSecondEnabled) {
//		target.getDirectionLight().setEnable(isSecondEnabled);
		System.out.println("setSecondLightSource has no effect.");
	}

	@Override
    public void setSelectionEnvelope(int envelopeDistance) {
        target.setEnvelopeDistance(envelopeDistance);
    }

}
