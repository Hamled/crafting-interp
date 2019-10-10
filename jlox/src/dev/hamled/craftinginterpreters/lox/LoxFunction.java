package dev.hamled.craftinginterpreters.lox;

import java.util.List;

class LoxFunction implements LoxCallable {
    private final Stmt.Function declaration;
    private final Environment closure;
    private final Boolean isInitializer;

    LoxFunction(Stmt.Function declaration, Environment closure, Boolean isInitializer) {
        this.isInitializer = isInitializer;
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

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
            if(isInitializer) return closure.getAt(0, thisToken());

            return returnValue.value;
        }

        if(isInitializer) return closure.getAt(0, thisToken());
        return null;
    }

    public LoxFunction bind(LoxInstance instance) {
        Environment environment = new Environment(closure);
        environment.define(thisToken(), instance);
        return new LoxFunction(declaration, environment, isInitializer);
    }

    private Token thisToken() {
        return new Token(TokenType.THIS, "this", null, -1);
    }
}
