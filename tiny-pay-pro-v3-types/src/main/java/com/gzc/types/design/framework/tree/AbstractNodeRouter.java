package com.gzc.types.design.framework.tree;

public abstract class AbstractNodeRouter<T, D, R> implements NodePointer<T, D, R>, NodeHandler<T, D, R> {


    protected NodeHandler<T, D, R>  defaultHandler = NodeHandler.DEFAULT;

    public R router(T reqParam, D context) throws Exception{
        NodeHandler<T, D, R> nodeHandler = get(reqParam, context);
        if (nodeHandler != null) return nodeHandler.apply(reqParam, context);

        return defaultHandler.apply(reqParam, context);
    }
}
