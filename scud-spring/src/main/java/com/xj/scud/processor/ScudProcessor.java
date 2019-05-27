package com.xj.scud.processor;

import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;
import com.xj.scud.annotation.Scud;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * Desc: 注解处理类
 * <p>
 * Created by xiajun
 * Date: 2019/2/17
 */
//@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.xj.scud.annotation.*"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ScudProcessor extends AbstractProcessor {

    /**
     * 语法树
     */
    private Trees trees;

    /**
     * 树节点创建工具类
     */
    private TreeMaker treeMaker;

    /**
     * 命名工具类
     */
    private Names names;

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = Trees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(Scud.class);
        messager.printMessage(Diagnostic.Kind.WARNING, "扫描Scud注解的类");
        if (!roundEnv.processingOver()) {
            for (Element element : elementsAnnotatedWith) {
                messager.printMessage(Diagnostic.Kind.WARNING, "开始处理 class name [" + element.getSimpleName().toString() + "]");
                if (element.getKind() == ElementKind.CLASS) {
                    JCTree tree = (JCTree) trees.getTree(element);
                    tree.accept(new ScudAsyncTranslator(treeMaker, names, messager));
                }
            }
            for (Element element : roundEnv.getRootElements()) {
                if (element.getKind() == ElementKind.INTERFACE) {
                    JCTree tree = (JCTree) trees.getTree(element);
                    tree.accept(new ScudInterfaceTranslator(treeMaker, names, messager));
                }
            }
        }
        messager.printMessage(Diagnostic.Kind.WARNING, "生成异步方法完成");

        return true;
    }
}
