/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.data_source;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import self.lesfoster.cylindrical_alignment.data_source.web_client.HostBean;

/**
 *
 * @author Leslie L Foster
 */
public class UrlDataSourceTest {
	
	public UrlDataSourceTest() {
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
	public void invokeServerDataSource() {
		HostBean hostBean = new HostBean();
		hostBean.setCurrentProtocol("https");
		hostBean.setCurrentPort(443);
		hostBean.setCurrentHost("jbosswildfly-cylalignvwr.rhcloud.com");
		hostBean.setCurrentUser("lesfoster");
		hostBean.setCurrentPass("lesfoster");
		final DataSource dataSource = DataSourceFactory.getSourceForServerId("1", hostBean);
		assertNotNull("No data source found from server.", dataSource);
		System.out.println(dataSource.getAnchorLength() + " is length of anchor.");
		List<Entity> entities = dataSource.getEntities();
		System.out.println(entities.size() + " entities found.");
	}
}
