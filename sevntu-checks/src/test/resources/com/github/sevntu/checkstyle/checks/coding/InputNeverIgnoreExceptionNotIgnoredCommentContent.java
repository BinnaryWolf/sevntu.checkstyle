package com.github.sevntu.checkstyle.checks.coding;

public class InputNeverIgnoreExceptionNotIgnoredCommentContent
{
    public void method()
    {
        try {
            throw new InterruptedException();
        }
        catch (InterruptedException iE) {
            //No need to handle cause .... reason
        }
    }

    

}
