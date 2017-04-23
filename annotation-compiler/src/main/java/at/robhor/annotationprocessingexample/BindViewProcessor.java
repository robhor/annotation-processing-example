package at.robhor.annotationprocessingexample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

public class BindViewProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Map<String, List<ElementBinding>> elementBindings = getElementBindings(roundEnvironment);

        return false;
    }

    private Map<String, List<ElementBinding>> getElementBindings(RoundEnvironment roundEnvironment) {
        Map<String, List<ElementBinding>> elementBindings = new HashMap<>();
        Set<? extends Element> elementsToBind = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element : elementsToBind) {
            BindView annotation = element.getAnnotation(BindView.class);

            String className = element.getEnclosingElement().asType().toString();

            String type = element.asType().toString();
            String name = element.getSimpleName().toString();
            int viewId = annotation.value();
            ElementBinding elementBinding = new ElementBinding(type, name, viewId);

            List<ElementBinding> classElementBindings = elementBindings.computeIfAbsent(className, k -> new ArrayList<>());
            classElementBindings.add(elementBinding);
        }

        return elementBindings;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BindView.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
