package com.github.sevntu.checkstyle.checks.coding;

public class InputNeverIgnoreExceptionIgnored
{

    public void method()
    {
        try {
            throw new InterruptedException();
        }
        catch (InterruptedException iE) {
        }
    }

}
