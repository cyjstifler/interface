package com.superhard.lbsw.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.superhard.lbsw.utils.GetProperty;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;



public class ReadExcel {
	public static int main(String arg,int type) throws BiffException, IOException {
		// 1、构造excel文件输入流对象
		String sFilePath = arg;

		int rows = 0;
		if (sFilePath.endsWith("xls") || sFilePath.endsWith("XLS")) {
			rows = readAsExcel2003(sFilePath,type);
		} else if (sFilePath.endsWith("xlsx") || sFilePath.endsWith("XLSX")) {
			rows = readAsExcel2007(sFilePath,type);
		}

		return rows;
	}

	private static int readAsExcel2003(String sFilePath,int type) throws BiffException, IOException {
		InputStream is = new FileInputStream(sFilePath);
		// 2、声明工作簿对象
		Workbook rwb = Workbook.getWorkbook(is);
		// 3、获得工作簿的个数,对应于一个excel中的工作表个数
		rwb.getNumberOfSheets();
		Sheet oFirstSheet = rwb.getSheet(0);// 使用索引形式获取第一个工作表，也可以使用rwb.getSheet(sheetName);其中sheetName表示的是工作表的名称
		// System.out.println("工作表名称：" + oFirstSheet.getName());
		int rows = oFirstSheet.getRows();// 获取工作表中的总行数
		int columns = oFirstSheet.getColumns();// 获取工作表中的总列数
		String[] ID = new String[columns];
		for (int i = 1; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				ID[j] = oFirstSheet.getCell(j, i).getContents();// 需要注意的是这里的getCell方法的参数，第一个是指定第几列，第二个参数才是指定第几行
			}
			saveImportedExcel(ID,type);
		}
		return rows;
	}

	private static int readAsExcel2007(String sFilePath,int type) throws FileNotFoundException, IOException {
		// File file = new File(sFilePath);

		XSSFWorkbook xwb = new XSSFWorkbook(sFilePath);
		XSSFSheet sheet = xwb.getSheetAt(0);
		int rows = sheet.getLastRowNum() + 1;
		int columns = sheet.getRow(0).getPhysicalNumberOfCells();
		String[] ID = new String[columns];
		for (int i = 1; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				XSSFCell oCell = sheet.getRow(i).getCell(j);
				String strCell = "";
				switch (oCell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					strCell = oCell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					strCell = String.valueOf(oCell.getNumericCellValue());
					if (strCell.endsWith(".0")) {
						strCell = strCell.substring(0, strCell.length()-2);
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					strCell = String.valueOf(oCell.getBooleanCellValue());
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					strCell = "";
					break;
				default:
					strCell = "";
					break;
				}
				ID[j] = strCell;
			}
			saveImportedExcel(ID,type);
		}
		xwb.close();
		return rows;
	}

	private static void saveImportedExcel(String[] ID,int type) {
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			String URL = GetProperty.getPropertyByName("project", "URL");
			String USERNAME = GetProperty.getPropertyByName("project", "User");
			String PASSWORD = GetProperty.getPropertyByName("project", "PassWord");
			// Create the connection.
			Connection con;// = DriverManager.getConnection(URL, USERNAME,
							// PASSWORD);;
			Statement stmt;
			java.sql.PreparedStatement pstmt;
			con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			con.setAutoCommit(true);
			stmt = con.createStatement();
			String str;
			switch (type) {
			case 1:
				str = "INSERT INTO DJAPP.T_EQUIP_INFO_9(EQUIP_ID,EQUIP_NAME,EQUIP_STD,EQUIP_COST_CENTER,EQUIP_CALSS,EQUIP_PROJECT,EQUIP_MANAGE,MAINT_STATION,MAINT_NAME,EQUIP_FACTORY) VALUES(?,?,?,?,?,?,?,?,?,?)";
				break;

			default:
				str = "INSERT INTO DJAPP.T_USER_INFO(USER_NUM,USER_NAME,USER_JOB,USER_STATION,USER_SEXUAL,USER_TEAM,USER_MAJOR,USER_PASSWORD,ID) VALUES(?,?,?,?,?,?,?,?,?)";
				break;
			}				
			pstmt = con.prepareStatement(str);
			for (int i = 0; i < ID.length; i++) {
				pstmt.setString(i + 1, ID[i]);
			}
			pstmt.executeUpdate();
			pstmt.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
