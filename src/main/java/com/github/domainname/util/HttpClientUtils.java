package com.github.domainname.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author jeff
 * @date 2019/10/28
 */
public abstract class HttpClientUtils {

    private static final int CONNECTION_REQUEST_TIMEOUT = 3000;
    private static final int CONNECTION_TIMEOUT = 5000;
    private static final int SOCKET_TIMEOUT = 5000;
    private static final int MAX_CONN_PER_ROUTE = 10;
    private static final int MAX_CONN_TOTAL = 50;

    public static CloseableHttpClient createHttpClient() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(MAX_CONN_TOTAL);
        cm.setDefaultMaxPerRoute(MAX_CONN_PER_ROUTE);

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(config)
                .setConnectionManager(cm)
                .build();
    }
}
