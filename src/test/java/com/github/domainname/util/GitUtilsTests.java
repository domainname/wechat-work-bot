package com.github.domainname.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.domainname.util.GitUtils.shortenCommitId;
import static org.junit.Assert.assertEquals;

/**
 * @author jeff
 * @date 2019/10/28
 */
@RunWith(SpringRunner.class)
public class GitUtilsTests {

    @Test
    public void test() {
        String commitId = "49cb7bdae13ace280decf723768c6eab230e23ed";
        String expected = "49cb7bda";
        assertEquals(expected, shortenCommitId(commitId));
    }
}
