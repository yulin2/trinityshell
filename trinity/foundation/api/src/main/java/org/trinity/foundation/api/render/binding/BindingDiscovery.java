package org.trinity.foundation.api.render.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.trinity.foundation.api.display.input.Input;
import org.trinity.foundation.api.display.input.InputModifiers;
import org.trinity.foundation.api.display.input.Keyboard;
import org.trinity.foundation.api.display.input.Momentum;
import org.trinity.foundation.api.render.binding.refactor.model.InputSlot;
import org.trinity.foundation.api.render.binding.refactor.model.View;
import org.trinity.foundation.api.render.binding.refactor.model.ViewProperty;
import org.trinity.foundation.api.render.binding.refactor.view.BoundInputEvent;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignal;
import org.trinity.foundation.api.render.binding.refactor.view.InputSignals;
import org.trinity.foundation.api.render.binding.refactor.view.PropertySlot;

import ca.odell.glazedlists.EventList;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.devsurf.injection.guice.annotations.Bind;
import de.devsurf.injection.guice.annotations.To;
import de.devsurf.injection.guice.annotations.To.Type;

@Singleton
@Bind(to = @To(value = Type.IMPLEMENTATION))
public class BindingDiscovery {

	// view properties
	private static final Cache<Class<?>, Method[]> allViewProperties = CacheBuilder.newBuilder().build();
	private static final Cache<Class<?>, Cache<String, Optional<Method>>> viewPropertiesByName = CacheBuilder
			.newBuilder().build();
	private static final Cache<Class<?>, Optional<Method>> views = CacheBuilder.newBuilder().build();
	private static final Cache<Class<?>, Cache<String, Optional<Method>>> viewSlots = CacheBuilder.newBuilder().build();

	// view inputs
	private static final Cache<Class<?>, Cache<Class<? extends Input>, Cache<String, Optional<Method>>>> inputSlots = CacheBuilder
			.newBuilder().build();
	private static final Cache<Class<?>, Method[]> inputEmitters = CacheBuilder.newBuilder().build();

	// observable collections
	private static final Cache<Class<?>, Method[]> allObservableCollections = CacheBuilder.newBuilder().build();
	private static final Cache<Class<?>, Cache<String, Optional<Method>>> observedCollectionsByName = CacheBuilder
			.newBuilder().build();

	// data contexes
	private static final Cache<Object, Object> dataContextsBySubDataContex = CacheBuilder.newBuilder().build();

	private final ViewSlotHandler viewSlotHandler;
	private final Keyboard keyboard;

	@Inject
	BindingDiscovery(final Keyboard keyboard, final ViewSlotHandler viewSlotHandler) {
		this.keyboard = keyboard;
		this.viewSlotHandler = viewSlotHandler;
	}

	/****************************************
	 * Find all methods that have been annotated with {@link ViewProperty}.
	 * 
	 * @param dataContextClass
	 *            The {@link Class} to scan for annotated methods.
	 * @return All found {@link Method}s
	 * @throws ExecutionException
	 *             Thrown if a badly placed annotation is encountered. ie on a
	 *             non public method, or duplicate properties are found.
	 *************************************** 
	 */
	public Method[] lookupAllViewProperties(final Class<?> dataContextClass) throws ExecutionException {
		return BindingDiscovery.allViewProperties.get(	dataContextClass,
														new Callable<Method[]>() {
															@Override
															public Method[] call() {
																return getAllViewProperties(dataContextClass);
															}
														});
	}

	protected Method[] getAllViewProperties(final Class<?> dataContextClass) {
		final List<Method> allViewProperties = new ArrayList<Method>();

		final List<ViewProperty> viewPropertyAnnotations = new ArrayList<ViewProperty>();

		for (final Method method : dataContextClass.getMethods()) {
			final ViewProperty viewPropertyAnnotation = method.getAnnotation(ViewProperty.class);
			if (viewPropertyAnnotation == null) {
				continue;
			}

			if (isDuplicateViewProperty(viewPropertyAnnotations,
										viewPropertyAnnotation)) {
				throw new IllegalArgumentException(String.format(	"Found duplicate %s with name=%s on %s",
																	ViewProperty.class.getName(),
																	viewPropertyAnnotation.value(),
																	dataContextClass.getName()));
			}

			viewPropertyAnnotations.add(viewPropertyAnnotation);
			allViewProperties.add(method);
		}

		return allViewProperties.toArray(new Method[] {});
	}

