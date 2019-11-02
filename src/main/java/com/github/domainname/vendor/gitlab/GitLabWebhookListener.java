package com.github.domainname.vendor.gitlab;

import com.github.domainname.WebhookClient;
import lombok.RequiredArgsConstructor;
import org.gitlab4j.api.webhook.*;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author jeff
 * @date 2019/10/28
 */
@RequiredArgsConstructor
public class GitLabWebhookListener implements WebHookListener {

    private static final List<String> NOTABLE_MERGE_REQUEST_ACTIONS = Arrays.asList("open", "merge");
    private static final List<String> NOTABLE_ISSUE_ACTIONS = Arrays.asList("open", "close", "reopen");
    private static final List<String> NOTABLE_PIPELINE_STATUS = Arrays.asList("failed");

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
        String action = event.getObjectAttributes().getAction();
        if (!NOTABLE_MERGE_REQUEST_ACTIONS.contains(action)) {
            return;
        }

        String key = MDC.get("key");
        String message = GitLabEventMessageBuilder.buildMergeRequestMessage(event);
        webhookClient.sendMarkdownMessage(key, message);
    }

    @Override
    public void onIssueEvent(IssueEvent event) {
        String action = event.getObjectAttributes().getAction();
        if (!NOTABLE_ISSUE_ACTIONS.contains(action)) {
            return;
        }

        String key = MDC.get("key");
        String message = GitLabEventMessageBuilder.buildIssueMessage(event);
        webhookClient.sendMarkdownMessage(key, message);
    }

    @Override
    public void onPipelineEvent(PipelineEvent event) {
        String status = event.getObjectAttributes().getStatus();
        if (!NOTABLE_PIPELINE_STATUS.contains(status)) {
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
