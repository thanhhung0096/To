package Node;

import java.awt.List;
import java.util.ArrayList;

public class Node {
	private int ID;//
	private double demand;//
	private int service_time;
	
	
	public Node findNodeClosed(ArrayList<Node> listNode)
	{
		Node closed = listNode.get(0);
		double min = _main.matrixDistance[this.ID][listNode.get(0).getID()]; 
		for(int i =0 ; i < listNode.size();i++)
		{
			if (_main.matrixDistance[this.ID][listNode.get(i).getID()] < min)
			{
				min =  _main.matrixDistance[this.ID][listNode.get(i).getID()];
				closed = listNode.get(i);
			}
		}
		
		return closed;
	}
	public Node(int iD, double demand) {
		super();
		ID = iD;
		this.demand = demand;
		if(demand ==2 )
		{
			this.service_time = 25;
		}
		else if(demand==5)
		{
			this.service_time = 40;
		}
		else
		{
			this.service_time = 60;
		}
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public double getDemand() {
		return demand;
	}
	public void setDemand(double demand) {
		this.demand = demand;
	}
	public double getService_time() {
		return service_time;
	}
	public void setService_time(int service_time) {
		this.service_time =  service_time;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Node ID: " + this.ID+ ", Demand: " + this.demand + ", service time: " + this.service_time;
	}
}
