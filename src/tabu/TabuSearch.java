package tabu;
import java.util.ArrayList;

import Node.Node;
import Node._main;
import Node.car;

public class TabuSearch {
	private ArrayList<car> initSolution;
	private ArrayList<car> bestSolution = new ArrayList<>();
	private int numOfInterations = 4;
	public TabuSearch(ArrayList<car> initSolution, int numOfinterations) {
		super();
		this.initSolution = initSolution;
		this.numOfInterations = numOfinterations;
	}
	
	public TabuSearch(ArrayList<car> initSolution) {
		super();
		this.initSolution = initSolution;
	}
	
	public void exchange1Route()
	{
		for(int i = 0 ; i < this.initSolution.size() ; i++)
		{
			car c = this.initSolution.get(i);
//			System.out.println("Car " + c);
			if (c.passedNodes.size() == 1)
			{
				bestSolution.add(c);
				continue;
			}
			ArrayList<Node> currSolution = c.getPassedNodes();
			double bestCost = c.getCost();
			ArrayList<Node> bestOfTabuSearch = new ArrayList<>();
			ArrayList<Integer> tabuList = new ArrayList<>();
			for(int k =  0 ; k < this.numOfInterations ; k ++)
			{
				currSolution = getBestNeighbour(tabuList, currSolution);
//				System.out.println("current solutions: "+currSolution);
				if(currSolution.size()>0 && getCostbaseDistance(currSolution) < bestCost)
				{
					bestOfTabuSearch = currSolution;
					bestCost = getCostbaseDistance(currSolution);
				}
			}
			if(getCostbaseDistance(bestOfTabuSearch) < getCostbaseDistance(c.passedNodes))
			{			
//				System.out.println("Ngon hon!!");
				c.setBestOfTabu(bestOfTabuSearch);
				
			}
			bestSolution.add(c);
		}
		
		
	}
	
	public void exchangeMoreRoute()
	{
		//use result of tabu1Route bestSolution
		@SuppressWarnings("unchecked")
		ArrayList<car> currSolution = (ArrayList<car>) bestSolution.clone();
		for(int i = 0 ; i < currSolution.size() -1  ; i++)
		{
			car c1 = currSolution.get(i);
			if(c1.getPassedNodes().size() < 3) continue;
			
			for(int j = i+1 ; j < currSolution.size() ; j++)
			{
				car c2 = currSolution.get(j);
				ArrayList<car> testSolution = swapNodeBtw2Route(currSolution, i, j);
				if(getCost(testSolution) < getCost(currSolution))
				{
					System.out.println("BINGO");
					currSolution = testSolution;
				}
				
			}
			
		}
		
		bestSolution = currSolution;
	}
	
	public ArrayList<car> swapNodeBtw2Route(ArrayList<car> currSolution, int p1, int p2)
	{
		@SuppressWarnings("unchecked")
		ArrayList<car> testSolution = (ArrayList<car>) currSolution.clone();
		car c1 = testSolution.get(p1);
		car c2 = testSolution.get(p2);
		
		for(int i = 0 ; i < c1.getPassedNodes().size() -1 ; i++)
		{
			Node n1 = c1.getPassedNodes().get(i);
			for(int j = 0 ; j < c2.getPassedNodes().size() -1 ; j++)
			{
				Node n2 = c2.getPassedNodes().get(j);
				if(n1.getDemand() == n2.getDemand())
				{
					Node tmp = n1;
					c1.passedNodes.set(i, n2);
					c2.passedNodes.set(j,tmp);
				}
			}
		}
		return testSolution;
	}
	public ArrayList<Node> getBestNeighbour(ArrayList<Integer> tabuList, ArrayList<Node> currSolution)
	{
		ArrayList<Node> bestNeighbour = new ArrayList<>();
		ArrayList<ArrayList<Node>> testSolutions = new ArrayList<>();
		ArrayList<Double> listCost = new ArrayList<>(); 
		ArrayList<ArrayList<Integer>> tabuTmp = new ArrayList<>();

		for(int i = 0 ; i < currSolution.size()-1 ; i++)
		{
			
			if(checkInTabuList(tabuList, currSolution.get(i),currSolution.get(i+1) ))
				continue;
			ArrayList<Node> test = swapNode(currSolution, i, i+1);
			testSolutions.add(test);
			
			ArrayList<Integer> thisTabu = new ArrayList<>();
			thisTabu.add(currSolution.get(i).getID());
			thisTabu.add(currSolution.get(i+1).getID());
			tabuTmp.add(thisTabu);
			listCost.add(getCostbaseDistance(test));
			
		}
//		System.out.println("test solutions: "+testSolutions);
//		System.out.println("cost: " +listCost);
		if(listCost.size() > 0 )
		{
			int index = findBestCost(listCost);
			bestNeighbour = testSolutions.get(index);
			tabuList.add(tabuTmp.get(index).get(0));
			tabuList.add(tabuTmp.get(index).get(1));
//			System.out.println("tabu: " + tabuList);
			
			
			return bestNeighbour;
		}
		return currSolution;
	}
	
