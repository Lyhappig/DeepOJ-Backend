package com.yuhao.deepoj.judge.codesandbox.service.proxy;

import com.yuhao.deepoj.judge.codesandbox.service.CodeSandbox;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationHandler;

@Slf4j
public class DebugInvocationHandler implements InvocationHandler {
    private final CodeSandbox codeSandboxImpl;

    public DebugInvocationHandler(CodeSandbox codeSandboxImpl) {
        this.codeSandboxImpl = codeSandboxImpl;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        log.info("代码沙箱请求信息：" + args[0].toString());
        Object result = method.invoke(codeSandboxImpl, args);
        log.info("代码沙箱响应信息：" + result.toString());
        return result;
    }
}
