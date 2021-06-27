package io.yoshizaki4439.robatest.utils;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;



public class ExcelUtils {
	

	private ExcelUtils() {
	}

	public static List<ExcelSheetData> readSheets(String filePath) throws Exception {
		ArrayList<ExcelSheetData> sheets = new ArrayList<>();
		InputStream inputStream = null;
		Workbook workbook = null;
		try {
			inputStream = ExcelUtils.class.getClassLoader().getResourceAsStream(filePath);
			workbook = WorkbookFactory.create(inputStream);
			int numSheets = workbook.getNumberOfSheets();
			for (int sheetIndex = 0; sheetIndex < numSheets; sheetIndex++) {
				ExcelSheetData sheetData = new ExcelSheetData();
				String sheetName = workbook.getSheetAt(sheetIndex).getSheetName();
				String[][] sheetArrayData = getSheetArrayData(workbook, sheetIndex);
				sheetData.setSheetName(sheetName);
				sheetData.setSheetArrayData(sheetArrayData);
				sheets.add(sheetData);
			}
		} catch (Exception e) {
			System.err.println(ExcelUtils.class.getSimpleName() + "-8000" + "readSheets Failed." + e);
			throw e;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
			}
			try {
				if (workbook != null) {
					workbook.close();
				}
			} catch (Exception e) {
			}
		}
		return sheets;
	}

	private static String[][] getSheetArrayData(Workbook workbook, int sheetIndex) {
		String[][] sheetData = null;
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		int firstRowNum = 0;
		int lastRowNum = sheet.getLastRowNum();
		int firstColumnNum = 0;
		int lastColumnNum = 0;
		for (int rowIndex = firstRowNum; rowIndex <= lastRowNum; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (row != null && StringUtils.isNotEmpty(getCellValue(row.getCell(0)))) {
				if (StringUtils.equalsIgnoreCase(getCellValue(row.getCell(0)), "end")) {
					lastRowNum = rowIndex;
					break;
				}
				int columnNum = row.getLastCellNum();
				if (lastColumnNum < columnNum) {
					lastColumnNum = columnNum;
				}
			}
		}
		sheetData = new String[lastRowNum + 1][lastColumnNum + 1];
		for (int rowIndex = firstRowNum; rowIndex <= lastRowNum; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (row != null && StringUtils.isNotEmpty(getCellValue(row.getCell(0)))) {
				for (int columnIndex = firstColumnNum; columnIndex <= lastColumnNum; columnIndex++) {
					Cell cell = sheet.getRow(rowIndex).getCell(columnIndex);
					sheetData[rowIndex][columnIndex] = getCellValue(cell);
				}
			}
		}
		return sheetData;
	}

	public static String getCellValue(Cell cell) {
		Object cellValue = null;
		if (cell == null) {
			return null;
		}
		switch (cell.getCellTypeEnum()) {
		case STRING:
			cellValue = cell.getStringCellValue();
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				cellValue = new Timestamp((cell.getDateCellValue()).getTime());
			} else {
				cellValue = BigDecimal.valueOf(cell.getNumericCellValue());
			}
			break;
		case BOOLEAN:
			cellValue = cell.getBooleanCellValue();
			break;
		case FORMULA:
			Workbook book = cell.getSheet().getWorkbook();
			cellValue = getCellValue(book.getCreationHelper().createFormulaEvaluator().evaluateInCell(cell));
			break;
		default:
			System.out.println(ExcelUtils.class.getSimpleName() + "-0001" + "Cell Type:" + cell.getCellTypeEnum());
			return null;
		}

		return cellValue != null ? cellValue.toString() : null;
	}

	public static String getStringRangeValue(Cell cell) {
		int rowIndex = cell.getRowIndex();
		int columnIndex = cell.getColumnIndex();
		Sheet sheet = cell.getSheet();
		int size = sheet.getNumMergedRegions();
		for (int i = 0; i < size; i++) {
			CellRangeAddress range = sheet.getMergedRegion(i);
			if (range.isInRange(rowIndex, columnIndex)) {
				Cell firstCell = getCell(sheet, range.getFirstRow(), range.getFirstColumn()); // 蟾ｦ荳翫�ｮ繧ｻ繝ｫ繧貞叙蠕�
				return getCellValue(firstCell);
			}
		}
		return null;
	}

	public static Cell getCell(Sheet sheet, int rowIndex, int columnIndex) {
		Row row = sheet.getRow(rowIndex);
		if (row != null) {
			return row.getCell(columnIndex);
		}
		return null;
	}
}
