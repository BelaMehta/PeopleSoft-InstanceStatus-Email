package com.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.model.ExlData;

public class ExcelReader {
	
	static Logger log = Logger.getLogger(ExcelReader.class);
	
	public static String label_URL, label_UserName, label_Password, label_AppType, label_Client, label_DBType, label_TO, label_CC;
	
	public List<ExlData> readExcelFile(String filepath) throws IOException{
		log.info("Reading data from Test Data file"); 
		List<ExlData> excelDataList = new ArrayList<ExlData>();
		
		FileInputStream fisExcel = new FileInputStream(filepath);
		XSSFWorkbook workbook = new XSSFWorkbook(fisExcel);
		XSSFSheet sheet = workbook.getSheet("URLs");
		int totalRow = sheet.getLastRowNum();
		int totalColumn = sheet.getRow(0).getLastCellNum();
		Row headerRow = sheet.getRow(0);
		
		//Read Column Labels and store in static variables
		for(int col=0;col<totalColumn;col++){
			Cell headerCell = headerRow.getCell(col);
			switch(col){
				case 0:
					label_URL=headerCell.getStringCellValue();
					break;
				case 1:
					label_UserName=headerCell.getStringCellValue();
					break;
				case 2:
					label_Password=headerCell.getStringCellValue();
					break;
				case 3:
					label_AppType=headerCell.getStringCellValue();
					break;
				case 4:
					label_Client=headerCell.getStringCellValue();
					break;
				case 5:
					label_DBType=headerCell.getStringCellValue();
					break;
				case 6:
					label_TO=headerCell.getStringCellValue();
					break;
				case 7:
					label_CC=headerCell.getStringCellValue();
					break;
				}
			}
		
		//iterate through each row
		for(int rowIterator=1;rowIterator<=totalRow;rowIterator++){
			ExlData excelData = new ExlData();
			Row row = sheet.getRow(rowIterator);
			//Iterate through each column
			for(int colIterator=0;colIterator<totalColumn;colIterator++){
				//System.out.println(row.getCell(colIterator).getStringCellValue());
			switch(colIterator){
				case 0:
					excelData.setUrl(row.getCell(colIterator).getStringCellValue());
					break;
				case 1:
					excelData.setUsername(row.getCell(colIterator).getStringCellValue());
					break;
				case 2:
					excelData.setPassword(row.getCell(colIterator).getStringCellValue());
					break;
				case 3:
					excelData.setAppType(row.getCell(colIterator).getStringCellValue());
					break;
				case 4:
					excelData.setClient(row.getCell(colIterator).getStringCellValue());
					break;
				case 5:
					excelData.setDbType(row.getCell(colIterator).getStringCellValue());
					break;
				case 6:
					excelData.setEmail(row.getCell(colIterator).getStringCellValue());
					break;
				case 7:
					excelData.setCCEmail(row.getCell(colIterator).getStringCellValue());
					break;
				}
			}
			excelDataList.add(excelData);
			log.info("Test data file loaded successfully...");
		}
		return excelDataList;
	}
}
