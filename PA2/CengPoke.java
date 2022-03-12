public class CengPoke {
	
	private Integer pokeKey;
	private String pokeName;
	private String pokePower;
	private String pokeType;
	//added
	private String pokeHash;
	private int hashMode = CengPokeKeeper.getHashMod();
	private int bitNum = (int)(Math.log(hashMode) / Math.log(2));

	
	public CengPoke(Integer pokeKey, String pokeName, String pokePower, String pokeType)
	{
		this.pokeKey = pokeKey;
		this.pokeName = pokeName;
		this.pokePower = pokePower;
		this.pokeType = pokeType;
		//added
		this.pokeHash = HashCreator();
	}
	
	// Getters
	
	public Integer pokeKey()
	{
		return pokeKey;
	}
	public String pokeName()
	{
		return pokeName;
	}
	public String pokePower()
	{
		return pokePower;
	}
	public String pokeType()
	{
		return pokeType;
	}
	public int bitNum(){ return bitNum; }


	//Added
	public String pokeHash()
	{
		return pokeHash;
	}
		
	// GUI method - do not modify
	public String fullName()
	{
		return "" + pokeKey() + "\t" + pokeName() + "\t" + pokePower() + "\t" + pokeType;
	}
		
	// Own Methods
	public String HashCreator(){
		int hashValue = pokeKey % hashMode;
		String temp = Integer.toBinaryString(hashValue);
		if(bitNum != temp.length()){
			for(int i = 0; i < bitNum - temp.length() +1 ; i++){
				temp = "0" + temp;
			}
		}
		while(bitNum != temp.length()){
			temp = "0" + temp;
		}
		return temp;
	}
}
