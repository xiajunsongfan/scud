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
 * Mail: chk19940609@gmail.com
 * Created by IceMimosa
 * Date: 2018/2/4
 */
public class ScudAsyncTreeTranslator extends TreeTranslator {

    private TreeMaker treeMaker;
    private Names names;
    private Messager messager;

    /**
     * 需要插入的Getter和Setter方法
     */
    private List<JCTree> methodList2 = List.nil();
    private Set<String> methodNames = new HashSet<>();

    public ScudAsyncTreeTranslator(TreeMaker treeMaker, Names names, Messager messager) {
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

    private JCMethodDecl createAsyncMethod(JCMethodDecl jcMethodDecl) {
        messager.printMessage(Diagnostic.Kind.WARNING, "开始编译异步方法 name [" + jcMethodDecl.name.toString() + "]");
        List<JCVariableDecl> params = jcMethodDecl.params;
        List<JCExpression> thorns = jcMethodDecl.thrown;
        boolean isThrown = false;
        if (!thorns.isEmpty()) {
            isThrown = true;
        }
        //ListBuffer<JCStatement> statements = new ListBuffer<>();
        /*JCExpressionStatement printVar = treeMaker.Exec(treeMaker.Apply(
                List.of(memberAccess("java.lang.String")),//参数类型
                memberAccess("java.lang.System.out.println"),
                List.of(treeMaker.Literal("xiao"))
        ));*/

        String future = "java.util.concurrent.CompletableFuture";
        String futureMethod = future + ".supplyAsync";
        try {
            JCExpression returnMethodType = memberAccess(future);
            JCExpression generics = autoTape(jcMethodDecl.getReturnType().type);
            if (generics == null) {
                List<Type> ps = List.of(jcMethodDecl.getReturnType().type);
                returnMethodType = treeMaker.TypeApply(returnMethodType, treeMaker.Types(ps));
            } else {
                List<JCExpression> ps = List.of(generics);
                returnMethodType = treeMaker.TypeApply(returnMethodType, ps);
                if (TypeTag.VOID == jcMethodDecl.getReturnType().type.getTag()) {
                    futureMethod = future + ".runAsync";
                }
            }

            List<JCExpression> args = List.nil();
            List<JCVariableDecl> decls = jcMethodDecl.getParameters();
            for (JCVariableDecl decl : decls) {
                args = args.append(treeMaker.Ident(decl.name));
            }

            JCLambda lambda;
            JCStatement tryBody = treeMaker.Exec(
                    treeMaker.Apply(List.nil(), treeMaker.Select(treeMaker.Ident(jcMethodDecl.name), jcMethodDecl.name).selected, args)
            );
            if (isThrown) {
                if (TypeTag.VOID != jcMethodDecl.getReturnType().type.getTag()) {
                    tryBody = treeMaker.Return(((JCExpressionStatement) tryBody).expr);
                }
                JCBlock catchBody = treeMaker.Block(0, List.of(treeMaker.Throw(treeMaker.NewClass(null, List.nil(), memberAccess("java.lang.RuntimeException"), List.of(treeMaker.Ident(getNameFromString("ex"))), null))));
                JCVariableDecl catchVar = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), names.fromString("ex"), memberAccess("java.lang.Exception"), null);
                //catchVar.getModifiers().flags |= Flags.PARAMETER;
                JCCatch catchB = treeMaker.Catch(catchVar, catchBody);
                JCTry aTry = treeMaker.Try(treeMaker.Block(0, List.of(tryBody)), List.of(catchB), null);
                lambda = treeMaker.Lambda(List.nil(), treeMaker.Block(0, List.of(aTry)));
            } else {
                lambda = treeMaker.Lambda(List.nil(), ((JCExpressionStatement) tryBody).expr);
            }
            JCReturn jcReturn = treeMaker.Return(
                    treeMaker.Exec(
                            treeMaker.Apply(List.nil(), memberAccess(futureMethod), List.of(lambda))
                    ).expr);
            JCBlock methodBody = treeMaker.Block(0, List.of(jcReturn));
            return treeMaker.MethodDef(
                    treeMaker.Modifiers(Flags.PUBLIC),
                    names.fromString(jcMethodDecl.getName().toString() + "Async"),
                    // 方法返回的类型
                    returnMethodType,
                    List.nil(),
                    // 方法参数
                    params,
                    // throw表达式
                    List.nil(),
                    // 方法体
                    methodBody,
                    null
            );
            //return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC),names.fromString(jcMethodDecl.getName().toString() + "Async"),
            //        treeMaker.Type(new Type.JCVoidType()),List.nil(),List.nil(),List.nil(),treeMaker.Block(0,List.of(printVar)),null);
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "构建异步方法失败" + e.getMessage());
        }
        return null;
    }


    private JCExpression memberAccess(String components) {
        String[] componentArray = components.split("\\.");
        JCExpression expr = treeMaker.Ident(getNameFromString(componentArray[0]));
        for (int i = 1; i < componentArray.length; i++) {
            expr = treeMaker.Select(expr, getNameFromString(componentArray[i]));
        }
        return expr;
    }

    private Name getNameFromString(String s) {
        return names.fromString(s);
    }

    /**
     * 首字母大写
     */
    public String toTitleCase(String str) {
        char first = str.charAt(0);
        if (first >= 'a' && first <= 'z') {
            first -= 32;
        }
        return first + str.substring(1);
    }

    private JCExpression autoTape(Type type) {
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

    private boolean checkRepetition(String methodName) {
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
