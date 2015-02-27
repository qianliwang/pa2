package pa2.cs535.cs.iastate.edu;

public class MinHashSpeed {
	
	public static void main(String[] args){
		String folderPath = null;
		int numPermutation = 0;
		float e = 0;
		
		MinHash mh= new MinHash(folderPath,numPermutation);
		
		Document tempD;
		float exactJacSimilarity = 0;
		float approJacSimilarity = 0;
		
		long timeStart;
		long timeEnd;
		
		timeStart = System.currentTimeMillis();
		for(int i=0;i<mh.getAllDocuments().size();i++){
			tempD = mh.getAllDocuments().get(i);
			for(int j=i+1;j<mh.getAllDocuments().size();j++){
				exactJacSimilarity = tempD.exactJaccard(mh.getAllDocuments().get(j));
			}
		}
		timeEnd = System.currentTimeMillis();
		
		System.out.println(mh.getAllDocuments().size()+" files takes "+(timeEnd-timeStart)+ "milliseconds to calculate the exact Jaccard similarity.");
		System.out.println("------------------------------------------------");
		
		timeStart = System.currentTimeMillis();
		
		for(int i=0;i<mh.getAllDocuments().size();i++){
			tempD = mh.getAllDocuments().get(i);
			for(int j=i+1;j<mh.getAllDocuments().size();j++){
				approJacSimilarity = tempD.approximateJaccard(mh.getAllDocuments().get(j), mh.getParaList(), mh.getModP());
				
			}
		}
		
		timeEnd = System.currentTimeMillis();
		System.out.println(mh.getAllDocuments().size()+" files takes "+(timeEnd-timeStart)+ "milliseconds to calculate the approximate Jaccard similarity.");
		}
}
