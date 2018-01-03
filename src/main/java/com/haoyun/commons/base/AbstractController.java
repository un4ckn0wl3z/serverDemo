package com.haoyun.commons.base;

/**
 * @author
 *         Created on 2017/12/26
 */
public abstract class AbstractController <S extends AbstractService>{

    protected S service;

    protected abstract void setService(S service);
}
