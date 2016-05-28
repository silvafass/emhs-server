/**
 * 
 */
package br.com.deployxtech.emhs.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import br.com.deployxtech.emhs.service.ServiceResponse;

/**
 * @author Francisco Silva
 *
 */
public class HttpServiceResponse implements ServiceResponse {

	private HttpExchange exchange;

	private String encoding = Charset.defaultCharset().name();//"UTF-8";
	private Map<String, List<String>> headers = new HashMap<String, List<String>>();

	private StringWriter stringWriter;
	private ByteArrayOutputStream outputStream;

	private boolean isResponseHeader = false;

	public HttpServiceResponse(HttpExchange exchange) {
		this.exchange = exchange;
	}

	@Override
	public void setCharacterEncoding(String value) {
		this.encoding = value;
		addHeader("Accept-encoding", value);
	}

	@Override
	public void setContentType(String value) {
		addHeader("Accept", value);
	}

	@Override
	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	@Override
	public void addHeader(String key, String value) {
		if (getHeaders().get(key) == null) {
			getHeaders().put(key, new ArrayList<String>());
		}
		getHeaders().get(key).add(value);
	}

	@Override
	public void sendResponseHeaders(int responseCode, int responseLength) throws IOException {
		getExchange().sendResponseHeaders(responseCode, responseLength);
		isResponseHeader = true;
	}

	@Override
	public StringWriter getWriter() {
		if (stringWriter == null) {
			stringWriter = new StringWriter();
		}
		return stringWriter;
	}

	@Override
	public ByteArrayOutputStream getOutputStream() {
		if (outputStream == null) {
			outputStream = new ByteArrayOutputStream();
		}
		return outputStream;
	}

	@Override
	public void error(Exception e) throws IOException {
		int codeError = 503;

		if (e instanceof NoSuchFileException || e instanceof NoSuchMethodException) {
			codeError = 404;
		}
		getWriter().write("<pre>");
		e.printStackTrace(new PrintWriter(this.getWriter()));
		getWriter().write("</pre>");
		this.end(codeError);
	}

	@Override
	public void end(int responseCode) throws IOException {
		sendResponseHeaders(responseCode, 0);
		end();
	}

	@Override
	public void end() throws IOException {
		try {
			if (!isResponseHeader) {
				sendResponseHeaders(200, 0);
			}
			getExchange().getResponseHeaders().putAll(getHeaders());
			getOutputStream().writeTo(getExchange().getResponseBody());
			getExchange().getResponseBody().write(getWriter().toString().getBytes(encoding));
			getWriter().close();
			getExchange().getResponseBody().close();
		}
		catch (IOException e) {
			getExchange().getResponseBody().close();
		}
	}

	public HttpExchange getExchange() {
		return exchange;
	}
}