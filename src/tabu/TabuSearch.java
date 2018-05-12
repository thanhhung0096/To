package tabu;

import java.util.ArrayList;

import Node.car;

public class TabuSearch {
	private ArrayList<car> initSolution;
	private ArrayList<car> bestSolution = new ArrayList<>();
	public TabuSearch(ArrayList<car> initSolution) {
		super();
		this.initSolution = initSolution;
	}
	
	public void exchange1Route()
	{
		for(int i = 0 ; i < this.initSolution.size() ; i++)
		{
			car c = this.initSolution.get(i);
			int numNode = c.getPassedNodes().size();
			switch (numNode) {
			case 1:
				bestSolution.add(c);
				break;
			default:
				bestSolution.add(c);
				for(int m = 0 ; m < numNode - 1 ; m++)
				{
					for(int n = m + 1 ; n < numNode ; n++)
					{
						car tmp = c;
						c.exchange2Node(m, n);
						if(tmp.getCost()  < c.getCost() )
						{
							System.out.println("Ngon hon.");
							bestSolution.set(bestSolution.size()-1, tmp);
						}
						
					}
				}
				break;
			}
		}
		System.out.println("Best Solution: " );
		for(int i = 0 ; i < bestSolution.size() ; i++)
			System.out.println(bestSolution.get(i));
		
	}
}
