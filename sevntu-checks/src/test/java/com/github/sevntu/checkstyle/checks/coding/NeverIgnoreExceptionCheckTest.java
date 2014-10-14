package com.github.sevntu.checkstyle.checks.coding;

import static com.github.sevntu.checkstyle.checks.coding.NeverIgnoreExceptionCheck.MSG_KEY;

import org.junit.Test;

import com.github.sevntu.checkstyle.BaseCheckTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;

public class NeverIgnoreExceptionCheckTest extends BaseCheckTestSupport
{

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
        checkConfig.addAttribute("isCommentAllowed",
                "true");
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
        checkConfig.addAttribute("isCommentAllowed",
                "false");
        String[] expected = {
                "10: " + getCheckMessage(MSG_KEY, "InterruptedException")
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
                "11: " + getCheckMessage(MSG_KEY, "InterruptedException")
        };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionIgnored.java"),
                expected);
    }

    @Test
    public void testCustomExceptionIgnoredAllowComment()
            throws Exception
    {
        final String exceptionNameRegexp = "NullPointerException";
        checkConfig.addAttribute("exceptionClassNameRegexp",
                exceptionNameRegexp);
        checkConfig.addAttribute("isCommentAllowed",
                "true");
        String[] expected = {
                "27: " + getCheckMessage(MSG_KEY, "NullPointerException")
        };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionCustomException.java"),
                expected);
    }

    @Test
    public void testCustomExceptionIgnoredNotAllowComment()
            throws Exception
    {
        final String exceptionNameRegexp = "NullPointerException";
        checkConfig.addAttribute("exceptionClassNameRegexp",
                exceptionNameRegexp);
        checkConfig.addAttribute("isCommentAllowed",
                "false");
        String[] expected = {
                "10: " + getCheckMessage(MSG_KEY, "NullPointerException"),
                "27: " + getCheckMessage(MSG_KEY, "NullPointerException")
        };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionCustomException.java"),
                expected);
    }

    @Test
    public void testCustomExceptionIgnoredAllowCommentAllPatern()
            throws Exception
    {
        final String exceptionNameRegexp = "[\\w\\.]*Exception";
        checkConfig.addAttribute("exceptionClassNameRegexp",
                exceptionNameRegexp);
        checkConfig.addAttribute("isCommentAllowed",
                "true");
        String[] expected = {
                "27: " + getCheckMessage(MSG_KEY, "NullPointerException"),
                "63: " + getCheckMessage(MSG_KEY, "InterruptedException")
        };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionDifferentExceptions.java"),
                expected);
    }

    @Test
    public void testCustomExceptionIgnoredNotAllowCommentAllPatern()
            throws Exception
    {
        final String exceptionNameRegexp = "[\\w\\.]*Exception";
        checkConfig.addAttribute("exceptionClassNameRegexp",
                exceptionNameRegexp);
        checkConfig.addAttribute("isCommentAllowed",
                "false");
        String[] expected = {
                "10: " + getCheckMessage(MSG_KEY, "NullPointerException"),
                "27: " + getCheckMessage(MSG_KEY, "NullPointerException"),
                "46: " + getCheckMessage(MSG_KEY, "InterruptedException"),
                "63: " + getCheckMessage(MSG_KEY, "InterruptedException")
        };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionDifferentExceptions.java"),
                expected);
    }

    @Test
    public void testCustomExceptionIgnoredAllowCommentNotAll()
            throws Exception
    {
        final String exceptionNameRegexp = "NullPointerException";
        checkConfig.addAttribute("exceptionClassNameRegexp",
                exceptionNameRegexp);
        checkConfig.addAttribute("isCommentAllowed",
                "true");
        String[] expected = {
                "27: " + getCheckMessage(MSG_KEY, "NullPointerException")
        };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionDifferentExceptions.java"),
                expected);
    }

    @Test
    public void testCustomExceptionIgnoredNotAllowCommentNotAll()
            throws Exception
    {
        final String exceptionNameRegexp = "NullPointerException";
        checkConfig.addAttribute("exceptionClassNameRegexp",
                exceptionNameRegexp);
        checkConfig.addAttribute("isCommentAllowed",
                "false");
        String[] expected = {
                "10: " + getCheckMessage(MSG_KEY, "NullPointerException"),
                "27: " + getCheckMessage(MSG_KEY, "NullPointerException")
        };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionDifferentExceptions.java"),
                expected);
    }

}
