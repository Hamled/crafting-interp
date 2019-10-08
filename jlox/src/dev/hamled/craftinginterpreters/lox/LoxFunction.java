package dev.hamled.craftinginterpreters.lox;

import java.util.List;

class LoxFunction extends LoxLambda {
    private final Stmt.Function declaration;

    LoxFunction(Stmt.Function declaration, Environment closure) {
        super(declaration.lambda, closure);
        this.declaration = declaration;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }
}
