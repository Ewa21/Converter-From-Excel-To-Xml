package sample.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class FilePDF {

    private String path;

    public FilePDF(String path) {
        this.path = path;
    }


    public void  getBase64(){

        String filePath = "C:\\Users\\PC\\Desktop\\";
        String originalFileName = "bilet.pdf";
        String newFileName = "test.txt";

        try {
            byte[] input_file = Files.readAllBytes(Paths.get(filePath + originalFileName));
            byte[] encodedBytes = Base64.getEncoder().encode(input_file);
            String encodedString =  new String(encodedBytes);
            System.out.println(encodedString);
            FileOutputStream fos = new FileOutputStream(filePath+newFileName);
            fos.write(encodedBytes);
            fos.flush();
            fos.close();
        }
        catch(IOException e){
            System.out.println("Readind file failed: " + e.getMessage());
        }



    }
}