	public int findBestCost(ArrayList<Double> listCost)
	{
		int  i = 0;
		double minCost = listCost.get(0);
		for(int k = 0 ; k < listCost.size() ; k++)
		{
			if (listCost.get(k) <= minCost )
			{
				minCost = listCost.get(k); 
				i = k;
			}
		}
//		System.out.println("best COst: " + minCost);
		return i;
	}
	public boolean checkInTabuList(ArrayList<Integer> tabuList, Node n1, Node n2)
	{
		for(int i = 0 ; i < tabuList.size(); i+=2)
		{
			if((n1.getID() == tabuList.get(i) && n2.getID() == tabuList.get(i+1))  ||
					(n1.getID() == tabuList.get(i+1) && n2.getID() == tabuList.get(i)))
				return true;
		}
		return false;
	}
	public ArrayList<Node> swapNode(ArrayList<Node> currSolution , int i, int j )
	{
		@SuppressWarnings("unchecked")
		ArrayList<Node> old = (ArrayList<Node>) currSolution.clone();
		Node tmp = currSolution.get(i);
		old.set(i, currSolution.get(j));
		old.set(j, tmp);
		return old;
		
	}
	
	public double getCostbaseDistance(ArrayList<Node> nodes)
	{
		double cost = 0 ;
		
		for(int i =0 ; i < nodes.size() ; i++)
		{
			if(i ==0)
			{
				cost += _main.matrixDistance[0][nodes.get(0).getID()]; 
			}
			else
			{
				cost += _main.matrixDistance[nodes.get(i-1).getID()][nodes.get(i).getID()];
			}
		}
		return cost;
	}
//	public void exchange1Route()
//	{
//		for(int i = 0 ; i < this.initSolution.size() ; i++)
//		{
//			car c = this.initSolution.get(i);
//			int numNode = c.getPassedNodes().size();
//			switch (numNode) {
//			case 1:
//				bestSolution.add(c);
//				break;
//			default:
//				bestSolution.add(c);
//				for(int m = 0 ; m < numNode - 1 ; m++)
//				{
//					for(int n = m + 1 ; n < numNode ; n++)
//					{
//						car tmp = c;
//						c.exchange2Node(m, n);
//						if(tmp.getCost()  < c.getCost() )
//						{
//							System.out.println("Ngon hon.");
//							bestSolution.set(bestSolution.size()-1, tmp);
//						}
//						
//					}
//				}
//				break;
//			}
//		}
//		System.out.println("Best Solution: " );
//		for(int i = 0 ; i < bestSolution.size() ; i++)
//			System.out.println(bestSolution.get(i));
//		
//	}
	
	public double getCost(ArrayList<car> listCar)
	{
		double cost  = 0;
		for(int i=0 ; i<listCar.size() ; i++)
		{
			cost += listCar.get(i).getCost();
		}
		return cost;
	}
	public ArrayList<car> getInitSolution() {
		return initSolution;
	}

	public void setInitSolution(ArrayList<car> initSolution) {
		this.initSolution = initSolution;
	}

	public ArrayList<car> getBestSolution() {
		return bestSolution;
	}

	public void setBestSolution(ArrayList<car> bestSolution) {
		this.bestSolution = bestSolution;
	}

	public int getNumOfInterations() {
		return numOfInterations;
	}

	public void setNumOfInterations(int numOfInterations) {
		this.numOfInterations = numOfInterations;
	}
	
}
