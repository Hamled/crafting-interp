package dev.hamled.craftinginterpreters.lox;

import dev.hamled.craftinginterpreters.lox.Expr;

class Interpreter implements Expr.Visitor<Object> {
    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitSequenceExpr(Expr.Sequence expr) {
        if(expr.expressions.size() == 0) {
            throw new RuntimeException("Sequence expression without any subexpressions.");
        }

        Object result = null; // Should be okay to initialize as null, because we have at least one assignment
        for(Expr e : expr.expressions) {
            result = evaluate(e);
        }

        return result;
    }
}
