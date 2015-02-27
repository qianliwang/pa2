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
	
 	public void MinHash(String folderPath, int numPermutations){
		this.folderPath = folderPath;
		this.numPermutations = numPermutations;
		this.allDocuments = new ArrayList<Document>();
		this.allTerms = new TreeSet<String>();
		this.paraList = new ArrayList<ParameterPair>();
		
		addDocuments();
		getAllTerms();
		
		this.numTerms = this.allTerms.size();
		this.modP = this.numTerms;
		while(!isPrime(this.modP)){
			this.modP++;
		}
		
		Random randomGenerator = new Random();
		int tempA;
		int tempB;
		for(int i=0;i<numPermutations;i++){
			tempA = randomGenerator.nextInt(this.numPermutations-1);
			tempB = randomGenerator.nextInt(this.numPermutations-1);
			paraList.add(new ParameterPair(tempA,tempB));
		}
		
		
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
}
