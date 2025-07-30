package com.gzc.types.design.framework.tree;

public interface NodePointer<T, D, R> {


    /**
     * 获取待执行策略
     *
     * @param reqParam 入参
     * @param context   上下文
     * @return 返参
     * @throws Exception 异常
     */
    NodeHandler<T, D, R> get(T reqParam, D context) throws Exception;
}
