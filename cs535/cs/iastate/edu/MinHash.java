package pa2.cs535.cs.iastate.edu;

import java.io.File;
import java.util.ArrayList;
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
	
	
 	public MinHash(String folderPath, int numPermutations){
		this.folderPath = folderPath;
		this.numPermutations = numPermutations;
		this.allDocuments = new ArrayList<Document>();
		this.allTerms = new TreeSet<String>();
		this.paraList = new ArrayList<ParameterPair>();
		addDocuments();
		System.out.println("Finish adding documents");
		getAllTerms();
		System.out.println("There are totally "+this.allTerms.size()+" terms under the folder.");
		System.out.println("There are "+this.numPermutations+" permutations.");
		
		this.minHashMatrix = new int[this.numPermutations][this.allDocuments.size()];
		
		this.numTerms = this.allTerms.size();
		this.modP = this.numTerms*10;
		while(!isPrime(this.modP)){
			this.modP++;
		}
		
		Random randomGenerator = new Random();
		int tempA;
		int tempB;
		for(int i=0;i<numPermutations;i++){
			tempA = randomGenerator.nextInt(this.numTerms-1);
			tempB = randomGenerator.nextInt(this.numTerms-1);
			paraList.add(new ParameterPair(tempA,tempB));
		}
		
		
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

	public ArrayList<Integer> minHashSig(String filePath){
		Document d = new Document(filePath);
		for(ParameterPair pp: paraList){
			d.getMinHashs().add(d.calcMinHash(pp.getA(), pp.getB(), this.modP));
		}
		return d.getMinHashs();
	}
	
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
			d.setMinHashs(minHashSig(d.getFilePath()));
			for(int j=0;j<this.numPermutations;j++){
				minHashMatrix[j][i] = d.getMinHashs().get(j);
			}
		}
		return this.minHashMatrix;
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
		}
	}
	private void getAllTerms(){
		for(Document d:allDocuments){
			for(String s:d.getTerms()){
				this.allTerms.add(s);
			}
		}
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
