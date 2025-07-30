package com.gzc.types.design.framework.tree;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public abstract class AbstractMultiThreadNodeRouter<T, D, R> extends AbstractNodeRouter<T, D, R> {


    @Override
    public R apply(T reqParam, D context) throws Exception {

        // 异步加载数据
        multiThread(reqParam, context);
        // 业务流程受理
        return doApply(reqParam, context);
    }

    /**
     * 异步加载数据
     */
    protected abstract void multiThread(T requestParameter, D dynamicContext) throws ExecutionException, InterruptedException, TimeoutException;

    /**
     * 业务流程受理
     */
    protected abstract R doApply(T requestParameter, D dynamicContext) throws Exception;
}
