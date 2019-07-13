/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.data_source;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Leslie L Foster
 */
public class Gff3ReadTest {
	private static final String FILE_LOC = "..\\samples\\gff3\\scerevisiae_regulatory.gff";
	@Test
	public void testReadCerevisae() throws Exception {
		Gff3DataSource dataSource = new Gff3DataSource(FILE_LOC);
		Assert.assertTrue("Unexpected anchor length.", dataSource.getAnchorLength() == 230208);
		Assert.assertTrue("No entities found.", dataSource.getEntities().size() > 0);
		Assert.assertEquals("Wrong number of entities found.", 59, dataSource.getEntities().size());
		System.out.println(dataSource.getEntities().get(0).getSubEntities().get(0));
		
	}
}
