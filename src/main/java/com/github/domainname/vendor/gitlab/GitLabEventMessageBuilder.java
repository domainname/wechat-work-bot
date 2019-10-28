package com.github.domainname.vendor.gitlab;

import org.gitlab4j.api.webhook.EventProject;
import org.gitlab4j.api.webhook.IssueEvent;
import org.gitlab4j.api.webhook.IssueEvent.ObjectAttributes;
import org.gitlab4j.api.webhook.PushEvent;

import java.util.stream.Collectors;

import static com.github.domainname.util.GitUtils.shortenCommitId;
import static java.lang.String.format;

/**
 * 将各种 GitLabEvent 组装成 MarkDown 消息，以便后续发送到企业微信。
 *
 * @author jeff
 * @date 2019/10/28
 */
public abstract class GitLabEventMessageBuilder {

    public static String buildPushMessage(PushEvent event) {
        String commits = event.getCommits().stream()
                .map(commit -> {
                    String shortId = shortenCommitId(commit.getId().substring(0, 8));
                    String url = commit.getUrl();
                    String author = commit.getAuthor().getName();
                    String message = commit.getMessage().replace("\n", "\n> ");

                    return format("> [%s](%s) - %s：%s", shortId, url, author, message);
                })
                .collect(Collectors.joining("\n"));

        String userName = event.getUserName();
        EventProject project = event.getProject();
        String projectPath = project.getPathWithNamespace();
        String projectWebUrl = project.getWebUrl();
        String branchName = event.getBranch();

        return format("%s pushed to [%s](%s) 的分支 %s\n" +
                        "%s",
                userName, projectPath, projectWebUrl, branchName, commits);
    }

    public static String buildIssueMessage(IssueEvent event) {
        String userName = event.getUser().getName();
        ObjectAttributes details = event.getObjectAttributes();
        String issueTitle = details.getTitle();
        String issueUrl = details.getUrl();

        return format("%s 提交了 Issue：[**%s**](%s)",
                userName, issueTitle, issueUrl);
    }
}
