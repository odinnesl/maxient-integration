package edu.nesl.maxient;

public class EmailUtils {
    public static String truncateEmail(String email) {
        if (email == null) {
            return null;  // You might also choose to throw an exception here.
        }
        int atIndex = email.indexOf("@");
        if (atIndex == -1) {
            // Return the original string if there's no "@" symbol
            return email;
        }
        return email.substring(0, atIndex);
    }

    // Example usage
    public static void main(String[] args) {
        String email = "odin@xyz.com";
        String truncated = truncateEmail(email);
        System.out.println("Truncated email: " + truncated);  // Output: "odin"
    }
}
