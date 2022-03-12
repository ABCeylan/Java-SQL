import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class CengPokeParser {

	public static ArrayList<CengPoke> parsePokeFile(String filename)
	{
		ArrayList<CengPoke> pokeList = new ArrayList<CengPoke>();

		// You need to parse the input file in order to use GUI tables.
		// TODO: Parse the input file, and convert them into CengPokes


		ArrayList<String> strings = new ArrayList<>();

		try (Scanner s = new Scanner(new FileReader(filename))) {
			while (s.hasNext()) {
				strings.add(s.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		ArrayList<String[]> data = new ArrayList<>();

		for (int i = 0; i < strings.size(); i++) {
			data.add(strings.get(i).split("\\t"));
			pokeList.add(new CengPoke(Integer.parseInt(data.get(i)[1]), data.get(i)[2], data.get(i)[3], data.get(i)[4]));
		}
		return pokeList;
	}
	
	public static void startParsingCommandLine() throws IOException
	{
		// TODO: Start listening and parsing command line -System.in-.
		// There are 5 commands:
		// 1) quit : End the app. Print nothing, call nothing.
		// 2) add : Parse and create the poke, and call CengPokeKeeper.addPoke(newlyCreatedPoke).
		// 3) search : Parse the pokeKey, and call CengPokeKeeper.searchPoke(parsedKey).
		// 4) delete: Parse the pokeKey, and call CengPokeKeeper.removePoke(parsedKey).
		// 5) print : Print the whole hash table with the corresponding buckets, call CengPokeKeeper.printEverything().

		// Commands (quit, add, search, print) are case-insensitive.
		Scanner sn = new Scanner(System.in);
		String input = sn.nextLine();
		String[] data;
		while(!input.startsWith("quit")){
			if(input.startsWith("add")){
				data = input.split("\\t");
				CengPokeKeeper.addPoke(new CengPoke(Integer.parseInt(data[1]), data[2], data[3], data[4]));
			}
			else if(input.startsWith("search")){
				data = input.split("\\t");
				CengPokeKeeper.searchPoke(Integer.parseInt(data[1]));
			}
			else if(input.startsWith("delete")){
				data = input.split("\\t");
				CengPokeKeeper.deletePoke(Integer.parseInt(data[1]));
			}
			else if(input.startsWith("print")) {
				CengPokeKeeper.printEverything();
			}
			input = sn.nextLine();
		}
	}
}
