/*
 *  Copyright (c) 2014, Lukas Tenbrink.
 *  * http://ivorius.net
 */

package ivorius.reccomplex.utils.algebra;

/**
 * Created by lukas on 05.10.16.
 */
public class FunctionExpressionCaches
{
    public static <T> FunctionExpressionCache.VariableType<T, Object, Object> unknown(T val)
    {
        return new FunctionExpressionCache.VariableType<T, Object, Object>("", "")
        {
            @Override
            public T evaluate(String var, Object o)
            {
                return val;
            }

            @Override
            public FunctionExpressionCache.Validity validity(String var, Object o)
            {
                return FunctionExpressionCache.Validity.KNOWN;
            }
        };
    }

    public static <T> FunctionExpressionCache.VariableType<T, Object, Object> constant(String id, T val)
    {
        return new FunctionExpressionCache.VariableType<T, Object, Object>(id, "")
        {
            @Override
            public T evaluate(String var, Object o)
            {
                return val;
            }

            @Override
            public FunctionExpressionCache.Validity validity(String var, Object o)
            {
                return var.equals(prefix) ? FunctionExpressionCache.Validity.KNOWN : FunctionExpressionCache.Validity.ERROR;
            }
        };
    }
}