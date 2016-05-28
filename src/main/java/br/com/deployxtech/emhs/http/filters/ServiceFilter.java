/**
 * 
 */
package br.com.deployxtech.emhs.http.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import br.com.deployxtech.emhs.router.Router;

/**
 * @author Francisco Silva
 *
 */
public class ServiceFilter extends Filter {

	@Override
	public String description() {
		return "Filter of services";
	}

	@Override
	public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
		prepareAttributes(exchange);
		parseParameters(exchange);
		chain.doFilter(exchange);
	}

	private void prepareAttributes(HttpExchange exchange) {
		exchange.setAttribute("hostClient", exchange.getRemoteAddress().getAddress().getHostAddress());
		exchange.setAttribute("User-Agent", exchange.getRequestHeaders().getFirst("User-Agent"));
	}

	private void parseParameters(HttpExchange exchange) throws IOException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		String query = exchange.getRequestURI().getRawQuery();
		parseQuery(query, parameters);
		if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
			try (BufferedReader buffer = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
				query = buffer.lines().collect(Collectors.joining("\n"));
				parseQuery(query, parameters);
			}
		}
		exchange.setAttribute("parameters", parameters);
	}

	private void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {
		if (query != null && !query.isEmpty()) {
			String pairs[] = query.split("[&]");

			for (String pair : pairs) {
				String param[] = pair.replaceFirst("=", "&").split("[&]");

				String key = null;
				String value = null;
				if (param.length > 0) {
					if (Router.isParameterNamePresent()) {
						key = URLDecoder.decode(param[0], Charset.defaultCharset().name());	
					}
					else {
						key = "arg"+parameters.size();
					}
				}

				if (param.length > 1) {
					value = URLDecoder.decode(param[1], Charset.defaultCharset().name());
				}

				if (parameters.containsKey(key)) {
					Object obj = parameters.get(key);
					if(obj instanceof List<?>) {
						List<String> values = (List<String>)obj;
						values.add(value);
					} else if(obj instanceof String) {
						List<String> values = new ArrayList<String>();
						values.add((String)obj);
						values.add(value);
						parameters.put(key, values);
					}
				} else {
					parameters.put(key, value);
				}
			}
		}
	}
}
