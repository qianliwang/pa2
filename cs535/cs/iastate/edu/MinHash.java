package pa2.cs535.cs.iastate.edu;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeSet;

public class MinHash {
	
	private String folderPath;
	private int numPermutations;
	private TreeSet<String> allTerms;
	private ArrayList<Document> allDocuments;
	private int numTerms;
	private int modP;
	private ArrayList<ParameterPair> paraList;
	private int minHashMatrix[][];
	
	private int kPermutations[][];
	
	private HashMap<String,Integer> allTermsHashMap;
	
 	public MinHash(String folderPath, int numPermutations){
		this.folderPath = folderPath;
		this.numPermutations = numPermutations;
		this.allDocuments = new ArrayList<Document>();
		this.allTerms = new TreeSet<String>();
		this.paraList = new ArrayList<ParameterPair>();
		this.allTermsHashMap = new HashMap<String,Integer>();
		addDocuments();
		
		this.numTerms = this.allTerms.size();
		this.minHashMatrix = new int[this.numPermutations][this.allDocuments.size()];
		this.kPermutations = new int[this.numTerms+1][this.numPermutations];
		
		this.modP = this.numTerms*20;
		while(!isPrime(this.modP)){
			this.modP++;
		}
		
		Random randomGenerator = new SecureRandom();
		int tempA;
		int tempB;
		for(int i=0;i<numPermutations;i++){
			tempA = randomGenerator.nextInt(this.numTerms-1);
			tempB = randomGenerator.nextInt(this.numTerms-1);
			paraList.add(new ParameterPair(tempA,tempB));
		}
		
		int count = 1;
		for(String s:this.allTerms){
			this.allTermsHashMap.put(s,count);
			count++;
		}
		
		
		genKPermutations(randomGenerator);
		calcMinHashSigs();
		
		System.out.println("Finish adding documents");

		System.out.println("There are totally "+this.allTerms.size()+" terms under the folder.");
		System.out.println("There are "+this.numPermutations+" permutations.");
		
	}
	
	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public ArrayList<Document> getAllDocuments() {
		return allDocuments;
	}

	public void setAllDocuments(ArrayList<Document> allDocuments) {
		this.allDocuments = allDocuments;
	}

	public int getModP() {
		return modP;
	}

	public void setModP(int modP) {
		this.modP = modP;
	}

	public ArrayList<ParameterPair> getParaList() {
		return paraList;
	}

	public void setParaList(ArrayList<ParameterPair> paraList) {
		this.paraList = paraList;
	}

	public void setAllTerms(TreeSet<String> allTerms) {
		this.allTerms = allTerms;
	}

	public HashMap<String, Integer> getAllTermsHashMap() {
		return allTermsHashMap;
	}

	public void setAllTermsHashMap(HashMap<String, Integer> allTermsHashMap) {
		this.allTermsHashMap = allTermsHashMap;
	}

	public ArrayList<Integer> minHashSig(String filePath){
		Document d = new Document(filePath);
		for(ParameterPair pp: paraList){
			d.getMinHashs().add(d.calcMinHash(pp.getA(), pp.getB(), this.modP));
		}
		return d.getMinHashs();
	}
/*	
	public float exactJaccard(String file1,String file2){
		Document d1 = new Document(file1);
		Document d2 = new Document(file2);
		
		float d1Size = d1.getTerms().size();
		float totalNum = d1Size + d2.getTerms().size();
		
		d1.getTerms().removeAll(d2.getTerms());
		float intersectionSize = d1Size - d1.getTerms().size();
		
		return intersectionSize/(totalNum-intersectionSize);
		
	}
	public float approximateJaccard(String file1,String file2){
		Document d1 = new Document(file1);
		Document d2 = new Document(file2);
		
		int tempMinD1;
		int tempMinD2;
		float intersectionSize = 0;
		for(ParameterPair pp: paraList){
			tempMinD1 = d1.calcMinHash(pp.getA(), pp.getB(), this.modP);
			d1.getMinHashs().add(tempMinD1);
			tempMinD2 = d2.calcMinHash(pp.getA(), pp.getB(), this.modP);
			d2.getMinHashs().add(tempMinD2);
			
			if(tempMinD1 == tempMinD2){
				intersectionSize++;
			}
		}
		return intersectionSize/this.numPermutations;
		
	}
	
*/	
	public ArrayList<String> allDocs(){
		ArrayList<String> fileNameList = new ArrayList<String>();
		for(Document d:allDocuments){
			fileNameList.add(d.getFileName());
		}
		return fileNameList;
	}
	
	public int numTerms(){
		return this.numTerms;
	}
	public int numPermutations(){
		return this.numPermutations;
	}
	
	public int[][] minHashMatrix(){
		Document d;
		for(int i=0;i<this.allDocuments.size();i++){
			d = this.allDocuments.get(i);
			for(int j=0;j<this.numPermutations;j++){
				minHashMatrix[j][i] = d.getMinHashs().get(j);
			}
		}
		return this.minHashMatrix;
	}
	
	private void genKPermutations(Random random){
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		
		for(int m = 0;m<this.numPermutations;m++){
			for(int i=1;i<=this.numTerms;i++){
				list1.add(i);
			}
			int temp = 0;
			int count = 1;
			while(list1.size()!=0){
				temp = random.nextInt(list1.size());
				this.kPermutations[count][m] = list1.get(temp);
				count++;
				list1.remove(temp);
			}
		}
		list1 = null;
	}
	
	private void addDocuments(){
		File folder = new File(this.folderPath);	
		File[] listOfFiles = folder.listFiles();
		System.out.println(this.folderPath+" contains "+listOfFiles.length+" files.\n");
		Document d;
		for(int i=0;i<listOfFiles.length;i++){
//			System.out.println(listOfFiles[i].getAbsolutePath());
			d = new Document(listOfFiles[i].getAbsolutePath());	
			this.allDocuments.add(d);
			for(String s:d.getTerms()){
				this.allTerms.add(s);
			}
		}
	}
	
/*	private void getAllTerms(){
		for(Document d:allDocuments){
			for(String s:d.getTerms()){
				this.allTerms.add(s);
			}
		}
	}*/
	
/*	
	private void calcMinHashSigs(){
		int temp;
		for(Document d:allDocuments){
			for(ParameterPair pp:paraList){
				temp = d.calcMinHash(pp.getA(), pp.getB(), this.modP,this.allTermsHashMap);
				if(temp<0){
					System.err.println(temp);
				}
				d.getMinHashs().add(temp);
			}
		}
		System.out.println();
	}
	*/
	
	private void calcMinHashSigs(){
		int temp;
		int tempPerm[] = new int [this.numTerms+1];
		for(Document d:allDocuments){
			
			for(int j = 0;j<this.numPermutations;j++){
				for(int i=1;i<=this.numTerms;i++){
					tempPerm[i] = this.kPermutations[i][j];
				}
				temp = d.calcMinHash(tempPerm,this.allTermsHashMap);
				d.getMinHashs().add(temp);
			}
		}
		System.out.println();
	}
	
	protected boolean isPrime(int n) {
	    //check if n is a multiple of 2
	    if (n%2==0) return false;
	    //if not, then just check the odds
	    for(int i=3;i*i<=n;i+=2) {
	        if(n%i==0)
	            return false;
	    }
	    return true;
	}
	@Override
	public String toString(){
		String result = this.folderPath+" contains "+this.allDocuments.size()+" documents and "+this.allTerms.size()+" terms totally.\n";
		return result;
	}
}
