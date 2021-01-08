package com.fsck.k9.message.html; // (rank 248) copied from https://github.com/k9mail/k-9/blob/908f77580622e6f8845bfee38a784a8a46b3795f/app/core/src/main/java/com/fsck/k9/message/html/EthereumUriParser.java


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Parses ERC-67 URIs
 * https://github.com/ethereum/EIPs/issues/67
 */
class EthereumUriParser implements UriParser {
    private static final Pattern ETHEREUM_URI_PATTERN =
            Pattern.compile("ethereum:0x[0-9a-fA-F]*(\\?[a-zA-Z0-9$\\-_.+!*'(),%:@&=]*)?");

    @Nullable
    @Override
    public UriMatch parseUri(@NotNull CharSequence text, int startPos) {
        Matcher matcher = ETHEREUM_URI_PATTERN.matcher(text);

        if (!matcher.find(startPos) || matcher.start() != startPos) {
            return null;
        }

        int startIndex = matcher.start();
        int endIndex = matcher.end();
        CharSequence uri = text.subSequence(startIndex, endIndex);
        return new UriMatch(startIndex, endIndex, uri);
    }
}