	/***************************************
	 * Find the method annotated with {@link ViewProperty} and the specified
	 * name.
	 * 
	 * @param dataContextClass
	 *            The {@link Class} to scan for the view property.
	 * @param viewPropertyName
	 *            The name of the view property.
	 * @return a {@link Method}.
	 * @throws ExecutionException
	 *             Thrown if a badly placed annotation is encountered. ie on a
	 *             non public method, or duplicate properties are found.
	 *************************************** 
	 */
	public Optional<Method> lookupViewProperty(	final Class<?> dataContextClass,
												final String viewPropertyName) throws ExecutionException {
		return BindingDiscovery.viewPropertiesByName.get(	dataContextClass,
															new Callable<Cache<String, Optional<Method>>>() {
																@Override
																public Cache<String, Optional<Method>> call() {
																	return CacheBuilder.newBuilder().build();
																}
															}).get(	viewPropertyName,
																	new Callable<Optional<Method>>() {
																		@Override
																		public Optional<Method> call() {
																			return getViewProperty(	dataContextClass,
																									viewPropertyName);
																		}
																	});
	}

	protected Optional<Method> getViewProperty(	final Class<?> clazz,
												final String viewPropertyName) {

		final Method[] viewProperties = getAllViewProperties(clazz);

		Method foundMethod = null;
		for (final Method method : viewProperties) {
			final String foundViewPropertyName = findViewPropertyName(method);
			if (foundViewPropertyName.equals(viewPropertyName)) {
				foundMethod = method;
				break;
			}
		}

		return Optional.fromNullable(foundMethod);
	}

	protected String findViewPropertyName(final Method method) {
		final String viewPropertyName = method.getAnnotation(ViewProperty.class).value();
		if (!viewPropertyName.equals("")) {
			return viewPropertyName;
		}

		final StringBuilder sb = new StringBuilder();
		final String methodName = method.getName();

		int methodNameFirstCharIdx = 0;
		if (methodName.startsWith("get")) {
			methodNameFirstCharIdx = 3;
		} else if (methodName.startsWith("is")) {
			methodNameFirstCharIdx = 2;
		}

		return sb.append(Character.toLowerCase(methodName.charAt(methodNameFirstCharIdx)))
				.append(methodName.substring(methodNameFirstCharIdx + 1)).toString();

	}

	protected boolean isDuplicateViewProperty(	final List<ViewProperty> viewProperties,
												final ViewProperty viewProperty) {
		for (final ViewProperty addedViewAttribute : viewProperties) {
			if (addedViewAttribute.value().equals(viewProperty.value())) {
				return true;
			}
		}
		return false;
	}

	protected boolean isDuplicateObservableCollection(	final List<ObservableCollection> observableCollections,
														final ObservableCollection observableCollection) {
		for (final ObservableCollection addedObservableCollection : observableCollections) {
			if (addedObservableCollection.name().equals(observableCollection.name())) {
				return true;
			}
		}
		return false;
	}

	/***************************************
	 * Find the getter annotated with {@link View}.
	 * 
	 * @param dataContextClass
	 *            The {@link Class} to scan.
	 * @return The found getter method.
	 * @throws ExecutionException
	 *             Thrown if a badly placed annotation is encountered. ie on a
	 *             non public method, or multiple views are found.
	 *************************************** 
	 */
	public Optional<Method> lookupView(final Class<?> dataContextClass) throws ExecutionException {

		return BindingDiscovery.views.get(	dataContextClass,
											new Callable<Optional<Method>>() {
												@Override
												public Optional<Method> call() {
													return getViewReferenceObject(dataContextClass);
												}
											});
	}

	protected Optional<Method> getViewReferenceObject(final Class<?> clazz) {
		Method foundMethod = null;
		final Method[] methods = clazz.getMethods();
		for (final Method method : methods) {
			final View view = method.getAnnotation(View.class);
			if ((view != null) && (foundMethod != null)) {
				throw new IllegalArgumentException(String.format(	"Found multiple %s on %s",
																	View.class.getName(),
																	clazz.getName()));
			} else if (view != null) {
				foundMethod = method;
			}
		}

		return Optional.fromNullable(foundMethod);
	}

