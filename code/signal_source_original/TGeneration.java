//import java.text.*;

public class TGeneration {
	float TrafficProducedataNode[];   //// from a centroid
	float TrafficAttractedtoaNode[];    //// to a cnetroid
	int extTrips[];/// external stations
	double ext_Betas_1990[];///from 1990 to 2000
	int vertices;
	int centroids ;
	int taz;
	public double trip_gen_mutiplier=1.0;
	public float totaltrips=0;
	float a0=0,a1=1,a2=(float)0.5,b0=0,b1=(float)0.5,b2=1;
	Automata ca;
	
	
	public TGeneration( DirectedGraph dgraph) {
		vertices=dgraph.Vertices() ;
		centroids=dgraph.Centroids() ;
		taz=dgraph.TAZ() ;
		TrafficProducedataNode=new float [centroids];
		TrafficAttractedtoaNode=new float [centroids];
		for(int i=0;i<centroids;i++){
			TrafficProducedataNode[i]=TrafficAttractedtoaNode[i]=0;
		}
	}
	
	public void tripGeneration(DirectedGraph dgraph,Automata ca) {
		this.ca=ca;
		float total_production=0,total_attraction=0;	   


		for(int i=0;i<taz;i++){
			
			TrafficProducedataNode[i]=a0+a1*dgraph.TAZ_info [i][1]+a2*dgraph.TAZ_info [i][2];
			total_production+=TrafficProducedataNode[i];	
				
			TrafficAttractedtoaNode[i]=b0+b1*dgraph.TAZ_info [i][1]+b2*dgraph.TAZ_info [i][2];
			total_attraction+=TrafficAttractedtoaNode[i];		
			
		}
		
		//System.out.print(total_production+"\t"+total_attraction+"\n");
		if(total_production!=total_attraction){
			for(int i=0;i<taz;i++){
				TrafficAttractedtoaNode[i]=TrafficAttractedtoaNode[i]*(total_production/total_attraction);
			}
		}
		totaltrips=2*total_production;
		//DecimalFormat myFormatter = new DecimalFormat("#######.00");			
		//System.out.print("\tTotal "+ myFormatter.format( (float)total_production/1000)+" thousand trips are produced by "+myFormatter.format(dgraph.juris_info[0][0]/1000) +" thousand households in the seven-county region in the morning peak hour.\n\n");
		//System.out.print(TrafficProducedataNode[189]+"\n");
	}
	
	
	
}