package aud.util;

import java.util.regex.*;

/** Base class for a simple <a
 * href="http://en.wikipedia.org/wiki/Lexical_analysis">lexical
 * scanner</a>.<p>
 *
 * A lexical scanner splits input into <em>tokens</em>. Here, token
 * types are referenced by integer constants given in a table of
 * rules. Each token is characterized by a <a
 * href="http://en.wikipedia.org/wiki/Regular_expression">regular
 * expression</a>.<p>
 *
 * Note: {@code java.util.Scanner} is often a better choice. However,
 * this requires definition of a <em>delimiter</em>, which is
 * problematic in this context.
 */
public class LexicalScanner {

  /** no more input */
  public final static int END_OF_INPUT = -1;
  /** no match (usually implies a syntax error) */
  public final static int NO_MATCH     = -2;

  /** a rule for lexical scanner
      @see LexicalScanner
  */
  public static class Rule {
     /** create rule
         @param id user defined identifier as returned by
         {@link LexicalScanner#next()}, must be a <b>positive</b> number
         @param pattern defines the rule a regular expression
       */
    public Rule(int id, Pattern pattern) {
      id_=id;
      pattern_=pattern;
    }

    /** create rule ({@code pattern} is compiled) */
    public Rule(int id, String pattern) {
      this(id,Pattern.compile(pattern));
    }

    int id_;
    Pattern pattern_;
  }

  String input_ = null;
  String text_  = null;
  int    id_    = NO_MATCH;
  Rule[] rules_ = null;

  /** white space */
  public static final Pattern P_WHITESPACE = Pattern.compile("\\s+");
  /** identifiers */
  public static final Pattern P_IDENTIFIER =
    Pattern.compile("[_a-zA-Z]?(\\w|_)+");
  /** floating point number */
  public static final Pattern P_FLOAT =
    Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");

  /** create new scanner processing {@code input}
      @param rules table of rules used by {@link #next},
      rules will be applied sequentially in the given order
      @param input text to be analyzed
   */
  public LexicalScanner(Rule[] rules,String input) {
    rules_=rules;
    input_=input;
  }

  /** ignore white space (called by {@link #match} */
  protected void eatWhiteSpace() {
    if (!endOfInput()) {
      Matcher m=P_WHITESPACE.matcher(input_);
      if (m.lookingAt()) {
        input_=input_.substring(m.end(),input_.length());
      }
    }
  }

  /** set input (resets scanner state) */
  public void setInput(String input) {
    input_=input;
  }
  /** get text of last {@link #match} or call to {@link #next} */
  public String matchedText() { return text_; }
  /** get result of last call to {@link #next()} */
  public int matchedTokenId() { return id_; }
  /** get remaining text */
  public String remainder() { return input_; }
  /** reached end of input? */
  public boolean endOfInput() {
    return input_==null || input_.length()==0;
  }

  /** Match {@link #remainder} against pattern {@code p}.<p>
      {@code match} skips any preceding white space.
      @param p regular expression pattern
      @return {@code true} if there was a match
   */
  protected boolean match(Pattern p) {
    text_=null;
    eatWhiteSpace();

    if (endOfInput())
      return false;

   Matcher m=p.matcher(input_);
    if (!m.lookingAt())
      return false;

    int n=m.end();
    text_=input_.substring(0,n);
    input_=input_.substring(m.end(),input_.length());

    return true;
  }
  /** match {@link #remainder} to table of {@code rules}
      @return rule id or {@code NO_MATCH} or {@code END_OF_INPUT}
   */
  protected int next(Rule[] rules) {
    eatWhiteSpace();

    if (endOfInput())  return id_=END_OF_INPUT;
    if (rules_==null)  return id_=NO_MATCH;

    for (Rule rule : rules) {
      if (match(rule.pattern_))
        return id_=rule.id_;
    }
    return id_=NO_MATCH;
  }

  /** match {@link #remainder} to rules provided to constructor
      @return rule id or {@code NO_MATCH} or {@code END_OF_INPUT}
   */
  public int next() { return next(rules_);  }


  /** testing and example for usage */
  public static void main(String[] args) {

    Rule[] rules={
      new Rule(1,"[0-9]*\\.?[0-9]+"),
      new Rule(2,"[a-z]+")
    };

    LexicalScanner s=new LexicalScanner
      (rules,args.length==0 ? "  12.3a 12 bcd 34 " : args[0]);

    System.out.println("input = '"+s.remainder()+"'");

    while (s.next()!=END_OF_INPUT) {
      if (s.matchedTokenId()==NO_MATCH) {
        System.out.println("syntax error near '"+s.remainder()+"'");
        break;
      }
      System.out.println("next token id = "+s.matchedTokenId());
      System.out.println("matched text = '"+s.matchedText()+"'");
      System.out.println("remaining input = '"+s.remainder()+"'");
    }
  }
}
