package funWithLambdas;

public class LambdaExerciser {

	/**
	 * FIs with various parameter counts
	 */
	interface FunctionalInterfaceNoParams {
		String noParamFunction();
	}

	interface FunctionalInterfaceOneParam {
		String oneParamFunction(String param);
	}

	interface FunctionalInterfaceMultiParams {
		String twoParamFunction(String param1, int param2);
	}

	/**
	 * Interfaces can have methods and still be FIs, sort of. There is little
	 * production value in extending FIs. Done here so I can use same 'emit'
	 * function.
	 */
	interface FunctionalInterfaceWithDefaults extends FunctionalInterfaceOneParam {
		/**
		 * You can have static fields and still be an FI.
		 */
		String member = "statically initialized String";

		/**
		 * If a method is "default" and it has an implementation, you can still be an
		 * FI.
		 */
		default String aDefaultMethod_preview(int param) {
			return Integer.toString(param);
		}

		/**
		 * You can have a static function and still be an FI.
		 */
		static String aStaticMethod(String param) {
			return member + ", " + param;
		}
	}

	/**
	 * Functions that accept a particular FI signature as a parameter. The compiler
	 * uses this to infer what FI to turn the lambda into.
	 */
	static void emitFINoParams(FunctionalInterfaceNoParams fi) {
		System.out.println(fi.noParamFunction());
	}

	static void emitFIOneParam(FunctionalInterfaceOneParam fi) {
		System.out.println(fi.oneParamFunction("a param"));
	}

	static void emitFIMultiParams(FunctionalInterfaceMultiParams fi) {
		System.out.println(fi.twoParamFunction("param 1", 2));
	}

	/**
	 * Let's enumerate what does and doesn't work with FI invocation and lambdas.
	 */
	public static void main(String args[]) {

		FunctionalInterfaceNoParams lambdasCanBeInstantiatedTraditionally = new FunctionalInterfaceNoParams() {
			@Override
			public String noParamFunction() {
				return "inline implemented anonymous behavior";
			}
		};
		emitFINoParams(lambdasCanBeInstantiatedTraditionally);

		FunctionalInterfaceNoParams lambdasCanBeNamed = () -> "Use empty parens when no params";
		emitFINoParams(lambdasCanBeNamed);

		emitFINoParams(() -> "Lambdas can be instantiated anonymously, inline.\n" +
				"Still use empty parens to rep no params.\n" +
				"FI base type inferred from param type of called function in this case.");

		// Does not compile, no cast
//		System.out.println(
//				(() -> "Compiler won't try to guess FI type").noParamFunction() );
		// But works when you add the cast
		System.out.println(
				((FunctionalInterfaceNoParams) () -> "Cast required if target FI not inferrable").noParamFunction());

		// With a Functional Interface with params, you can,
		// declare it tradtionally
		// explicitly identify params, code block, and return value
		// leave out param types, and/or imply return value inline
		// and in the case of a single param, leave out the param parentheses.
		// Compiler will infer from type, cast, or context (required type for function
		// call) FI type.
		FunctionalInterfaceOneParam waysToDeclareOneParamFI = new FunctionalInterfaceOneParam() {
			@Override
			public String oneParamFunction(String param) {
				return "Traditional inline implementation: " + param;
			}
		};
		emitFIOneParam(waysToDeclareOneParamFI);

		waysToDeclareOneParamFI = (String param) -> {
			String beginning = "Fully explicit, parens around params, param types, brace block, return statement: ";
			return beginning + param;
		};
		emitFIOneParam(waysToDeclareOneParamFI);

		waysToDeclareOneParamFI = ( /* eclipse has stupid default idea of paren placement here */
				param) -> "Param types can be dropped. If simple emough brace block can be dropped and return value inline: "
						+ param;
		emitFIOneParam(waysToDeclareOneParamFI);

		waysToDeclareOneParamFI = param -> "For single-param FIs only, param parens optional: " + param;
		emitFIOneParam(waysToDeclareOneParamFI);

		emitFIOneParam(param -> "Or infer FI base type from context: " + param);

		// multiple params work for the FI work too. Pretty much the same except you
		// can't omit param parens.
		FunctionalInterfaceMultiParams waysToDeclareMultiParamFI = new FunctionalInterfaceMultiParams() {
			@Override
			public String twoParamFunction(String param1, int param2) {
				return "Traditional inline implementation: " + param1 + ", " + param2;
			}
		};
		emitFIMultiParams(waysToDeclareMultiParamFI);

		waysToDeclareMultiParamFI = (String param1, int param2) -> {
			String beginning = "Fully explicit, parens around params, param types, brace block, return statement: ";
			return beginning + param1 + ", " + param2;
		};
		emitFIMultiParams(waysToDeclareMultiParamFI);

		waysToDeclareMultiParamFI = (param1,
				param2) -> "Param types can be dropped. If simple emough brace block can be dropped and return value inline: "
						+ param1 + ", " + param2;
		emitFIMultiParams(waysToDeclareMultiParamFI);

		// does not compile
//		waysToDeclareMultiParamFI = param1, param2 -> "Can't drop param parens if there's a comma: " + param1 + param2;
//		emitFIMultiParams( waysToDeclareMultiParamFI );

		emitFIMultiParams((param1, param2) -> "Or infer FI base type from context: " + param1 + param2);

		// default and static functions don't break FI.
		FunctionalInterfaceWithDefaults fiwd = param -> (param
				+ "FIs can have default and static non-abstract functions");
		emitFIOneParam(fiwd);
	}
}
