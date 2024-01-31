package tools;

public class StringOperations {
    public static boolean isNumeric(String s) {
        try {
            // Attempt to parse the string to a number
            double number = Double.parseDouble(s);

            // If successful, it's a number
            return true;
        } catch (NumberFormatException e) {
            // If an exception is caught, it's not a number
            return false;
        }
    }
}
