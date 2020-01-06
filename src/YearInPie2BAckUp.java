import components.queue.Queue1L;
import components.queue.Queue;
import components.map.Map1L;
import components.map.Map;
import components.map.Map.Pair;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

import components.array.Array1L;

import java.util.Iterator;

import components.array.Array;

/**
 * Takes a txt file, counts how many occurences of each word there is, and outputs the 
 * results in an html page
 *
 * @author Daniel Amar
 *
 */
public final class YearInPie2BAckUp {

	/**
	 * Private constructor so this utility class cannot be instantiated.
	 */
	private YearInPie2BAckUp() {
	}


	/**
	 * Main method.
	 *
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		SimpleReader in = new SimpleReader1L();
		SimpleWriter out = new SimpleWriter1L();

		out.println("Input data file:");
		String inputName = in.nextLine();
		SimpleReader file = new SimpleReader1L(inputName);



		out.println("enter an output file name: ");
		String outputFile = in.nextLine();


		startProgram(file,outputFile);


		in.close();
		out.close();
	}

	public static void startProgram(SimpleReader file, String outputFile) {

		Queue<String> lines = parseToQueue(file); 
		Map<String,String> mappedDays = queueToMap(lines);

		/**
		 * pos0 = great days(green) 
		 * pos1 = good days with no comment (blue)
		 * pos2 = good days with positive comment(blue) 
		 * pos3 = good days with negative comment (blue)
		 * pos4 = low energy, below average, sad days(yellow)
		 * pos5 = bad day, stressed, overwhelmed(Dark Blue)
		 * pos6 = Bad Day, Frustrated, Angry(Red)
		 * pos7 = Bad Day, Sad, Depressed(Purple)
		 * pos8 = Data Not Available
		 */
		Array<Integer> occurances = countOccurances(mappedDays);




	}

	public static void printResults(Array<Integer> occurances,String outputFile) {
		SimpleWriter out = new SimpleWriter1L(outputFile);

		out.println("Great Days: "  + occurances.entry(0));
		out.println("Good Days(No Comment): "  + occurances.entry(1));
		out.println("Good Days(Positive Comment): "  + occurances.entry(2));
		out.println("Good Days(Negative Comment): "  + occurances.entry(3));
		out.println("Low Energy, Below Average, Sad Days: "  + occurances.entry(4));
		out.println("Bad Day, Stressed, Overwhelmed Days: "  + occurances.entry(5));
		out.println("Bad Day, Frustrated, Angry Days: "  + occurances.entry(6));
		out.println("Bad Day, Sad, Depressed Days: "  + occurances.entry(7));
		out.println("Data Not Available: "  + occurances.entry(8));

		out.println();
		out.println("Total Good Days: " + occurances.entry(1) + occurances.entry(2) + occurances.entry(3));

		int totalDays = 0;
		for(int i = 0; i < occurances.length(); i++) {
			totalDays += occurances.entry(i);
		}
		out.println("Total Days Logged: " + totalDays);
	}

	public static Queue<String> parseToQueue(SimpleReader file) {

		Queue<String> words = new Queue1L<String>();

		while(!file.atEOS()) {
			words.enqueue(file.nextLine());
		}

		return words;
	}

	public static Map<String,String> queueToMap(Queue<String> lines){

		Map<String,String> mappedLines = new Map1L<>();

		while(lines.length() > 0) {
			String removedFromQueue = lines.dequeue();

			int indexOfDash = removedFromQueue.indexOf("-"); 
			String key = removedFromQueue.substring(0,indexOfDash); //gets date
			key.replaceAll("\\s", ""); //removes whitespaces

			String value = removedFromQueue.substring(indexOfDash+1); //gets comment 

			mappedLines.add(key, value); //adds date, comment
		}


		return mappedLines;
	}



	public static Array<Integer> countOccurances(Map<String,String> mappedList){

		/**
		 * pos0 = great days(green) 
		 * pos1 = good days with no comment (blue)
		 * pos2 = good days with positive comment(blue) 
		 * pos3 = good days with negative comment (blue)
		 * pos4 = low energy, below average, sad days(yellow)
		 * pos5 = bad day, stressed, overwhelmed(Dark Blue)
		 * pos6 = Bad Day, Frustrated, Angry(Red)
		 * pos7 = Bad Day, Sad, Depressed(Purple)
		 * pos8 = Data Not Available
		 */
		Array<Integer> typesOfDays = new Array1L<>(9);
		Map<String,String> mapOfDaysWithGoodComments = mappedList.newInstance();
		Map<String,String> mapOfDaysWithBadComments = mappedList.newInstance();

		for(int i = 0; i < typesOfDays.length(); i++) {
			typesOfDays.setEntry(i, 0);
		}


		for(Pair<String,String> s : mappedList) {
			String key = s.key();
			String value = s.value();

			value.toLowerCase();
			value.stripLeading(); //removes whitespaces at beginnging

			if(value.length() > 0) {
				if(value.contains("great")) {
					typesOfDays.setEntry(0,typesOfDays.entry(0) + 1);
				}
				else if(value.contains("good")) {

					value.stripTrailing(); //gets rid of white spaces at end of string
					System.out.println(value);
					/**
					 * if length is 4 then there is no comment add to pos 2
					 * else there is a good comment add to pos2, 
					 * and adds to map
					 */
					if(value.length() == 4) {
						mapOfDaysWithGoodComments.add(key,value); 
						//adds good comment to map
						typesOfDays.setEntry(1,typesOfDays.entry(1) + 1); 

					}else {
						typesOfDays.setEntry(2,typesOfDays.entry(2) + 1);
					}

				}
				else if(value.contains("giod")) {
					//good day, negative comment
					typesOfDays.setEntry(3,typesOfDays.entry(3) + 1);
					//adds to negative comment map
					mapOfDaysWithBadComments.add(key,value); 
				}
				else if(value.contains("dark blue")) {
					typesOfDays.setEntry(5,typesOfDays.entry(5) + 1);
				}
				else if(value.contains("blue")) {
					//good day also
					typesOfDays.setEntry(1,typesOfDays.entry(1) + 1);
				}
				else if(value.contains("red")) {
					typesOfDays.setEntry(6,typesOfDays.entry(6) + 1);
				}
				else if(value.contains("green")) {
					typesOfDays.setEntry(0,typesOfDays.entry(0) + 1);
				}
				else if(value.contains("purple")) {
					typesOfDays.setEntry(7,typesOfDays.entry(7) + 1);
				}
				else if(value.contains("N/A")) {
					typesOfDays.setEntry(8,typesOfDays.entry(8) + 1);
				}
			}

		}

		return typesOfDays;
	}

	//	public static Map<String,String> getDaysWithNegativeComment(Map<String,String> mappedList){
	//
	//		Map<String, String> temp = mappedList.newInstance();
	//
	//
	//
	//		while(mappedList.size() > 0) {
	//			Map.Pair<String, String> removedPair = mappedList.removeAny();
	//			if(removedPair.value().contains("giod")) {
	//				temp.add(removedPair.key(), removedPair.value());
	//			} 
	//			mappedList.add(removedPair.key(), removedPair.value());
	//		}
	//
	//		return temp;
	//
	//	}

}



