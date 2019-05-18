package com.xj.scud.processor;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import com.xj.scud.annotation.Async;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;
import java.util.HashSet;
import java.util.Set;

/**
 * Desc: 遍历抽象语法树
 * <p>
 * Created by xiajun
 * Date: 2018/2/4
 */
public abstract class ScudTranslator extends TreeTranslator {

    protected TreeMaker treeMaker;
    protected Names names;
    protected Messager messager;

    /**
     * 需要插入的Getter和Setter方法
     */
    private List<JCTree> methodList2 = List.nil();
    private Set<String> methodNames = new HashSet<>();

    public ScudTranslator(TreeMaker treeMaker, Names names, Messager messager) {
        this.treeMaker = treeMaker;
        this.names = names;
        this.messager = messager;
    }

    /**
     * 遍历到类的时候执行
     */
    @Override
    public void visitClassDef(JCClassDecl jcClassDecl) {
        List<JCTree> vars = jcClassDecl.defs;
        for (JCTree var : vars) {
            if (var.getKind() == Tree.Kind.METHOD) {
                methodNames.add(((JCMethodDecl) var).name.toString());
            }
        }
        super.visitClassDef(jcClassDecl);
        if (!methodList2.isEmpty()) {
            jcClassDecl.defs = jcClassDecl.defs.appendList(this.methodList2);
        }
        this.result = jcClassDecl;
    }

    @Override
    public void visitMethodDef(JCMethodDecl jcMethodDecl) {
        super.visitMethodDef(jcMethodDecl);
        JCModifiers modifiers = jcMethodDecl.getModifiers();
        List<JCAnnotation> annotations = modifiers.getAnnotations();
        if (annotations == null || annotations.size() <= 0) {
            return;
        }
        for (JCAnnotation annotation : annotations) {
            if (Async.class.getName().equals(annotation.type.toString())) {
                if (this.checkRepetition(jcMethodDecl.name.toString())) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "取消异步方法生成 name[" + jcMethodDecl.name.toString() + "] 该方法已经存在异步方法或本身就是异步方法.");
                    return;
                }
                JCMethodDecl getterMethod = createAsyncMethod(jcMethodDecl);
                this.methodList2 = this.methodList2.append(getterMethod);
                messager.printMessage(Diagnostic.Kind.WARNING, "生成异步方法 name [" + getterMethod.name.toString() + "]" + getterMethod.toString());
            }
        }
    }

    public abstract JCMethodDecl createAsyncMethod(JCMethodDecl jcMethodDecl);


    protected JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, getNameFromString(componentArray[i]));
        }
        return expr;
    }

    protected Name getNameFromString(String s) {
        return names.fromString(s);
    }

    protected JCExpression autoTape(Type type) {
        String name = null;
        switch (type.getTag()) {
            case BYTE:
                name = Byte.class.getName();
                break;
            case CHAR:
                name = Character.class.getName();
                break;
            case SHORT:
                name = Short.class.getName();
                break;
            case INT:
                name = Integer.class.getName();
                break;
            case LONG:
                name = Long.class.getName();
                break;
            case FLOAT:
                name = Float.class.getName();
                break;
            case DOUBLE:
                name = Double.class.getName();
                break;
            case BOOLEAN:
                name = Boolean.class.getName();
                break;
            case VOID:
                name = Void.class.getName();
                break;
        }
        if (name != null) {
            return memberAccess(name);
        }
        return null;
    }

    protected boolean checkRepetition(String methodName) {
        if (methodName.endsWith("Async")) {
            return true;
        }
        methodName = methodName + "Async";
        if (methodNames.contains(methodName)) {
            return true;
        }
        return false;
    }
}
