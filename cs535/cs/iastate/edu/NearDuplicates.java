package pa2.cs535.cs.iastate.edu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeSet;

public class NearDuplicates {

	public static void main(String args[]){
		
		String folderPath = null;
		int numPermutations = 0;
		int tempMinHashMatrix[][];
		float s = 0;
		int bands;
		int r = 1;
		String testFile = null;
		
		MinHash minHash;
		ArrayList<String> fileNames;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Please input the folder path:");
//			folderPath = br.readLine();
			folderPath = "/Users/watson/Code/workspace/cs535/pa2/";
			System.out.println("Please input the number of permutations:");
//			numPermutations = Integer.parseInt(br.readLine());
			numPermutations = 400;
			System.out.println("Building MinHash...");
			minHash = new MinHash(folderPath,numPermutations);
			System.out.println("Please input the similarity rate, for example, 0.8");
//			s = Float.parseFloat(br.readLine());
			s = (float) 0.8;
			
			int tempResult = (int) Math.ceil((1/Math.pow(s, r))*r);
			while(numPermutations > tempResult && r < numPermutations){
				r++;
				tempResult = (int)Math.ceil((1/Math.pow(s, r))*r);
			}
			bands = numPermutations/r;
			
			System.out.println("Given NumOfPermutations "+numPermutations+" and the similarity rate "+s+", the ideal number of bands is "+bands);
			
			System.out.println("Please input the number of bands:");
//			bands = Integer.parseInt(br.readLine());
			bands = 100;
			
			System.out.println("NumOfPermutations "+numPermutations+", the similarity rate "+s+" and the number of bands is "+bands);
			System.out.println("Building LSH:");
			fileNames = new ArrayList<String>();
			for(Document d:minHash.getAllDocuments()){
				fileNames.add(d.getFileName());
			}
			tempMinHashMatrix = minHash.minHashMatrix();
			LSH lsh = new LSH(tempMinHashMatrix,fileNames,bands);
			TreeSet<String> result;
			System.out.println("Finish buidling LSH.");
			boolean isSTOP = false;
			while(!isSTOP){
				System.out.println("Please input a file name in which you want to search for the nearly duplicates:");
				testFile = br.readLine();
				if(!testFile.equals("")){
					result = lsh.nearDuplicatesOf(testFile);
					
					lsh.removeFalsePostive(testFile, result, s);
					
					for(String ss:result){
						System.out.println(ss);
					}
				}else{
					isSTOP = true;
				}
				
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
