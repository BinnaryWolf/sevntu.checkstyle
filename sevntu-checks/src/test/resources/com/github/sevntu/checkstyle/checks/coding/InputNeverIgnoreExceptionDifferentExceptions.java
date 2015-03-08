package com.github.sevntu.checkstyle.checks.coding;

public class InputNeverIgnoreExceptionDifferentExceptions
{
    public void method1()
    {
        try {
            throw new NullPointerException();
        }
        catch (NullPointerException iE) {
            //No need to handle that.... reason
            //No need to handle cause .... reason

            //No need to handle cause .... reason
            /**
             * No need to handle cause .... reason No need to handle cause ....
             * reason
             */
        }
    }

    public void method2()
    {
        try {
            throw new NullPointerException();
        }
        catch (NullPointerException iE) {
        }
    }

    public void method3()
    {
        try {
            throw new NullPointerException();
        }
        catch (NullPointerException iE) {
            handleNPE(iE);
        }
    }

    public void method4()
    {
        try {
            throw new InterruptedException();
        }
        catch (InterruptedException iE) {
            //No need to handle cause .... reason
            //No need to handle cause .... reason

            //No need to handle cause .... reason
            /**
             * No need to handle that.... reason No need to handle cause ....
             * reason
             */
        }
    }

    public void method5()
    {
        try {
            throw new InterruptedException();
        }
        catch (InterruptedException iE) {
        }
    }

    public void method6()
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

    private void handleNPE(NullPointerException iE)
    {
        //some code
    }

}
