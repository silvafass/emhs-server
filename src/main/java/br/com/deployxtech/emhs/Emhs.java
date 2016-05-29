/**
 * 
 */
package br.com.deployxtech.emhs;

import java.io.IOException;

import br.com.deployxtech.emhs.http.server.EmhsServer;
import br.com.deployxtech.emhs.utils.EmhsConfiguraction;

/**
 * @author Francisco Silva
 *
 */
public class Emhs {

	private static Emhs instance;

	private EmhsServer server;

	private Emhs() {}

	public static EmhsConfiguraction config() {
		return EmhsConfiguraction.getInstance();
	}

	public static EmhsServer server(int port) throws IOException {
		if (getInstance().server == null) {
			getInstance().server = new EmhsServer(port);
		}
		return getInstance().server;		
	}

	public static EmhsServer server() throws IOException {
		if (getInstance().server == null) {
			getInstance().server = new EmhsServer();
		}
		return getInstance().server;		
	}

	private static Emhs getInstance() {
		if (instance == null) {
			instance = new Emhs();
		}
		return instance;
	}
}
