package org.mystudying.app;

import java.util.regex.Pattern;

public class ContactFactory {
    private static final String PHONE_REGEXP = "^(0\\d{8,9})|(\\+32[^0]\\d{8})$";
    private static final String EMAIL_REGEXP = "[-.\\w]+@([\\w-]+\\.)+[\\w-]+";

    public static Contact createContact(String name, String phone, String email) {
        if (!Pattern.matches(PHONE_REGEXP, phone)) {
            throw new IllegalArgumentException("Wrong format of phone number!");
        }

        if (!Pattern.matches(EMAIL_REGEXP, email)) {
            throw new IllegalArgumentException("Wrong format of email!");
        }

        return new Contact(name, phone, email);
    }


}
