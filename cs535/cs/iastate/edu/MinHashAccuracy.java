package pa2.cs535.cs.iastate.edu;

public class MinHashAccuracy {

	public static void main(String[] args){
		String folderPath = null;
		folderPath = "/Users/watson/Code/workspace/cs535/space/";
		int numPermutation = 400;
		float e = (float) 0.09;
		
		MinHash mh= new MinHash(folderPath,numPermutation);
		
		Document tempD;
		float exactJacSimilarity;
		float approJacSimilarity;
		
		int count = 0;
		
		for(int i=0;i<mh.getAllDocuments().size();i++){
			tempD = mh.getAllDocuments().get(i);
			for(int j=i+1;j<mh.getAllDocuments().size();j++){
//				System.out.println(j);
				exactJacSimilarity = tempD.exactJaccard(mh.getAllDocuments().get(j));
				approJacSimilarity = tempD.approximateJaccard(mh.getAllDocuments().get(j), mh.getParaList(), mh.getModP());
				if(Math.abs(exactJacSimilarity-approJacSimilarity)>e){
					count++;
					System.err.println(exactJacSimilarity+"<--->"+approJacSimilarity);
					System.err.println(mh.getAllDocuments().get(i).getFileName()+"<--->"+mh.getAllDocuments().get(j).getFileName());
				}
			}
		}
		
		System.out.println("There are "+count+" pairs for which exact and approximate similarities differ by more than "+ e);
	}
}
