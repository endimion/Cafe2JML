package model;


public class ObsValPair{
	CafeTerm obs;
	CafeTerm val;
	
	public ObsValPair(CafeTerm observer, CafeTerm value){
		this.obs = observer;
		this.val = value;
	}
	public CafeTerm getObs(){return obs;}
	public CafeTerm getValue(){return val;}

	
	
	

}//end of ObsValPair
