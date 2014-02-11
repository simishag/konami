package konami.sampleJB;

/**
 * Domain object for data submitted in XML request. This represents 1 row.
 * 
 * @author judd
 * 
 */
public class RequestData {
    private String description;
    private String value;

    public RequestData(String description, String value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}