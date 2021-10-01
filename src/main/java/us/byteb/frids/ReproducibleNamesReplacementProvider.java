package us.byteb.frids;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ReproducibleNamesReplacementProvider implements ReplacementProvider {

	private static final List<String> nouns = new ArrayList<>();
	private static final List<String> adjectives = new ArrayList<>();
	private static final Map<String, String> issuedReplacementsToIds = new HashMap<>();
	{
		final InputStream is = ReproducibleNamesReplacementProvider.class.getClassLoader().getResourceAsStream("nouns.txt");
		final Scanner scanner = new Scanner(is);

		while (scanner.hasNextLine()) {
			nouns.add(scanner.nextLine());
		}

		final InputStream is2 = ReproducibleNamesReplacementProvider.class.getClassLoader().getResourceAsStream("adjectives.txt");
		final Scanner scanner2 = new Scanner(is2);

		while (scanner2.hasNextLine()) {
			adjectives.add(scanner2.nextLine());
		}
	}

	@Override
	public String provide(final String id) throws UniqueReplacementsExhaustedException {
		final int hash = murmur2(id.getBytes(StandardCharsets.UTF_8));
		final int nounPos = toPositive(hash) % nouns.size();
		final int adjectivePos = toPositive(hash) % adjectives.size();
		final String replacement = adjectives.get(adjectivePos).toUpperCase() + "_" + nouns.get(nounPos).toUpperCase();

		final String alreadyIssuedReplacement = issuedReplacementsToIds.get(replacement);
		if (alreadyIssuedReplacement != null && !alreadyIssuedReplacement.equals(id)) {
			return id;
		} else {
			issuedReplacementsToIds.put(replacement, id);
			return replacement;
		}
	}

	/**
	 * A cheap way to deterministically convert a number to a positive value. When the input is
	 * positive, the original value is returned. When the input number is negative, the returned
	 * positive value is the original value bit AND against 0x7fffffff which is not its absolute
	 * value.
	 */
	public static int toPositive(int number) {
		return number & 0x7fffffff;
	}


	/**
	 * Generates 32 bit murmur2 hash from byte array
	 * @param data byte array to hash
	 * @return 32 bit hash of the given array
	 */
	@SuppressWarnings("fallthrough")
	public static int murmur2(final byte[] data) {
		int length = data.length;
		int seed = 0x9747b28c;
		// 'm' and 'r' are mixing constants generated offline.
		// They're not really 'magic', they just happen to work well.
		final int m = 0x5bd1e995;
		final int r = 24;

		// Initialize the hash to a random value
		int h = seed ^ length;
		int length4 = length / 4;

		for (int i = 0; i < length4; i++) {
			final int i4 = i * 4;
			int k = (data[i4 + 0] & 0xff) + ((data[i4 + 1] & 0xff) << 8) + ((data[i4 + 2] & 0xff) << 16) + ((data[i4 + 3] & 0xff) << 24);
			k *= m;
			k ^= k >>> r;
			k *= m;
			h *= m;
			h ^= k;
		}

		// Handle the last few bytes of the input array
		switch (length % 4) {
			case 3:
				h ^= (data[(length & ~3) + 2] & 0xff) << 16;
			case 2:
				h ^= (data[(length & ~3) + 1] & 0xff) << 8;
			case 1:
				h ^= data[length & ~3] & 0xff;
				h *= m;
		}

		h ^= h >>> 13;
		h *= m;
		h ^= h >>> 15;

		return h;
	}

}
