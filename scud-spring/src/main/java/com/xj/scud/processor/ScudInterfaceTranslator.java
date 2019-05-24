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
public class ScudInterfaceTranslator extends ScudTranslator {

    public ScudInterfaceTranslator(TreeMaker treeMaker, Names names, Messager messager) {
        super(treeMaker, names, messager);
    }

    public JCMethodDecl createAsyncMethod(JCMethodDecl jcMethodDecl) {
        messager.printMessage(Diagnostic.Kind.WARNING, "开始编译异步方法 name [" + jcMethodDecl.name.toString() + "]");

        List<JCVariableDecl> params = jcMethodDecl.params;
        List<JCExpression> thorns = jcMethodDecl.thrown;

        String future = "java.util.concurrent.CompletableFuture";
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
                }
            }

            return treeMaker.MethodDef(
                    treeMaker.Modifiers(Flags.PUBLIC),
                    names.fromString(jcMethodDecl.getName().toString() + "Async"),
                    // 方法返回的类型
                    returnMethodType, List.nil(), params, thorns, null, null
            );
        } catch (Exception e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "构建异步方法失败" + e.getMessage());
        }
        return null;
    }
}
