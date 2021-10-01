package us.byteb.frids;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

	private static final Pattern UUID_PATTERN = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89AB][0-9a-f]{3}-[0-9a-f]{12}", Pattern.CASE_INSENSITIVE);
	private static final ReplacementProvider replacementProvider = new ReproducibleNamesReplacementProvider();

	public static void main(String[] args) {
		final Scanner scanner = new Scanner(System.in);
		final HashMap<String, String> idReplacements = new HashMap<>();

		while (scanner.hasNextLine()) {
			final String line = scanner.nextLine();

			final Set<String> uuidsInLine = new HashSet<>();
			final Matcher uuidMatcher = UUID_PATTERN.matcher(line);
			while(uuidMatcher.find()) {
				uuidsInLine.add(uuidMatcher.group());
			}

			String output = line;
			for (final String id : uuidsInLine) {
				final String replacement = idReplacements.computeIfAbsent(id, replacementProvider::provide);
				output = output.replace(id, replacement);
			}

			System.out.println(output);
		}
	}

}
