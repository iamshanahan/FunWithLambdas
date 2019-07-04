package funWithLambdas;

public class LambdaWithDefaultExerciser {

	/**
	 * Interfaces can have methods and still be FIs, sort of. (Methods must be
	 * static or default.)
	 */
	interface FunctionalInterfaceWithDefaults {
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

		/**
		 * The FI method
		 */
		String oneParamFunction(String param);
	}

	private static void emitFIOneParam(FunctionalInterfaceWithDefaults fiwd) {
		System.out.println(fiwd.oneParamFunction("a param"));

	}

	public static void main(String[] args) {

		// default and static functions don't break FI.
		FunctionalInterfaceWithDefaults fiwd = param -> (param
				+ "FIs can have default and static non-abstract members and functions");
		emitFIOneParam(fiwd);

		fiwd = param -> (param
				+ "Static method can be called from anywhere, including a lambda--" +
				FunctionalInterfaceWithDefaults.aStaticMethod("thirty-seven"));
		emitFIOneParam(fiwd);

		// Does not compile
//		fiwd = param -> param +  "But you can't call a default method " +
//			"because scope for lambda is calling block" +
//			aDefaultMethod_preview( 37 );
//		emitFIOneParam(fiwd);

		// But a full anonymous implementation creates a new scope, so you can call a
		// default
		fiwd = new FunctionalInterfaceWithDefaults() {
			@Override
			public String oneParamFunction(String param) {
				return "Here we call another method in the FI" +
						aDefaultMethod_preview(37);
			}
		};
		emitFIOneParam(fiwd);

		int i = 38;

		// Does not compile
//		fiwd = param -> {
//			int i = 39;
//			return param + "Similarly, you can't reassign local variables in a lambda, because same scope" ;
//		};
//		emitFIOneParam(fiwd);

		// And again, a full anonymous implementation creates a new scope, so you can
		// reassign a variable
		fiwd = new FunctionalInterfaceWithDefaults() {
			@Override
			public String oneParamFunction(String param) {
				int i = 39;
				return "Here we override a variable local to the calling block: " + i;
			}
		};
		emitFIOneParam(fiwd);
	}

}
