package uk.co.creativefootprint.sixpack4j.model;

public class ParticipationResult {

    private Client client;
    private Boolean isParticipating;
    private Alternative alternative;

    public ParticipationResult(Client client, Boolean isParticipating, Alternative alternative) {
        this.client = client;
        this.isParticipating = isParticipating;
        this.alternative = alternative;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParticipationResult that = (ParticipationResult) o;

        if (!client.equals(that.client)) return false;
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
        int result = client.hashCode();
        result = 31 * result + isParticipating.hashCode();
        result = 31 * result + alternative.hashCode();
        return result;
    }
}

