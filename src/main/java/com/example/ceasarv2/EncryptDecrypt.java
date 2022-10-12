package com.example.ceasarv2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;

public class EncryptDecrypt {

    public String checkWorkMethod(LinkedList<String> fileContents, int key, File file, String alphabet) throws IOException { //
        LinkedList<String> endList;
        if (key == 0) {
            endList = bruteForce(fileContents, alphabet);
        } else {
            endList = encryptionDecryption(fileContents, key, alphabet);
        }
        return writeToFile(endList, file);
    }

    private String writeToFile(LinkedList<String> endList, File file) throws IOException { //Создание файла и запись в него
        Path path = Path.of(file.toURI());
        String[] tempArray = path.toString().split(".txt");
        System.out.println(tempArray[0]);
        path = Path.of(tempArray[0] + "worked.txt");
        Files.createFile(path);
        Files.write(path, endList);
        return tempArray[0] + "worked.txt";
    }

    private LinkedList<String> bruteForce(LinkedList<String> fileContent, String alphabet) {
        LinkedList<String> checkList = fileContent;
        boolean rightKey;
        for (int i = 0; i < alphabet.length(); i++) {
            checkList = encryptionDecryption(checkList, -1, alphabet);
            rightKey = checkEncrypt(checkList);
            if (rightKey) {
                System.out.println(i+1);
                break;
            }
        }
        return checkList;
    }

    private boolean checkEncrypt(LinkedList<String> checkList) {
        for (String s : checkList) {
            for (int j = 0; j < s.length(); j++) {
                if (Character.toString(s.charAt(j)).equals(".") && s.length() >= j + 2) { // Проверка на совпадение с точкой и возможностью проверить еще 2 символа
                    return Character.toString(s.charAt(j + 1)).equals(" ") && Character.isUpperCase(s.charAt(j + 2)); // Возврат совпадения точки, пробела и символа в верхнем регистре
                }
            }
        }
        return false;
    }

    private LinkedList<String> encryptionDecryption(LinkedList<String> fileContents, int key, String alphabet) {
        LinkedList<String> linkedList = new LinkedList<>();
        while (fileContents.size() > 0) {
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < fileContents.getFirst().length(); i++) {
                int charPosition = alphabet.indexOf(fileContents.getFirst().charAt(i));
                if (charPosition != -1) {
                    int keyValue = (key + charPosition) % alphabet.length();
                    if (keyValue < 0) {
                        keyValue = alphabet.length() + keyValue;
                    }
                    char replaceValue = alphabet.charAt(keyValue);
                    buffer.append(replaceValue);
                } else {
                    buffer.append(fileContents.getFirst().charAt(i));
                }
            }
            fileContents.removeFirst();
            linkedList.add(String.valueOf(buffer));
        }
        return linkedList;
    }
}
