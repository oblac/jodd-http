package jodd.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FragmentTest {

	@Test
	void testFragment() {
		final HttpRequest request = HttpRequest.get("https://studioharrison.com/#product-group");

		assertEquals("/", request.path);
		assertEquals("product-group", request.fragment());

		assertEquals("https://studioharrison.com/#product-group", request.url());
	}

	@Test
	void testFragmentWithQuery() {
		final HttpRequest request = HttpRequest.get("https://studioharrison.com/foo?me=1#foo");

		assertEquals("/foo", request.path);
		assertEquals("foo", request.fragment());

		assertEquals("https://studioharrison.com/foo?me=1#foo", request.url());
	}
}
