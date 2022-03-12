import com.sun.jdi.IntegerValue;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Iterator;

public class CengHashTable {


	public static int globalD;
	public ArrayList<CengHashRow> hashRows;
	public CengHashRow hashRow;
	public CengBucket bucket;


	public CengHashTable()
	{
		// TODO: Create a hash table with only 1 row.

		this.hashRows = new ArrayList<CengHashRow>();
		this.globalD = 1;
		CengHashRow hashRow1 = new CengHashRow("0", 1);
		CengHashRow hashRow2 = new CengHashRow("1", 1);

		CengBucket bucket1 = new CengBucket();
		CengBucket bucket2 = new CengBucket();

		hashRow1.bucket = bucket1;
		hashRow2.bucket = bucket2;
		hashRows.add(hashRow1);
		hashRows.add(hashRow2);

	}

	public String HashCreator(Integer pokeKey){
		int hashMode = CengPokeKeeper.getHashMod();
		int hashValue = pokeKey % hashMode;
		int bitNum = (int)(Math.log(hashMode) / Math.log(2));

		String temp = Integer.toBinaryString(hashValue);
		if(bitNum != temp.length()){
			for(int i = 0; i < bitNum - temp.length() +1 ; i++){
				temp = "0" + temp;
			}
		}
		// burda pokeKeylerin binary hash valuelarini yaratirken bir 0 eksik ekliyordu onu duzelttim.
		while(bitNum != temp.length()){
			temp = "0" + temp;
		}
		return temp;
	}

	public void deletePoke(Integer pokeKey)
	{
		// TODO: Empty Implementation
		String pokeHash = HashCreator(pokeKey);
		int count = 0;
		CengHashRow hashRow0 = findTheHashRow(pokeHash.substring(0, globalD), hashRows);
		int size = hashRow0.bucket.pokes.size();
		ArrayList<CengBucket> buckets = new ArrayList<CengBucket>();
		CengPoke deletePoke = null;
		for(int i = 0; i < size; i++){
			if (hashRow0.bucket.pokes.get(i).pokeKey().equals(pokeKey)){

				deletePoke = hashRow0.bucket.pokes.get(i);

			}
		}
		hashRow0.bucket.pokes.remove(deletePoke);
		hashRow0.bucket.len--;
		//direk row uzerinden saydigim icin ayni bucketti gosteren rowlari tekrar geziyordum. onun onune gectim
		for(int i = 0; i< hashRows.size(); i++){
			if(hashRows.get(i).bucket.isEmpty() && !buckets.contains(hashRows.get(i).bucket)){
				buckets.add(hashRows.get(i).bucket);
			}


		}

		System.out.println("\"delete\": {");
		System.out.println("\t\"emptyBucketNum\": " + buckets.size());
		System.out.println("}");



	}

	public void addPoke(CengPoke poke) {
		// TODO: Empty Implementation




			CengHashRow hashRow0 = findTheHashRow(poke.pokeHash().substring(0, globalD), hashRows);
			CengBucket bucket0 = hashRow0.bucket;
			bucket0.insert(poke);
			CengHashRow hashRow1 = new CengHashRow("0", 0);
			ArrayList<CengPoke> pokes = new ArrayList<CengPoke>();
			ArrayList<CengHashRow> forOne = new ArrayList<CengHashRow>();
			ArrayList<CengPoke> deletePokes = new ArrayList<CengPoke>();
			ArrayList<CengPoke> insertPokes = new ArrayList<CengPoke>();
			int pokeSize = hashRow0.bucket.pokes.size();
			if (bucket0.isOverFlow()) {
				while (bucket0.isOverFlow()) {
					if (bucket0.localD == globalD) {
						//bucket0.localD++;
						ArrayList<CengHashRow> rows = hashRows;
						int size = rows.size();
						for (int i = 0; i < size; i++) {
							forOne.add(arrangeBucketsAndRows(hashRows.get(i)));
						}
						for(int i = 0; i < forOne.size(); i++){
							if(forOne.get(i) != null){
								hashRow1 = forOne.get(i);
							}
						}
						for (int i = 0; i < pokeSize; i++) {
							if (hashRow0.bucket.pokes.get(i).pokeHash().substring(0,globalD).equals(hashRow1.hashPrefix)) {
								insertPokes.add(hashRow0.bucket.pokes.get(i));
								deletePokes.add(hashRow0.bucket.pokes.get(i));
							}
						}
						if(insertPokes.size() == 0) {
							continue;
						}
						for (int i = 0; i < deletePokes.size(); i++) {
							hashRow1.bucket.pokes = pokes;
							hashRow1.bucket.insert(insertPokes.get(i));
							bucket0.pokes.remove(deletePokes.get(i));
							bucket0.len--;
						}
					}
					else if(bucket0.localD < globalD){
						bucket0.localD++;
						hashRow1 = arrangeBucketsAndRows2(hashRows, bucket0);
						for(int i= 0; i < pokeSize; i++){											// alt satirda kiyaslamayi yanlis yapiyormusum onu substring haline getirdim.
																							//.equals(hashRow1.hashPrefix) old version
							if (bucket0.pokes.get(i).pokeHash().substring(0,bucket0.localD).equals(hashRow1.hashPrefix.substring(0, bucket0.localD))) {
								insertPokes.add(bucket0.pokes.get(i));
								deletePokes.add(bucket0.pokes.get(i));
							}
						}
						if(insertPokes.size() == 0){
							continue;
						}

						int deleteInsertSize = deletePokes.size();
						for (int i = 0; i < deleteInsertSize; i++) {
							hashRow1.bucket.pokes = pokes;
							hashRow1.bucket.insert(insertPokes.get(i));
							bucket0.pokes.remove(deletePokes.get(i));
							bucket0.len--;
						}

					}

				}
				insertPokes.clear();
				deletePokes.clear();
			}


	}

