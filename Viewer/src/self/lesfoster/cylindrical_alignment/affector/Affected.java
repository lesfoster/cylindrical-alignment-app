/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.affector;

/**
 * Implement this to indicate that the class exposes affectors to its behavior.
 *
 * @author Leslie L Foster
 */
public interface Affected {
	Affector[] getAffectors();
}
