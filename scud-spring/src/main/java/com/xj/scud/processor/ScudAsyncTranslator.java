package com.xj.scud.processor;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * Desc: 遍历抽象语法树
 * <p>
 * Created by xiajun
 * Date: 2018/2/4
 */
public class ScudAsyncTranslator extends ScudTranslator {

    public ScudAsyncTranslator(TreeMaker treeMaker, Names names, Messager messager) {
        super(treeMaker, names, messager);
    }


    public JCMethodDecl createAsyncMethod(JCMethodDecl jcMethodDecl) {
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
}