	public CengHashRow findTheHashRow(String hashPrefix, ArrayList<CengHashRow> hashRows) {
		CengHashRow hashRow = null;
		for (int i = 0; i < hashRows.size(); i++) {
			if (hashPrefix.equals(hashRows.get(i).hashPrefix)) {
				hashRow = hashRows.get(i);
			}
		}
		return hashRow;
	}

	public CengHashRow arrangeBucketsAndRows(CengHashRow hashRow){


		if(hashRow.bucket.isOverFlow()){
			hashRow.bucket.incrementLocalD();
			globalD++;
			ArrayList<CengPoke> pokes = new ArrayList<CengPoke>();
			CengBucket bucket1 = new CengBucket(hashRow.bucket.localD, 0);
			bucket1.pokes = pokes;
			String prefix = hashRow.hashPrefix;
			hashRow.hashPrefix = hashRow.hashPrefix + "0";
			hashRow.hashToInt *= 2;
			CengHashRow hashRow2 = new CengHashRow(  prefix +"1", globalD);
			hashRow2.bucket = bucket1;
			hashRows.add(hashRow2);

			return hashRow2;

		}
		else{
			String prefix = hashRow.hashPrefix;
			hashRow.hashPrefix = hashRow.hashPrefix+ "0";
			hashRow.hashToInt *= 2;
			CengHashRow hashRow2 = new CengHashRow(prefix + "1", globalD);
			hashRow2.bucket = hashRow.bucket;
			hashRows.add(hashRow2);
			return null;


		}


	}
	public CengHashRow arrangeBucketsAndRows2(ArrayList<CengHashRow> hashRows, CengBucket bucket){
		//bucket.incrementLocalD();
		CengBucket bucket1 = new CengBucket(bucket.localD, 0);
		int count = 0;
		CengHashRow importantHashRow = new CengHashRow("0", 0);
		ArrayList<CengHashRow> sameBucketRows = new ArrayList<CengHashRow>();
		for(int i = 0; i < hashRows.size(); i++){
			if(hashRows.get(i).bucket == bucket){
				count++;
				sameBucketRows.add(hashRows.get(i));
			}
		}
		for(int i = 0; i < count/2; i++){
			sameBucketRows.get(i).bucket = bucket;
			sameBucketRows.remove(i);
		}
		for (int i = 0; i < sameBucketRows.size(); i++){
			importantHashRow = sameBucketRows.get(0);
			sameBucketRows.get(i).bucket = bucket1;
		}
		return importantHashRow;
	}


