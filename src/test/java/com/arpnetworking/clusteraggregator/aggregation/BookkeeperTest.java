/**
 * Copyright 2014 Groupon.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arpnetworking.clusteraggregator.aggregation;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.UnhandledMessage;
import akka.testkit.TestActorRef;
import akka.testkit.TestProbe;
import com.arpnetworking.clusteraggregator.bookkeeper.persistence.BookkeeperPersistence;
import com.arpnetworking.clusteraggregator.models.BookkeeperData;
import com.arpnetworking.utility.BaseActorTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Tests for the cluster metrics Bookkeeper.
 *
 * @author Brandon Arp (brandon dot arp at inscopemetrics dot com)
 */
public class BookkeeperTest extends BaseActorTest {

    @Before
    @Override
    public void setUp() {
        super.setUp();
        Mockito.when(_persistence.getBookkeeperData())
                .thenReturn(
                        CompletableFuture.completedFuture(
                                new BookkeeperData.Builder()
                                        .setClusters(0L)
                                        .setMetrics(0L)
                                        .setServices(0L)
                                        .setStatistics(0L)
                                        .build()));
    }

    @Test
    public void propsCreation() {
        TestActorRef.create(getSystem(), Bookkeeper.props(_persistence));
    }

    @Test
    public void doesNotSwallowUnhandled() {
        final TestProbe probe = TestProbe.apply(getSystem());
        final TestActorRef<Actor> ref = TestActorRef.create(getSystem(), Bookkeeper.props(_persistence));
        getSystem().eventStream().subscribe(probe.ref(), UnhandledMessage.class);
        ref.tell("notAValidMessage", ActorRef.noSender());
        probe.expectMsgClass(FiniteDuration.apply(3, TimeUnit.SECONDS), UnhandledMessage.class);
    }

    @Test
    public void things() {
        final TestProbe probe = TestProbe.apply(getSystem());
        final TestActorRef<Actor> ref = TestActorRef.create(getSystem(), Bookkeeper.props(_persistence));
        getSystem().eventStream().subscribe(probe.ref(), UnhandledMessage.class);
        ref.tell("notAValidMessage", ActorRef.noSender());
        probe.expectMsgClass(FiniteDuration.apply(3, TimeUnit.SECONDS), UnhandledMessage.class);
    }

    @Mock
    private BookkeeperPersistence _persistence;
}
