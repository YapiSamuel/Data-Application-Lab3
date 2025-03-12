public class Politician {
    private String name;
    private String country;
    private String birthYear;  // Change to String to handle missing values
    private String party;

    public Politician(String name, String country, String birthYear, String party) {
        this.name = name;
        this.country = country;
        this.birthYear = birthYear.isEmpty() ? "Unknown" : birthYear; // Handle missing birth year
        this.party = party;
    }
    // Getter methods (FIXES the issue)
    public String getName() { return name; }
    public String getCountry() { return country; }
    public String getBirthYear() { return birthYear; }
    public String getParty() { return party; }


    @Override
    public String toString() {
        return "Name: " + name + ", Country: " + country +
                ", Birth Year: " + birthYear + ", Party: " + party;
    }
}
