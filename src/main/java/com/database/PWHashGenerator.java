package com.database;

import org.mindrot.jbcrypt.BCrypt;

public class PWHashGenerator {

    public static void main(String[] args) {
        System.out.println(
                BCrypt.hashpw("admin123", BCrypt.gensalt(12))
        );

        System.out.println(
                BCrypt.hashpw("alice123", BCrypt.gensalt(12))
        );
    }
}
