package com.xj.scud.spring.bean;

/**
 * Author: baichuan - xiajun
 * Date: 2017/04/13 15:25
 */
public class ProviderBean<T> {
    private Class<T> interfaze;
    private T ref;
    private String version;

    public Class<T> getInterfaze() {
        return interfaze;
    }

    public void setInterfaze(Class<T> interfaze) {
        this.interfaze = interfaze;
    }

    public T getRef() {
        return ref;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ProviderBean<?> that = (ProviderBean<?>) o;
        return interfaze.equals(that.interfaze);
    }

    @Override
    public int hashCode() {
        return interfaze.hashCode();
    }

    @Override
    public String toString() {
        return "ProviderBean{" +
                "interfaze=" + interfaze +
                ", ref=" + ref +
                '}';
    }
}