	/***************************************
	 * Find the method annotated with a {@link PropertySlot} with the given
	 * name.
	 * 
	 * @param viewClass
	 *            The view {@link Class} to scan.
	 * @param viewPropertyName
	 *            The name that should be present in the
	 *            {@code  PropertySlot}.
	 * @return The found method.
	 * @throws ExecutionException
	 *             Thrown if a badly placed annotation is found.
	 *************************************** 
	 */
	public Optional<Method> lookupViewPropertySlot(	final Class<?> viewClass,
													final String viewPropertyName) throws ExecutionException {
		return BindingDiscovery.viewSlots.get(	viewClass,
												new Callable<Cache<String, Optional<Method>>>() {
													@Override
													public Cache<String, Optional<Method>> call() throws Exception {
														return CacheBuilder.newBuilder().build();
													}
												}).get(	viewPropertyName,
														new Callable<Optional<Method>>() {
															@Override
															public Optional<Method> call() {
																return getViewPropertySlot(	viewClass,
																							viewPropertyName);
															}
														});
	}

	protected Optional<Method> getViewPropertySlot(	final Class<?> viewClass,
													final String viewAttributeName) {
		Method foundMethod = null;

		for (final Method method : viewClass.getMethods()) {

			final PropertySlot viewSlot = method.getAnnotation(PropertySlot.class);

			if (viewSlot == null) {
				continue;
			}

			final String[] values = viewSlot.value();
			if (Arrays.asList(values).contains(viewAttributeName)) {
				if ((foundMethod == null)) {
					foundMethod = method;
				} else if ((foundMethod != null)) {
					throw new IllegalArgumentException(String.format(	"Found multiple identical %s in the class hierarchy of %s",
																		PropertySlot.class.getName(),
																		viewClass.getName()));
				}
			}
		}

		return Optional.fromNullable(foundMethod);
	}

	/***************************************
	 * Manually notify a view slot.
	 * 
	 * @param dataContextClass
	 *            The class of the object the encloses the annotated view
	 *            object.
	 * @param dataContext
	 *            The instance that encloses the view object.
	 * @param propertyNames
	 *            The names of the view properties who's matching view slots
	 *            should be notified.
	 * @throws ExecutionException
	 *             Thrown if a badly placed annotation is encountered.
	 *************************************** 
	 */
	public <T> void notifyViewPropertySlot(	final Class<? extends T> dataContextClass,
											final T dataContext,
											final String... propertyNames) throws ExecutionException {

		final Optional<Method> viewReference = lookupView(dataContextClass);
		final Method viewField = viewReference.get();
		Object view;
		try {
			view = viewField.invoke(dataContext);

			for (final String viewAttributeName : propertyNames) {
				final Optional<Method> viewPropertyMethod = lookupViewProperty(	dataContextClass,
																				viewAttributeName);
				if (!viewPropertyMethod.isPresent()) {
					continue;
				}

				final Optional<Method> viewSlotMethod = lookupViewPropertySlot(	view.getClass(),
																				viewAttributeName);
				if (!viewSlotMethod.isPresent()) {
					continue;
				}

				Object argument;

				argument = viewPropertyMethod.get().invoke(dataContext);

				this.viewSlotHandler.invokeSlot(dataContext,
												view,
												viewSlotMethod.get(),
												argument);

			}
		} catch (final IllegalAccessException e) {
			throw new ExecutionException(e);
		} catch (final InvocationTargetException e) {
			throw new ExecutionException(e);
		}
	}

	public Optional<Method> lookupInputSlot(final Class<?> dataContextClass,
											final Class<? extends Input> inputType,
											final String inputSlotName) throws ExecutionException {
		return inputSlots.get(	dataContextClass,
								new Callable<Cache<Class<? extends Input>, Cache<String, Optional<Method>>>>() {
									@Override
									public Cache<Class<? extends Input>, Cache<String, Optional<Method>>> call()
											throws Exception {
										return CacheBuilder.newBuilder().build();
									}
								}).get(	inputType,
										new Callable<Cache<String, Optional<Method>>>() {
											@Override
											public Cache<String, Optional<Method>> call() throws Exception {
												return CacheBuilder.newBuilder().build();
											}
										}).get(	inputSlotName,
												new Callable<Optional<Method>>() {
													@Override
													public Optional<Method> call() throws Exception {
														return getInputSlot(dataContextClass,
																			inputType,
																			inputSlotName);
													}
												});
	}

