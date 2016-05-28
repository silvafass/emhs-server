/**
 * 
 */
package br.com.deployxtech.emhs.http;

import java.nio.charset.Charset;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import br.com.deployxtech.emhs.service.ServiceRequest;

/**
 * @author Francisco Silva
 *
 */
public class HttpServiceRequest implements ServiceRequest {

	private HttpExchange exchange;
	private String encoding = Charset.defaultCharset().name();//"UTF-8";

	public HttpServiceRequest(HttpExchange exchange) {
		this.exchange = exchange;
	}

	public HttpExchange getExchange() {
		return exchange;
	}

	@Override
	public String getParameter(String name) {
		return getParamns().get(name);
	}

	@Override
	public Map<String, String> getParamns() {
		return (Map<String, String>) getExchange().getAttribute("parameters");
	}

	@Override
	public String getURL() {
		String pathContext = getExchange().getHttpContext().getPath();
		if (pathContext.equals("/")) {
			return getExchange().getRequestURI().getPath().toLowerCase();
		}
		else {
			return getExchange().getRequestURI().getPath().replaceFirst(pathContext, "").toLowerCase();
		}
	}

	@Override
	public Object getAttribute(String key) {
		return getExchange().getAttribute(key);
	}
}
