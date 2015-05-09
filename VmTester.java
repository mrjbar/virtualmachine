
public class VmTester {

	public static void main(String[] args) {
		
		/* Lab 3 */
		VM vmAdd = new VM();
		vmAdd.compile("addition.pl");
		vmAdd.run();
		System.out.println("Addition Macro: n + m");
		System.out.println(vmAdd);
		
		/* Lab 4 */
		VM vmMax = new VM();
		vmMax.compile("max.pl");
		vmMax.run();
		System.out.println("\nMax Macro: max(n, m)");
		System.out.println(vmMax);
		
		VM vmSub = new VM();
		vmSub.compile("subtract.pl");
		vmSub.run();
		System.out.println("\nSubtraction Macro: n - m");
		System.out.println(vmSub);
		
		VM vmMul = new VM();
		vmMul.compile("multiply.pl");
		vmMul.run();
		System.out.println("\nMultiplication Macro: n * m");
		System.out.println(vmMul);
		
		/* Lab 8 */
		VM vmDouble = new VM();
		vmDouble.compile("double.pl");
		vmDouble.run();
		System.out.println("\nDouble Macro: double(x) = x + x");
		System.out.println(vmDouble);
		
		VM vmExp = new VM();
		vmExp.compile("exp.pl");
		vmExp.run();
		System.out.println("\nExponent Macro: exp(x) = 2^x");
		System.out.println(vmExp);
		
		VM vmHyper = new VM();
		vmHyper.compile("hyper.pl");
		vmHyper.run();
		System.out.println("\nHyper 1 Macro: exp(exp(...exp(1)...))");
		System.out.println(vmHyper);
		
		VM vmHyperTwo = new VM();
		vmHyperTwo.compile("hypertwo.pl");
		vmHyperTwo.run();
		System.out.println("\nHyper 2 Macro: hyperExp(hyperExp(... hyperExp(1)...))");
		System.out.println(vmHyperTwo);
		
		VM vmHyperThree = new VM();
		vmHyperThree.compile("hyperthree.pl");
		vmHyperThree.run();
		System.out.println("\nHyper 3 Macro: hyper2Exp(hyper2Exp(... hyper2Exp(1)...))");
		System.out.println(vmHyperThree);
		
		VM testVM = new VM();
		testVM.add("AAA: load x, 5");
		testVM.add("loop 6");
		testVM.add("goto AAA");
		testVM.add("end");
		testVM.run();
		System.out.println("\nTest Code");
		System.out.println(testVM);
	}
}
