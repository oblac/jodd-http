package jodd.http;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class RemoveHeaderTest {

	@Test
	void testRemoveHeader() {
		HttpRequest request = HttpRequest
				.get("https://example.com")
				.header("x-dummy-header", "remove me");

		assertNotNull(request.header("x-dummy-header"));

		request.headerRemove("x-dummy-header");

		assertNull(request.header("x-dummy-header"));
	}

	@Test
	void testRemoveHeaderUserAgent() throws IOException {
		HttpBase.Defaults.headers.remove("User-Agent");

		HttpRequest request = HttpRequest
				.get("https://example.com")
				.header("x-dummy-header", "remove me");

		assertNull(request.header("User-Agent"));

		request.headerRemove("User-Agent");

		assertNull(request.header("User-Agent"));

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		request.sendTo(baos);
		String out = baos.toString();

		assertFalse(out.contains("User-Agent"));

		HttpBase.Defaults.headers.addHeader("User-Agent", "Jodd HTTP");
	}
}
