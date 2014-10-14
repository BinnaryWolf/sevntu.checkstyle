package com.github.sevntu.checkstyle.checks.coding;

public class InputNeverIgnoreExceptionCustomException
{
    public void method1()
    {
        try {
            throw new NullPointerException();
        }
        catch (NullPointerException iE) {
            //No need to handle cause .... reason
            //No need to handle cause .... reason

            //No need to handle cause .... reason
            /**
             * No need to handle cause .... reason
             * No need to handle cause .... reason
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
            handleInterrupt(iE);
        }
    }

    private void handleInterrupt(NullPointerException iE)
    {
        //some code
    }

}
