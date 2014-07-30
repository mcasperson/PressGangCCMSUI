/*
  Copyright 2011-2014 Red Hat, Inc

  This file is part of PresGang CCMS.

  PresGang CCMS is free software: you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  PresGang CCMS is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with PresGang CCMS.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.jboss.pressgang.ccms.ui.client.local.ui.keypresshandler;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.ValueBoxBase;
import org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities;
import org.jetbrains.annotations.NotNull;

/**
 * Cancels and key press that is not a digit or comma, and removes any non digit or comma character on
 * value change.
 * <p/>
 * Use KeyDownHandler rather than KeyPresshandler because of http://code.google.com/p/google-web-toolkit/issues/detail?id=5557
 *
 * @author Matthew Casperson
 */
public final class WholeNumbersValidator implements KeyDownHandler, ValueChangeHandler<Integer> {

    private static final int ZERO_KEY_CODE = 48;
    private static final int NINE_KEY_CODE = 57;
    private static final int INSERT_KEY_CODE = 45;
    private static final int MINUS_KEY_CODE = 189;
    private static final int NUMPAD_MINUS_KEY_CODE = 109;
    private static final int NUMPAD_0 = 96;
    private static final int NUMPAD_9 = 105;

    @NotNull
    private final ValueBoxBase<Integer> source;


    public WholeNumbersValidator(@NotNull final ValueBoxBase<Integer> source) {
        this.source = source;
        source.addKeyDownHandler(this);
        source.addValueChangeHandler(this);
    }

    @Override
    public void onKeyDown(@NotNull final KeyDownEvent event) {
        final int keyCode = event.getNativeKeyCode();

        /* Allow navigation keys */
        if (keyCode == KeyCodes.KEY_DELETE ||
                keyCode == KeyCodes.KEY_BACKSPACE ||
                keyCode == KeyCodes.KEY_LEFT ||
                keyCode == KeyCodes.KEY_RIGHT ||
                keyCode == KeyCodes.KEY_UP ||
                keyCode == KeyCodes.KEY_DOWN ||
                keyCode == KeyCodes.KEY_HOME ||
                keyCode == KeyCodes.KEY_END ||
                keyCode == KeyCodes.KEY_ENTER ||

                (event.isControlKeyDown() && keyCode == 'V') ||                  // paste
                (event.isControlKeyDown() && keyCode == 'C') ||                 // copy
                (event.isControlKeyDown() && keyCode == 'X') ||                 // cut
                (event.isControlKeyDown() && keyCode == INSERT_KEY_CODE) ||     // copy
                (event.isShiftKeyDown() && keyCode == INSERT_KEY_CODE)) {      // paste
            return;
        }
        
        /* Allow numeric keys */
        if (keyCode >= ZERO_KEY_CODE && keyCode <= NINE_KEY_CODE) {
            return;
        }

        /* Allow keypad keys */
        if (keyCode >= NUMPAD_0 && keyCode <= NUMPAD_9) {
            return;
        }
        
        /* Allow minus keys */
        if (keyCode == MINUS_KEY_CODE || keyCode == NUMPAD_MINUS_KEY_CODE) {
            return;
        }
        
        /* Block all others */
        source.cancelKey();
    }

    @Override
    public void onValueChange(final ValueChangeEvent<Integer> event) {
        source.setText(GWTUtilities.fixUpIdSearchString(source.getText()));
    }

}
