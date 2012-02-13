package org.hypercube.protocol.x11;

import java.util.HashMap;
import java.util.Map;

import org.fusion.x11.core.IntDataContainer;
import org.fusion.x11.core.XAtom;
import org.fusion.x11.core.XDisplay;
import org.fusion.x11.core.XPropertyInstanceXAtoms;
import org.fusion.x11.core.XPropertyXAtom;
import org.fusion.x11.core.XPropertyXAtomAtoms;
import org.fusion.x11.core.XPropertyXAtomMultiText;
import org.fusion.x11.core.XPropertyXAtomSingleText;
import org.fusion.x11.core.XProtocolConstants;
import org.fusion.x11.core.XWindow;
import org.fusion.x11.core.event.XClientMessageEvent;
import org.fusion.x11.icccm.Icccm;
import org.fusion.x11.icccm.IcccmAtoms;
import org.fusion.x11.icccm.WmHints;
import org.fusion.x11.icccm.WmHintsInstance;
import org.fusion.x11.icccm.WmNormalHints;
import org.fusion.x11.icccm.WmSizeHintsInstance;
import org.fusion.x11.icccm.WmStateEnum;
import org.fusion.x11.icccm.WmStateInstance;
import org.hydrogen.displayinterface.PlatformRenderArea;
import org.hydrogen.displayinterface.PropertyInstance;
import org.hydrogen.displayinterface.PropertyInstanceText;
import org.hydrogen.displayinterface.PropertyInstanceTexts;
import org.hydrogen.eventsystem.EventHandler;
import org.hypercube.protocol.AbstractDesktopProtocol;
import org.hyperdrive.core.ClientWindow;
import org.hyperdrive.core.ManagedDisplay;
import org.hyperdrive.core.RenderAreaPropertiesManipulator;
import org.hyperdrive.core.RenderAreaPropertyChangedEvent;
import org.hyperdrive.geo.GeoEvent;

public final class XDesktopProtocol extends AbstractDesktopProtocol {

	private abstract class PropertyListener<P extends PropertyInstance, T extends XPropertyXAtom<P>>
			implements EventHandler<RenderAreaPropertyChangedEvent<T>> {

		@Override
		public void handleEvent(final RenderAreaPropertyChangedEvent<T> event) {

			final ClientWindow client = (ClientWindow) event.getRenderArea();
			final PlatformRenderArea platformRenderArea = client
					.getPlatformRenderArea();
			final T changedProperty = event.getChangedProperty();

			final P propertyInstance = platformRenderArea
					.getPropertyInstance(changedProperty);

			handlePropertyInstance(client, propertyInstance);
		}

		abstract void handlePropertyInstance(ClientWindow client,
				P propertyInstance);
	}

	private final class WmHintsListener extends
			PropertyListener<WmHintsInstance, WmHints> {

		@Override
		void handlePropertyInstance(final ClientWindow client,
				final WmHintsInstance propertyInstance) {
			XDesktopProtocol.this.wmHintsInterpreter.handleWmHint(client,
					propertyInstance);
		}
	}

	private final class WmNormalHintsListener extends
			PropertyListener<WmSizeHintsInstance, WmNormalHints> {

		@Override
		void handlePropertyInstance(final ClientWindow client,
				final WmSizeHintsInstance propertyInstance) {
			XDesktopProtocol.this.wmNormalHintsInterpreter.handleWmNormalHints(
					client, propertyInstance);
		}
	}

	private final class WmNameListener extends
			PropertyListener<PropertyInstanceText, XPropertyXAtomSingleText> {

		@Override
		void handlePropertyInstance(final ClientWindow client,
				final PropertyInstanceText propertyInstance) {
			XDesktopProtocol.this.wmNameInterpreter.handleWmName(client,
					propertyInstance);
		}
	}

	private final class WmClassListener extends
			PropertyListener<PropertyInstanceTexts, XPropertyXAtomMultiText> {
		@Override
		void handlePropertyInstance(final ClientWindow client,
				final PropertyInstanceTexts propertyInstance) {
			XDesktopProtocol.this.wmClassInterpreter.handleWmClass(client,
					propertyInstance);
		}
	}

