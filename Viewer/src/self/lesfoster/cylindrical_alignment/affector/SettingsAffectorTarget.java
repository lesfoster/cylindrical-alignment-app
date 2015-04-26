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

//import javax.media.j3d.DirectionalLight;

import javafx.scene.Scene;


//import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * Implement this to provide a settings affector with the hooks it needs to perform its task. 
 * @author Leslie L Foster
 */
public interface SettingsAffectorTarget {
	Scene getUniverse();
	//DirectionalLight getDirectionLight();
    void setEnvelopeDistance(int envelopeDistance);
}
