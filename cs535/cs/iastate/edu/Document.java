package pa2.cs535.cs.iastate.edu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
		long minPermValue = 0xFFFFFFFFFFFFFFFFL;
		for(String s: terms){
			hashValue = s.hashCode();
			h = 0xFFFFFFFFL & hashValue;
			permValue = (h*a+b)%modP;
			if(minPermValue > permValue){
				minPermValue = permValue;
			}
		}
		
		return (int)minPermValue;
	}
}
