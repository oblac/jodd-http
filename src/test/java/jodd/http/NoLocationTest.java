package jodd.http;

import jodd.http.fixture.StringHttpRequest;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

class NoLocationTest {

	@Test
	void testNoLocationInResponse() {
		HttpRequest httpRequest = StringHttpRequest.create(
				"HTTP/1.1 302 OK\n" +
				"Connection: close\n" +
				"\n");
		HttpResponse response = httpRequest.followRedirects(true).send();
		assertEquals(302, response.statusCode());
	}
}
