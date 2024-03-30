package com.yuhao.deepoj.judge.codesandbox;

import com.yuhao.deepoj.constant.CommonConstant;
import com.yuhao.deepoj.judge.codesandbox.service.impl.RemoteCodeSandbox;
import com.yuhao.deepoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yuhao.deepoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yuhao.deepoj.judge.codesandbox.service.proxy.CodeSandboxDynamicProxy;
import com.yuhao.deepoj.judge.codesandbox.service.CodeSandbox;
import com.yuhao.deepoj.judge.codesandbox.service.factory.CodeSandboxFactory;
import com.yuhao.deepoj.model.enums.SubmissionLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SpringBootTest
class CodeSandboxTest {
    @Value("${codesandbox.type:example}")
    private String type;

    @Test
    void executeCodeByType() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        String code = "import java.io.*;\n" +
                "import java.util.*;\n" +
                "public class Main {\n" +
                "    public static void main(String args[]) throws Exception {\n" +
                "        Scanner cin=new Scanner(System.in);\n" +
                "        int a = cin.nextInt(), b = cin.nextInt();\n" +
                "        System.out.println(a+b);\n" +
//                        "        Thread.sleep(5 * 60 * 1000);\n" +
                "    }\n" +
                "}";
        String language = SubmissionLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .timeLimit(1000L)
                .memoryLimit(256L)
                .stackLimit(CommonConstant.MB)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }

    @Test
    void executeCodeByProxy() {
        CodeSandbox codeSandbox = CodeSandboxDynamicProxy.createCodeSandboxProxy(type);
        String code = "import java.io.*;\n" +
                "import java.util.*;\n" +
                "public class Main {\n" +
                "    public static void main(String args[]) throws Exception {\n" +
                "        Scanner cin=new Scanner(System.in);\n" +
                "        int a = cin.nextInt(), b = cin.nextInt();\n" +
                "        System.out.println(a+b);\n" +
                "    }\n" +
                "}";
        String language = SubmissionLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .timeLimit(2000L)
                .memoryLimit(256L)
                .stackLimit(CommonConstant.MB)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }
}