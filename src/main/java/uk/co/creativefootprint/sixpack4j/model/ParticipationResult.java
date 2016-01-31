package uk.co.creativefootprint.sixpack4j.model;

public class ParticipationResult {

    private Boolean isParticipating;
    private Alternative alternative;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParticipationResult that = (ParticipationResult) o;

        if (!isParticipating.equals(that.isParticipating)) return false;
        return alternative.equals(that.alternative);

    }

    public Alternative getAlternative() {
        return alternative;
    }

    public Boolean isParticipating() {
        return isParticipating;
    }

    @Override
    public String toString() {
        return "ParticipationResult{" +
                "isParticipating=" + isParticipating +
                ", alternative=" + alternative +
                '}';
    }

    @Override
    public int hashCode() {
        int result = isParticipating.hashCode();
        result = 31 * result + alternative.hashCode();
        return result;
    }

    public ParticipationResult(Boolean isParticipating, Alternative alternative) {
        this.isParticipating = isParticipating;
        this.alternative = alternative;
    }
}

