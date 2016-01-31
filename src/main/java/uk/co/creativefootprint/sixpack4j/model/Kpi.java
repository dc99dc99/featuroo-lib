package uk.co.creativefootprint.sixpack4j.model;


public class Kpi {

    private String name;

    public Kpi(String name){

        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Kpi kpi = (Kpi) o;

        return name.equals(kpi.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
