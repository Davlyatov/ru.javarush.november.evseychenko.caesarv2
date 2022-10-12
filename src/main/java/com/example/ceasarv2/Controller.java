package com.example.ceasarv2;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Controller extends Window {

    public static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя.,\":-!? ";

    @FXML
    private CheckBox bruteForceCB;

    @FXML
    private TextField keyTextField;

    @FXML
    private Label choosenFileLbl;

    @FXML
    private Button startBtn;

    @FXML
    private RadioButton decryptRadioBtn;

    @FXML
    private RadioButton encryptRadioBtn;

    @FXML
    public TextArea textFromFileTA;

    File file;

    public LinkedList<String> fileContents = new LinkedList<>();

    @FXML
    public void chooseFile() throws IOException { //Открытие окна выбора файла и наполнение текстом
        textFromFileTA.setText("");
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Текстовые файлы (.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setTitle("Выберите файл");
        file = fileChooser.showOpenDialog(this);
        choosenFileLbl.setText("Выбран файл: " + file.getName());
        choosenFileLbl.setVisible(true);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            fileContents.add(str);
            textFromFileTA.appendText(fileContents.getLast() + "\n");
        }
        startBtn.setDisable(false);
    }

    @FXML
    public void checkDecryptRB() { //Нажатие на кнопку Дешифрации
        if (decryptRadioBtn.isSelected()) {
            encryptRadioBtn.setSelected(false);
            bruteForceCB.setDisable(false);
        }
    }

    @FXML
    public void checkEncryptRB() { //Нажатие на кнопку шифрации
        if (encryptRadioBtn.isSelected()) {
            decryptRadioBtn.setSelected(false);
            bruteForceCB.setDisable(true);
            keyTextField.setEditable(true);
        }
    }

    @FXML
    public void checkBruteForceCB() { //Нажатие на чекбокс брутфорса
        if (bruteForceCB.isSelected()) {
            keyTextField.setEditable(false);
        } else {
            keyTextField.setEditable(true);
        }
    }

    @FXML
    public void start() throws IOException { //Нажали на кнопку старт
        String done;
        int key;
        EncryptDecrypt encryptDecrypt = new EncryptDecrypt();
        if (encryptRadioBtn.isSelected()) { //Если нажата шифрация, то делаем шифрацию
            if (keyTextField.getText() != null && !keyTextField.getText().equals("")) {
                key = Integer.parseInt(keyTextField.getText());
                if (key >= 1 && key <= ALPHABET.length()) {
                    done = encryptDecrypt.checkWorkMethod(fileContents, key, file, ALPHABET);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("");
                    alert.setHeaderText("Готово");
                    alert.setContentText("Файл зашифрован! Его путь: " + done);
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("");
                alert.setHeaderText("Неверный сдвиг");
                alert.setContentText("Введите сдвиг от 1 до " + (ALPHABET.length() + 1) + "!");
                alert.show();
            }
        } else if (decryptRadioBtn.isSelected()) { //Если нажата дешифрация, отправляем на дешифрацию с последующей проверкой на брутфорс
            if (keyTextField.getText() != null && !keyTextField.getText().equals("") || bruteForceCB.isSelected()) {
                if (bruteForceCB.isSelected()) {
                    key = 0;
                    done = encryptDecrypt.checkWorkMethod(fileContents, key, file, ALPHABET);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("");
                    alert.setHeaderText("Готово");
                    alert.setContentText("Файл зашифрован! Его путь: " + done);
                    alert.show();
                } else {
                    key = Integer.parseInt(keyTextField.getText());
                    if (key >= 1 && key <= ALPHABET.length()) {
                        done = encryptDecrypt.checkWorkMethod(fileContents, -key, file, ALPHABET);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("");
                        alert.setHeaderText("Готово");
                        alert.setContentText("Файл зашифрован! Его путь: " + done);
                        alert.show();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("");
                alert.setHeaderText("Неверный сдвиг");
                alert.setContentText("Введите сдвиг от 1 до " + (ALPHABET.length() + 1) + " или выберите BruteForce!");
                alert.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("Не выбран метод работы");
            alert.setContentText("Выберите шифратор или дешифратор!");
            alert.show();
        }
    }
}