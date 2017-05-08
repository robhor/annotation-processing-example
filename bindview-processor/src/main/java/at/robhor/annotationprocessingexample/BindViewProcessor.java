package at.robhor.annotationprocessingexample;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

public class BindViewProcessor extends AbstractProcessor {

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BindView.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        BindingSet bindingSet = buildBindingSet(roundEnvironment);
        Set<JavaFile> javaFiles = generateBinderClasses(bindingSet);
        writeFiles(javaFiles);
        return true;
    }

    private BindingSet buildBindingSet(RoundEnvironment roundEnvironment) {
        BindingSet bindingSet = new BindingSet();

        Set<? extends Element> elementsToBind = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        elementsToBind.forEach(element -> addElementBinding(bindingSet, element));

        return bindingSet;
    }

    private void addElementBinding(BindingSet bindingSet, Element element) {
        ViewBinding elementBinding = getViewBinding(element);
        if (elementBinding == null) {
            return;
        }

        TypeElement classElement = (TypeElement) element.getEnclosingElement();
        PackageElement packageElement = getPackage(classElement);

        bindingSet.addBinding(packageElement, classElement, elementBinding);
    }

    private PackageElement getPackage(Element element) {
        while (element.getKind() != ElementKind.PACKAGE) {
            element = element.getEnclosingElement();
        }

        return (PackageElement) element;
    }

    private ViewBinding getViewBinding(Element element) {
        if (!isFieldAccessible(element)) {
            Messager messager = processingEnv.getMessager();
            messager.printMessage(Diagnostic.Kind.ERROR, "Field not accessible, it cannot be private or static to bind");
            return null;
        }

        BindView annotation = element.getAnnotation(BindView.class);
        int viewId = annotation.value();
        TypeMirror type = element.asType();
        String name = element.getSimpleName().toString();

        return new ViewBinding(type, name, viewId);
    }

    private boolean isFieldAccessible(Element element) {
        Set<Modifier> modifiers = element.getModifiers();
        return !modifiers.contains(PRIVATE) && !modifiers.contains(STATIC);
    }

    private Set<JavaFile> generateBinderClasses(BindingSet bindingSet) {
        Set<JavaFile> files = new HashSet<>();

        for (PackageElement packageElement : bindingSet.getPackages()) {
            TypeSpec binderClass = generateBinderClass(bindingSet, packageElement);
            String packageName = packageElement.getQualifiedName().toString();
            JavaFile javaFile = JavaFile.builder(packageName, binderClass).build();
            files.add(javaFile);
        }

        return files;
    }

    private TypeSpec generateBinderClass(BindingSet bindingSet, PackageElement packageElement) {
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder("ViewBinder")
                .addModifiers(FINAL)
                .addMethod(MethodSpec.constructorBuilder().addModifiers(PRIVATE).build());

        Set<TypeElement> classes = bindingSet.getClasses(packageElement);
        for (TypeElement classElement : classes) {
            Set<ViewBinding> bindings = bindingSet.getBindings(packageElement, classElement);
            MethodSpec method = generateBindMethod(classElement, bindings);
            classBuilder.addMethod(method);
        }

        return classBuilder.build();
    }

    private MethodSpec generateBindMethod(TypeElement classElement, Set<ViewBinding> elementBindings) {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("bind")
                .addModifiers(PUBLIC, STATIC)
                .addParameter(ClassName.get(classElement), "target");

        for (ViewBinding elementBinding : elementBindings) {
            methodBuilder.addStatement("target.$N = ($T) target.findViewById($L)",
                    elementBinding.getName(),
                    ClassName.get(elementBinding.getType()),
                    elementBinding.getViewId());
        }

        return methodBuilder.build();
    }

    private void writeFiles(Collection<JavaFile> javaFiles) {
        javaFiles.forEach(this::writeFile);
    }

    private void writeFile(JavaFile javaFile) {
        Filer filer = processingEnv.getFiler();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            Messager messager = processingEnv.getMessager();
            String message = String.format("Unable to write file: %s", e.getMessage());
            messager.printMessage(Diagnostic.Kind.ERROR, message);
        }
    }
}
