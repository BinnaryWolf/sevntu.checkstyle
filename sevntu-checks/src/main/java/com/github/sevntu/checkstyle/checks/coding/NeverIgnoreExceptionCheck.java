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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * <p>
 * This Check warns on non handling exceptions.<br>
 * Examples:
 * <p>
 * <code><pre>
 * try{
 *      //some code
 * }
 * catch (InterruptedException e) {
 * }
 * <br>
 * try{
 *      //some code
 * }
 * catch (InterruptedException e) {
 *      //No code here
 * }
 * </pre></code>Check could be configured to validate exceptions that match
 * regex pattern(InterruptedException by default).<br>
 * Also you can allow using comment to decribe why exception not handle instead
 * of handling exception with code.
 * @author <a href="mailto:binnarywolf@gmail.com">Dmitriy Bazunov</a>
 */
public class NeverIgnoreExceptionCheck extends Check
{
    /**
     * Warning message key.
     */
    public static final String MSG_KEY = "ignore.exception";
    /**
     * Default isCommentAllowed.
     */
    public static final boolean DEFAULT_IS_COMMENT_ALLOWED = true;

    /**
     * Default regex pattern for exception class name.
     */
    public static final String DEFAULT_EXCEPTION_CLASSNAME_REGEXP = "InterruptedException";

    /**
     * If true allow to use comments instead of handling method.
     */
    private boolean mIsCommentAllowed;

    /**
     * Regex pattern for Exception name.
     */
    private String mExceptionClassNameRegexp;

    /**
     * Constructor with default parameters.
     */
    public NeverIgnoreExceptionCheck()
    {
        mIsCommentAllowed = DEFAULT_IS_COMMENT_ALLOWED;
        mExceptionClassNameRegexp = DEFAULT_EXCEPTION_CLASSNAME_REGEXP;
    }

    public void setIsCommentAllowed(boolean aAllow)
    {
        mIsCommentAllowed = aAllow;
    }

    public void setExceptionClassNameRegexp(String aRegexp)
    {
        mExceptionClassNameRegexp = aRegexp;
    }

    @Override
    public int[] getDefaultTokens()
    {
        return new int[] {TokenTypes.LITERAL_CATCH };
    }

    @Override
    public void visitToken(final DetailAST aCatchNode)
    {
        final DetailAST parameterDef = aCatchNode
                .findFirstToken(TokenTypes.PARAMETER_DEF);
        final String exceptionName = parameterDef
                .findFirstToken(TokenTypes.TYPE)
                .getFirstChild().getText();
        if (isExceptionMatchRegexp(exceptionName)) {
            final DetailAST sListToken = aCatchNode
                    .findFirstToken(TokenTypes.SLIST);
            if (isEmptySlist(sListToken)) {
                if (!(mIsCommentAllowed && isSListContainsComment(sListToken))) {
                    log(aCatchNode.getLineNo(), MSG_KEY, exceptionName);
                }
            }
        }

    }

    /**
     * Method check if exception name match regex pattern.
     * @param aExceptionName
     *        - String class name of exception to match.
     * @return true if exception class name match and false otherwise.
     */
    private boolean isExceptionMatchRegexp(final String aExceptionName)
    {
        final Pattern methodNamePattern = Pattern
                .compile(mExceptionClassNameRegexp);
        final Matcher regexpMatcher = methodNamePattern.matcher(aExceptionName);
        return regexpMatcher.matches();
    }

    /**
     * Method check if catch body has no code.
     * @param aSlistToken
     *        - DetailAST node of AST which represent catch body.
     * @return true if catch has no expressions and false otherwise.
     */
    private boolean isEmptySlist(final DetailAST aSlistToken)
    {
        return aSlistToken.getFirstChild().getType() == TokenTypes.RCURLY;
    }

    /**
     * Method check if catch body has comments.
     * @param aSlistToken
     *        - DetailAST node of AST which represent catch body.
     * @return true if catch body has comments and false otherwise.
     */
    private boolean isSListContainsComment(final DetailAST aSlistToken)
    {
        final DetailAST rCurlyTocken = aSlistToken
                .findFirstToken(TokenTypes.RCURLY);
        return getFileContents().hasIntersectionWithComment(
                aSlistToken.getLineNo(), aSlistToken.getColumnNo(),
                rCurlyTocken.getLineNo(), rCurlyTocken.getColumnNo());
    }
}
