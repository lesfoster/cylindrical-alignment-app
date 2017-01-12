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
