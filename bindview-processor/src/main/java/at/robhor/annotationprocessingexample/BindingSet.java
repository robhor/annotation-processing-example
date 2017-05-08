package at.robhor.annotationprocessingexample;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * @author Robert Horvath
 */

public class BindingSet {
    private Map<PackageElement, Map<TypeElement, Set<ViewBinding>>> bindings;

    BindingSet() {
        this.bindings = new HashMap<>();
    }

    void addBinding(PackageElement packageElement, TypeElement classElement, ViewBinding elementBinding) {
        Set<ViewBinding> elementBindings = getElementBindings(packageElement, classElement);
        elementBindings.add(elementBinding);
    }

    private Set<ViewBinding> getElementBindings(PackageElement packageElement, TypeElement classElement) {
        Map<TypeElement, Set<ViewBinding>> classBindings = bindings.computeIfAbsent(packageElement, k -> new HashMap<>());
        return classBindings.computeIfAbsent(classElement, k -> new HashSet<>());
    }

    Set<PackageElement> getPackages() {
        return bindings.keySet();
    }

    Set<TypeElement> getClasses(PackageElement packageElement) {
        return bindings.get(packageElement).keySet();
    }

    Set<ViewBinding> getBindings(PackageElement packageElement, TypeElement classElement) {
        return bindings.get(packageElement).get(classElement);
    }
}
