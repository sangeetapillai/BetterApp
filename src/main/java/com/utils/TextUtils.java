package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.CollectionUtils;

public class TextUtils {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static String convertObjectToJsonString(Object obj) {
        String jsonString = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonString = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
        }
        return jsonString;
    }

    public static boolean validateEmail(String emailId) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(emailId);
        return matcher.matches();
    }

    public static List<String> convertToLowerCase(List<String> inputList) {
        List<String> outputList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(inputList)) {
            for (String str : inputList) {
                if (!org.springframework.util.StringUtils.isEmpty(str)) {
                    outputList.add(str.trim().toLowerCase());
                }
            }
        }
        return outputList;
    }

}
