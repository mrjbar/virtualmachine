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
	Stack<Frame> commandStack = new Stack<Frame>();
	Map<String, Integer> vars = new HashMap<String, Integer>();
	private Integer pc = 0;
	private Integer numOfExe = 0;
	
	public void add(String cmd)
	{
		String label = "(?<label>[A-Z]+:)";
		String opcode = "(?<opcode>load|loop|inc|goto|end)";
		String arg1 = "(?<arg1>\\d+|[a-zA-Z]+)";
		String arg2 = "(?<arg2>\\d+|[a-z]+)";
		String instruction = String.format("(%s\\s)?%s(\\s%s)?(\\s%s)?", label, opcode, arg1, arg2);
		
		//System.out.println(instruction);
		Pattern pattern = Pattern.compile(instruction);
		Matcher matcher = pattern.matcher(cmd);
		
		if(matcher.find()) {
			//System.out.println(matcher.group());
			
			/* Add instruction to the program */
				/* Initialize the command object */
				Command c = new Command();
				c.label = matcher.group("label");
				if(c.label != null) c.label = c.label.substring(0, c.label.length() - 1);
				c.opcode = matcher.group("opcode");
				c.arg1 = matcher.group("arg1");
				c.arg2 = matcher.group("arg2");
				
				/* Add the command object to the program */
				program.add(c);
				
				/* special case. gotos and labels */
				if(c.label != null) {
					int index = program.indexOf(c);
					vars.put(c.label, index);
				}		
		}
	}
	
	public void compile(String filename)
	{
		Charset charset = Charset.forName("US-ASCII");
		String dir = System.getProperty("user.dir") + "/src/";
		Path path = FileSystems.getDefault().getPath(dir, filename);
		System.out.println(dir);
 		try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	if(!line.isEmpty()) {
		    		line.trim();
		    		if(line.charAt(0) != '#') {
		    			System.out.println(line);
		    			add(line.trim());
		    		}
		    	}
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	public void execute(Command cmd)
	{
		numOfExe++;
		
		if(cmd.opcode.equals("goto")) {
			if(vars.get(cmd.arg1) != null) {
				pc = vars.get(cmd.arg1);
			} else {
				pc = program.size();
			}
			return;
		}
		
		if(cmd.opcode.equals("load")) {
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
			return;
		}
		
		if(cmd.opcode.equals("inc")) {
			Integer var;
			if(vars.containsKey(cmd.arg1)) {
				var = vars.get(cmd.arg1);
			} else {
				var = 0;
				vars.put(cmd.arg1, var);
			}
			var++;
			vars.replace(cmd.arg1, var);
			return;
		}
		
		//update later
		if(cmd.opcode.equals("loop")) {
			Frame frame = new Frame();
			char c = cmd.arg1.charAt(0);
			if(Character.isDigit(c)) {
				frame.count = Integer.parseInt(cmd.arg1) - 1;
			} else {
				if(vars.containsKey(cmd.arg1)) {
					frame.count = vars.get(cmd.arg1);
					frame.count = vars.get(cmd.arg1) - 1;
				} else {
					frame.count = 0;
					vars.put(cmd.arg1, 0);
				}
				
			}
			frame.pc = pc;
			commandStack.push(frame);
			return;
		}
		
		if(cmd.opcode.equals("end")) {
			Frame frame = commandStack.peek();
			if(frame.count == 0) {
				commandStack.pop();
			} else {
				frame.count--;
				pc = frame.pc;
			}
			return;
		}
	}
	
	public void run()
	{
		while(pc < program.size())
			execute(program.get(pc++));
	}
	
	public String toString() {
		return String.format("lines executed = %d, pc = %d, vars[%d] = %s", numOfExe, pc,  vars.size(), vars);
	}
}
