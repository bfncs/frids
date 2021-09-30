package us.byteb.frids;

public class CounterReplacementProvider implements ReplacementProvider {
		private static long replacementCounter = 0;

	@Override
	public String provide(final String id) throws UniqueReplacementsExhaustedException {
		if (replacementCounter < 0) {
			throw new UniqueReplacementsExhaustedException();
		}

		return "id" + replacementCounter++;
	}
}
