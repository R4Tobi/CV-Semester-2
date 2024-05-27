import aud.example.expr.*; // To extend ExpressionParser2
import aud.util.*;

public class ExprWithPow extends ExpressionParser2 {

    @Override
    protected ExpressionTree factor(int level) {
        verbose(level, "<factor>");

        ExpressionTree base = super.factor(level + 1);

        // Check for power operator
        while (lookahead() == Tokenizer.POWER) {
            nextToken(); // consume the power operator token
            ExpressionTree exponent = factor(level + 1);
            base = new ExpressionTree(new Operator('^'), base, exponent);
        }

        return base;
    }

    public static void main(String[] args) {
        String input = (args.length > 0) ? args[0] : "3^2+5";

        System.out.println("input = '" + input + "'");

        ExprWithPow p = new ExprWithPow();
        p.setVerbose(true);
        ExpressionTree tree = p.parse(input);

        System.out.println("output = '" + tree + "'");
        System.out.println(tree.postorder().toString());
        System.out.println(tree.toTikZ());
    }
}
