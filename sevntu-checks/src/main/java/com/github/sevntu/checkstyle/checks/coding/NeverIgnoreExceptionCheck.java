package com.github.sevntu.checkstyle.checks.coding;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

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
    public void visitToken(DetailAST aMethodNode)
    {
        
    }
}
