package com.yuhao.deepoj.judge.codesandbox.proxy;

import com.yuhao.deepoj.judge.codesandbox.CodeSandbox;
import com.yuhao.deepoj.judge.codesandbox.CodeSandboxFactory;

import java.lang.reflect.Proxy;

/**
 * 动态代理类
 */
public class CodeSandboxDynamicProxy {
    public static CodeSandbox createCodeSandboxProxy(String type) {
        CodeSandbox factoryInstance = CodeSandboxFactory.newInstance(type);
        return (CodeSandbox) Proxy.newProxyInstance(
                factoryInstance.getClass().getClassLoader(),
                factoryInstance.getClass().getInterfaces(),
                new DebugInvocationHandler(factoryInstance));
    }
}
