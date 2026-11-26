# FormulaForge

An Expression Evaluation and Visualization System built for a small observatory where volunteers got different results for the same expression and the old calculator showed only the final answer.

FormulaForge produces an accurate result **and** an understandable record of how it was obtained:

1. Tokenizer: validates input and detects unary minus
2. Shunting-Yard: infix to postfix, with a step-by-step stack trace
3. Expression tree: built from postfix with a stack, then drawn
4. Postorder evaluation: every intermediate step is logged

## Files

- `ObservatoryEvaluator.java`: console version (Java, uses `java.util.Stack`, no eval)
- `index.html`: FormulaForge website (open it in any browser)

## Run the Java version

```
javac ObservatoryEvaluator.java
java ObservatoryEvaluator
```
