public enum Token {
    FORWARD_ARROW("->"),
    BACK_ARROW("<-"),
    LESS_THAN_EQUAL("<="),
    GREATER_THAN_EQUAL(">="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    SPACE(" "),
    OPEN_CROTCHET("["),
    CLOSE_CROTCHET("]"),
    OPEN_BRACKET("("),
    CLOSE_BRACKET(")"),
    OPEN_BRACE("{"),
    CLOSE_BRACE("}"),
    SEMI_COLON(";"),
    COMMA(","),
    DOT("."),
    EQUALS("="),
    NEWLINE("\n"),
    CARRIAGE("\r"),
    QUOTE("\"");

    public final String token;
    Token(String token){
        this.token = token;
    }

}
