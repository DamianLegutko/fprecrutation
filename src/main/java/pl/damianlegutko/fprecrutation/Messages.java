package pl.damianlegutko.fprecrutation;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages/message_en");

    public static String get(String key) throws MissingResourceException {
        return resourceBundle.getString(key);
    }

    public static String get(String key, Object... params ) throws MissingResourceException {
        return MessageFormat.format(resourceBundle.getString(key), params);
    }
}