	protected Optional<Method> getInputSlot(final Class<?> dataContextClass,
											final Class<? extends Input> inputType,
											final String inputSlotName) {
		final Method[] allMethods = dataContextClass.getMethods();
		Method foundMethod = null;

		for (final Method method : allMethods) {
			final InputSlot inputSlotAnnotation = method.getAnnotation(InputSlot.class);
			if (inputSlotAnnotation == null) {
				continue;
			}

			final Class<? extends Input> validInputType = inputSlotAnnotation.input();
			String validInputSlotName = inputSlotAnnotation.name();
			if (validInputSlotName.equals("")) {
				validInputSlotName = method.getName();
			}
			if (validInputType.isAssignableFrom(inputType) && validInputSlotName.equals(inputSlotName)) {
				if (foundMethod == null) {
					foundMethod = method;
				} else {
					throw new IllegalArgumentException(String.format(	"Found multiple identical %s with name %s in the class hierarchy of %s",
																		InputSlot.class.getName(),
																		inputSlotName,
																		dataContextClass.getName()));
				}
			}
		}
		return Optional.fromNullable(foundMethod);
	}

	public Method[] lookupInputEmitters(final Class<?> viewClass) throws ExecutionException {
		return inputEmitters.get(	viewClass,
									new Callable<Method[]>() {
										@Override
										public Method[] call() throws Exception {
											return getInputEmitters(viewClass);
										}
									});
	}

	protected Method[] getInputEmitters(final Class<?> viewClass) {
		final Method[] allMethods = viewClass.getMethods();

		final List<Method> foundMethods = new ArrayList<Method>();
		for (final Method method : allMethods) {
			final InputSignals inputEmitterAnnotation = method.getAnnotation(InputSignals.class);
			if (inputEmitterAnnotation == null) {
				continue;
			}

			foundMethods.add(method);
		}

		return foundMethods.toArray(new Method[] {});
	}

	public Optional<String> lookupInputEmitterInputSlotName(final Object view,
															final Object possibleInputEmitter,
															final Class<? extends Input> inputType)
			throws ExecutionException {
		final Method[] inputEmitters = lookupInputEmitters(view.getClass());
		String inputSlotName = null;
		for (final Method inputEmitterMethod : inputEmitters) {
			try {
				final Object inputEmitter = inputEmitterMethod.invoke(view);
				if (inputEmitter.equals(possibleInputEmitter)) {
					if (inputSlotName != null) {
						throw new IllegalArgumentException(String.format(	"There is ambiguity as to which slot should handle the given input type. Found multiple matching %s for type %s coming from %s as declared in %s.",
																			InputSignal.class.getName(),
																			inputType.getName(),
																			possibleInputEmitter,
																			view));
					}

					final InputSignals inputEmitterAnnotation = inputEmitterMethod.getAnnotation(InputSignals.class);
					final InputSignal[] inputSignals = inputEmitterAnnotation.value();
					for (final InputSignal inputSignal : inputSignals) {
						final boolean validType = inputSignal.inputType().isAssignableFrom(inputType);
						if (validType) {
							if (inputSlotName != null) {
								throw new IllegalArgumentException(String.format(	"There is ambiguity as to which slot should handle the given input type. Found multiple matching %s for type %s coming from %s as declared in %s.",
																					InputSignal.class.getName(),
																					inputType.getName(),
																					possibleInputEmitter,
																					view));
							}
							inputSlotName = inputSignal.name();
						}
					}
				}
			} catch (final IllegalAccessException e) {
				throw new ExecutionException(e);
			} catch (final InvocationTargetException e) {
				throw new ExecutionException(e);
			}
		}
		return Optional.fromNullable(inputSlotName);
	}

