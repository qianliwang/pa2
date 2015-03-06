package pa2.cs535.cs.iastate.edu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class LSH {
	private int[][] minHashMatrix;
	private int bands;
	private ArrayList<String> fileNames;
	private ArrayList<HashMap<String,TreeSet<String>>> hashTableList;
	
	public LSH(int[][] minHashMatrix,ArrayList<String> fileNames,int bands){
		this.minHashMatrix = minHashMatrix;
		this.bands = bands;
		this.fileNames = fileNames;
		this.hashTableList = new ArrayList<HashMap<String,TreeSet<String>>>();
		buildLSH();
	}
	
	private void buildLSH(){
		int numPermutations = this.minHashMatrix.length;
		int rows = numPermutations/this.bands;
		
		HashMap<String,TreeSet<String>> tempHashMap;
		StringBuffer tempSB;
		TreeSet<String> tempLinkSet = null;
		for(int i=0;i<this.bands;i++){
			tempHashMap = new HashMap<String,TreeSet<String>>();
			for(int j=0;j<this.fileNames.size();j++){
				tempSB = new StringBuffer();
				for(int r=0;r<rows;r++){
					tempSB.append(this.minHashMatrix[i*rows+r][j]);
					tempSB.append("$");
				}
//				System.out.println(tempSB.toString());
				if(tempHashMap.containsKey(tempSB.toString())){
					tempLinkSet = tempHashMap.get(tempSB.toString());
				}else{
					tempLinkSet = new TreeSet<String>();
				}
				tempLinkSet.add(this.fileNames.get(j));
				tempHashMap.put(tempSB.toString(), tempLinkSet);
			}
			hashTableList.add(tempHashMap);
		}
		
	}
	
	public TreeSet<String> nearDuplicatesOf(String docName){
		TreeSet<String> result = new TreeSet<String>();
		
		int numPermutations = this.minHashMatrix.length;
		int rows = numPermutations/this.bands;
		int currentBand = 0;
		for(HashMap<String,TreeSet<String>> hm: this.hashTableList){
			for(String s:hm.keySet()){
				if(hm.get(s).contains(docName)){
					boolean isFalsePositive;
					int docNameIndex = this.fileNames.indexOf(docName);
					int fileName2Index;
					for(String fileName2:hm.get(s)){
						isFalsePositive = false;
						fileName2Index = this.fileNames.indexOf(fileName2);
						for(int i=0;i<rows;i++){
							if(this.minHashMatrix[currentBand*rows+i][docNameIndex]!=this.minHashMatrix[currentBand*rows+i][fileName2Index]){
								isFalsePositive = true;
								break;
							}
						}
						if(!isFalsePositive){
							result.addAll(hm.get(s));
						}
					}
				}
			}
			currentBand++;
		}
		result.remove(docName);
		return result;
	}
	
}
