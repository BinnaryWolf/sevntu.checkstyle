package com.github.sevntu.checkstyle.checks.coding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.sun.mirror.util.Types;

public class NeverIgnoreExceptionCheck extends Check
{
    /**
     * Warning message key.
     */
    public final static String MSG_KEY = "ignore.exception";
    /**
     * Default isCommentAllowed
     */
    public final static boolean DEFAULT_IS_COMMENT_ALLOWED = true;

    /**
     * Default regexp for exception classname
     */
    public final static String DEFAULT_EXCEPTION_CLASSNAME_REGEXP = "InterruptedException";

    private boolean mIsCommentAllowed;
    private String mExceptionClassNameRegexp;

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
        return new int[] { TokenTypes.LITERAL_CATCH };
    }

    @Override
    public void visitToken(final DetailAST aMethodNode)
    {
        final DetailAST parameterDef = aMethodNode
                .findFirstToken(TokenTypes.PARAMETER_DEF);
        final String methodName = parameterDef.findFirstToken(TokenTypes.TYPE)
                .getFirstChild().getText();
        if (isExceptionMatchRegexp(methodName)) {
            final DetailAST sListToken = aMethodNode
                    .findFirstToken(TokenTypes.SLIST);
            if (isEmptySlist(sListToken)) {
                if (!(mIsCommentAllowed && isSListContainsComment(sListToken))) {
                    log(aMethodNode.getLineNo(), MSG_KEY, methodName);
                }
            }
        }

    }

    private boolean isExceptionMatchRegexp(final String aMethodName)
    {
        final Pattern methodNamePattern = Pattern
                .compile(mExceptionClassNameRegexp);
        final Matcher regexpMatcher = methodNamePattern.matcher(aMethodName);
        return regexpMatcher.matches();
    }

    private boolean isEmptySlist(final DetailAST aSlistToken)
    {
        return aSlistToken.getFirstChild().getType() == TokenTypes.RCURLY;
    }

    private boolean isSListContainsComment(final DetailAST aSlistToken)
    {
        final DetailAST rCurlyTocken = aSlistToken
                .findFirstToken(TokenTypes.RCURLY);
        return getFileContents().hasIntersectionWithComment(
                aSlistToken.getLineNo(), aSlistToken.getColumnNo(),
                rCurlyTocken.getLineNo(), rCurlyTocken.getColumnNo());
    }
}
