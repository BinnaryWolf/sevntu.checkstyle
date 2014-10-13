package com.github.sevntu.checkstyle.checks.coding;

import org.junit.Test;

import com.github.sevntu.checkstyle.BaseCheckTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;

public class NeverIgnoreExceptionCheckTest extends BaseCheckTestSupport
{
    /**
     * Warning message key.
     */
    public final static String MSG_KEY = "ignore.exception";

    private final DefaultConfiguration checkConfig = createCheckConfig(NeverIgnoreExceptionCheck.class);

    @Test
    public void testNotIgnored()
            throws Exception
    {
        String[] expected = {
                };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionNotIgnored.java"),
                expected);
    }

    @Test
    public void testNotIgnoredByCommentAllowed()
            throws Exception
    {
        String[] expected = {
                };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionNotIgnoredCommentContent.java"),
                expected);
    }

    @Test
    public void testNotIgnoredByCommentForbiden()
            throws Exception
    {
        String[] expected = {
                };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionNotIgnoredCommentContent.java"),
                expected);
    }

    @Test
    public void testExceptionIgnored()
            throws Exception
    {
        String[] expected = {
                };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionIgnored.java"),
                expected);
    }

    @Test
    public void testCustomExceptionIgnoredAllowComment()
            throws Exception
    {
        String[] expected = {
                };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionCustomException.java"),
                expected);
    }

    @Test
    public void testCustomExceptionIgnoredNotAllowComment()
            throws Exception
    {
        String[] expected = {
                };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionCustomException.java"),
                expected);
    }

}
