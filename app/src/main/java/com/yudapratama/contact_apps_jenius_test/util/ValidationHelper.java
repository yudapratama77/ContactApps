package com.yudapratama.contact_apps_jenius_test.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yudapratama on 10/15/18.
 * yudaapratamaa77@gmail.com
 */

public class ValidationHelper {

    public static final String TAG = ValidationHelper.class.getName();

    //region Empty Field Validator

    /**
     * Checks if a {@link String} is empty.
     * Replaces all whitespaces, so if the text is "   " it will be treated as an empty String.
     * Only checks for visible {@link TextView}s
     * @param textView the {@link TextView} containing the {@link String} to check
     * @return true if empty, false if filled / not visible
     */
    public static boolean isEmpty(@NonNull TextView textView) {
        return textView.getVisibility() == View.VISIBLE && isEmpty(textView.getText().toString());
    }

    /**
     * Checks if a {@link String} is empty.
     * Replaces all whitespaces, so if the text is "   " it will be treated as an empty String.
     * @param message the string to check
     * @return true if empty
     */
    public static boolean isEmpty(@Nullable String message) {
        return message == null || message.length() == 0 || message.replaceAll(" ", "").length() == 0;
    }

    //endregion
    //region Password format validator

    /**
     * Determines if a password input is valid.
     * Only checks for visible {@link TextView}s
     * @param textView the {@link TextView} containing the {@link String} to check
     * @return true if password is valid / not visible, false if invalid
     */
    public static boolean isPasswordValid(@NonNull TextView textView) {
        return textView.getVisibility() != View.VISIBLE || isPasswordValid(textView.getText().toString());
    }

    /**
     * Determines if a password input is valid.
     * @param password the string to check
     * @return true if password is valid
     */
    public static boolean isPasswordValid(@Nullable String password) {
        if (password == null || password.isEmpty()) return false;

        // This pattern checks for:
        // \S{6,}           - No whitespaces, and minimum of 6 chars
        // TODO: 1/27/2017 Is there any specific password requirements?
        Pattern pattern = Pattern.compile("\\A\\S{6,}\\Z");
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    //endregion
    //region Email format validator

    /**
     * Determines if an email input is valid.
     * Only checks for visible {@link TextView}s
     * @param emailTextView the {@link TextView} containing the {@link String} to check
     * @return true if it's a valid email / not visible, false if invalid
     */
    public static boolean isEmailValid(@NonNull TextView emailTextView) {
        return emailTextView.getVisibility() != View.VISIBLE || isEmailValid(emailTextView.getText().toString());
    }

    /**
     * Determines if an email input is valid.
     * @param email the string to check
     * @return true if it's a valid email
     */
    public static boolean isEmailValid(@Nullable String email) {
        return !(email == null || email.isEmpty()) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