	public void searchPoke(Integer pokeKey) {
		// TODO: Empty Implementation
		String pokeHash = HashCreator(pokeKey);
		ArrayList<CengHashRow> rows = new ArrayList<CengHashRow>();
		CengHashRow hashRow;
		for (int i = 0; i < hashRows.size(); i++) {
			for(int j = 0; j < hashRows.get(i).bucket.len; j++){
				if(pokeKey.equals(hashRows.get(i).bucket.pokes.get(j).pokeKey())){
					rows.add(hashRows.get(i));
				}
			}

		}
		System.out.println("\"search\": {");
		for (int i = 0; i < rows.size(); i++) {
			if(i == rows.size()-1){
				System.out.println("\t\"row\": {");
				System.out.println("\t\t\"hashPref\": " + rows.get(i).hashPrefix + ",");
				System.out.println("\t\t\"bucket\": {");
				System.out.println("\t\t\t\"hashLength\": " + rows.get(i).bucket.localD + ",");
				System.out.println("\t\t\t\"pokes\": [");
				if (!rows.get(i).bucket.isEmpty()) {
					for (int j = 0; j < rows.get(i).bucket.pokes.size(); j++) {
						if(j == rows.get(i).bucket.pokes.size() - 1){
							System.out.println("\t\t\t\t\"poke\": {");
							System.out.println("\t\t\t\t\t\"hash\": " + rows.get(i).bucket.pokes.get(j).pokeHash() + ",");
							System.out.println("\t\t\t\t\t\"pokeKey\": " + rows.get(i).bucket.pokes.get(j).pokeKey() + ",");
							System.out.println("\t\t\t\t\t\"pokeName\": " + rows.get(i).bucket.pokes.get(j).pokeName() + ",");
							System.out.println("\t\t\t\t\t\"pokePower\": " + rows.get(i).bucket.pokes.get(j).pokePower() + ",");
							System.out.println("\t\t\t\t\t\"pokeType\": " + rows.get(i).bucket.pokes.get(j).pokeType());
							System.out.println("\t\t\t\t}");
						}
						else{
							System.out.println("\t\t\t\t\"poke\": {");
							System.out.println("\t\t\t\t\t\"hash\": " + rows.get(i).bucket.pokes.get(j).pokeHash() + ",");
							System.out.println("\t\t\t\t\t\"pokeKey\": " + rows.get(i).bucket.pokes.get(j).pokeKey() + ",");
							System.out.println("\t\t\t\t\t\"pokeName\": " + rows.get(i).bucket.pokes.get(j).pokeName() + ",");
							System.out.println("\t\t\t\t\t\"pokePower\": " + rows.get(i).bucket.pokes.get(j).pokePower() + ",");
							System.out.println("\t\t\t\t\t\"pokeType\": " + rows.get(i).bucket.pokes.get(j).pokeType());
							System.out.println("\t\t\t\t},");
						}





					}
				}
				System.out.println("\t\t\t]");
				System.out.println("\t\t}");
				System.out.println("\t}");
			}
			else{
				System.out.println("\t\"row\": {");
				System.out.println("\t\t\"hashPref\": " + rows.get(i).hashPrefix + ",");
				System.out.println("\t\t\"bucket\": {");
				System.out.println("\t\t\t\"hashLength\": " + rows.get(i).bucket.localD + ",");
				System.out.println("\t\t\t\"pokes\": [");
				if (!rows.get(i).bucket.isEmpty()) {
					for (int j = 0; j < rows.get(i).bucket.pokes.size(); j++) {
						if(j == rows.get(i).bucket.pokes.size() - 1){
							System.out.println("\t\t\t\t\"poke\": {");
							System.out.println("\t\t\t\t\t\"hash\": " + rows.get(i).bucket.pokes.get(j).pokeHash() + ",");
							System.out.println("\t\t\t\t\t\"pokeKey\": " + rows.get(i).bucket.pokes.get(j).pokeKey() + ",");
							System.out.println("\t\t\t\t\t\"pokeName\": " + rows.get(i).bucket.pokes.get(j).pokeName() + ",");
							System.out.println("\t\t\t\t\t\"pokePower\": " + rows.get(i).bucket.pokes.get(j).pokePower() + ",");
							System.out.println("\t\t\t\t\t\"pokeType\": " + rows.get(i).bucket.pokes.get(j).pokeType());
							System.out.println("\t\t\t\t}");
						}
						else{
							System.out.println("\t\t\t\t\"poke\": {");
							System.out.println("\t\t\t\t\t\"hash\": " + rows.get(i).bucket.pokes.get(j).pokeHash() + ",");
							System.out.println("\t\t\t\t\t\"pokeKey\": " + rows.get(i).bucket.pokes.get(j).pokeKey() + ",");
							System.out.println("\t\t\t\t\t\"pokeName\": " + rows.get(i).bucket.pokes.get(j).pokeName() + ",");
							System.out.println("\t\t\t\t\t\"pokePower\": " + rows.get(i).bucket.pokes.get(j).pokePower() + ",");
							System.out.println("\t\t\t\t\t\"pokeType\": " + rows.get(i).bucket.pokes.get(j).pokeType());
							System.out.println("\t\t\t\t},");
						}





					}

				}
				System.out.println("\t\t\t]");
				System.out.println("\t\t}");
				System.out.println("\t},");
			}


		}
		System.out.println("}");

	}

