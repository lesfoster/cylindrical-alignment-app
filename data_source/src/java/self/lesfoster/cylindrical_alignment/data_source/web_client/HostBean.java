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
