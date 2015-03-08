////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2015  Oliver Burn
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
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
 * Also you can allow missed handling by special excuse comment, that you can
 * set up in option suppressionCommentRegExp.<br>
 * To configure check suppress exception by comment you need to set
 * IsCommentAllowed property to true and configure SuppressCommentRegexp as you
 * need. Example: <code><pre>
 * try{
 *      //some code
 * }
 * catch (InterruptedException e) {
 *      //No code here
 * }
 * <br>
 * try{
 *      //some code
 * }
 * catch (InterruptedException e) {
 *      // No need to handle cause
 * }
 * </pre></code> First catch will be warn by check cause "No code here" doesn't
 * match suppress comment pattern.
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
    public static final boolean DEFAULT_IS_COMMENT_ALLOWED = false;

    /**
     * Default regex pattern for exception class name.
     */
    public static final String DEFAULT_EXCEPTION_CLASSNAME_REGEXP =
            "(java.lang.)*InterruptedException";

    /**
     * Default regex pattern for suppress comment.
     */
    public static final String DEFAULT_SUPPRESS_COMMENT_REGEXP = ".+No need to handle that.+";

    /**
     * If true allow to use comments instead of handling method.
     */
    private boolean isCommentAllowed = DEFAULT_IS_COMMENT_ALLOWED;

    /**
     * Regex pattern for Exception name.
     */
    private Pattern exceptionClassNameRegexpPattern = Pattern
            .compile(DEFAULT_EXCEPTION_CLASSNAME_REGEXP);

    /**
     * Regex pattern for Exception name.
     */
    private Pattern commentSuppressRegexpPattern = Pattern
            .compile(DEFAULT_SUPPRESS_COMMENT_REGEXP);

    public void setCommentSuppressRegexp(String aCommentSuppressRegexp)
    {
        commentSuppressRegexpPattern = Pattern.compile(aCommentSuppressRegexp);
    }

    public void setIsCommentAllowed(boolean aAllow)
    {
        isCommentAllowed = aAllow;
    }

    public void setExceptionClassNameRegexp(String aRegexp)
    {
        exceptionClassNameRegexpPattern = Pattern.compile(aRegexp);
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
        final DetailAST catchTypeNode = parameterDef
                .findFirstToken(TokenTypes.TYPE).getFirstChild();
        final List<String> exceptionsNameList = new ArrayList<String>();
        /*
         * Switch is needed to divide 3 catch type situations.
         * 1) Simple catch.
         * catch(Exception e) - token has type IDENT and it's enough just to get text from it
         *
         * 2)Full path catch
         * catch(java.lang.Exception e) -token that represent such exception is DOT.
         * DOT(.)
         * |__DOT(.)
         * |    |__IDENT(java)
         * |    |__IDENT(lang)
         * |__IDENT(Exception)
         *
         * 3)Multi catch
         * catch (java.lang.InterruptedException | NullPointerException| IndexOutOfBoundsException iE)
         * token that represent such catch is BOR
         * BOR(|)
         * |__BOR(|)
         * |    |__DOT(.)
         * |    |__IDENT(NullPointerException)
         * |__IDENT(IndexOutOfBoundsException)
        */
        switch (catchTypeNode.getType()) {
            case TokenTypes.IDENT:
                exceptionsNameList.add(catchTypeNode.getText());
                break;
            case TokenTypes.DOT:
                exceptionsNameList.add(getExceptionNameWithPackage(catchTypeNode));
                break;
            case TokenTypes.BOR:
                getExceptionNameFromMultiCatch(catchTypeNode, exceptionsNameList);
                break;
            default:
                break;
        }
        for (String exceptionName : exceptionsNameList) {
            if (isExceptionMatchRegexp(exceptionName)) {
                final DetailAST sListToken = aCatchNode
                        .findFirstToken(TokenTypes.SLIST);
                if (isEmptyCatch(sListToken)) {
                    if (!(isCommentAllowed && isCatchContainsSuppressComment(sListToken))) {
                        log(aCatchNode.getLineNo(), MSG_KEY, exceptionName);
                    }
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
        final Matcher regexpMatcher = exceptionClassNameRegexpPattern
                .matcher(aExceptionName);
        return regexpMatcher.matches();
    }

    /**
     * Method check if catch body has no code.
     * @param aSlistToken
     *        - DetailAST node of AST which represent catch body.
     * @return true if catch has no expressions and false otherwise.
     */
    private static boolean isEmptyCatch(final DetailAST aSlistToken)
    {
        return aSlistToken.getFirstChild().getType() == TokenTypes.RCURLY;
    }

    /**
     * Method check if catch body has comments.
     * @param aSlistToken
     *        - DetailAST node of AST which represent catch body.
     * @return true if catch body has comments and false otherwise.
     */
    private boolean isCatchContainsSuppressComment(final DetailAST aSlistToken)
    {
        final DetailAST rCurlyTocken = aSlistToken
                .findFirstToken(TokenTypes.RCURLY);
        return isCommentContainsSuppressLine(aSlistToken.getLineNo(),
                rCurlyTocken.getLineNo());
    }

    /**
     * Method get full exception name with package name.
     * @param aDotNode
     *        - DetailAST node of AST which represent node with type DOT.
     * @return String which content exception name with full pass.
     */
    private static String getExceptionNameWithPackage(final DetailAST aDotNode)
    {
        String beforeDotString = "";
        final DetailAST leftNode = aDotNode.getFirstChild();
        switch (leftNode.getType()) {
            case TokenTypes.IDENT:
                beforeDotString = leftNode.getText();
                break;
            case TokenTypes.DOT:
                beforeDotString = getExceptionNameWithPackage(leftNode);
                break;
            default:
                break;
        }
        final String nameBuilder = beforeDotString + '.'
                + leftNode.getNextSibling().getText();
        return nameBuilder.toString();
    }

    /**
     * Method add exception names from multi catch to exception name list.
     * @param aBorNode
     *        - DetailAST node of AST which represent node with type BOR.
     * @param aNameList
     *        - List of exception names from catch block.
     */
    private static
            void
            getExceptionNameFromMultiCatch(final DetailAST aBorNode, final List<String> aNameList)
    {
        final DetailAST leftNode = aBorNode.getFirstChild();
        final DetailAST rightNode = leftNode.getNextSibling();
        switch (leftNode.getType()) {
            case TokenTypes.IDENT:
                aNameList.add(leftNode.getText());
                break;
            case TokenTypes.DOT:
                aNameList.add(getExceptionNameWithPackage(leftNode));
                break;
            case TokenTypes.BOR:
                getExceptionNameFromMultiCatch(leftNode, aNameList);
                break;
            default:
                break;
        }
        switch (rightNode.getType()) {
            case TokenTypes.IDENT:
                aNameList.add(rightNode.getText());
                break;
            case TokenTypes.DOT:
                aNameList.add(getExceptionNameWithPackage(rightNode));
                break;
            default:
                break;
        }
    }

    /**
     * Method checks if one of comment line match suppress regexp.
     * @param aStartLine
     *        - Number of first line of the catch block.
     * @param aEndLine
     *        - Number of last line of the catch block.
     * @return boolean true if one of lines match suppress regexp and false
     *         otherwise.
     */
    private
            boolean
            isCommentContainsSuppressLine(final int aStartLine, final int aEndLine)
    {
        boolean result = false;
        final ImmutableMap<Integer, List<TextBlock>> cCommentMaps = getFileContents()
                .getCComments();
        final ImmutableMap<Integer, TextBlock> cppCommentsMap = getFileContents()
                .getCppComments();
        ImmutableSet<Integer> lineNumbers = cCommentMaps.keySet();
        for (Integer commentLineNumber : lineNumbers) {
            if (commentLineNumber >= aStartLine
                    && commentLineNumber <= aEndLine)
            {
                for (TextBlock block : cCommentMaps.get(commentLineNumber)) {
                    for (String commentLine : block.getText()) {
                        result |= isCommentMatchSuppressRegexp(commentLine);
                    }
                }
            }
        }
        lineNumbers = cppCommentsMap.keySet();
        for (Integer commentLineNumber : lineNumbers) {
            if (commentLineNumber >= aStartLine
                    && commentLineNumber <= aEndLine)
            {
                for (String commentLine : cppCommentsMap.get(commentLineNumber)
                        .getText())
                {
                    result |= isCommentMatchSuppressRegexp(commentLine);
                }
            }
        }
        return result;
    }

    /**
     * Method check if comment line match suppress regex pattern.
     * @param aCommentLine
     *        - String comment line to match.
     * @return true if exception class name match and false otherwise.
     */
    private boolean isCommentMatchSuppressRegexp(final String aCommentLine)
    {
        final Matcher regexpMatcher = commentSuppressRegexpPattern
                .matcher(aCommentLine);
        return regexpMatcher.matches();
    }

}
