package sample.model;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.Iterator;
import java.util.List;

public class DataConverter {
    private static DataConverter instance = new DataConverter();

    private DataConverter(){

    }

    public static DataConverter getInstance(){
        return instance;
    }

    public Boolean convert(String path, Integer amount, List<File> list,TextArea textArea) throws IOException, InvalidFormatException {

        System.out.println("conver start");
        File excelFile=new File(path);
        String fileName=excelFile.getName();
        String xmlName= fileName.substring(0,fileName.length()-4);
        System.out.println(xmlName);
        xmlName+="xml";
        System.out.println(xmlName);
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(xmlName), "UTF-8"));

        Workbook workbook = WorkbookFactory.create(excelFile);

        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Iterator<Row> rowIterator = sheet.rowIterator();
        rowIterator.next(); // first row is skipped, row with column names
        Boolean skipNext=false;
        while (rowIterator.hasNext()) {
            if(!skipNext) {
                Row row = rowIterator.next();
                Tag tag = new Tag(dataFormatter.formatCellValue(row.getCell(1)), dataFormatter.formatCellValue(row.getCell(2)), dataFormatter.formatCellValue(row.getCell(3)), dataFormatter.formatCellValue(row.getCell(4)), dataFormatter.formatCellValue(row.getCell(6)));
                String output = tag.getTag();
                if (output.equals(Tag.ERROR_NAME)) {
                    throw new InvalidFormatException("Nieprawidłowe dane");
                } else if (output.equals(Tag.SKIP_NEXT)) {
                    skipNext=true;
                }
                else if (output.equals(Tag.SKIP)){

                }
                else if(output.equals(Tag.FILE_NAME)){
                    FilePDF filePDF;
                    for(File f : list){
                        filePDF=new FilePDF(f);
                        out.write(filePDF.getTags());
                    }

                }
                else {
                    out.write(output);
                    textArea.appendText(output);
                    System.out.print(output);
                   // textUser.appendText(output);
                }
            }
            else{
                rowIterator.next();
                skipNext=false;
            }
        }
        checkValidation(xmlName);
        out.close();
        workbook.close();
        return true;
    }

    private void checkValidation(String file){

        File schemaFile = new File("C:\\Users\\PC\\Desktop\\IT_development\\Java_all\\Java-Job\\generatorSF\\src\\main\\java\\sample\\model\\scheme.xsd"); // etc.
        Source xmlFile = new StreamSource(new File(file));
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            System.out.println(xmlFile.getSystemId() + " is valid");
        } catch (SAXException e) {
            System.out.println(xmlFile.getSystemId() + " is NOT valid reason:" + e);
        } catch (IOException e) {}

    }
}
