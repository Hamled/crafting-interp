package dev.hamled.craftinginterpreters.lox;

import java.util.List;

public class LoxLambda implements LoxCallable {
    private final Expr.Lambda declaration;
    private final Environment closure;

    LoxLambda(Expr.Lambda declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public String toString() {
        return "<fn lambda>";
    }

    @Override
    public int arity() { return declaration.params.size(); }


    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        // Setup a new environment from passed argument values
        Environment environment = new Environment(closure);
        for(int i = 0; i < declaration.params.size(); i++) {
            environment.define(declaration.params.get(i), arguments.get(i));
        }

        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch(Return returnValue) {
            return returnValue.value;
        }
        return null;
    }
}
