/**
 * The MIT License
 *
 * Copyright for portions of OpenUnirest/uniresr-java are held by Mashape (c) 2013 as part of Kong/unirest-java.
 * All other copyright for OpenUnirest/unirest-java are held by OpenUnirest (c) 2018.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package unirest;

import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public class HttpRequestWithBody extends BaseRequest<HttpRequestBody> implements HttpRequestBody {

	private Charset charSet = StandardCharsets.UTF_8;

	public HttpRequestWithBody(Config config, HttpMethod method, String url) {
		super(config, method, url);
	}

	@Override
	public HttpRequestMultPart field(String name, Collection<?> value) {
		HttpRequestMultPart body = new MultipartBody(this).field(name, value);
		this.body = body;
		return body;
	}

	@Override
	public HttpRequestMultPart field(String name, File file) {
		return field(name, file, null);
	}

	@Override
	public HttpRequestMultPart field(String name, File file, String contentType) {
		HttpRequestMultPart body = new MultipartBody(this).field(name, file, contentType);
		this.body = body;
		return body;
	}

	@Override
	public HttpRequestMultPart field(String name, Object value) {
		return field(name, value, null);
	}

	@Override
	public HttpRequestMultPart field(String name, Object value, String contentType) {
		HttpRequestMultPart body = new MultipartBody(this).field(name, nullToEmpty(value), contentType);
		this.body = body;
		return body;
	}

	@Override
	public HttpRequestMultPart fields(Map<String, Object> parameters) {
		MultipartBody body = new MultipartBody(this);
		if (parameters != null) {
			for (Entry<String, Object> param : parameters.entrySet()) {
				if (param.getValue() instanceof File) {
					body.field(param.getKey(), (File) param.getValue());
				} else {
					body.field(param.getKey(), nullToEmpty(param.getValue()));
				}
			}
		}
		this.body = body;
		return body;
	}

	private String nullToEmpty(Object v) {
		if(v == null){
			return "";
		}
		return v.toString();
	}

	@Override
	public HttpRequestMultPart field(String name, InputStream stream, ContentType contentType, String fileName) {
		HttpRequestMultPart body = new MultipartBody(this).field(name, stream, contentType, fileName);
		this.body = body;
		return body;
	}

	@Override
	public HttpRequestMultPart field(String name, InputStream stream, String fileName) {
		HttpRequestMultPart body = field(name, stream, ContentType.APPLICATION_OCTET_STREAM, fileName);
		this.body = body;
		return body;
	}

	@Override
	public HttpRequestWithBody charset(Charset charset) {
		this.charSet = charset;
		return this;
	}

	@Override
	public HttpRequestUniBody body(JsonNode body) {
		return body(body.toString());
	}

	@Override
	public HttpRequestUniBody body(String body) {
        HttpRequestUniBody b = new RequestBodyEntity(this).body(body);
		this.body = b;
		return b;
	}

	@Override
	public HttpRequestUniBody body(Object body) {
		return body(config.getObjectMapper().writeValue(body));
	}

	@Override
	public HttpRequestUniBody body(byte[] body) {
        HttpRequestUniBody b = new RequestBodyEntity(this).body(body);
		this.body = b;
		return b;
	}

	/**
	 * Sugar method for body operation
	 *
	 * @param body raw org.JSONObject
	 * @return RequestBodyEntity instance
	 */
	@Override
	public HttpRequestUniBody body(JSONObject body) {
		return body(body.toString());
	}

	/**
	 * Sugar method for body operation
	 *
	 * @param body raw org.JSONArray
	 * @return RequestBodyEntity instance
	 */
	@Override
	public HttpRequestUniBody body(JSONArray body) {
		return body(body.toString());
	}

	@Override
	public Charset getCharset() {
		return charSet;
	}

	void setCharset(Charset charset) {
		this.charSet = charset;
	}
}