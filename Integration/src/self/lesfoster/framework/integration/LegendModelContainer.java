/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.framework.integration;

/**
 * Container for legend model.
 *
 * @author Leslie L Foster
 */
public class LegendModelContainer extends SharedObjectContainer<LegendModel> {
	private LegendModelContainer() {}
	private static LegendModelContainer instance = new LegendModelContainer();
	public static LegendModelContainer getInstance() { return instance; }
}
