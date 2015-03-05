package pa2.cs535.cs.iastate.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document {

	private TreeSet<String> terms;
	private String filePath;
	private ArrayList<Integer> minHashs;
	
	public Document(String filePath){
		this.terms = new TreeSet<String>();
		this.filePath = filePath;
		preProcessing();
		this.minHashs = new ArrayList<Integer>();
	}
	
		
	public TreeSet<String> getTerms() {
		return terms;
	}

	public void setTerms(TreeSet<String> terms) {
		this.terms = terms;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public ArrayList<Integer> getMinHashs() {
		return minHashs;
	}
	public void setMinHashs(ArrayList<Integer> minHashs) {
		this.minHashs = minHashs;
	}

	public String getFileName(){
		File f = new File(this.filePath);
		String fName = f.getName();
		f = null;
		return fName;
	}
	
	public float exactJaccard(Document d2){
		
		float d1Size = this.terms.size();
		float d2Size = d2.getTerms().size();
		float totalNum = d1Size + d2Size;
		
		TreeSet<String> tempD1 = (TreeSet)this.terms.clone();
		
		tempD1.removeAll(d2.getTerms());
		float intersectionSize = d1Size - tempD1.size();
		tempD1 = null;
		return intersectionSize/(totalNum-intersectionSize);
		
	}
	
	public float approximateJaccard(Document d2){
		float intersectionSize = 0;
		float result = 0;
		for(int i=0; i<this.minHashs.size();i++){
			if(this.minHashs.get(i) == d2.getMinHashs().get(i)){
				intersectionSize++;
			}
		}
		result = intersectionSize/this.minHashs.size();
		return result;
	}
/*	
	public float approximateJaccard(Document d2, ArrayList<ParameterPair> paraList, int modP){
		
		int tempMinD1;
		int tempMinD2;
		float intersectionSize = 0;
		for(ParameterPair pp: paraList){
			tempMinD1 = this.calcMinHash(pp.getA(), pp.getB(), modP);
			tempMinD2 = d2.calcMinHash(pp.getA(), pp.getB(), modP);
			
			if(tempMinD1 == tempMinD2){
				intersectionSize++;
			}
		}
//		if(intersectionSize>0){
//		System.out.println(intersectionSize);
//		}
		return intersectionSize/paraList.size();
		
	}
	public float approximateJaccard(Document d2, ArrayList<ParameterPair> paraList, int modP,HashMap<String,Integer> hm){
		
		int tempMinD1;
		int tempMinD2;
		float intersectionSize = 0;
		for(ParameterPair pp: paraList){
			int a = this.terms.size();
			tempMinD1 = calcMinHash(pp.getA(), pp.getB(), modP,hm);
			tempMinD2 = d2.calcMinHash(pp.getA(), pp.getB(), modP,hm);
			
			if(tempMinD1 == tempMinD2){
				intersectionSize++;
			}
		}
//		if(intersectionSize>0){
//		System.out.println(intersectionSize);
//		}
		return intersectionSize/paraList.size();
		
	}
*/	
	private void preProcessing(){
		FileInputStream fstream;
		BufferedReader br = null;
		try {
			fstream = new FileInputStream(this.filePath);
			br = new BufferedReader(new InputStreamReader(fstream));

			String strLine = null;
			
			Pattern pattern = Pattern.compile("([A-Za-z]{3,})");
			Matcher matcher;
			String tempString;
			while ((strLine = br.readLine()) != null) {
				matcher = pattern.matcher(strLine);
				while(matcher.find()){
					tempString = matcher.group();
					if(!tempString.matches("(?i)the")){
						this.terms.add(tempString.toLowerCase());
					}
					
//					System.out.println(tempString);
				}
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public int calcMinHash(int a,int b, int modP){
		int hashValue;
		long h;
		long permValue;
		long minPermValue = 0x0FFFFFFFFFFFFFFFL;
		int tempModP = modP/10;
		for(String s: this.terms){
			hashValue = s.hashCode()%tempModP;
			h = 0xFFFFFFFFL & hashValue;
			permValue = (h*a+b)%modP;
			if(minPermValue > permValue){
				minPermValue = permValue;
			}
		}
		
		return (int)minPermValue;
	}
	public int calcMinHash(int a,int b, int modP,HashMap<String,Integer> hm){
		int hashValue;
		int permValue;
		int minPermValue = modP;
		for(String s: this.terms){
			hashValue = hm.get(s);
			permValue = (hashValue*a+b)%modP;
			if(minPermValue > permValue){
				minPermValue = permValue;
			}
		}
		
		return minPermValue;
	}
}
