import java.util.ArrayList;

public class CengBucket {

	// GUI-Based Methods
	// These methods are required by GUI to work properly.
	public ArrayList<CengPoke> pokes;
	public int localD;
	public int bucketSize;
	public int bigBucketSize;
	public int len;


	public  CengBucket(){
		this.localD = 1;
		this.bucketSize = CengPokeKeeper.getBucketSize();
		this.bigBucketSize = bucketSize + 1;
		this.pokes = new ArrayList<CengPoke>();
		this.len = 0;


	}
	public CengBucket(int localD, int len){
		this.localD = localD;
		this.len = len;
		this.pokes = new ArrayList<CengPoke>();
		this.bucketSize = CengPokeKeeper.getBucketSize();
		this.bigBucketSize = bucketSize + 1;
	}

	public int incrementLocalD(){
		return localD++;
	}

	public void insert(CengPoke poke){

		pokes.add(poke);
			this.len++;

	}

	public void delete(Integer pokeKey){
		for(int i = 0; i < len; i++){
			if(pokes.get(i).pokeKey() == pokeKey){
				pokes.remove(pokes.get(i));
			}
		}
	}

	public boolean isInTheBucket(Integer pokeKey){
		int status;
		for(int i = 0;i<bucketSize;i++){
			if(this.pokes.get(i) ==null) {
				return false;
			}
			status = pokeKey.compareTo(this.pokes.get(i).pokeKey());
			if(status == 0){
				return true;
			}
		}
		return false;
	}

	public boolean isOverFlow()
	{
		if(pokes.size() == bigBucketSize)
		{
			return true;
		}

		else{
			return false;
		}
	}
	public boolean isFull()
	{
		if(pokes.size() == bucketSize)
		{
			return true;
		}

		else{
			return false;
		}
	}
	public boolean isEmpty()
	{
		if(pokes.size()==0)
		{
			return true;
		}else
		{
			return false;
		}
	}

	public int pokeCount()
	{
		// TODO: Return the pokemon count in the bucket.

		return pokes.size();
		//return 0;
	}
	
	public CengPoke pokeAtIndex(int index)
	{
		// TODO: Return the corresponding pokemon at the index.
		if(index >= this.pokeCount()){
			return null;
		}
		else{
			CengPoke poke = this.pokes.get(index);
			return poke;
		}
		//return null;

	}

	public int getHashPrefix()
	{
		// TODO: Return hash prefix length.
		//return 0;
		return localD;
	}
	
	public Boolean isVisited()
	{
		// TODO: Return whether the bucket is found while searching.
		return false;		
	}
	
	// Own Methods
}
