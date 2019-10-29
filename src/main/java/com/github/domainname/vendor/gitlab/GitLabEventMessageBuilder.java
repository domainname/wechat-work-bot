package com.github.domainname.vendor.gitlab;

import lombok.extern.slf4j.Slf4j;
import org.gitlab4j.api.webhook.*;

import java.util.stream.Collectors;

import static com.github.domainname.util.GitUtils.shortenCommitId;
import static java.lang.String.format;

/**
 * 将各种 GitLabEvent 组装成 MarkDown 消息，以便后续发送到企业微信。
 *
 * @author jeff
 * @date 2019/10/28
 */
@Slf4j
public abstract class GitLabEventMessageBuilder {

    public static String buildPushMessage(PushEvent event) {
        log.debug("收到：{}", event);

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
        String branchName = event.getBranch();
        String projectPath = project.getPathWithNamespace();
        String projectWebUrl = project.getWebUrl();

        return format("%s pushed to [%s](%s) 的分支 %s\n" +
                        "%s",
                userName, projectPath, projectWebUrl, branchName, commits);
    }

    public static String buildMergeRequestMessage(MergeRequestEvent event) {
        log.debug("收到：{}", event);

        String userName = event.getUser().getName();
        EventProject project = event.getProject();
        String projectPath = project.getPathWithNamespace();
        String projectWebUrl = project.getWebUrl();

        MergeRequestEvent.ObjectAttributes details = event.getObjectAttributes();
        Integer mrId = details.getIid();
        String mrTitle = details.getTitle();
        String mrUrl = details.getUrl();

        return format("%s 合并了 [MR !%d](%s) in [%s](%s)：**%s**",
                userName, mrId, mrUrl, projectPath, projectWebUrl, mrTitle);
    }

    public static String buildIssueMessage(IssueEvent event) {
        log.debug("收到：{}", event);

        String userName = event.getUser().getName();

        IssueEvent.ObjectAttributes details = event.getObjectAttributes();
        String issueTitle = details.getTitle();
        String issueUrl = details.getUrl();

        return format("%s 提交了 Issue：[**%s**](%s)",
                userName, issueTitle, issueUrl);
    }

    public static String buildPipelineMessage(PipelineEvent event) {
        log.debug("收到：{}", event);

        String userName = event.getUser().getName();

        EventProject project = event.getProject();
        String projectPath = project.getPathWithNamespace();
        String projectWebUrl = project.getWebUrl();

        PipelineEvent.ObjectAttributes details = event.getObjectAttributes();
        Integer pipelineId = details.getId();
        String branchName = details.getRef();

        String pipelineUrl = projectWebUrl + "/pipelines/" + pipelineId;
        String branchUrl = projectWebUrl + "/commits/" + branchName;

        return format("[%s](%s)：Pipeline [#%d](%s) <font color=\"warning\">失败</font>，分支 [%s](%s) by %s",
                projectPath, projectWebUrl, pipelineId, pipelineUrl, branchName, branchUrl, userName);
    }

    public static String buildNoteMessage(NoteEvent event) {
        log.debug("收到：{}", event);

        String userName = event.getUser().getName();

        EventProject project = event.getProject();
        String projectPath = project.getPathWithNamespace();
        String projectWebUrl = project.getWebUrl();

        NoteEvent.ObjectAttributes details = event.getObjectAttributes();
        String note = details.getNote();
        String noteUrl = details.getUrl();
        NoteEvent.NoteableType noteableType = details.getNoteableType();

        String title;
        String action;

        switch (noteableType) {
            case MERGE_REQUEST:
                EventMergeRequest mr = event.getMergeRequest();
                title = mr.getTitle();
                action = "回复了 MR !" + mr.getIid();
                break;

            case ISSUE:
                EventIssue issue = event.getIssue();
                title = issue.getTitle();
                action = "回复了 Issue #" + issue.getIid();
                break;

            case COMMIT:
                EventCommit commit = event.getCommit();
                // FIXME title 不应该为空
                title = null;
                action = "回复了 commit " + commit.getId();
                break;

            case SNIPPET:
                EventSnippet snippet = event.getSnippet();
                title = snippet.getTitle();
                action = "回复了 snippet " + snippet.getId();
                break;

            default:
                throw new IllegalStateException("不支持的 noteableType：" + noteableType);
        }

        return format("%s [%s](%s) of [%s](%s)：%s\n" +
                        "> %s",
                userName, action, noteUrl, projectPath, projectWebUrl, title,
                note);
    }
}
