package jodd.http.fixture;

import jodd.http.HttpConnection;
import jodd.http.HttpConnectionProvider;
import jodd.http.HttpRequest;
import jodd.http.ProxyInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class StringHttpRequest {

	public static HttpRequest create(final String payload) {
		return new HttpRequest().withConnectionProvider(new HttpConnectionProvider() {
			@Override
			public void useProxy(ProxyInfo proxyInfo) {
			}

			@Override
			public HttpConnection createHttpConnection(HttpRequest httpRequest) {
				return new HttpConnection() {

					private final ByteArrayInputStream in = new ByteArrayInputStream(payload.getBytes());
					private final ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

					@Override
					public void init() {
					}

					@Override
					public OutputStream getOutputStream() {
						return out;
					}

					@Override
					public InputStream getInputStream() {
						return in;
					}

					@Override
					public void close() {
					}

					@Override
					public void setTimeout(int milliseconds) {
					}
				};
			}
		});
	}
}
