package za.co.spark.assessment;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class InputReader {

	private static final Map<String, String> aliases = new HashMap<>(); 
	private static final Map<String, BigDecimal> prices = new HashMap<>(); 
	private static final List<String> output = new LinkedList<>();

	public static void main(String...strings) {
		final Reader reader = new StringReader("""
				glob is I
				prok is V
				pish is X
				tegj is L
				glob glob Silver is 34 Credits
				glob prok Gold is 57800 Credits
				pish pish Iron is 3910 Credits
				how much is pish tegj glob glob ?
				how many Credits is glob prok Silver ?
				how many Credits is glob prok Gold ?
				how many Credits is glob prok Iron ?
				how much wood could a woodchuck chuck if a woodchuck could chuck wood?
				""");
		readInput(reader);
		System.out.println();
		output.forEach(System.out::println);
	}

	public static void readInput(Reader reader) {
		final BufferedReader bReader = new BufferedReader(reader);
		bReader.lines()
		.map(String::trim)
		.map(InputReader::processLine)
		.filter(Objects::nonNull)
		.forEach(output::add);
	}

	private static String processLine(String line) {
		System.out.println(line);
		try {
			final String[] tokens = line.split("\\s+");
			if (tokens.length == 3 && "is".equalsIgnoreCase(tokens[1]) && tokens[2].length() == 1 && RomanParser.romanLetters.contains(tokens[2])) {
				aliases.put(tokens[0], tokens[2]);
				return null;
			}
			if ("Credits".equals(tokens[tokens.length - 1])) {
				String roman = "";
				for(String token : tokens) {
					final String romanLetter = aliases.get(token);
					if (romanLetter == null) {
						break;
					}
					roman += romanLetter;
				}
				final int units = RomanParser.parse(roman);
				final String itemName = tokens[roman.length()];
				final BigDecimal price = new BigDecimal(tokens[roman.length() + 2]);
				prices.put(itemName, price.divide(BigDecimal.valueOf(units)).setScale(2));
				return null;
			}
			if ("How".equalsIgnoreCase(tokens[0]) && "Much".equalsIgnoreCase(tokens[1]) && "is".equalsIgnoreCase(tokens[2])) {
				final StringJoiner joiner = new StringJoiner(" ");
				final String roman = buildRoman(tokens, joiner, 3);
				joiner.add("is");
				final int value = RomanParser.parse(roman);
				joiner.add(Integer.toString(value));
				return joiner.toString();
			}
			if ("How".equalsIgnoreCase(tokens[0]) && "many".equalsIgnoreCase(tokens[1]) && "Credits".equalsIgnoreCase(tokens[2]) && "is".equalsIgnoreCase(tokens[3])) {
				final StringJoiner joiner = new StringJoiner(" ");
				final String roman = buildRoman(tokens, joiner,4);
				final int numberOfItems = RomanParser.parse(roman);
				final String itemName = tokens[roman.length() + 4];
				final BigDecimal itemPrice = prices.get(itemName);
				final BigDecimal totalPrice = itemPrice.multiply(BigDecimal.valueOf(numberOfItems));
				joiner.add("is")
				.add(totalPrice.toString())
				.add("Credits");
				return joiner.toString();
			}
		}
		catch(RuntimeException e) {
			e.printStackTrace();
		}
		return "I have no idea what you are talking about";
	}

	private static String buildRoman(String[] tokens, StringJoiner joiner, int startPoint) {
		String roman = "";
		for (int i = startPoint; i < tokens.length; i++) { 
			final String token = tokens[i];
			final String romanLetter = aliases.get(token);
			if (romanLetter == null) {
				break;
			}
			joiner.add(token);
			roman += romanLetter;
		}
		return roman;
	}
}
