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
import components.array.Array;


/**
 * Takes a txt file, counts how many occurences of each word there is, and outputs the 
 * results in a txt page
 *
 * @author Daniel Amar
 *
 */
public class YearInPie {

	/**
	 * Private constructor so this utility class cannot be instantiated.
	 */
	public YearInPie() {
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

		//Executes Code after input
		startProgram(file,outputFile);


		in.close();
		out.close();
	}

	public static void startProgram(SimpleReader file, String outputFile) {

		//Queue of line strings
		Queue<String> lines = parseToQueue(file); 

		//empty to be initialized as having keys after call to queueToMap
		Queue<String> justKeys = new Queue1L<>();
		//Map of days and feeling
		Map<String,String> mappedDays = queueToMap(lines,justKeys);



		/*
		 * Array of occurances
		 **************************************************
		 * [0] = great days(green) 
		 * [1] = good days with no comment (blue)
		 * [2] = good days with positive comment(blue) 
		 * [3] = good days with negative comment (blue)
		 * [4] = low energy, below average, sad days(yellow)
		 * [5] = bad day, stressed, overwhelmed(Dark Blue)
		 * [6] = Bad Day, Frustrated, Angry(Red)
		 * [7] = Bad Day, Sad, Depressed(Purple)
		 * [8] = Data Not Available
		 ***************************************************
		 */
		Array<Integer> occurances = countOccurances(mappedDays);
		printResults(occurances,outputFile);




	}
	/**
	 * Creates new blank feeling sheet
	 * @param fullMap
	 */
	public static void createNewTemplate(Queue<String> fullMap) {
		SimpleWriter out = new SimpleWriter1L("output/templateFile.txt");

		boolean isGood = checkData(fullMap);
		System.out.println("Data is Valid: " + isGood);

		//adds dash after each date for easier separation later on
		while(fullMap.length() > 0) {
			String oneDate = fullMap.dequeue();
			oneDate = oneDate + "- ";
			out.println(oneDate);
		}

		out.close();
	}

	/**
	 * Checks if the data has 365 lines and ordered by time correctly. 
	 * More precisely it checks only the KEYS which are the dates
	 * 
	 * @param fullMap
	 * 	queue of dates AKA the keys from the fullMap
	 * 
	 * @return
	 * true if data is true, false otherwise.
	 */
	public static boolean checkData(Queue<String> fullMap) {

		//# of days in each month
		//index 0 is January index 11 is December
		int[] months = {31,28,31,30,31,30,31,31,30,31,30,31};

		boolean isValid = true;
		String takenOut = "";

		for(int i = 0; i < 12; i++) {
			for(int j = 1; j <= months[i]; j++) {

				//take front of queue and remove white space
				takenOut = fullMap.front().stripTrailing();

				//put item that was in the front in the back
				fullMap.enqueue(fullMap.dequeue());

				//checks to see if specific date at some line is the expected value.
				if(!takenOut.equals((i+1) + "/" + j + "")) {
					System.out.println("Expected: " + takenOut + " Observed: " + (i+1) + "/" + j + "");
					j++;
					isValid = false;
				}
			}

		}



		return isValid;
	}



	/**
	 * Prints results to text page
	 * 
	 * @param occurances
	 * Array of occurrence Amounts
	 * 
	 * @param outputFile
	 * name of output file
	 */
	public static void printResults(Array<Integer> occurances,String outputFile) {
		
		SimpleWriter out = new SimpleWriter1L(outputFile);

		//Each line of each day based on each occurance index
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
		int totalGoodDays = occurances.entry(1) + occurances.entry(2) + occurances.entry(3);
		out.println("Total Good Days: " + totalGoodDays);
		out.println();

		int totalPosDays = totalGoodDays + occurances.entry(0);
		out.println("Total Positive Days:" + totalPosDays);

		int totalDays = 0;
		for(int i = 0; i < occurances.length(); i++) {
			totalDays += occurances.entry(i);
		}
		out.println("Total Days Logged: " + totalDays);



		out.close();
	}

