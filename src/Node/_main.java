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
import tabu.*;
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
	matrixTime = readMatrixExcel("TimeMatrix.xlsx")	;
	matrixDistance = readMatrixExcel("DistantMatrix.xlsx");
	
	initDataAndNodes("DATA-NHU-CAU.xlsx");
	
	//Algorithm
	for(int currentDay = 0 ; currentDay < listDate.size();currentDay++)
	{
		System.out.println("\n\n\t\t\t-----------" +listDate.get(currentDay)+"-------------------");
		ArrayList<Node> NodesToday = listNodebaseDay.get(currentDay);
		showDemand(NodesToday);
		ArrayList<car> listCar = initCar();
		
		ArrayList<car> firstSolution = initSolution(listCar, NodesToday);
//		System.out.println(firstSolution);
		TabuSearch tabu = new TabuSearch(firstSolution);
		tabu.exchange1Route();
		tabu.exchangeMoreRoute();
		ArrayList<car> bestSolution = tabu.getBestSolution();
		showSolution(bestSolution);
		System.out.println("TOTAL COST of DAY: " + tabu.getCost(bestSolution));
	}
	
	}
	
	public static void showSolution(ArrayList<car> bestSolution)
	{
		for(int i = 0 ; i < bestSolution.size() ; i++)
		{
			System.out.println("------------");
			if(bestSolution.get(i).getBestOfTabu().size() > 0){
				bestSolution.get(i).goHome();
				String s = "";
				s = "Init solution: \n" + bestSolution.get(i);
				bestSolution.get(i).setPassedNodes(bestSolution.get(i).getBestOfTabu());
				bestSolution.get(i).goHome();
				s = "Best Solution: \n" + bestSolution.get(i) + "\n" +s ;
				System.out.println(s);
				continue;
			}
			else{
				bestSolution.get(i).goHome();
				System.out.println(bestSolution.get(i));
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
		for (int i = 0; i <=14;i++ )
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
		
		for (int i = 0; i <=14;i++ )
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
		
		for (int i = 0; i <=14;i++ )
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
//		    System.out.println(rows);
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
	public static ArrayList<car> initSolution(ArrayList<car> listCar, ArrayList<Node> nodes){
		ArrayList<car> initSolution = new ArrayList<>();
		ArrayList<car> xe8Ts = new ArrayList<>();
		ArrayList<car> xe5Ts = new ArrayList<>();
		ArrayList<car> xe2Ts = new ArrayList<>();
		for (int i =0 ; i< 15 ; i++)
		{
			xe8Ts.add(listCar.get(i));
			xe5Ts.add(listCar.get(i+15));
			xe2Ts.add(listCar.get(i+30));
			
		}
	
		
		while(nodes.size() > 0 )
		{
			ArrayList<Integer> listDemand = new ArrayList<>();
			for (Node n : nodes)
				listDemand.add((int) n.getDemand());
			int TH = checkTH(listDemand);
			car c;
//			showDemand(nodes);
			switch (TH)
			{
			case 1:
				c = xe2Ts.get(0);
				xe2Ts.remove(c);
	
				c.Go(nodes);
				initSolution.add(c);
				break;
			case 2:
				c = xe5Ts.get(0);
				xe5Ts.remove(c);
				
				c.Go(nodes);
				initSolution.add(c);
				break;
			case 3:
				c = xe8Ts.get(0);
				
				xe8Ts.remove(0);
				c.Go(nodes);
				initSolution.add(c);
				break;

			}
//			System.out.println(nodes);
			
		}
		//Check again
//		System.out.println("Init Solution: " + initSolution);
		for(int i = 0 ; i < initSolution.size() ; i++){
			car tmp = initSolution.get(i);
			if(tmp.getLoad() - tmp.getHasLoaded() >2)
			{
				car c  = new car(tmp.getNoiDau(), tmp.getHasLoaded());
				for(int j = 0 ; j < tmp.getPassedNodes().size() ; j++)
				{
					c.goTo(tmp.getPassedNodes().get(j));
				}
				initSolution.set(i,c);
			}
		}
		return initSolution;
	}
	
	public static int checkTH(ArrayList<Integer> demand)
	{
		int TH = 0;
		if(demand.size() == 1 && demand.get(0) == 2)
			TH = 1;
		else if( (demand.size() == 1 && demand.get(0) == 5) || (demand.size() == 2 && demand.get(0) == 2 && demand.get(1) == 2 )
								|| (!demand.contains(2) && !demand.contains(8)))
			TH = 2;
		else
			TH =3;
//		System.out.println(TH);
		return TH;
	}
	public static void showDemand(ArrayList<Node> nodes)
	{
		System.out.print("Demand: ");
		for(int i= 0 ; i < nodes.size() ; i++)
		{
			if(i==0)
				System.out.print(nodes.get(i).getDemand());
			else
				System.out.print(" ,"+nodes.get(i).getDemand());
		}
		System.out.println();
	}
}
