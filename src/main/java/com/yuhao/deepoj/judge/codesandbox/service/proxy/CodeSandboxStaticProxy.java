package com.yuhao.deepoj.judge.codesandbox.service.proxy;

import com.yuhao.deepoj.judge.codesandbox.service.CodeSandbox;
import com.yuhao.deepoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yuhao.deepoj.judge.codesandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 静态代理类
 */
@Slf4j
public class CodeSandboxStaticProxy implements CodeSandbox {
    private final CodeSandbox codeSandboxImpl;


    public CodeSandboxStaticProxy(CodeSandbox codeSandboxImpl) {
        this.codeSandboxImpl = codeSandboxImpl;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandboxImpl.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
