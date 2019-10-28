package com.github.domainname.vendor.gitlab;

import com.github.domainname.WebhookClient;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.webhook.IssueEvent;
import org.gitlab4j.api.webhook.PushEvent;
import org.gitlab4j.api.webhook.WebHookListener;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @author jeff
 * @date 2019/10/28
 */
@Component
@RequiredArgsConstructor
public class GitLabWebhookListener implements WebHookListener {

    private final WebhookClient webhookClient;

    @Override
    public void onPushEvent(PushEvent event) {
        String key = MDC.get("key");

        String message = GitLabEventMessageBuilder.buildPushMessage(event);
        webhookClient.sendMarkdownMessage(key, message);
    }

    @Override
    public void onIssueEvent(IssueEvent event) {
        String key = MDC.get("key");

        String message = GitLabEventMessageBuilder.buildIssueMessage(event);
        webhookClient.sendMarkdownMessage(key, message);
    }
}
