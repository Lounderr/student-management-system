package utility;

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

    public static boolean isAlphabetic(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }
}
