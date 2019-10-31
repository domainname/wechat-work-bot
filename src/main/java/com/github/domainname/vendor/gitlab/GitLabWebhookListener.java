package com.github.domainname.vendor.gitlab;

import com.github.domainname.WebhookClient;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.webhook.*;
import org.slf4j.MDC;

/**
 * @author jeff
 * @date 2019/10/28
 */
@RequiredArgsConstructor
public class GitLabWebhookListener implements WebHookListener {

    private final WebhookClient webhookClient;

    @Override
    public void onPushEvent(PushEvent event) {
        // 忽略 commits 为空的事件
        if (event.getTotalCommitsCount() <= 0) {
            return;
        }

        String key = MDC.get("key");
        String message = GitLabEventMessageBuilder.buildPushMessage(event);
        webhookClient.sendMarkdownMessage(key, message);
    }

    @Override
    public void onMergeRequestEvent(MergeRequestEvent event) {
        // 忽略 open、merge 以外的事件
        String action = event.getObjectAttributes().getAction();
        if (!"open".equals(action) && !"merge".equals(action)) {
            return;
        }

        String key = MDC.get("key");
        String message = GitLabEventMessageBuilder.buildMergeRequestMessage(event);
        webhookClient.sendMarkdownMessage(key, message);
    }

    @Override
    public void onIssueEvent(IssueEvent event) {
        String key = MDC.get("key");
        String message = GitLabEventMessageBuilder.buildIssueMessage(event);
        webhookClient.sendMarkdownMessage(key, message);
    }

    @Override
    public void onPipelineEvent(PipelineEvent event) {
        // 忽略 pipeline 失败以外的事件
        String status = event.getObjectAttributes().getStatus();
        if (!"failed".equals(status)) {
            return;
        }

        String key = MDC.get("key");
        String message = GitLabEventMessageBuilder.buildPipelineMessage(event);
        webhookClient.sendMarkdownMessage(key, message);
    }

    @Override
    public void onNoteEvent(NoteEvent event) {
        String key = MDC.get("key");
        String message = GitLabEventMessageBuilder.buildNoteMessage(event);
        webhookClient.sendMarkdownMessage(key, message);
    }
}
