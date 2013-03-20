package org.trinity.foundation.api.shared;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;

import javax.annotation.concurrent.ThreadSafe;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

@ThreadSafe
public class AsyncListenableEventBus extends EventBus implements AsyncListenable {

	private final Map<Object, AsyncEventBus> asyncEventBusByListener = new WeakHashMap<Object, AsyncEventBus>();
	private final ListeningExecutorService listenableExecutorService;

	public AsyncListenableEventBus(final ListeningExecutorService postingExecutorService) {
		this.listenableExecutorService = postingExecutorService;
	}

	@Override
	public void register(final Object object) {
		register(	object,
					MoreExecutors.sameThreadExecutor());
	}

	@Override
	public void register(	final Object object,
							final ExecutorService executor) {
		this.listenableExecutorService.submit(	new Runnable() {

													@Override
													public void run() {
														final AsyncEventBus asyncEventBus = new AsyncEventBus(executor);
														asyncEventBus.register(object);
														AsyncListenableEventBus.this.asyncEventBusByListener
																.put(	object,
																		asyncEventBus);
													}
												},
												null);
	}

	@Override
	public void unregister(final Object object) {
		this.listenableExecutorService.submit(	new Runnable() {
													@Override
													public void run() {
														AsyncListenableEventBus.this.asyncEventBusByListener
																.remove(object);
													}
												},
												null);
	}

	@Override
	public void post(final Object event) {
		this.listenableExecutorService.submit(	new Runnable() {
													@Override
													public void run() {
														for (final AsyncEventBus asyncEventBus : AsyncListenableEventBus.this.asyncEventBusByListener
																.values()) {
															asyncEventBus.post(event);
														}
													}
												},
												null);
	}

}
