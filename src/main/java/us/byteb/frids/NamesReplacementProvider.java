package us.byteb.frids;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class NamesReplacementProvider implements ReplacementProvider {

	private static final List<String> names = new ArrayList<>();
	private static int currentItem = 0;
	{
		final InputStream is = NamesReplacementProvider.class.getClassLoader().getResourceAsStream("names.txt");
		final Scanner scanner = new Scanner(is);

		while (scanner.hasNextLine()) {
			names.add(scanner.nextLine());
		}

		Collections.shuffle(names);
	}

	@Override
	public String provide(final String id) throws UniqueReplacementsExhaustedException {
		if (currentItem >= names.size()) {
			throw new UniqueReplacementsExhaustedException();
		}

		return names.get(currentItem++).toUpperCase();
	}
}
