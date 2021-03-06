/** (rank 64) copied from https://github.com/openhab/openhab1-addons/blob/f7c26e5361c1cfe0382817ce7310a624e4ce89a0/bundles/binding/org.openhab.binding.plugwise/src/main/java/org/openhab/binding/plugwise/protocol/PowerInformationResponseMessage.java
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
package org.openhab.binding.plugwise.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.openhab.binding.plugwise.internal.Energy;

/**
 * Message containing real-time energy consumption
 * Not all parts of this kind of Message are already reverse engineered
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class PowerInformationResponseMessage extends Message {

    private static final Pattern RESPONSE_PATTERN = Pattern
            .compile("(\\w{16})(\\w{4})(\\w{4})(\\w{8})(\\w{8})(\\w{4})");

    private Energy oneSecond;
    private Energy eightSecond;
    private Energy oneHourConsumed;
    private Energy oneHourProduced;
    private double secondsCorrection;

    public PowerInformationResponseMessage(int sequenceNumber, String payLoad) {
        super(sequenceNumber, payLoad);
        type = MessageType.POWER_INFORMATION_RESPONSE;
    }

    @Override
    protected String payLoadToHexString() {
        return payLoad;
    }

    @Override
    protected void parsePayLoad() {
        Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
        if (matcher.matches()) {
            MAC = matcher.group(1);
            secondsCorrection = Integer.parseInt(matcher.group(6), 16) / 46875.0;
            oneSecond = new Energy(DateTime.now(), Integer.parseInt(matcher.group(2), 16), 1 + secondsCorrection);
            eightSecond = new Energy(DateTime.now(), Integer.parseInt(matcher.group(3), 16), 8 + secondsCorrection);
            oneHourConsumed = new Energy(DateTime.now(), Long.parseLong(matcher.group(4), 16), 3600);
            oneHourProduced = new Energy(DateTime.now(), Long.parseLong(matcher.group(5), 16), 3600);
        } else {
            logger.debug("Plugwise protocol PowerInformationResponseMessage error: {} does not match", payLoad);
        }
    }

    public Energy getOneSecond() {
        return oneSecond;
    }

    public Energy getEightSecond() {
        return eightSecond;
    }

    public Energy getOneHourConsumed() {
        return oneHourConsumed;
    }

    public Energy getOneHourProduced() {
        return oneHourProduced;
    }
}
