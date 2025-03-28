package org.example;


import managers.Console;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String fileName = System.getenv("LABWORKS_FILEPATH");
        if (fileName == null) {
            System.out.println("Something wrong with argument");
        } else {
            File file = new File(fileName);
            if (file.canRead() && file.canWrite()) {
                Console console = new Console();
                console.start(System.in);
            } else {
                System.out.println("You do not have enough root or file don't exists");
            }
        }
    }
}
