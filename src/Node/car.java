package Node;

import java.util.ArrayList;

public class car {
	private String bienSo="";
	private int noiDau;
	private int Load;
	private int hasLoaded = 0;
	private boolean canLoadMore = true;
	private int timeTraveled = 0;
	public ArrayList<Node> passedNodes = new ArrayList<Node>();
	public ArrayList<Node> bestOfTabu = new ArrayList<Node>();
	private Node currentNode = new Node(0, 0);
	private boolean end = false;
	private double cost = 0 ;
	private double distance = 0;
	public car( int noiDau, int load) {
		
		this.noiDau = noiDau;
		this.Load = load;
		
	}
	
	public void goTo(Node node)
	{
		this.passedNodes.add(node);
		
		this.hasLoaded += node.getDemand();
		
		
		this.canLoadMore = ((this.Load - this.hasLoaded)  > 2)? true : false;
		
		this.timeTraveled += (int)_main.matrixTime[currentNode.getID()][node.getID()] + node.getService_time();
		if (! this.canLoadMore || this.timeTraveled > 20*60)
		{
			this.end = true;
		}
		this.distance +=  _main.matrixDistance[this.currentNode.getID()][node.getID()];
		
		
		this.currentNode = node;
	}
	
	public void Go(ArrayList<Node> Nodes)
	{
		
		while(!this.end && Nodes.size() > 0)
		{
			ArrayList<Node> listCanGoNext = listNodeCanGo(Nodes);
			if(listCanGoNext.size() == 0)
				break;
			Node goNext = this.currentNode.findNodeClosed(listCanGoNext);
//			System.out.println("\t* " + goNext);
			this.goTo(goNext);
			
			Nodes.remove(goNext);
		}
//		goHome();
		
	}
	
	public String getBienSo() {
		return bienSo;
	}


	public void setBienSo(String bienSo) {
		this.bienSo = bienSo;
	}


	public int getNoiDau() {
		return noiDau;
	}


	public void setNoiDau(int noiDau) {
		this.noiDau = noiDau;
	}


	public int getLoad() {
		return Load;
	}


	public void setLoad(int load) {
		Load = load;
	}

	
	
	
	public boolean isCanLoadMore() {
		return canLoadMore;
	}

	public void setCanLoadMore(boolean canLoadMore) {
		this.canLoadMore = canLoadMore;
	}


	public int getHasLoaded() {
		return hasLoaded;
	}

	public void setHasLoaded(int hasLoaded) {
		this.hasLoaded = hasLoaded;
	}

	public int getTimeTraveled() {
		int t = 0;
		for(int i = 0 ; i < this.passedNodes.size();i++)
		{
			if(i ==0)
			{
				t += _main.matrixTime[0][this.passedNodes.get(0).getID()] + this.passedNodes.get(0).getService_time(); 
			}
			else
			{
				t += _main.matrixTime[this.passedNodes.get(i-1).getID()][this.passedNodes.get(i).getID()]
						+ this.passedNodes.get(i).getService_time();
			}
		}
		
		return timeTraveled;
	}

	public void setTimeTraveled(int timeTraveled) {
		this.timeTraveled = timeTraveled;
	}

	public ArrayList<Node> getPassedNodes() {
		return passedNodes;
	}

	public void setPassedNodes(ArrayList<Node> passedNodes) {
		this.passedNodes = passedNodes;
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public double getCost() {
		
		if(this.Load == 2)
			return 100 + this.getDistance() * 1.6296;
		if(this.Load == 5)
			return 250 + this.getDistance()*2.4056;
		return 300 + this.getDistance()*2.5608;
		
	}

	
	public double getDistance() {
		double d = 0 ; 
		for(int i = 0 ; i < this.passedNodes.size();i++)
		{
			if(i ==0)
			{
				d += _main.matrixDistance[0][this.passedNodes.get(0).getID()]; 
			}
			else
			{
				d += _main.matrixDistance[this.passedNodes.get(i-1).getID()][this.passedNodes.get(i).getID()];
			}
		}
		return d;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "Xe " + this.Load + "T";
		for(int i = 0 ; i < this.getPassedNodes().size() ; i++)
		{
			s += "\n\t*" + this.getPassedNodes().get(i);
		}
		s+="\n\tTotal Distance: " +  this.getDistance() +"\tTotal Cost: "
				+ this.getCost()+ "\tTotal time: " + this.getTimeTraveled() + "\n";
		return s;
	}
	
	public ArrayList<Node> listNodeCanGo(ArrayList<Node> Nodes)
	{
		ArrayList<Node> output = new ArrayList<Node>();
		for (int i = 0 ; i < Nodes.size() ; i++ )
		{
			if ((this.Load - this.hasLoaded) >= Nodes.get(i).getDemand() && (this.timeTraveled +
					_main.matrixTime[this.currentNode.getID()][Nodes.get(i).getID()] 
							+ Nodes.get(i).getService_time()) < 20*60 )
			{
				output.add(Nodes.get(i));
			}
		}
		return output;
	}
	public void goHome()
	{
		if((_main.matrixDistance[this.currentNode.getID()][19] + _main.matrixDistance[19][0])
			> (_main.matrixDistance[this.currentNode.getID()][18] + _main.matrixDistance[18][0])) // BD > HM
				{
					goTo(_main.endAtHocMon);
				}
		else
			goTo(_main.endAtHocMon);
	}
	
	public void exchange2Node(int i, int j )
	{
//		System.out.println("Before: " + this.passedNodes);
		Node tmp = this.getPassedNodes().get(i);
		this.passedNodes.set(i, this.passedNodes.get(j));
		this.passedNodes.set(j, tmp);
//		System.out.println("After : " + this.passedNodes);
	}

	public ArrayList<Node> getBestOfTabu() {
		return bestOfTabu;
	}

	public void setBestOfTabu(ArrayList<Node> bestOfTabu) {
		this.bestOfTabu = bestOfTabu;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
	
}
