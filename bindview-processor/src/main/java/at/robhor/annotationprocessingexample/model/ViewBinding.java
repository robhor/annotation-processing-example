package at.robhor.annotationprocessingexample.model;

import javax.lang.model.type.TypeMirror;

/**
 * @author Robert Horvath
 */

public class ViewBinding {
    private final TypeMirror type;
    private final String name;
    private final int viewId;

    public ViewBinding(TypeMirror type, String name, int viewId) {
        this.type = type;
        this.name = name;
        this.viewId = viewId;
    }

    public TypeMirror getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getViewId() {
        return viewId;
    }
}
