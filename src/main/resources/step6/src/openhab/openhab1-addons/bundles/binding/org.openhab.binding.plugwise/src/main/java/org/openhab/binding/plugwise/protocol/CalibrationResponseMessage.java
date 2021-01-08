/** (rank 64) copied from https://github.com/openhab/openhab1-addons/blob/f7c26e5361c1cfe0382817ce7310a624e4ce89a0/bundles/binding/org.openhab.binding.plugwise/src/main/java/org/openhab/binding/plugwise/protocol/CalibrationResponseMessage.java
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

/**
 * Circle Calibration response
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class CalibrationResponseMessage extends Message {

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("(\\w{16})(\\w{8})(\\w{8})(\\w{8})(\\w{8})");

    private double gainA;
    private double gainB;
    private double offsetTotal;
    private double offsetNoise;

    public double getGainA() {
        return gainA;
    }

    public double getGainB() {
        return gainB;
    }

    public double getOffsetTotal() {
        return offsetTotal;
    }

    public double getOffsetNoise() {
        return offsetNoise;
    }

    public CalibrationResponseMessage(int sequenceNumber, String payLoad) {
        super(sequenceNumber, payLoad);
        type = MessageType.DEVICE_CALIBRATION_RESPONSE;
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

            gainA = Float.intBitsToFloat((int) (Long.parseLong(matcher.group(2), 16)));
            gainB = Float.intBitsToFloat((int) (Long.parseLong(matcher.group(3), 16)));
            offsetTotal = Float.intBitsToFloat((int) (Long.parseLong(matcher.group(4), 16)));
            offsetNoise = Float.intBitsToFloat((int) (Long.parseLong(matcher.group(5), 16)));
        } else {
            logger.debug("Plugwise protocol RoleCallResponseMessage error: {} does not match", payLoad);
        }
    }

}
