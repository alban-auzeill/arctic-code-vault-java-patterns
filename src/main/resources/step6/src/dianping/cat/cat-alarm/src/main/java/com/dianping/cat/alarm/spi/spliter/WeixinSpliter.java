/* (rank 15) copied from https://github.com/dianping/cat/blob/801f0b7b358814f8176dd2c47e25a85a9c83e6bc/cat-alarm/src/main/java/com/dianping/cat/alarm/spi/spliter/WeixinSpliter.java
 * Copyright (c) 2011-2018, Meituan Dianping. All Rights Reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dianping.cat.alarm.spi.spliter;

import java.util.regex.Pattern;

import com.dianping.cat.alarm.spi.AlertChannel;

public class WeixinSpliter implements Spliter {

	public static final String ID = AlertChannel.WEIXIN.getName();

	@Override
	public String getID() {
		return ID;
	}

	@Override
	public String process(String content) {
		String weixinContent = content.replaceAll("<br/>", "\n");
		weixinContent = Pattern.compile("<div.*(?=</div>)</div>", Pattern.DOTALL).matcher(weixinContent).replaceAll("");
		weixinContent = Pattern.compile("<table.*(?=</table>)</table>", Pattern.DOTALL).matcher(weixinContent).replaceAll("");

		return weixinContent;
	}

}
