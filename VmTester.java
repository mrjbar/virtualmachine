
public class VmTester {

	public static void main(String[] args) {
		VM vm = new VM();
		//vm.compile("program.pl");
        vm.add("load x 0");
        vm.add("load w 2");
        vm.add("goto AAA");
        vm.add("loop x");
        vm.add("inc w");
        vm.add("end");
        vm.add("AAA: load w 20");
        vm.run();
		System.out.println("");
		System.out.println(vm);
	}

}
