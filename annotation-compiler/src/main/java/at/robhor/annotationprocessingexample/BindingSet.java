package at.robhor.annotationprocessingexample;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Robert Horvath
 */

public class BindingSet {
    private Map<String, Map<String, Set<ElementBinding>>> bindings;

    BindingSet() {
        this.bindings = new HashMap<>();
    }

    void addBinding(String packageName, String className, ElementBinding elementBinding) {
        Set<ElementBinding> elementBindings = getElementBindings(packageName, className);
        elementBindings.add(elementBinding);
    }

    private Set<ElementBinding> getElementBindings(String packageName, String className) {
        Map<String, Set<ElementBinding>> classBindings = bindings.computeIfAbsent(packageName, k -> new HashMap<>());
        return classBindings.computeIfAbsent(className, k -> new HashSet<>());
    }

    Set<String> getPackageNames() {
        return bindings.keySet();
    }

    Set<String> getClassNames(String packageName) {
        return bindings.get(packageName).keySet();
    }

    Set<ElementBinding> getBindings(String packageName, String className) {
        return bindings.get(packageName).get(className);
    }
}
