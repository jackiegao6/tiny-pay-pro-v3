package com.gzc.types.design.framework.tree;


public interface NodeHandler<T, D, R> {

    NodeHandler  DEFAULT  = (T, D) -> null;

    R apply(T reqParam, D context) throws Exception;
}
