package tools;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Dialog {
    public static void showError(Component owner, Exception ex) {

        StringBuilder sb = new StringBuilder();
        sb.append("\n\n~~Exception occured~~\n");
        sb.append(ex.getMessage());
        sb.append("\n");
        Arrays.stream(ex.getStackTrace()).forEach((t) -> sb.append(t).append("\n"));
        sb.append("~~~~~~~~~~~~~~~~~~\n\n");
        System.out.println(sb);


        JOptionPane.showMessageDialog(owner, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

    }

    public static void show(Component owner, String message) {
        JOptionPane.showMessageDialog(owner, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}
