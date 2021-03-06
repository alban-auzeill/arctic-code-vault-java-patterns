/** (rank 64) copied from https://github.com/openhab/openhab1-addons/blob/f7c26e5361c1cfe0382817ce7310a624e4ce89a0/bundles/binding/org.openhab.binding.plugwise/src/main/java/org/openhab/binding/plugwise/protocol/RealTimeClockGetResponseMessage.java
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
import org.joda.time.DateTimeZone;

/**
 * Real time clock response message. The Circle+ is the only device to hold a real real-time clock
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class RealTimeClockGetResponseMessage extends Message {

    private static final Pattern RESPONSE_PATTERN = Pattern
            .compile("(\\w{16})(\\w{2})(\\w{2})(\\w{2})(\\w{2})(\\w{2})(\\w{2})(\\w{2})");

    private int seconds;
    private int minutes;
    private int hour;
    @SuppressWarnings("unused")
    private int weekday;
    private int day;
    private int month;
    private int year;

    public RealTimeClockGetResponseMessage(int sequenceNumber, String payLoad) {
        super(sequenceNumber, payLoad);
        type = MessageType.REALTIMECLOCK_GET_RESPONSE;
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
            seconds = Integer.parseInt(matcher.group(2));
            minutes = Integer.parseInt(matcher.group(3));
            hour = Integer.parseInt(matcher.group(4));
            weekday = Integer.parseInt(matcher.group(5));
            day = Integer.parseInt(matcher.group(6));
            month = Integer.parseInt(matcher.group(7));
            year = Integer.parseInt(matcher.group(8)) + 2000;
        } else {
            logger.debug("Plugwise protocol RealTimeClockGetResponseMessage error: {} does not match", payLoad);
        }
    }

    public DateTime getTime() {
        return new DateTime(year, month, day, hour, minutes, seconds, DateTimeZone.UTC)
                .toDateTime(DateTimeZone.getDefault());
    }

}