	public <T> void notifyDataContextInputSlot(	final Class<? extends T> dataContextClass,
												final T dataContext,
												final BoundInputEvent boundInputEvent) {
		try {
			final Input input = boundInputEvent.getInput();
			final Optional<Method> optionalInputSlot = lookupInputSlot(	dataContext.getClass(),
																		input.getClass(),
																		boundInputEvent.getInputSlotName());
			if (optionalInputSlot.isPresent()) {
				final Method inputSlot = optionalInputSlot.get();
				final InputSlot inputSlotAnnotation = inputSlot.getAnnotation(InputSlot.class);

				final InputModifiers slotInputModifiers = this.keyboard.modifiers(inputSlotAnnotation.modifier());
				final Momentum[] slotMomenta = inputSlotAnnotation.momentum();
				final InputModifiers inputModifiers = input.getInputModifiers();
				final Momentum inputMomentum = input.getMomentum();

				final boolean validMomemtum = Arrays.asList(slotMomenta).contains(inputMomentum);
				final boolean validModifiers = slotInputModifiers.equals(inputModifiers);

				if (validMomemtum && validModifiers) {
					optionalInputSlot.get().invoke(	dataContext,
													input);
				}
			}
		} catch (final ExecutionException e) {
			Throwables.propagate(e);
		} catch (final IllegalAccessException e) {
			Throwables.propagate(e);
		} catch (final IllegalArgumentException e) {
			Throwables.propagate(e);
		} catch (final InvocationTargetException e) {
			Throwables.propagate(e);
		}
	}

	public Method[] lookupAllObservableCollections(final Class<?> dataContextClass) throws ExecutionException {
		return allObservableCollections.get(dataContextClass,
											new Callable<Method[]>() {
												@Override
												public Method[] call() throws Exception {
													return getAllObservableCollections(dataContextClass);
												}
											});
	}

	protected Method[] getAllObservableCollections(final Class<?> dataContextClass) {
		final List<Method> allViewProperties = new ArrayList<Method>();

		final List<ObservableCollection> observableCollectionAnnotations = new ArrayList<ObservableCollection>();

		for (final Method method : dataContextClass.getMethods()) {
			final ObservableCollection observableCollectionAnnotation = method
					.getAnnotation(ObservableCollection.class);
			if (observableCollectionAnnotation == null) {
				continue;
			}

			if (isDuplicateObservableCollection(observableCollectionAnnotations,
												observableCollectionAnnotation)) {
				throw new IllegalArgumentException(String.format(	"Found duplicate %s with name %s on %s",
																	ObservableCollection.class.getName(),
																	observableCollectionAnnotation.name(),
																	dataContextClass.getName()));
			}

			if (!EventList.class.isAssignableFrom(method.getReturnType())) {
				throw new IllegalArgumentException(String.format(	"Found %s with name %s who's method return type is not a %s",
																	ObservableCollection.class.getName(),
																	observableCollectionAnnotation.name(),
																	EventList.class.getName()));
			}

			observableCollectionAnnotations.add(observableCollectionAnnotation);
			allViewProperties.add(method);
		}

		return allViewProperties.toArray(new Method[] {});
	}

	public Optional<Method> lookupObservedCollection(	final Class<?> viewClass,
														final String name) throws ExecutionException {
		return observedCollectionsByName.get(	viewClass,
												new Callable<Cache<String, Optional<Method>>>() {
													@Override
													public Cache<String, Optional<Method>> call() throws Exception {
														return CacheBuilder.newBuilder().build();
													}
												}).get(	name,
														new Callable<Optional<Method>>() {
															@Override
															public Optional<Method> call() throws Exception {
																return getObservedCollection(	viewClass,
																								name);
															}
														});
	}

	protected Optional<Method> getObservedCollection(	final Class<?> viewClass,
														final String name) {

		Method foundMethod = null;

		for (final Method method : viewClass.getMethods()) {

			final ObservedCollection observedCollection = method.getAnnotation(ObservedCollection.class);

			if (observedCollection == null) {
				continue;
			}

			final String collectionName = observedCollection.name();
			if (collectionName.equals(name)) {
				if ((foundMethod == null)) {
					foundMethod = method;
				} else if ((foundMethod != null)) {
					throw new IllegalArgumentException(String.format(	"Found multiple identical %s with name %s in the class hierarchy of %s",
																		ObservedCollection.class.getName(),
																		name,
																		viewClass.getName()));
				}
			}
		}

		return Optional.fromNullable(foundMethod);
	}

}