public enum Token {
    FORWARD_ARROW("->"),
    BACK_ARROW("<-"),
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    SPACE(" "),
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
