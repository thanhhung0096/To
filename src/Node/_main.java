package Node;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class _main{
	public static double[][] matrixTime;
	public static double[][] matrixDistance;
	static ArrayList<ArrayList<Node>> listNodebaseDay = new ArrayList<ArrayList<Node>>();
	static ArrayList<Date> listDate = new ArrayList<Date>();
	final public static Node startNode = new Node(0, 0);
	final public static Node endAtBinhDuong = new Node(19,0);
	final public static Node endAtHocMon = new Node(18, 0);
	final public static ArrayList<Node> finishNodes = new ArrayList<>();
	
	
	
	public static void main(String[] args) throws IOException {
	endAtBinhDuong.setService_time(0);
	endAtHocMon.setService_time(0);
		
	finishNodes.add(endAtBinhDuong);
	finishNodes.add(endAtHocMon);
	matrixTime = readMatrixExcel("ma-trận-thời-gian.xlsx")	;
	matrixDistance = readMatrixExcel("MA-TRẬN-KC-chinh-sua.xlsx");
	
	initDataAndNodes("DATA-NHU-CAU.xlsx");
	
	//Algorithm
	for(int currentDay = 0 ; currentDay < listDate.size();currentDay++)
	{
		System.out.println("\n\n\t\t\t-----------" +listDate.get(currentDay)+"-------------------");
		ArrayList<Node> NodesToday = listNodebaseDay.get(currentDay);
		ArrayList<car> listCar = initCar();
		
		for(int i = 0 ; i < listCar.size() ; i++)
		{
			if(NodesToday.size() == 0)
				break;
			System.out.println("+ " +listCar.get(i) + " has Loaded at:");
			listCar.get(i).Go(NodesToday);
		}

		
	}
	
	}
	
	public static double[][] readMatrixExcel(String pathFile)
	{
		double[][] matrix = new double[20][20];
		try {
			OPCPackage pkg = OPCPackage.open(new File(pathFile));
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
		    XSSFSheet sheet = wb.getSheetAt(0);
		    XSSFRow row;
		    XSSFCell cell;

		    int rows; // No of rows
		    rows = sheet.getRow(0).getPhysicalNumberOfCells();
		    int cols; // No of columns
		    cols = sheet.getRow(0).getLastCellNum();
		   
	

		    for(int r = 1; r < rows; r++) {
		        row = sheet.getRow(r);
		        if(row != null) {
		            for(int c = 1; c < cols; c++) {
		                cell = row.getCell((short)c);
		                if(cell != null) {
		                	DataFormatter df = new DataFormatter();
		                
		                		try{
		                			
		                			matrix[r-1][c-1] = cell.getNumericCellValue();
		                		}
		                		catch (Exception e) {
									// TODO: handle exception
		                			matrix[r-1][c-1] = -1;
//		                			System.out.println("here");
//		                			System.out.println(r);
//		                			System.out.println(c);
		                			
								}
		                		
		                		
		                	
		                }
		            }
		        }
		    }
		} catch(Exception ioe) {
		    ioe.printStackTrace();
		}
	return matrix;
	}
	public static ArrayList<car> initCar()
	{
		ArrayList<car> listCar = new ArrayList<car>();
		for (int i = 0; i <=15;i++ )
		{
			if(i<=9)
			{
				listCar.add(new car(18, 8));
			}
			else
			{
				listCar.add(new car(19, 8));
			}
		}
		
		for (int i = 0; i <=15;i++ )
		{
			if(i<=10)
			{
				listCar.add(new car(18, 5));
			}
			else
			{
				listCar.add(new car(19, 5));
			}
		}
		
		for (int i = 0; i <=15;i++ )
		{
			if(i<=8)
			{
				listCar.add(new car(18, 2));
			}
			else
			{
				listCar.add(new car(19, 2));
			}
		}
		return listCar;
	}
	public static void initDataAndNodes(String pathFile)
	{
		
		int currentDay = 0;
		try {
			OPCPackage pkg = OPCPackage.open(new File(pathFile));
			XSSFWorkbook wb = new XSSFWorkbook(pkg);
		    XSSFSheet sheet = wb.getSheetAt(2);
		    XSSFRow row;
		    XSSFCell cell;

		    int rows; // No of rows
		    rows = sheet.getPhysicalNumberOfRows();
		    System.out.println(rows);
		    int cols; // No of columns
		    cols = sheet.getRow(0).getLastCellNum();
		    short cellDate =0;
		    short cellDemand =1;
		    short cellID =4;
		    for(int r = 1; r < rows; r++) {
		        row = sheet.getRow(r);
		        if(row != null) {
		            cell = row.getCell(cellDemand);
		            cell.setCellType(CellType.STRING);
		            int Demand = Integer.parseInt(cell.getStringCellValue());
		            cell = row.getCell(cellID);
		            cell.setCellType(CellType.STRING);
		            int ID = Integer.parseInt(cell.getStringCellValue());
		        	cell = row.getCell((cellDate));
		        	Node n = new Node(ID, Demand);
		        	
		            if(cell != null) {
		                if(r==1)
		            	{
		                	ArrayList<Node> firstNode = new ArrayList<Node>();
		                	firstNode.add(n);
		                	listNodebaseDay.add(firstNode);
		            		listDate.add(cell.getDateCellValue());
		            		continue;
		            	}
		                
		                Date tmp = cell.getDateCellValue();
		               
		                if(tmp.compareTo(listDate.get(listDate.size() -1))!=0)
		                {
		                	listDate.add(tmp);
		                	currentDay +=1;
		                	ArrayList<Node> newDayNodes = new ArrayList<Node>();
		                	newDayNodes.add(n);
		                	listNodebaseDay.add(newDayNodes);
		                }
		                else
		                {
		                	listNodebaseDay.get(currentDay).add(n);
		                }
		               
		                
		            }
		            
		        }
		    }
		} catch(Exception ioe) {
		    ioe.printStackTrace();
		}
		
	}
	
}
