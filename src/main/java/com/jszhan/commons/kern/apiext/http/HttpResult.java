package com.jszhan.commons.kern.apiext.http;

import java.io.Serializable;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.protocol.HttpContext;

public class HttpResult implements Serializable {
	private static final long serialVersionUID = 1L;
	private HttpContext context;
	private byte[] body;
	private Header contentEncoding;
	private long contentLength;
	private Header contentType;
	private StatusLine statusLine;

	public int getHttpStatus() {
		return this.statusLine == null ? 0 : this.statusLine.getStatusCode();
	}

	public StatusLine getStatusLine() {
		return statusLine;
	}

	public void setStatusLine(StatusLine statusLine) {
		this.statusLine = statusLine;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public Header getContentType() {
		return contentType;
	}

	public void setContentType(Header contentType) {
		this.contentType = contentType;
	}

	public HttpContext getContext() {
		return context;
	}

	public void setContext(HttpContext context) {
		this.context = context;
	}

	public byte[] getBody() {
		return body;
	}

	public String getBodyStr() {
		try {
			return new String(this.body, "UTF-8");
		} catch (Exception e) {
			return new String(this.body);
		}

	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public Header getContentEncoding() {
		return contentEncoding;
	}

	public void setContentEncoding(Header contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

}
