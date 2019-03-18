package sample;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import sample.model.DataConverter;


import java.io.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {

    private String excelPath="";
    private  Integer pdfAmount=0;
    private  List<File> pdfFiles= new ArrayList<>();
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button confirmButton;
    @FXML
    private Button generateButton;
    @FXML
    private Button getPDFButton;
    @FXML
    private Label browserExcel;
    @FXML
    private Label browserPDF;
    @FXML
    private Label pdfAmountLabel;
    @FXML
    private Label notification;
    private final ObservableList<String> options =
            FXCollections.observableArrayList(
                    "1",
                    "2",
                    "3",
                    "4",
                    "5"
            );
    @FXML
    private ComboBox<String> comboBox=new ComboBox(options);



    public void setComboBox(){

             comboBox.setItems(options);
             comboBox.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldValue, String newValue) {
                        System.out.println("Value is: "+newValue);
                        pdfAmount=Integer.parseInt(newValue);
                        confirmButton.setDisable(false);

                    }
                });
    }

    @FXML
    public void test( ActionEvent event ) {

        String output = comboBox.getSelectionModel().getSelectedItem().toString();
        System.out.println(output);


    }
    @FXML
    public void confirmNumberOfPDFs() {


        if (!pdfAmount.equals(0)) {

            pdfAmountLabel.setText("Proszę załadować " + pdfAmount + " pliki pdf.");
            getPDFButton.setDisable(false);

        } else {
            notification.setText("Wybierz liczbę plików pdf.");
        }
    }

    @FXML
    public void getPdfFiles() {

        List<File> selectedFiles;
        String text="";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select PDF files");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                selectedFiles = fileChooser.showOpenMultipleDialog(null);


                if(selectedFiles.size()>(pdfAmount-pdfFiles.size())){
                    text+="Załadowano za dużo plików.\n";
                    text+="Oczekiwana liczba plików: "+pdfAmount+"\n";
                    generateButton.setDisable(true);
                }
                else if (selectedFiles.size()<(pdfAmount-pdfFiles.size())){
                    text+="Załadowano za mało plików.\n";
                    text+="Oczekiwana liczba plików: "+pdfAmount+"\n";
                    text+="Załądowano liczba plików: "+(pdfFiles.size()+selectedFiles.size())+"\n";
                }
                else{
                    generateButton.setDisable(false);
                }
            Integer  amountToDisplay=((pdfFiles.size()+selectedFiles.size())-pdfAmount)>=0 ? pdfAmount:selectedFiles.size()+pdfFiles.size();

        if (selectedFiles != null) {
            text+= "Załadowano " + amountToDisplay + " plików PDF \n";

            for(File f : pdfFiles){
                text+=f.getName() + "\n";
            }

            for(File f : selectedFiles){
                if(pdfFiles.size()<pdfAmount) {
                    text += f.getName() + "\n";
                    pdfFiles.add(f);
                }
            }
            browserPDF.setText(text);


        }
        else {
            browserPDF.setText("PDF file selection cancelled.");
        }

        if(pdfFiles.size()==pdfAmount){
            generateButton.setDisable(false);
        }

    }

    @FXML
    public void getExcelFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
           excelPath=selectedFile.getAbsolutePath();
            browserExcel.setText("Wybrano plik: " + selectedFile.getName());

        } else {
            browserExcel.setText("Bląd podczas wybierania pliku.");
        }
    }
    @FXML
    public void convert() {

        Stage stage = new Stage();
        VBox box = new VBox();
        TextArea textArea = new TextArea();
        textArea.setMinSize(600,400);
        box.getChildren().add(textArea);
        Scene scene = new Scene(box, 600, 400);
        stage.setScene(scene);


        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    return DataConverter.getInstance().convert(excelPath, pdfAmount,pdfFiles,textArea);

                }
                catch(InvalidFormatException e){
                    System.out.println("Błąd "+ e.getMessage());
                    return null;
                }
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        progressBar.setVisible(true);
        task.setOnSucceeded(e -> {progressBar.setVisible(false);stage.show();});

        task.setOnFailed(e -> progressBar.setVisible(false));


        new Thread(task).start();
    }



}

