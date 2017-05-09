package at.robhor.annotationprocessingexample.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * @author Robert Horvath
 */

public class PackageBinding {
    private PackageElement packageElement;
    private Map<TypeElement, ClassBinding> classBindings;

    public PackageBinding(PackageElement packageElement) {
        this.packageElement = packageElement;
        this.classBindings = new HashMap<>();
    }

    public String getPackageName() {
        return packageElement.getQualifiedName().toString();
    }

    public Collection<ClassBinding> getClassBindings() {
        return classBindings.values();
    }

    public ClassBinding getClassBinding(TypeElement typeElement) {
        return classBindings.computeIfAbsent(typeElement, ClassBinding::new);
    }
}
