/*
 * Copyright (c) 2012-2017 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package io.moquette.spi.impl.subscriptions;

import io.moquette.spi.ISessionsStore;
import io.moquette.spi.ISubscriptionsStore;

import java.util.List;

public interface ISubscriptionsDirectory {

    void init(ISessionsStore sessionsStore);

    void add(ISubscriptionsStore.ClientTopicCouple newSubscription);

    void removeSubscription(Topic topic, String clientID);

    List<Subscription> matches(Topic topic);

    int size();

    String dumpTree();
}