	private final class ClientVisibilityListener implements
			EventHandler<GeoEvent> {
		@Override
		public void handleEvent(final GeoEvent event) {
			final ClientWindow client = (ClientWindow) event
					.getTransformableSquare();
			final boolean visible = event.getTransformation().isVisible1();
			updateWmState(client, visible);
		}
	}

	private final Icccm icccm;
	private final InputPreferenceParser inputPreferenceParser;

	private final XDisplay display;

	private final WmNormalHintsInterpreter wmNormalHintsInterpreter;
	private final WmHintsInterpreter wmHintsInterpreter;
	private final WmNameInterpreter wmNameInterpreter;
	private final WmClassInterpreter wmClassInterpreter;

	private final Map<ClientWindow, RenderAreaPropertiesManipulator> clientPropertiesManipulators;
	static final String EMPTY_STRING = "";

	public XDesktopProtocol(final ManagedDisplay managedDisplay) {
		this.inputPreferenceParser = new InputPreferenceParser();
		this.clientPropertiesManipulators = new HashMap<ClientWindow, RenderAreaPropertiesManipulator>();
		// this.managedDisplay = managedDisplay;
		this.display = (XDisplay) managedDisplay.getDisplay();

		this.icccm = new Icccm(this.display);

		this.wmNormalHintsInterpreter = new WmNormalHintsInterpreter(this);
		this.wmHintsInterpreter = new WmHintsInterpreter(this);
		this.wmNameInterpreter = new WmNameInterpreter(this);
		this.wmClassInterpreter = new WmClassInterpreter(this);

		if (this.icccm.getSelectionManager().isScreenSelectionAvailable()) {
			this.icccm.getSelectionManager().initScreenSelection();
		} else {
			// TODO handle screen nro's. For now default to 0
			throw new Error(
					String.format(
							"No screen selection available! Is there a window manager running for screen %d?",
							0));
		}
	}

	@Override
	public void registerClient(final ClientWindow client) {
		final RenderAreaPropertiesManipulator propertiesManipulator = new RenderAreaPropertiesManipulator(
				client);

		this.clientPropertiesManipulators.put(client, propertiesManipulator);
		readProtocolProperties(client);
		installListeners(client);
	}

