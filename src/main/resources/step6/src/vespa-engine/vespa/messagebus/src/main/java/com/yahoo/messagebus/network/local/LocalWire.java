// Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root. (rank 298) copied from https://github.com/vespa-engine/vespa/blob/f8dfd639931905a9d6dae95f5b4e0ae812042117/messagebus/src/main/java/com/yahoo/messagebus/network/local/LocalWire.java
package com.yahoo.messagebus.network.local;

import com.yahoo.jrt.slobrok.api.IMirror;
import com.yahoo.jrt.slobrok.api.Mirror;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * @author Simon Thoresen Hult
 */
public class LocalWire implements IMirror {

    private final AtomicInteger serviceId = new AtomicInteger();
    private final AtomicInteger updateCnt = new AtomicInteger();
    private final ConcurrentHashMap<String, LocalNetwork> services = new ConcurrentHashMap<>();

    public void registerService(String serviceName, LocalNetwork owner) {
        if (services.putIfAbsent(serviceName, owner) != null) {
            throw new IllegalStateException();
        }
        updateCnt.incrementAndGet();
    }

    public void unregisterService(String serviceName) {
        services.remove(serviceName);
        updateCnt.incrementAndGet();
    }

    public LocalServiceAddress resolveServiceAddress(String serviceName) {
        final LocalNetwork owner = services.get(serviceName);
        return owner != null ? new LocalServiceAddress(serviceName, owner) : null;
    }

    public String newHostId() {
        return "tcp/local:" + serviceId.getAndIncrement();
    }

    @Override
    public List<Mirror.Entry> lookup(String pattern) {
        List<Mirror.Entry> out = new ArrayList<>();
        Pattern regex = Pattern.compile(pattern.replace("*", "[a-zA-Z0-9_-]+"));
        for (String key : services.keySet()) {
            if (regex.matcher(key).matches()) {
                out.add(new Mirror.Entry(key, key));
            }
        }
        return out;
    }

    @Override
    public int updates() {
        return updateCnt.get();
    }

}