	/**
	 * Parses lines into a queue
	 * 
	 * @param file
	 * Actual file itself
	 * 
	 * @return
	 * Queue with lines of text.
	 */
	public static Queue<String> parseToQueue(SimpleReader file) {

		Queue<String> words = new Queue1L<String>();

		//takes stuff out and puts it in queue.
		while(!file.atEOS()) {
			String temp = file.nextLine();
			//System.out.println(temp);
			words.enqueue(temp);
		}

		return words;
	}

	/**
	 * Turns a queue of lines into a map with each line as one new pair.
	 * 
	 * @param lines
	 * raw text line
	 * 
	 * @param justKeys
	 * 	Empty initially but eventually stores just keys from the map.
	 * @return
	 * map of the data with each date as its own key
	 */
	public static Map<String,String> queueToMap(Queue<String> lines,Queue<String> justKeys){

		Map<String,String> mappedLines = new Map1L<>();

		String key = " ";

		while(lines.length() > 0) {
			
			String removedFromQueue = lines.dequeue();

			//finds where to separate key and value
			int indexOfDash = removedFromQueue.indexOf("-"); 
			if(indexOfDash != -1) {
				
				//gets date
				key = removedFromQueue.substring(0,indexOfDash); 
				
				//removes whitespaces
				key.replaceAll("\\s", ""); 

				//gets comment 
				String value = removedFromQueue.substring(indexOfDash+1); 

				//adds date, comment
				mappedLines.add(key, value); 
				
				//adds keys to queue of keys, keeping order
				justKeys.enqueue(key);
			}else{
				System.out.println("String:" + removedFromQueue + " Gives a problem");
			}

		}
		
		//Line below creates a new blank yearly template based off given data.
		//createNewTemplate(justKeys);


		return mappedLines;
	}



	/**
	 * Counts occurrence of each emotion type and stores the resulting value in an array
	     **************************************************
		 * [0] = great days(green) 
		 * [1] = good days with no comment (blue)
		 * [2] = good days with positive comment(blue) 
		 * [3] = good days with negative comment (blue)
		 * [4] = low energy, below average, sad days(yellow)
		 * [5] = bad day, stressed, overwhelmed(Dark Blue)
		 * [6] = Bad Day, Frustrated, Angry(Red)
		 * [7] = Bad Day, Sad, Depressed(Purple)
		 * [8] = Data Not Available
		 ***************************************************
	 * @param mappedList
	 * List with dates mapping emotion
	 * 
	 * @return 
	 * an array with occurrence of emotions at each respective position
	 */
	public static Array<Integer> countOccurances(Map<String,String> mappedList){

		
		Array<Integer> typesOfDays = new Array1L<>(9);
		
		//For Further use later.
		Map<String,String> mapOfDaysWithGoodComments = mappedList.newInstance();
		Map<String,String> mapOfDaysWithBadComments = mappedList.newInstance();

		//Declares all array indexes to be 0
		for(int i = 0; i < typesOfDays.length(); i++) {
			typesOfDays.setEntry(i, 0);
		}

		//Iterates through each pair in the map and 
		for(Pair<String,String> s : mappedList) {
			
			//gets key and value of pair
			String key = s.key();
			String value = s.value().toLowerCase();

			//removes white spaces at beginning of string
			value = value.stripLeading(); 
			
			if(value.length() > 0) {
				if(value.contains("great")) {
					typesOfDays.setEntry(0,typesOfDays.entry(0) + 1);
				}
				else if(value.contains("good")) {

					//gets rid of white spaces at end of string
					value.stripTrailing(); 

					/*
					 * if length is 4 then there is no comment add to pos 2
					 * else there is a good comment add to pos2, 
					 * and adds to map
					 */
					if(value.length() == 4) {

						typesOfDays.setEntry(1,typesOfDays.entry(1) + 1); 

					}else {
						//adds good comment to map
						mapOfDaysWithGoodComments.add(key,value); 
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
				else if(value.contains("yellow")) {
					typesOfDays.setEntry(4,typesOfDays.entry(4) + 1);
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
				else if(value.contains("n/a")) {
					typesOfDays.setEntry(8,typesOfDays.entry(8) + 1);
				} else {
					System.out.println("Key: " + key + " Value: " + value + " <-- Occurrence Not Counted");
				}

			}

		}

		return typesOfDays;
	}


}



