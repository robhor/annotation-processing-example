package at.robhor.annotationprocessingexample.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

/**
 * @author Robert Horvath
 */

public class BindingSet {
    private final Map<PackageElement, PackageBinding> packageBindingMap;

    public BindingSet() {
        this.packageBindingMap = new HashMap<>();
    }

    public Collection<PackageBinding> getPackageBindings() {
        return packageBindingMap.values();
    }

    public PackageBinding getPackageBinding(PackageElement packageElement) {
        return packageBindingMap.computeIfAbsent(packageElement, PackageBinding::new);
    }

    public void addBinding(PackageElement packageElement, TypeElement classElement,
                           ViewBinding elementBinding) {
        PackageBinding packageBinding = getPackageBinding(packageElement);
        ClassBinding classBinding = packageBinding.getClassBinding(classElement);
        classBinding.addViewBinding(elementBinding);
    }
}
