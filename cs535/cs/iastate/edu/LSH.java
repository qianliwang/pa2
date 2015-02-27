package pa2.cs535.cs.iastate.edu;

import java.util.ArrayList;
import java.util.HashMap;

public class LSH {
	private int[][] minHashMatrix;
	private int bands;
	private ArrayList<String> fileNames;
	private ArrayList<HashMap<String,ArrayList<String>>> hashTableList;
	
	public LSH(int[][] minHashMatrix,ArrayList<String> fileNames,int bands){
		this.minHashMatrix = minHashMatrix;
		this.bands = bands;
		this.fileNames = fileNames;
		this.hashTableList = new ArrayList<HashMap<String,ArrayList<String>>>();
	}
	
	private void buildLSH(){
		int numPermutations = this.minHashMatrix.length;
		int rows = numPermutations/this.bands;
		
		HashMap<String,ArrayList<String>> tempHashMap;
		StringBuffer tempSB;
		ArrayList<String> tempLinkList = null;
		for(int i=0;i<this.bands;i++){
			tempHashMap = new HashMap<String,ArrayList<String>>();
			for(int j=0;j<this.fileNames.size();j++){
				tempSB = new StringBuffer();
				for(int r=0;r<rows;r++){
					tempSB.append(this.minHashMatrix[i*rows+r][j]);
				}
				if(tempHashMap.containsKey(tempSB.toString())){
					tempLinkList = tempHashMap.get(tempSB.toString());
				}else{
					tempLinkList = new ArrayList<String>();
				}
				tempLinkList.add(String.valueOf(j));
				tempHashMap.put(tempSB.toString(), tempLinkList);
			}
			hashTableList.add(tempHashMap);
		}
		
	}
	
}
