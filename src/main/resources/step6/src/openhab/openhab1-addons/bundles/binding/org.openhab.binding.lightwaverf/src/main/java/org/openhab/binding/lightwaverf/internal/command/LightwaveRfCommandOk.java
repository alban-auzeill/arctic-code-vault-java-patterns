/** (rank 64) copied from https://github.com/openhab/openhab1-addons/blob/f7c26e5361c1cfe0382817ce7310a624e4ce89a0/bundles/binding/org.openhab.binding.lightwaverf/src/main/java/org/openhab/binding/lightwaverf/internal/command/LightwaveRfCommandOk.java
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.lightwaverf.internal.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.AbstractLightwaveRfCommand;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfGeneralMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.types.State;

/**
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRfCommandOk extends AbstractLightwaveRfCommand implements LightwaveRFCommand {

    private static final Pattern REG_EXP = Pattern.compile(".*?([0-9]{1,3}),OK\\s*");

    private final LightwaveRfMessageId messageId;

    public LightwaveRfCommandOk(String message) throws LightwaveRfMessageException {
        try {
            Matcher m = REG_EXP.matcher(message);
            m.matches();
            this.messageId = new LightwaveRfGeneralMessageId(Integer.valueOf(m.group(1)));
        } catch (Exception e) {
            throw new LightwaveRfMessageException("Error converting message: " + message, e);
        }
    }

    @Override
    public String getLightwaveRfCommandString() {
        return getOkString(messageId);
    }

    @Override
    public State getState(LightwaveRfType type) {
        return null;
    }

    @Override
    public LightwaveRfMessageId getMessageId() {
        return messageId;
    }

    public static boolean matches(String message) {
        Matcher m = REG_EXP.matcher(message);
        return m.matches();
    }

    @Override
    public LightwaveRfMessageType getMessageType() {
        return LightwaveRfMessageType.OK;
    }

}