	public ArrayList<CengHashRow> hashRowSort(ArrayList<CengHashRow> hashRows){

		int size = hashRows.size();
		ArrayList<CengHashRow> sortedHashRows = new ArrayList<CengHashRow>();
		for(int i = 0; i < size; i++){
			sortedHashRows.add(null);
		}

		for(int i = 0; i < hashRows.size(); i++){
			sortedHashRows.set(hashRows.get(i).hashToInt, hashRows.get(i));
		}
		return sortedHashRows;
	}
	public void print()
	{


		// TODO: Empty Implementation
		int pokeCount = 0;
		ArrayList<CengPoke> pokeArray = new ArrayList<CengPoke>();
		for(int i = 0; i < hashRows.size(); i++){
			if(!hashRows.get(i).bucket.isEmpty()){
				pokeCount += hashRows.get(i).bucket.len;

			}
		}
		if(hashRows.size() == 2 && hashRows.get(0).bucket.pokes.size() == 0 && hashRows.get(1).bucket.pokes.size() == 0){
			System.out.println("\"table\": {");
			System.out.println("\t\"row\": {");
			System.out.println("\t\t\"hashPref\": 0," );
			System.out.println("\t\t\"bucket\": {" );
			System.out.println("\t\t\t\"hashLength\": 0," );
			System.out.println("\t\t\t\"pokes\": [" );
			System.out.println("\t\t\t]");
			System.out.println("\t\t}" );
			System.out.println("\t}");
			System.out.println("}");

		}

		else if(hashRows.size() == 2 && pokeCount <= hashRows.get(0).bucket.bucketSize &&  0 < pokeCount){

			for(int i = 0; i < hashRows.size(); i++){
				if(!hashRows.get(i).bucket.isEmpty()){
					for(int j = 0; j < hashRows.get(i).bucket.pokes.size(); j++){
						pokeArray.add(hashRows.get(i).bucket.pokes.get(j));
					}
				}
			}
			System.out.println("\"table\": {");
			System.out.println("\t\"row\": {");
			System.out.println("\t\t\"hashPref\": 0," );
			System.out.println("\t\t\"bucket\": {" );
			System.out.println("\t\t\t\"hashLength\": 0," );
			System.out.println("\t\t\t\"pokes\": [" );
			for(int i = 0; i < pokeArray.size(); i++){
				if(i == pokeArray.size() - 1){
					System.out.println("\t\t\t\t\"poke\": {" );
					System.out.println("\t\t\t\t\t\"hash\": " + pokeArray.get(i).pokeHash() + ",");
					System.out.println("\t\t\t\t\t\"pokeKey\": " + pokeArray.get(i).pokeKey() + ",");
					System.out.println("\t\t\t\t\t\"pokeName\": " + pokeArray.get(i).pokeName() + ",");
					System.out.println("\t\t\t\t\t\"pokePower\": " + pokeArray.get(i).pokePower() + ",");
					System.out.println("\t\t\t\t\t\"pokeType\": " + pokeArray.get(i).pokeType());
					System.out.println("\t\t\t\t}");
					System.out.println("\t\t\t]");
				}else{
					System.out.println("\t\t\t\t\"poke\": {" );
					System.out.println("\t\t\t\t\t\"hash\": " + pokeArray.get(i).pokeHash() + ",");
					System.out.println("\t\t\t\t\t\"pokeKey\": " + pokeArray.get(i).pokeKey() + ",");
					System.out.println("\t\t\t\t\t\"pokeName\": " + pokeArray.get(i).pokeName() + ",");
					System.out.println("\t\t\t\t\t\"pokePower\": " + pokeArray.get(i).pokePower() + ",");
					System.out.println("\t\t\t\t\t\"pokeType\": " + pokeArray.get(i).pokeType());
					System.out.println("\t\t\t\t},");
				}


			}

			System.out.println("\t\t}" );
			System.out.println("\t}");
			System.out.println("}");

		}
		else {
			ArrayList<CengHashRow> sortedHashRows = new ArrayList<CengHashRow>();
			sortedHashRows = hashRowSort(hashRows);
			System.out.println("\"table\": {");


			for (int i = 0; i < sortedHashRows.size(); i++) {
				if(i == sortedHashRows.size()-1){

					System.out.println("\t\"row\": {");
					System.out.println("\t\t\"hashPref\": " + sortedHashRows.get(i).hashPrefix + ",");
					System.out.println("\t\t\"bucket\": {");
					System.out.println("\t\t\t\"hashLength\": " + sortedHashRows.get(i).bucket.localD + ",");
					System.out.println("\t\t\t\"pokes\": [");
					if (!sortedHashRows.get(i).bucket.isEmpty()) {
						for (int j = 0; j < sortedHashRows.get(i).bucket.pokes.size(); j++) {
							if(j == sortedHashRows.get(i).bucket.pokes.size() - 1){
								System.out.println("\t\t\t\t\"poke\": {");
								System.out.println("\t\t\t\t\t\"hash\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeHash() + ",");
								System.out.println("\t\t\t\t\t\"pokeKey\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeKey() + ",");
								System.out.println("\t\t\t\t\t\"pokeName\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeName() + ",");
								System.out.println("\t\t\t\t\t\"pokePower\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokePower() + ",");
								System.out.println("\t\t\t\t\t\"pokeType\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeType());
								System.out.println("\t\t\t\t}");


							}
							else{
								System.out.println("\t\t\t\t\"poke\": {");
								System.out.println("\t\t\t\t\t\"hash\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeHash() + ",");
								System.out.println("\t\t\t\t\t\"pokeKey\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeKey() + ",");
								System.out.println("\t\t\t\t\t\"pokeName\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeName() + ",");
								System.out.println("\t\t\t\t\t\"pokePower\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokePower() + ",");
								System.out.println("\t\t\t\t\t\"pokeType\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeType());
								System.out.println("\t\t\t\t},");

							}

						}
					}
					System.out.println("\t\t\t]");
					System.out.println("\t\t}");
					System.out.println("\t}");
				}
				else{

					System.out.println("\t\"row\": {");
					System.out.println("\t\t\"hashPref\": " + sortedHashRows.get(i).hashPrefix + ",");
					System.out.println("\t\t\"bucket\": {");
					System.out.println("\t\t\t\"hashLength\": " + sortedHashRows.get(i).bucket.localD + ",");
					System.out.println("\t\t\t\"pokes\": [");
					if (!sortedHashRows.get(i).bucket.isEmpty()) {
						for (int j = 0; j < sortedHashRows.get(i).bucket.pokes.size(); j++) {
							if(j == sortedHashRows.get(i).bucket.pokes.size() - 1){
								System.out.println("\t\t\t\t\"poke\": {");
								System.out.println("\t\t\t\t\t\"hash\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeHash() + ",");
								System.out.println("\t\t\t\t\t\"pokeKey\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeKey() + ",");
								System.out.println("\t\t\t\t\t\"pokeName\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeName() + ",");
								System.out.println("\t\t\t\t\t\"pokePower\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokePower() + ",");
								System.out.println("\t\t\t\t\t\"pokeType\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeType());
								System.out.println("\t\t\t\t}");//burasiiii




							}
							else{
								System.out.println("\t\t\t\t\"poke\": {");
								System.out.println("\t\t\t\t\t\"hash\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeHash() + ",");
								System.out.println("\t\t\t\t\t\"pokeKey\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeKey() + ",");
								System.out.println("\t\t\t\t\t\"pokeName\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeName() + ",");
								System.out.println("\t\t\t\t\t\"pokePower\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokePower() + ",");

								System.out.println("\t\t\t\t\t\"pokeType\": " + sortedHashRows.get(i).bucket.pokes.get(j).pokeType());
								System.out.println("\t\t\t\t},");


							}

						}
					}
					System.out.println("\t\t\t]");
					System.out.println("\t\t}");
					System.out.println("\t},");
				}


			}
			System.out.println("}");

		}
	}

	// GUI-Based Methods
	// These methods are required by GUI to work properly.
	
	public int prefixBitCount()
	{
		// TODO: Return table's hash prefix length.
		return globalD;
	}
	
	public int rowCount()
	{
		// TODO: Return the count of HashRows in table.
		return hashRows.size();
	}
	
	public CengHashRow rowAtIndex(int index)
	{
		// TODO: Return corresponding hashRow at index.
		return hashRows.get(index);
	}
	
	// Own Methods
}
