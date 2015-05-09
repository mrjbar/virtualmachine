import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VM {
	ArrayList<Command> program = new ArrayList<Command>();
	Map<String, Integer> vars = new HashMap<String, Integer>();
	private Integer pc = 0;
	private Integer numOfExe = 0;
	
	/* Lab 2 */
	public void add(String cmmd) {
		program.add(new Command(cmmd, pc++));
	}
	
	/* Lab 2 */
	public void compile(String filename)
	{
		Charset charset = Charset.forName("US-ASCII");
		String dir = System.getProperty("user.dir") + "/src/";
		Path path = FileSystems.getDefault().getPath(dir, filename);
		//System.out.println(dir);
 		try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	if(!line.isEmpty()) {
		    		line.trim();
		    		if(line.charAt(0) != '#') {
		    			//System.out.println(line);
		    			add(line.trim());
		    		}
		    	}
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	private void resolveLabels() {
		   Stack<Command> loopStack = new Stack<Command>();
		   Map<String, Integer> targets = new HashMap<String, Integer>();
		   
		   // pass 1
		   for(Command cmmd: program) { 
			   if(cmmd.label != null)
				   targets.put(cmmd.label, cmmd.pc);
			   else if(cmmd.opcode.equals("loop"))
				   loopStack.push(cmmd);
			   else if (cmmd.opcode.equals("end")) {
				   Command loop = loopStack.pop();
				   loop.target = cmmd.pc;
				   cmmd.target = loop.pc;
			   }
		   }
		   
		   // pass 2
		   for(Command cmmd: program) {
			   if(cmmd.opcode.equals("goto"))
				   cmmd.target = targets.get(cmmd.arg1);	   
		   }
	}
	
	public void execute(Command cmd)
	{
		numOfExe++;
		
		if(cmd.opcode.equals("goto")) 
		{
			pc = cmd.target;
		} 
		else if(cmd.opcode.equals("load")) 
		{
			char c = cmd.arg2.charAt(0);
			if(Character.isDigit(c)) {
				vars.put(cmd.arg1, Integer.parseInt(cmd.arg2));
			} else {
				if(vars.containsKey(cmd.arg2)) {
					Integer x = vars.get(cmd.arg2);
					vars.put(cmd.arg1, x);
				} else {
					vars.put(cmd.arg2, 0);
					vars.put(cmd.arg1, 0);
				}
			}
		} 
		else if(cmd.opcode.equals("inc")) 
		{
			Integer var;
			if(vars.containsKey(cmd.arg1)) {
				var = vars.get(cmd.arg1);
			} else {
				var = 0;
				vars.put(cmd.arg1, var);
			}
			var++;
			vars.replace(cmd.arg1, var);
			
		} 
		else if (cmd.opcode.equals("loop")) 
		{
			
			char c = cmd.arg1.charAt(0);
			if(Character.isDigit(c)) 
			{
				cmd.count = Integer.parseInt(cmd.arg1);
			} 
			else 
			{
				if(vars.containsKey(cmd.arg1)) 
				{
					cmd.count = vars.get(cmd.arg1);
				} 
				else 
				{
					// Initilize variable to 0 and add it to the vars table
					cmd.count = 0;
					vars.put(cmd.arg1, 0);
				}
			}
			
			// Jump to the corresponding "end" if count is less
			// than or equal to zero
			if(cmd.count <= 0)
				pc = cmd.target + 1;
				
			
		} 
		else if (cmd.opcode.equals("end")) 
		{
			Command myLoop = program.get(cmd.target);
			myLoop.count--;
			if(myLoop.count > 0) {
				pc = myLoop.pc + 1;
			}
		}
	}
	
	public void run() {
		resolveLabels();
		pc = 0;
		while(pc < program.size())
			execute(program.get(pc++));
	}
	
	/* Lab 7 */
	public String toString() {
		return String.format("lines executed = %d, pc = %d, vars[%d] = %s", numOfExe, pc,  vars.size(), vars);
	}
}
