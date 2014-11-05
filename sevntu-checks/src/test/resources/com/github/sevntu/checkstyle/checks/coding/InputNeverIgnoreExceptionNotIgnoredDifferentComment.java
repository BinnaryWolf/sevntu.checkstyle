package com.github.sevntu.checkstyle.checks.coding;

public class InputNeverIgnoreExceptionNotIgnoredDifferentComment
{
    public void method1()
    {
        try {
            throw new InterruptedException();
        }
        catch (InterruptedException iE) {
            //No need to handle cause .... reason
        }
    }

    public void method2()
    {
        try {
            throw new InterruptedException();
        }
        catch (InterruptedException iE) {
            //No code
        }
    }

    public void method3()
    {
        try {
            throw new InterruptedException();
        }
        catch (InterruptedException iE) {
            /*
             * No need to handle cause .... reason
             */
        }
    }

    public void method4()
    {
        try {
            throw new InterruptedException();
        }
        catch (InterruptedException iE) {
            /**
             * No need to handle cause .... reason
             */
        }
    }

}
