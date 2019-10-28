package com.github.domainname.util;

import java.util.Objects;

/**
 * @author jeff
 * @date 2019/10/27
 */
public abstract class GitUtils {

    /**
     * 将 commit ID 缩短至 8 位
     *
     * @return
     */
    public static String shortenCommitId(String commitId) {
        Objects.requireNonNull(commitId, "commitId");

        return commitId.length() > 8 ? commitId.substring(0, 8) : commitId;
    }
}
