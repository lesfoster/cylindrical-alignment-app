/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.data_source;

import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import self.lesfoster.cylindrical_alignment.data_source.web_client.HostBean;

/**
 *
 * @author Leslie L Foster
 */
public class PrecomputedBlastDSTest {
	private static HostBean hostBean;
	
	@BeforeClass
	public static void init() {
		hostBean = new HostBean();
		hostBean.setCurrentProtocol("https");
		hostBean.setCurrentPort(443);
		hostBean.setCurrentHost("jbosswildfly-cylalignvwr.rhcloud.com");
		hostBean.setCurrentUser("lesfoster");
		hostBean.setCurrentPass("lesfoster");	}
	
	@Test
	public void testSingleStringSearches() throws Exception {
		PrecomputedBlastXmlDataSource pbds = new PrecomputedBlastXmlDataSource();
		pbds.setHostBean(hostBean);
		List<SearchResult> results = pbds.getBySingleStringSearchResults(PrecomputedBlastXmlDataSource.StringSearchType.SPECIES, "Mus musculus");
		for (SearchResult result: results) {
			System.out.println(result.getDescription() + " with " + result.getFetchId());
		}
	}
}
