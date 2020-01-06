import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.queue.Queue;
import components.queue.Queue1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.statement.Statement;
import components.statement.Statement1;
import components.utilities.Tokenizer;
import components.map.Map;
import components.map.Map1L;

/**
 * JUnit test fixture for {@code CountPrimitiveCalls}'s static method
 * countOfPrimitiveCalls.
 *
 * @author Put your name here
 *
 */
public final class YearInPieTest {

    /**
     * Creates and returns a {@code Statement} constructed from a given input
     * file.
     *
     * @param fileName
     *            the name of the file containing the statement
     * @param block
     *            flag to indicate whether to read an entire BLOCK (sequence of
     *            statements) or a single statement
     * @return the constructed statement
     * @requires <pre>
     * [fileName is the name of a file containing zero, one, or more
     *  valid BL statements]
     * </pre>
     * @ensures createFromArgs = [statement(s) from file fileName]
     */
    private Statement createFromArgs(String fileName, boolean block) {
        SimpleReader in = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(in);
        in.close();
        Statement s = new Statement1();
        if (block) {
            s.parseBlock(tokens);
        } else {
            s.parse(tokens);
        }
        return s;
    }

    @Test
    public void test1true() {

    	SimpleReader in = new SimpleReader1L("data/dayLog.txt");
    	Queue<String> test = YearInPie.parseToQueue(in);
    	
    	Queue<String> justKeys = new Queue1L<>();
		//Map of days and feeling
		Map<String,String> mappedDays = YearInPie.queueToMap(test,justKeys);
		
    	
        boolean count = YearInPie.checkData(justKeys);
        System.out.println(count);

    }
    

    

    
   
}
