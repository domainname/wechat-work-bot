package com.github.domainname;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.github.domainname.util.HttpClientUtils.createHttpClient;
import static java.lang.String.format;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

/**
 * @author jeff
 * @date 2019/10/28
 */
@Component
@Slf4j
public class WebhookClient {

    private final CloseableHttpClient httpClient = createHttpClient();

    /**
     * @param key       Webhook key
     * @param mdMessage Markdown 格式的消息
     * @throws RuntimeException 如果调用 Webhook 失败
     */
    public void sendMarkdownMessage(String key, String mdMessage) {
        String json = format("{\"msgtype\":\"markdown\",\"markdown\":{\"content\":\"%s\"}}", mdMessage);
        StringEntity stringEntity = new StringEntity(json, APPLICATION_JSON);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=" + key;
        HttpUriRequest post = RequestBuilder.post()
                .setUri(url)
                .setEntity(stringEntity)
                .build();

        try {
            String result = sendRequest(httpClient, post);
            log.debug("收到：{}", result);
        } catch (IOException e) {
            throw new RuntimeException("调用 Webhook 失败", e);
        }
    }

    private static String sendRequest(CloseableHttpClient httpClient, HttpUriRequest request) throws IOException {
        String content;

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            content = EntityUtils.toString(entity);
            log.debug("收到：{}", content);
        }

        return content;
    }
}