	@Override
	public boolean requestDelete(final ClientWindow client) {
		final XPropertyInstanceXAtoms wmProtocolsInstance = this.clientPropertiesManipulators
				.get(client)
				.getPropertyValue(IcccmAtoms.WM_PROTOCOLS_ATOM_NAME);
		final XAtom wmDeleteWindow = this.icccm.getIcccmAtoms()
				.getWmDeleteWindow();

		boolean wmDelete = false;
		if (wmDeleteWindow != null) {
			for (final XAtom xAtom : wmProtocolsInstance.getAtoms()) {
				wmDelete = xAtom.equals(wmDeleteWindow);
				if (wmDelete) {
					break;
				}
			}
		}

		if (wmDelete) {

			final XWindow window = (XWindow) client.getPlatformRenderArea();

			final IntDataContainer intDataContainer = new IntDataContainer(2);
			intDataContainer.writeDataBlock(Integer.valueOf(wmDeleteWindow
					.getAtomId().intValue()));
			intDataContainer.writeDataBlock(Integer
					.valueOf((int) XProtocolConstants.CURRENT_TIME));

			final XPropertyXAtomAtoms wmProtocols = this.icccm.getIcccmAtoms()
					.getWmProtocols();
			final XClientMessageEvent deleteWindowRequest = new XClientMessageEvent(
					window, wmProtocols, intDataContainer);
			window.sendMessage(deleteWindowRequest);

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean offerInput(final ClientWindow client) {
		// TODO we might want to grab the display and ungrab it when the focus
		// is transferred. This way the user is ensured that when he starts
		// typing after an focus transfer, all the input will be send to the
		// correct client.

		final RenderAreaPropertiesManipulator propertiesManipulator = this.clientPropertiesManipulators
				.get(client);
		final WmHintsInstance wmHintsInstance = propertiesManipulator
				.getPropertyValue(IcccmAtoms.WM_HINTS_ATOM_NAME);
		final XPropertyInstanceXAtoms wmProtocolsInstance = propertiesManipulator
				.getPropertyValue(IcccmAtoms.WM_PROTOCOLS_ATOM_NAME);

		final XWindow window = (XWindow) client.getPlatformRenderArea();
		final InputPreference inputPreference = this.inputPreferenceParser
				.parseInputPreference(window, wmHintsInstance,
						wmProtocolsInstance);
		switch (inputPreference) {
		case GLOBAL_INPUT:
			// Windows with the atom WM_TAKE_FOCUS in their WM_PROTOCOLS
			// property may receive a ClientMessage event from the window
			// manager (as described in section 4.2.8) with WM_TAKE_FOCUS in its
			// data[0] field and a valid timestamp (i.e. not CurrentTime ) in
			// its data[1] field.

			final XWindow screenSelectionOwner = this.icccm
					.getSelectionManager().getIcccmSelectionOwner();
			final XAtom wmProtocolsAtom = this.icccm.getIcccmAtoms()
					.getWmProtocols();

			// construct clientmessage data
			final IntDataContainer clientMessageData = new IntDataContainer(2);
			final XAtom wmTakeFocus = this.icccm.getIcccmAtoms()
					.getWmTakeFocus();
			final Integer wmTakeFocusId = Integer.valueOf(wmTakeFocus
					.getAtomId().intValue());
			final Integer time = Integer.valueOf(this.display
					.getLastServerTime());
			clientMessageData.writeDataBlock(wmTakeFocusId);
			clientMessageData.writeDataBlock(time);

			final XClientMessageEvent setInputFocusMessage = new XClientMessageEvent(
					screenSelectionOwner, wmProtocolsAtom, clientMessageData);
			window.sendMessage(setInputFocusMessage);

			break;
		case LOCAL_INPUT:
			if (!client.hasInputFocus()) {
				client.giveInputFocus();
			}
			break;
		case NO_INPUT:
			// Don't give any input
			break;
		case PASSIVE_INPUT:
			client.giveInputFocus();
			break;
		}

		return true;
	}

	protected void updateWmState(final ClientWindow client,
			final boolean visible) {
		final WmStateEnum state = visible ? WmStateEnum.NormalState
				: WmStateEnum.WithdrawnState;
		final XWindow iconWindow = this.display.getNoneWindow();
		this.clientPropertiesManipulators.get(client).setPropertyValue(
				IcccmAtoms.WM_STATE_ATOM_NAME,
				new WmStateInstance(this.display, state, iconWindow));
	}

	protected void readProtocolProperties(final ClientWindow client) {
		// TODO update all protocol events for the client so they can be queried
		// in the future
	}

	protected void addClientToSaveSet(final ClientWindow client) {
		final XWindow window = (XWindow) client.getPlatformRenderArea();
		window.addToSaveSet();
	}

	protected void installListeners(final ClientWindow client) {
		client.addEventHandler(new WmHintsListener(),
				RenderAreaPropertyChangedEvent
						.TYPE(IcccmAtoms.WM_HINTS_ATOM_NAME));
		client.addEventHandler(new WmNormalHintsListener(),
				RenderAreaPropertyChangedEvent
						.TYPE(IcccmAtoms.WM_NORMAL_HINTS_ATOM_NAME));
		client.addEventHandler(new WmNameListener(),
				RenderAreaPropertyChangedEvent
						.TYPE(IcccmAtoms.WM_NAME_ATOM_NAME));
		client.addEventHandler(new WmClassListener(),
				RenderAreaPropertyChangedEvent
						.TYPE(IcccmAtoms.WM_CLASS_ATOM_NAME));
		client.addEventHandler(new ClientVisibilityListener(),
				GeoEvent.VISIBILITY);
	}

}
