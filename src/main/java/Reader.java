import classes.DataRow;
import classes.IssueType;
import classes.StatusTime;
import com.opencsv.CSVWriter;
import org.apache.poi.ss.formula.functions.Today;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Reader {
    static String filePath = "C:\\Users\\btorok\\IdeaProjects\\ProjectStatistics\\src\\main\\resources\\statistics.csv";
    public static XSSFSheet openXlsx() throws IOException {
        FileInputStream fis=new FileInputStream(new File("C:\\Users\\btorok\\IdeaProjects\\ProjectStatistics\\src\\main\\resources\\e-Delphyn LIS 21.1.0 US Statistics from Jira.xlsx"));
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);

        return sheet;
    }
    public static void readDataToObjects(XSSFSheet sheet) {
        List<DataRow> dataRowList = new ArrayList<>();
        //get the data to the DataRow class
        for (Row myRow : sheet) {
            if (myRow.getRowNum() == 0) continue; //skip header row

            Cell issueTypeCell = myRow.getCell(0);
            Cell keyCell = myRow.getCell(1);
            Cell statusCell = myRow.getCell(2);
            Cell createdCell = myRow.getCell(3);
            Cell transitionDateFromCell = myRow.getCell(4);
            Cell transitionDateToCell = myRow.getCell(5);
            Cell transitionFromCell = myRow.getCell(6);
            Cell transitionToCell = myRow.getCell(7);

            DataRow dataRow;
            if (statusCell.equals("New")) {
                dataRow = new DataRow(issueTypeCell.getStringCellValue(), keyCell.getStringCellValue(),
                        statusCell.getStringCellValue(), createdCell.getDateCellValue(), transitionDateFromCell.getDateCellValue(),
                        new Date(), "New", "New");
            }
            else {
                try {
                    dataRow = new DataRow(issueTypeCell.getStringCellValue(), keyCell.getStringCellValue(),
                            statusCell.getStringCellValue(), createdCell.getDateCellValue(), transitionDateFromCell.getDateCellValue(),
                            transitionDateToCell.getDateCellValue(), transitionFromCell.getStringCellValue(), transitionToCell.getStringCellValue());
                } catch (NullPointerException ex) {
                    dataRow = new DataRow(issueTypeCell.getStringCellValue(), keyCell.getStringCellValue(),
                            statusCell.getStringCellValue(), createdCell.getDateCellValue(), transitionDateFromCell.getDateCellValue(),
                            new Date(), "New", "New");
                }
            }
            dataRowList.add(dataRow);
        }

        //Decide if the type already exist or not
        List<IssueType> issueTypeList = new ArrayList<>();
        for (DataRow dataRow : dataRowList){
            if (dataRow.getTransitionFrom().equals("Completed")) {continue;}
            String issueType = dataRow.getIssueType();
            if (!issueTypeList.isEmpty()) {
                boolean isInList = false;
                for (IssueType issueTypeInList : issueTypeList) {
                    if (issueTypeInList.getIssueType().equals(issueType)) {
                        int counter = issueTypeInList.getCounter();
                        counter++;
                        issueTypeInList.setCounter(counter);
                        saveExistingTypeData(issueTypeInList, dataRow);
                        isInList = true;
                        break;
                    }
                }
                if (!isInList) {
                    issueTypeList.add(saveNonExistentTypeDataFrom(dataRow));
                }
            }
            else issueTypeList.add(saveNonExistentTypeDataFrom(dataRow));
        }
        getAverage(issueTypeList);
        writeDataLineByLine(filePath, issueTypeList);
    }

    private static IssueType saveNonExistentTypeDataFrom(DataRow dataRow) {
        //Create new IssueType, save data, add IssueType to list.
        long timeDifference = setTimeDifference(dataRow);
        StatusTime statusTime = new StatusTime(dataRow.getTransitionFrom(), timeDifference, 1);
        System.out.println("Look here for null: " + dataRow.getTransitionFrom() + " " + dataRow.getKey());
        List<StatusTime> statusTimeList = new ArrayList<>();
        statusTimeList.add(statusTime);

        return new IssueType(dataRow.getIssueType(), statusTimeList, 1);
    }
    private static void saveExistingTypeData(IssueType issueType, DataRow dataRow) {
        long timeDifference;
        timeDifference = setTimeDifference(dataRow);

        boolean isStatusThere = false;
        for (StatusTime statusTime : issueType.getStatusTimeList()) {
            if (dataRow.getTransitionTo() == null){
                dataRow.setTransitionTo("New");
            }
            System.out.println(issueType.getIssueType() + " " + dataRow.getKey() + " " + dataRow.getTransitionTo());

            if (statusTime.getStatus().equals(dataRow.getTransitionFrom())) {
                isStatusThere = true;
                System.out.println("Does this happen?");
                System.out.println("Line 108 " + dataRow.getIssueType() + " " + statusTime.getStatus()
                        + " " + statusTime.getTimeSpent());
                int counter = statusTime.getCounter() + 1;
                statusTime.setTimeSpent(statusTime.getTimeSpent() + timeDifference);
                statusTime.setCounter(counter);
                break;
            }
        }

        if (!isStatusThere) {
            StatusTime newStatusTime = new StatusTime(dataRow.getTransitionFrom(), timeDifference, 1);
            issueType.getStatusTimeList().add(newStatusTime);
        }
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    private static long setTimeDifference(DataRow dataRow) {
        long timeDifference;
        Date defaultDate = new Date(1900,01,00,0,00);
        if (dataRow.getTransitionDateFrom().equals(defaultDate) &&
                dataRow.getTransitionFrom() != null){
            timeDifference = getDateDiff(dataRow.getCreated(),
                    dataRow.getTransitionDateTo(), TimeUnit.HOURS);
        }
        else if (dataRow.getTransitionFrom() == null){
            Date today = new Date();
            timeDifference = getDateDiff(dataRow.getCreated(), today, TimeUnit.HOURS);
        }
        else {
            timeDifference = getDateDiff(dataRow.getTransitionDateFrom(),
                    dataRow.getTransitionDateTo(), TimeUnit.HOURS);
        }
        return timeDifference;
    }

    private static void getAverage(List<IssueType> issueTypeList){
            for (IssueType issueType : issueTypeList){
                for (StatusTime statusTime : issueType.getStatusTimeList()){
                    statusTime.setTimeSpent(statusTime.getTimeSpent()/statusTime.getCounter());
                }
            }
        }
    private static void writeDataLineByLine(String filePath, List<IssueType> issueTypeList)
    {
        // first create file object for file placed at location
        // specified by filepath
        File file = new File(filePath);
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = {"Issuetype", "Status", "Time Spent"};
            writer.writeNext(header);

            // add data to csv
            for (IssueType issueType : issueTypeList) {
                for (StatusTime statusTime : issueType.getStatusTimeList()) {
                    String[] data = {issueType.getIssueType(), statusTime.getStatus(), String.valueOf(statusTime.getTimeSpent())};
                    writer.writeNext(data);
                }
            }

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

