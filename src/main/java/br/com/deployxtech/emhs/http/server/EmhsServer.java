/**
 * 
 */
package br.com.deployxtech.emhs.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import br.com.deployxtech.emhs.http.filters.ServiceFilter;

/**
 * @author Francisco Silva
 *
 */
@SuppressWarnings("restriction")
public final class EmhsServer {

	public final static int DEFAULT_PORT = 80;

	public HttpServer server;

	public EmhsServer() throws IOException {
		this(EmhsServer.DEFAULT_PORT);
	}

	public EmhsServer(int port) throws IOException {
		server = HttpServer.create(new InetSocketAddress(port),0);
	}

	public void start() {		
		HttpContext context = server.createContext("/", new EmhsHandler());
		context.getFilters().add(new ServiceFilter());
		server.setExecutor(null);
		server.start();
		System.out.println("Emhs Server started...");
	}
}