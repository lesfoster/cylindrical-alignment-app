/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.data_source.web_client;

/**
 * This represents info to build up a URL for fetching data.
 *
 * @author Leslie L Foster
 */
public class HostBean {
    private String user;
	private String pass;
	private String currentHost;
	private int currentPort;
	private String currentProtocol;

	/**
	 * @return the user
	 */
	public String getCurrentUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setCurrentUser(String user) {
		this.user = user;
	}

	/**
	 * @return the pass
	 */
	public String getCurrentPass() {
		return pass;
	}

	/**
	 * @param pass the pass to set
	 */
	public void setCurrentPass(String pass) {
		this.pass = pass;
	}

	/**
	 * @return the currentHost
	 */
	public String getCurrentHost() {
		return currentHost;
	}

	/**
	 * @param currentHost the currentHost to set
	 */
	public void setCurrentHost(String currentHost) {
		this.currentHost = currentHost;
	}

	/**
	 * @return the currentPort
	 */
	public int getCurrentPort() {
		return currentPort;
	}

	/**
	 * @param currentPort the currentPort to set
	 */
	public void setCurrentPort(int currentPort) {
		this.currentPort = currentPort;
	}

	/**
	 * @return the currentProtocol
	 */
	public String getCurrentProtocol() {
		return currentProtocol;
	}

	/**
	 * @param currentProtocol the currentProtocol to set
	 */
	public void setCurrentProtocol(String currentProtocol) {
		this.currentProtocol = currentProtocol;
	}
}
