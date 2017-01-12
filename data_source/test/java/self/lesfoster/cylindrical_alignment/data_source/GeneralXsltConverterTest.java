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


package self.lesfoster.cylindrical_alignment.data_source;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test functionality of XSLT conversion.
 *
 * @author Leslie L Foster
 */
public class GeneralXsltConverterTest {
	
	public GeneralXsltConverterTest() {		
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	
	@Test
	public void getAllConverters() {
		GeneralXsltConverter blastConverter = GeneralXsltConverter.getConverter("..\\samples\\raw_zebra_mussel\\AF265353_1.BlastOutput.xml");
		GeneralXsltConverter msProjectConverter = GeneralXsltConverter.getConverter("..\\samples\\ms_project\\ProjectExercise.Project.xml");
		GeneralXsltConverter geneMachineConverter = GeneralXsltConverter.getConverter("..\\samples\\raw_gene_machine\\gene_machine_2.INSDSet.xml");
		
		assertNotNull("No Blast Converter", blastConverter);
		assertNotNull("No Project Converter", msProjectConverter);
		assertNotNull("No Gene Machine Converter", geneMachineConverter);
		
		System.out.println("loc " + System.getProperty("user.dir"));
	
		assertNotNull("No Blast Converter Stream", blastConverter.getStream());
		assertNotNull("No Project Converter Stream", msProjectConverter.getStream());
		assertNotNull("No Gene Machihne Converter Stream", geneMachineConverter.getStream());
	}

}
