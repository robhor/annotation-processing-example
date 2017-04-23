package at.robhor.annotationprocessingexample;

/**
 * @author Robert Horvath
 */

public class ElementBinding {
    private String type;
    private String name;
    private int id;

    public ElementBinding(String type, String name, int id) {
        this.type = type;
        this.name = name;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
