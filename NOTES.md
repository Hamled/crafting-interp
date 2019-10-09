# Notes on jlox implementation

## Resolving and Binding
We need to have a way to track each variable reference to a particular binding. This binding needs to be resolved when the function declaration is being evaluated. When the function is actually being executed, variable references need to use the binding to obtain a value, rather than the existing environment-chain lookup process.

This means that all AST nodes with a direct reference to a variable (Expr.Variable, Expr.Assign) need to have an additional field to hold the binding. I think what we'll likely need to do is create another Visitor-pattern implementation, which can be used to visit all of the statements in a function/lambda body and, for actual variable references, do a lookup through the environment-chain which returns the first/innermost environment that contains the variable name referenced. This would be the binding's representation, and it should be sufficient because Lox does not support removal of variables from an environment.

Still, it feels like putting runtime-derived data on the structure which is supposed to be a plain-old-data bridge between static parser and runtime interpreter. But, it's also effectively static data which we happen to be able to determine at runtime because function declarations are evaluated at runtime in Lox.

Maybe it could be implemented with a separate pass through the AST prior to the interpreter's execution. I'm not sure how the binding would be represented in that case, We wouldn't have any environments yet, so we'd either have to create some other structure to represent bindings, and I guess match those up somehow in the interpreter phase? If the binding was from one AST node to another AST node it might work, but I think we'd have to do something messy like referencing the environment on the AST nodes for variable declarations.

### Result
We did implement this as a second pass, but instead of representing the binding as a reference to another AST node, we stored the number environments to walk "up" in the runtime environment chain. This information was then stored on the Interpreter as a map from Expr -> Integer.
