package funWithLambdas;

/**
 * Demonstrates how lambdas automagically infer generic types from context.
 * 
 * @author bshanahan
 *
 */
public class LambdaGenericsExerciser {

	static class Type1 {

		Type1(String member1) {
			Type1.this.member1 = member1;
		}

		String member1;
	}

	static class Type2 {

		Type2(String member2) {
			Type2.this.member2 = member2;
		}

		String member2;
	}

	interface DoublyGenericInterface<RetType, ParamType> {
		RetType fiMethod(ParamType param);
	}

	static <RetType, ParamType> RetType genericFunction(DoublyGenericInterface<RetType, ParamType> genFunc,
			ParamType param) {
		return genFunc.fiMethod(param);
	}

	public static void main(String[] args) {

		Type2 type2 = new Type2("type 2's member");

		Type1 type1a = genericFunction(
				new DoublyGenericInterface<Type1, Type2>() {
					@Override
					public Type1 fiMethod(Type2 param) {
						return new Type1("Made in explicit inline anonymous class: " + param.member2);
					}
				},
				type2);
		System.out.println(type1a.member1);

		type1a = genericFunction(p -> {
			return new Type1("Made in lambda.  Note that generics are inferred: " + p.member2);
		}, type2);
		System.out.println(type1a.member1);

		type1a = genericFunction(p -> new Type1("Made in inline lambda.  Note compactness: " + p.member2),
				type2);
		System.out.println(type1a.member1);

	}

}
