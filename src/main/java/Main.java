import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException {
        //Getting the sheet
        XSSFSheet sheet = Reader.openXlsx();
        //Getting the data
        Reader.readDataToObjects(sheet);
    }
}
