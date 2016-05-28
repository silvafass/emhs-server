/**
 * 
 */
package br.com.deployxtech.emhs.http.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import br.com.deployxtech.emhs.http.HttpServiceRequest;
import br.com.deployxtech.emhs.http.HttpServiceResponse;
import br.com.deployxtech.emhs.router.RouterControl;
import br.com.deployxtech.emhs.service.ServiceRequest;
import br.com.deployxtech.emhs.service.ServiceResponse;
import br.com.deployxtech.emhs.utils.EmhsConfiguraction;
import br.com.deployxtech.emhs.utils.SearchUtils;

/**
 * @author Francisco Silva
 *
 */
public class EmhsHandler implements HttpHandler {

	private RouterControl routerControl = new RouterControl();
	
	public EmhsHandler() {
		List<Class<?>> types = SearchUtils.scan();
		for (Class<?> type: types) {
			routerControl.createProvider(type);
		}
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		ServiceRequest request = new HttpServiceRequest(exchange);
		ServiceResponse response = new HttpServiceResponse(exchange);
		try {
			handle(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			response.error(e);
		}
		finally {
			response.end();
		}
	}

	public void handle(ServiceRequest request, ServiceResponse response) throws Exception {
		if (getStaticResource(request, response)) {
			response.end();
		}
		else {
			routerControl.invoke(request, response);			
		}
	}

	private boolean getStaticResource(ServiceRequest request, ServiceResponse response) throws IOException {
		if (EmhsConfiguraction.getInstance().getPathStatics() != null) {
			for (String pathStatic: EmhsConfiguraction.getInstance().getPathStatics()) {
				Path path = Paths.get(pathStatic, request.getURL());
				if (Files.exists(path)) {
					if (Files.isDirectory(path) && Files.exists(path.resolve("index.html"))) {
						byte[] bytes = Files.readAllBytes(path.resolve("index.html"));
						response.getOutputStream().write(bytes);
						return true;
					}
					else {
						byte[] bytes = Files.readAllBytes(path);
						response.getOutputStream().write(bytes);
						return true;
					}
				}				
			}
		}
		return false;
	}
}