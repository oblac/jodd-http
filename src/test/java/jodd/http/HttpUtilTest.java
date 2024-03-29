// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package jodd.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HttpUtilTest {

	@Test
	void testNiceHeaderNames() {
		assertEquals("Content-Type", HttpUtil.prepareHeaderParameterName("conTent-tyPe"));
		assertEquals("ETag", HttpUtil.prepareHeaderParameterName("etag"));
	}

	@Test
	void testMediaTypeAndParameters() {
		String contentType = "text/html";

		assertEquals("text/html", HttpUtil.extractMediaType(contentType));
		assertEquals(null, HttpUtil.extractHeaderParameter(contentType, "charset", ';'));

		contentType = "text/html;"; // special case, see #588

		assertEquals("text/html", HttpUtil.extractMediaType(contentType));
		assertEquals(null, HttpUtil.extractHeaderParameter(contentType, "charset", ';'));

		contentType = "text/html; charset=ISO-8859-4";

		assertEquals("text/html", HttpUtil.extractMediaType(contentType));
		assertEquals("ISO-8859-4", HttpUtil.extractHeaderParameter(contentType, "charset", ';'));


		contentType = "text/html;charset=ISO-8859-4";

		assertEquals("text/html", HttpUtil.extractMediaType(contentType));
		assertEquals("ISO-8859-4", HttpUtil.extractHeaderParameter(contentType, "charset", ';'));


		contentType = "text/html; pre=foo; charset=ISO-8859-4";

		assertEquals("text/html", HttpUtil.extractMediaType(contentType));
		assertEquals("ISO-8859-4", HttpUtil.extractHeaderParameter(contentType, "charset", ';'));


		contentType = "text/html; pre=foo; charset=ISO-8859-4; post=bar";

		assertEquals("text/html", HttpUtil.extractMediaType(contentType));
		assertEquals("ISO-8859-4", HttpUtil.extractHeaderParameter(contentType, "charset", ';'));
		assertEquals("foo", HttpUtil.extractHeaderParameter(contentType, "pre", ';'));
		assertEquals(null, HttpUtil.extractHeaderParameter(contentType, "na", ';'));
	}

	@Test
	void testDefaultPort() {
		HttpRequest request;

		request = HttpRequest.get("jodd.org");
		assertEquals("http", request.protocol());
		assertEquals(80, request.port());

		request = HttpRequest.get("jodd.org:80");
		assertEquals("http", request.protocol());
		assertEquals(80, request.port());

		request = HttpRequest.get("jodd.org:801");
		assertEquals("http", request.protocol());
		assertEquals(801, request.port());

		request = HttpRequest.get("http://jodd.org");
		assertEquals("http", request.protocol());
		assertEquals(80, request.port());

		request = HttpRequest.get("https://jodd.org");
		assertEquals("https", request.protocol());
		assertEquals(443, request.port());

		request = HttpRequest.get("https://jodd.org:8443");
		assertEquals("https", request.protocol());
		assertEquals(8443, request.port());
	}

	@Test
	void testBuildQuery() {
		final HttpMultiMap<String> map = HttpMultiMap.newCaseInsensitiveMap();

		assertEquals("", HttpUtil.buildQuery(map, StandardCharsets.UTF_8.name()));

		map.add("aaa", "one");
		assertEquals("aaa=one", HttpUtil.buildQuery(map, StandardCharsets.UTF_8.name()));

		map.add("bbb", "two");
		assertEquals("aaa=one&bbb=two", HttpUtil.buildQuery(map, StandardCharsets.UTF_8.name()));

		map.clear().add("ccc", null);
		assertEquals("ccc", HttpUtil.buildQuery(map, StandardCharsets.UTF_8.name()));

		map.add("ddd", "four");
		assertEquals("ccc&ddd=four", HttpUtil.buildQuery(map, StandardCharsets.UTF_8.name()));
	}

	@Test
	void testParseQuery() {
		HttpMultiMap<String> map = HttpUtil.parseQuery("a=b", false);

		assertEquals(1, map.size());
		assertEquals("b", map.get("a"));


		map = HttpUtil.parseQuery("a=b&c=d", false);

		assertEquals(2, map.size());
		assertEquals("b", map.get("a"));
		assertEquals("d", map.get("c"));
	}

	@Test
	void testParseQuery_specialCase() {
		HttpMultiMap<String> map = HttpUtil.parseQuery("a&b", false);

		assertEquals(2, map.size());
		assertNull(map.get("a"));
		assertNull(map.get("b"));


		map = HttpUtil.parseQuery("a&c=d", false);

		assertEquals(2, map.size());
		assertNull(map.get("a"));
		assertEquals("d", map.get("c"));
	}

	@Test
	void testAbsoluteUrls() {
		assertTrue(HttpUtil.isAbsoluteUrl("http://jodd.org"));

		assertTrue(HttpUtil.isAbsoluteUrl("https://jodd.org"));

		assertFalse(HttpUtil.isAbsoluteUrl("just/a/path"));
		assertFalse(HttpUtil.isAbsoluteUrl("/just/a/path"));
		assertFalse(HttpUtil.isAbsoluteUrl("/ju:st/a/path"));

		assertTrue(HttpUtil.isAbsoluteUrl("ju:st/a/path"));
	}

	@Test
	void testAbsoluteUrls2() {
		{
			HttpRequest request = HttpRequest.get("https://jodd.org/hello/world");
			String location = "https://jodd.org/foo";

			assertEquals("https://jodd.org/foo", locationOf(request, location));
		}
		{
			HttpRequest request = HttpRequest.get("https://jodd.org/hello/world");
			String location = "foo";

			assertEquals("https://jodd.org/hello/foo", locationOf(request, location));
		}
	}


	private String locationOf(HttpRequest httpRequest, String location) {
		HttpResponse r = new HttpResponse();
		r.assignHttpRequest(httpRequest);
		r.header("location", location);
		return r.location();
	}

}
