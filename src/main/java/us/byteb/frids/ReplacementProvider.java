package us.byteb.frids;

public interface ReplacementProvider {

	String provide(final String id) throws UniqueReplacementsExhaustedException;

	class UniqueReplacementsExhaustedException extends RuntimeException {
	}
}
