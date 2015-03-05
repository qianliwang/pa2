package pa2.cs535.cs.iastate.edu;

public class MinHashAccuracy {

	public static void main(String[] args){
		String folderPath = null;
		folderPath = "/Users/watson/Code/workspace/cs535/space/";
		int numPermutation = 400;
		float e = (float) 0.09;
		
		MinHash mh= new MinHash(folderPath,numPermutation);
		
		Document tempD1;
		Document tempD2;
		float exactJacSimilarity;
		float approJacSimilarity;
		
		int count = 0;
		int total = 0;
		for(int i=0;i<mh.getAllDocuments().size();i++){
			tempD1 = mh.getAllDocuments().get(i);
			for(int j=i+1;j<mh.getAllDocuments().size();j++){
//				System.out.println(j);
				tempD2 = mh.getAllDocuments().get(j);
				exactJacSimilarity = tempD1.exactJaccard(tempD2);
				approJacSimilarity = tempD1.approximateJaccard(tempD2);
				if(Math.abs(exactJacSimilarity-approJacSimilarity)>e){
					count++;
					System.err.println(exactJacSimilarity+"<--->"+approJacSimilarity);
					System.err.println(tempD1.getFileName()+"<--->"+tempD2.getFileName());
				}else{
					System.out.println(tempD1.getFileName()+":   "+exactJacSimilarity+"<--->"+approJacSimilarity);
				}
				total++;
			}
		}
		
		System.out.println("There are "+count+"/"+total+" pairs for which exact and approximate similarities differ by more than "+ e);
	}
}