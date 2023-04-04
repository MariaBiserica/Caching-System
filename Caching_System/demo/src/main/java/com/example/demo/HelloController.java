package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HelloController {
    private final int MEMORY_SIZE = 5;
    private String address;
    private List<CacheObject> objectsInMemory = new ArrayList<>();
    @FXML
    public Label insertMessage;
    @FXML
    public Label searchMessage;
    @FXML
    public TextField objectAddressField;
    @FXML
    public TextField searchField;
    @FXML
    public Button addButton;
    @FXML
    public Button searchButton;

    @FXML
    public void onObjectAddressFieldChanged(ActionEvent actionEvent) {
        address = objectAddressField.getText();
    }

    @FXML
    public void onAddButtonClicked(ActionEvent actionEvent) {
        String filePath = "D:\\Medii si Instrumente de Programare\\examen\\demo\\src\\main\\java\\CacheMemory.txt";
        Integer fileSize = -1;
        Boolean multipleInseritonsAtTheSameTime = false;

        try (Stream<String> fileStream = Files.lines(Paths.get(filePath))) {
            fileSize = (int) fileStream.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(fileSize < MEMORY_SIZE){
            LocalDateTime myDateObj = LocalDateTime.now();
            objectsInMemory.add(new CacheObject(address, myDateObj));
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = myDateObj.format(myFormatObj);


            String newLine = address + "," + formattedDate;

            try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))){
                writer.newLine();
                writer.append(newLine);
                insertMessage.setText("Object successfully added!");
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else{
            insertMessage.setText("Memory is full!");

            //stergem obiectul cel mai VECHI INTRODUS
            LocalDateTime oldestDate = LocalDateTime.MAX;
            CacheObject oldestObject = null;

            for (CacheObject obj: objectsInMemory) {
                Integer compareToResult = obj.getDate().compareTo(oldestDate);
                if(compareToResult < 0){
                    oldestDate = obj.getDate();
                    oldestObject = obj;
                }
                else if(compareToResult == 0){
                    multipleInseritonsAtTheSameTime = true;
                }
            }

            if(!multipleInseritonsAtTheSameTime){
                System.out.print("BEFORE" + '\n');
                for (CacheObject obj: objectsInMemory) {
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String formattedDate = obj.getDate().format(myFormatObj);
                    System.out.print("Object: " + obj.getIdentifier() + ", Introduction Date: " + formattedDate + '\n');
                }

                objectsInMemory.remove(oldestObject);
                objectsInMemory.add(new CacheObject(address, LocalDateTime.now()));

                System.out.print("AFTER" + '\n');
                for (CacheObject obj: objectsInMemory) {
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String formattedDate = obj.getDate().format(myFormatObj);
                    System.out.print("Object: " + obj.getIdentifier() + ", Introduction Date: " + formattedDate + '\n');
                }
            }
            else{
                //stergem obiectul cu CELE MAI PUTINE CAUTARI
                Integer leastSearches = Integer.MAX_VALUE;
                CacheObject leastSearchedObject = null;

                for (CacheObject obj: objectsInMemory) {
                    if(obj.getNumberOfSearches() < leastSearches){
                        leastSearches = obj.getNumberOfSearches();
                        leastSearchedObject = obj;
                    }
                }

                System.out.print("BEFORE" + '\n');
                for (CacheObject obj: objectsInMemory) {
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String formattedDate = obj.getDate().format(myFormatObj);
                    System.out.print("Object: " + obj.getIdentifier() + ", Introduction Date: " + formattedDate + '\n');
                }

                objectsInMemory.remove(leastSearchedObject);
                objectsInMemory.add(new CacheObject(address, LocalDateTime.now()));

                System.out.print("AFTER" + '\n');
                for (CacheObject obj: objectsInMemory) {
                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String formattedDate = obj.getDate().format(myFormatObj);
                    System.out.print("Object: " + obj.getIdentifier() + ", Introduction Date: " + formattedDate + '\n');
                }
            }
        }

    }

    @FXML
    public void onSearchButtonClicked(ActionEvent actionEvent) {
        String filePath = "D:\\Medii si Instrumente de Programare\\examen\\demo\\src\\main\\java\\CacheMemory.txt";
        String line;
        String objectIdentifier = searchField.getText();
        LocalDateTime date;
        Integer counter = 1;
        Boolean found = false;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] wordsOfLine = line.split(",");
                for(String word : wordsOfLine) {
                    if(counter == 2){
                        System.out.print(word + '\n');
                        counter = 1;
                        for (CacheObject obj: objectsInMemory) {
                            if(obj.getIdentifier().equals(objectIdentifier)){
                                obj.updateNumberOfSearches();
                            }
                        }
                    }
                    if (objectIdentifier.equals(word)){
                        found = true;
                        searchMessage.setText("Object is found!");
                        System.out.print("Object: " + word + ", Introduction Date: ");
                        counter = 2;
                    }
                }
            }
        } catch (FileNotFoundException f) {
            System.out.println(filePath + " does not exist");
            f.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!found){
            searchMessage.setText("Object is not found!");
        }
    }

}