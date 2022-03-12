import java.util.ArrayList;

public class CengHashRow {

	// GUI-Based Methods
	// These methods are required by GUI to work properly.
	public CengBucket bucket;
	public int globalD;
	public String hashPrefix;
	public ArrayList<Integer> hashInteger;
	public int hashToInt;

	public CengHashRow(String hashPrefix, int globalD){
		this.bucket = new CengBucket();
		this.globalD = globalD;
		this.hashPrefix = hashPrefix;
		this.hashToInt = Integer.parseInt(hashPrefix, 2);
	}


	public String hashPrefix()
	{
		// TODO: Return row's hash prefix (such as 0, 01, 010, ...)
		return hashPrefix;
	}
	
	public CengBucket getBucket()
	{
		// TODO: Return the bucket that the row points at.
		return bucket;
	}
	
	public boolean isVisited()
	{
		// TODO: Return whether the row is used while searching.
		return false;		
	}
	
	// Own Methods
}
