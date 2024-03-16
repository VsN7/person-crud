package elotech.personcrud.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonUtil {

    public static String extractNumbers(String input) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            result.append(matcher.group());
        }
        return result.toString();
    }

}
