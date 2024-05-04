package aud.example.expr;

import aud.util.LexicalScanner;

import java.util.regex.Pattern;

/** Breaks input string into pieces ("tokens").<p>
 */
public class Tokenizer extends LexicalScanner {

  protected static final Pattern P_PLUS  = Pattern.compile("\\+");
  protected static final Pattern P_MINUS = Pattern.compile("-");
  protected static final Pattern P_TIMES = Pattern.compile("\\*");
  protected static final Pattern P_DIVIDE = Pattern.compile("/");
  protected static final Pattern P_POWER = Pattern.compile("(\\*\\*)|\\^");
  protected static final Pattern P_LEFTPAREN = Pattern.compile("\\(");
  protected static final Pattern P_RIGHTPAREN = Pattern.compile("\\)");

  public static final int PLUS='+';
  public static final int MINUS='-';
  public static final int TIMES='*';
  public static final int DIVIDE='/';
  public static final int POWER='^';
  public static final int LEFT_PAREN='(';
  public static final int RIGHT_PAREN=')';

  public static final int IDENTIFIER=0x100;
  public static final int NUMBER=0x101;

  protected static final LexicalScanner.Rule[] RULES =
  {
    new LexicalScanner.Rule(PLUS,  P_PLUS),
    new LexicalScanner.Rule(MINUS, P_MINUS),
    new LexicalScanner.Rule(POWER, P_POWER), // test '**' before '*' !
    new LexicalScanner.Rule(TIMES, P_TIMES),
    new LexicalScanner.Rule(DIVIDE,P_DIVIDE),
    new LexicalScanner.Rule(LEFT_PAREN,P_LEFTPAREN),
    new LexicalScanner.Rule(RIGHT_PAREN,P_RIGHTPAREN),
    new LexicalScanner.Rule(NUMBER,LexicalScanner.P_FLOAT),
    new LexicalScanner.Rule(IDENTIFIER,LexicalScanner.P_IDENTIFIER)
  };

  /** create new tokenizer for {@code input} */
  public Tokenizer(String input) {
    super(RULES,input);
  }

  /** testing and example for usage */
  public static void main(String[] args) {

    Tokenizer t=new Tokenizer
      (args.length==0 ? "2*(3+4)/x-3.14+2^3-2**3" : args[0]);

    System.out.println("input = '"+t.remainder()+"'");

    while (t.next()!=END_OF_INPUT) {
      if (t.matchedTokenId()==NO_MATCH) {
        System.out.println("syntax error near '"+t.remainder()+"'");
        break;
      }
      else if (t.matchedTokenId()==NUMBER) {
        double d=Double.parseDouble(t.matchedText());
        System.out.println("read a number: "+d);
      }
      System.out.println("next token id = "+t.matchedTokenId());
      System.out.println("matched text = '"+t.matchedText()+"'");
      System.out.println("remaining input = '"+t.remainder()+"'");
    }
  }
}
