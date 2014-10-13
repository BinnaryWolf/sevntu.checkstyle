package com.github.sevntu.checkstyle.checks.coding;

public class InputNeverIgnoreExceptionNotIgnored
{

    public void method()
    {
        try {
            throw new InterruptedException();
        }
        catch (InterruptedException iE) {
            handleInterrupt(iE);
        }
    }

    private void handleInterrupt(InterruptedException iE)
    {
        //some code
    }

}
