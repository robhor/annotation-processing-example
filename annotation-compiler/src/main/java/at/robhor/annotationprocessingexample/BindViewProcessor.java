package at.robhor.annotationprocessingexample;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class BindViewProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        BindingSet bindingSet = getElementBindings(roundEnvironment);

        Filer filer = processingEnv.getFiler();

        for (String packageName : bindingSet.getPackageNames()) {
            TypeSpec binderClass = generateBinderClass(bindingSet, packageName);
            JavaFile javaFile = JavaFile.builder(packageName, binderClass).build();
            try {
                javaFile.writeTo(filer);
            } catch (IOException e) {
                Messager messager = processingEnv.getMessager();
                messager.printMessage(Diagnostic.Kind.ERROR, e.toString());
            }
        }

        return true;
    }

    private BindingSet getElementBindings(RoundEnvironment roundEnvironment) {
        BindingSet bindingSet = new BindingSet();

        Set<? extends Element> elementsToBind = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element : elementsToBind) {
            BindView annotation = element.getAnnotation(BindView.class);

            TypeElement classElement = (TypeElement) element.getEnclosingElement();
            String className = classElement.getQualifiedName().toString();
            String packageName = getPackageName(classElement);

            String type = element.asType().toString();
            String name = element.getSimpleName().toString();
            int viewId = annotation.value();
            ElementBinding elementBinding = new ElementBinding(type, name, viewId);

            bindingSet.addBinding(packageName, className, elementBinding);
        }

        return bindingSet;
    }

    private String getPackageName(Element element) {
        if (element instanceof PackageElement) {
            return ((PackageElement) element).getQualifiedName().toString();
        }

        return getPackageName(element.getEnclosingElement());
    }

    private TypeSpec generateBinderClass(BindingSet bindingSet, String packageName) {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("ViewBinder")
                .addModifiers(Modifier.FINAL)
                .addMethod(MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build());

        Set<String> classNames = bindingSet.getClassNames(packageName);
        for (String className : classNames) {
            Set<ElementBinding> bindings = bindingSet.getBindings(packageName, className);
            MethodSpec method = generateBindMethod(className, bindings);
            classBuilder.addMethod(method);
        }

        return classBuilder.build();
    }

    private MethodSpec generateBindMethod(String className, Set<ElementBinding> elementBindings) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ClassName.bestGuess(className), "target");

        for (ElementBinding elementBinding : elementBindings) {
            methodBuilder.addStatement("target.$N = ($T) target.findViewById($L)",
                    elementBinding.getName(),
                    ClassName.bestGuess(elementBinding.getType()),
                    elementBinding.getId());
        }

        return methodBuilder.build();
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
