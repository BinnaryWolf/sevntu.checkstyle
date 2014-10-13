package com.github.sevntu.checkstyle.checks.coding;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class NeverIgnoreExceptionCheck extends Check
{

    @Override
    public int[] getDefaultTokens()
    {
        return new int [] {TokenTypes.LITERAL_CATCH};
    }
    
    @Override
    public void visitToken(DetailAST aMethodNode)
    {
        
    }
}
