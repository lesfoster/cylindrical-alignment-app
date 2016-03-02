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

//import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.group.TransformableGroup;

/** Implement this to allow the positioning affector to get objects needed to carry out operations. */
public interface CylinderPositioningAffectorTarget {
	//@TODO: once mouse rotation in JavaFX is better understood, change below.
	//MouseRotate getMouseRotateBehavior();
	TransformableGroup getTransformGroup();
	TransformableGroup getTransform();
}
