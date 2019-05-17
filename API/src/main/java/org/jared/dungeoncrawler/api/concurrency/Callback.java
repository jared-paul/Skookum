package org.jared.dungeoncrawler.api.concurrency;

public interface Callback<T>
{
    void onSuccess(T t);

    void onFailure(Throwable cause);
}
