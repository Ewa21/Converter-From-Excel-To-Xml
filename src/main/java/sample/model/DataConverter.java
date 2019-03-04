package sample.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

public class DataConverter {

    public static final String SAMPLE_XLSX_FILE_PATH = "C:\\Users\\PC\\Desktop\\IT_development\\Java_all\\Java-Job\\report1\\src\\main\\java\\sample\\model\\data.xlsx";
    public void convert() throws IOException, InvalidFormatException {


        PrintWriter out = new PrintWriter("reportOutput.xml");

        Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));

        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            Tag tag = new Tag(dataFormatter.formatCellValue(row.getCell(0)), dataFormatter.formatCellValue(row.getCell(1)));
            out.print(tag.getString());
            System.out.print(tag.getString());

        }
        out.close();
    }
}
