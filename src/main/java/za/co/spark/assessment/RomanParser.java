package za.co.spark.assessment;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RomanParser {

	private enum Roman {
			I(1),
			V(5),
			X(10),
			L(50),
			C(100),
			D(500),
			M(1000);

		final int value;

		private Roman(int value) {
			this.value = value;
		}

		static final Set<String> letters = Stream.of(values())
				.map(Roman::name)
				.collect(Collectors.toSet());
	}
	
	private static final Set<String> ILLEGAL_EXPRESSIONS = Set.of(
			"IIII", 
			"VV", 
			"XXXX", 
			"LL", 
			"CCCC", 
			"DD", 
			"MMMM",
			"IL",
			"IC",
			"ID",
			"IM",
			"VX",
			"VL",
			"VC",
			"VD",
			"VM",
			"XD",
			"XM",
			"LC",
			"LD",
			"LM",
			"DM"
			/* Add more illegal expressions */
			);
	
	public static void main(String...strings) {
		System.out.println(parse("XCIX"));
	}
	
	private static boolean certify(String roman) {
		final boolean allLettersAreRoman = Stream.of(roman.split(""))
			.allMatch(Roman.letters::contains);
		if (!allLettersAreRoman) {
			return false;
		}
		
		final boolean containsIllegalExpression = ILLEGAL_EXPRESSIONS
				.stream()
				.anyMatch(roman::contains);
		if (containsIllegalExpression) {
			return false;
		}

		return true;
	}

	public final static int parse(String roman) {
		if (!certify(roman)) {
			throw new IllegalArgumentException(roman);
		}
		int result = 0;
		Roman previousLetter = null;
		for (String letter : roman.split("")) {
			final Roman thisLetter = Roman.valueOf(letter);
			if (previousLetter != null && previousLetter.compareTo(thisLetter) < 0) {
				// Validate
				// previousLetter has been added so must be subtracted twice
				result -= previousLetter.value * 2;
			}
			result += thisLetter.value;
			previousLetter = thisLetter;
		}
		return result;
	}
}
