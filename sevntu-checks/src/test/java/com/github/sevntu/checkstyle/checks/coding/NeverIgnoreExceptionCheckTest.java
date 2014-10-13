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
                "10: " + getCheckMessage(MSG_KEY)
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
                "11: " + getCheckMessage(MSG_KEY)
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
                "True");
        String[] expected = {
                "27: " + getCheckMessage(MSG_KEY)
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
                "10: " + getCheckMessage(MSG_KEY),
                "27: " + getCheckMessage(MSG_KEY)
        };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionCustomException.java"),
                expected);
    }

}
