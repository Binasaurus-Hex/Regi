import com.diogonunes.jcolor.Attribute;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;

public class Main {
    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(args[0])));
        System.out.println("---- input text ----");
        System.out.println(text+"\n");
        Token[] tokens = Token.values();
        List<Object> tokenStream = new ArrayList<>();
        tokenStream.add(text);
        for(Token token : tokens){
            List<Object> totalObjects = new ArrayList<>();
            for(Object o : tokenStream){
                if(o instanceof String){
                    List<Object> parsed = tokenize((String)o, token);
                    totalObjects.addAll(parsed);
                }
                else{
                    totalObjects.add(o);
                }
            }
            tokenStream = totalObjects;
        }
        tokenStream = parseStrings(tokenStream);
        tokenStream = parseIdentifiers(tokenStream);
        tokenStream = parseFloats(tokenStream);
        //tokenStream = removeWhiteSpace(tokenStream);
        Object[] function = new Object[]{Token.OPEN_BRACKET, "*", Token.CLOSE_BRACKET, Token.FORWARD_ARROW, Identifier.class, Token.OPEN_BRACE, "*", Token.CLOSE_BRACE};
        Object[] assignment = new Object[]{Identifier.class, Token.EQUALS, "*", Token.SEMI_COLON};
        Object[] statement = new Object[]{"*", Token.SEMI_COLON};
        System.out.println("---- tokens ----");
        print(tokenStream);
        System.out.println("");
        System.out.println("---- syntax highlighting ----");
        for(Object o : tokenStream){
            if(o instanceof Identifier){
                if(((Identifier) o).name.startsWith("\"")){
                    System.out.print(colorize(((Identifier) o).name, Attribute.GREEN_TEXT()));
                    continue;
                }
                if(((Identifier) o).name.matches("[0-9].+")){
                    System.out.print(colorize(((Identifier) o).name, Attribute.BLUE_TEXT()));
                    continue;
                }
                if(((Identifier) o).name.matches("[0-9]")){
                    System.out.print(colorize(((Identifier) o).name, Attribute.BLUE_TEXT()));
                    continue;
                }
                System.out.print(colorize(o.toString(), Attribute.TEXT_COLOR(176, 237, 255)));
                continue;
            }
            if(o instanceof Token){
                if(o == Token.EQUALS){
                    System.out.print(colorize(((Token) o).token, Attribute.CYAN_TEXT()));
                    continue;
                }
                if(o == Token.SEMI_COLON){
                    System.out.print(colorize(((Token) o).token, Attribute.TEXT_COLOR(255, 123, 0)));
                    continue;
                }
                if(o == Token.OPEN_BRACE || o == Token.CLOSE_BRACE){
                    System.out.print(colorize(((Token) o).token, Attribute.CYAN_TEXT()));
                    continue;
                }
                if(o == Token.BACK_ARROW || o == Token.FORWARD_ARROW){
                    System.out.print(colorize(((Token) o).token, Attribute.CYAN_TEXT()));
                    continue;
                }
                System.out.print(((Token) o).token);
                continue;
            }
            System.out.print(o);
        }
    }

    public static List<Object> parseFloats(List<Object> tokenStream){
        List<Object> parsed = new ArrayList<>();
        for(int i = 0; i < tokenStream.size(); i++){
            Object token = tokenStream.get(i);
            if(token == Token.DOT){
                if(i == 0 || i == tokenStream.size() -1){
                    parsed.add(token);
                    continue;
                }
                Object previousToken = tokenStream.get(i - 1);
                Object nextToken = tokenStream.get(i + 1);
                if(!(previousToken instanceof Identifier) || !(nextToken instanceof Identifier)){
                    parsed.add(token);
                    continue;
                }
                Identifier previousIdentifier = (Identifier) previousToken;
                Identifier nextIdentifier = (Identifier) nextToken;
                String numberMatch = "[0-9]+";
                if(!previousIdentifier.name.matches(numberMatch) || !nextIdentifier.name.matches(numberMatch)){
                    parsed.add(token);
                    continue;
                }
                parsed.remove(parsed.indexOf(previousIdentifier));
                Identifier floatIdentifier = new Identifier();
                floatIdentifier.name = previousIdentifier.name + "." + nextIdentifier.name;
                parsed.add(floatIdentifier);
                i++;
                continue;
            }
            parsed.add(token);
        }
        return parsed;
    }

    public static List<Object> parseStrings(List<Object> tokenStream){
        List<Object> parsed = new ArrayList<>();
        boolean combining = false;
        StringBuilder combined = new StringBuilder();
        for(int i = 0; i < tokenStream.size(); i++){
            Object token = tokenStream.get(i);
            if(token == Token.QUOTE && !combining){
                combined.append(((Token) token).token);
                combining = true;
                continue;
            }
            if(token == Token.QUOTE){
                combined.append(((Token) token).token);
                parsed.add(combined.toString());
                combined = new StringBuilder();
                combining = false;
                continue;
            }
            if(combining){
                if(token instanceof Token){
                    combined.append(((Token) token).token);
                }
                else{
                    combined.append(token);
                }
                continue;
            }
            parsed.add(token);
        }
        return parsed;
    }

    public static List<Object> parseIdentifiers(List<Object> tokenStream){
        List<Object> parsed = new ArrayList<>();
        for(Object o : tokenStream){
            if(o instanceof String){
                Identifier i = new Identifier();
                i.name = (String)o;
                parsed.add(i);
            }
            else{
                parsed.add(o);
            }
        }
        return parsed;
    }

    public static List<Object> removeWhiteSpace(List<Object> tokenStream){
        List<Object> parsed = new ArrayList<>();
        for(Object o : tokenStream){
            if(o != Token.SPACE && o != Token.NEWLINE && o != Token.CARRIAGE){
                parsed.add(o);
            }
        }
        return parsed;
    }

    public static List<Object> tokenize(String text, Token searchToken){
        List<Object> objects = new ArrayList<>();
        if(text.equals("")){
            return objects;
        }
        int tokenIndex = text.indexOf(searchToken.token);
        if(tokenIndex == -1){
            objects.add(text);
            return objects;
        }
        String leftSide = text.substring(0, tokenIndex);
        if(!leftSide.equals("")){
            objects.add(leftSide);
        }
        objects.add(searchToken);
        String rightSide = text.substring(tokenIndex + searchToken.token.length());
        if(rightSide.equals("")){
            return objects;
        }
        objects.addAll(tokenize(rightSide, searchToken));
        return objects;
    }

    public static <T> void print(List<T> list){
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(int i = 0; i < list.size(); i++){
            T item = list.get(i);
            builder.append(item);
            if(i < list.size() - 1){
                builder.append(",");
            }
        }
        builder.append("]");
        System.out.println(builder);
    }
}
