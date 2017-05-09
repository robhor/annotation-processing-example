package at.robhor.annotationprocessingexample.model;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.TypeElement;

/**
 * @author Robert Horvath
 */

public class ClassBinding {
    private final TypeElement classElement;
    private final Set<ViewBinding> viewBindingSet;

    public ClassBinding(TypeElement classElement) {
        this.classElement = classElement;
        this.viewBindingSet = new HashSet<>();
    }

    public TypeElement getClassElement() {
        return classElement;
    }

    public Set<ViewBinding> getViewBindings() {
        return viewBindingSet;
    }

    public void addViewBinding(ViewBinding viewBinding) {
        viewBindingSet.add(viewBinding);
    }
}
