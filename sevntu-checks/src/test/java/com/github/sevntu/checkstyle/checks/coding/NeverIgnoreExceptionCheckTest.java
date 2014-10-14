////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2014  Oliver Burn
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////
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
        final String[] expected = {
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
        final String[] expected = {
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
        final String[] expected = {
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
        final String[] expected = {
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
        final String[] expected = {
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
        final String[] expected = {
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
        final String[] expected = {
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
        final String[] expected = {
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
        final String[] expected = {
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
        final String[] expected = {
                "10: " + getCheckMessage(MSG_KEY, "NullPointerException"),
                "27: " + getCheckMessage(MSG_KEY, "NullPointerException")
        };
        verify(checkConfig,
                getPath("InputNeverIgnoreExceptionDifferentExceptions.java"),
                expected);
    }

}
