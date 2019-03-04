package sample;
import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import sample.model.DataConverter;
import sample.model.FilePDF;

import java.io.IOException;

public class Controller {

    private DataConverter dataConverter;

public void getFiles(){
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select PDF files");


}
    public void convert( ){
        dataConverter = new DataConverter();
        try {
            dataConverter.convert();
        }
        catch(IOException e1){
            System.out.println("Conversion failed: "+ e1.getMessage());
        }
        catch(InvalidFormatException e2){
            System.out.println("Conversion failed: "+ e2.getMessage());
        }

    }

    public void getBase64(){
        FilePDF file = new FilePDF("");
        file.getBase64();
    }
}
