import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Command {
	public String label;
	public String opcode;
	public String arg1;
	public String arg2;
	public int target;
	public int pc;
	public int count;
	
	
	public Command(String cmmd, int pc)
	{
		String label = "(?<label>[A-Z]+:)";
		String opcode = "(?<opcode>load|loop|inc|goto|end)";
		String arg1 = "(?<arg1>\\d+|[a-zA-Z]+)";
		String arg2 = "(?<arg2>\\d+|[a-z]+)";
		String instruction = String.format("(%s\\s)?%s(\\s%s)?(\\s%s)?", label, opcode, arg1, arg2);
		
		//System.out.println(instruction);
		Pattern pattern = Pattern.compile(instruction);
		Matcher matcher = pattern.matcher(cmmd);
		
		if(matcher.find()) {			
			/* Add instruction to the program */
			this.label = matcher.group("label");
			if(this.label != null) 
				this.label = this.label.substring(0, this.label.length() - 1);
			this.opcode = matcher.group("opcode");
			this.arg1 = matcher.group("arg1");
			this.arg2 = matcher.group("arg2");	
			this.pc = pc;
		}
	}
	
	public String toString() {
		return String.format("Label = %s, opcode = %s, arg1 = %s, arg2 = %s, pc = %s", label, opcode, arg1, arg2, pc);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arg1 == null) ? 0 : arg1.hashCode());
		result = prime * result + ((arg2 == null) ? 0 : arg2.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((opcode == null) ? 0 : opcode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Command other = (Command) obj;
		if (arg1 == null) {
			if (other.arg1 != null)
				return false;
		} else if (!arg1.equals(other.arg1))
			return false;
		if (arg2 == null) {
			if (other.arg2 != null)
				return false;
		} else if (!arg2.equals(other.arg2))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (opcode == null) {
			if (other.opcode != null)
				return false;
		} else if (!opcode.equals(other.opcode))
			return false;
		return true;
	}
	
	
}
