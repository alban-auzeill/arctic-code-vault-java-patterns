/* (rank 45) copied from https://github.com/apache/cassandra/blob/79e693e16e2152097c5b27d2d7aaa1763e34f594/src/java/org/apache/cassandra/db/marshal/UUIDType.java
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.cassandra.db.marshal;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.regex.Pattern;

import com.google.common.primitives.UnsignedLongs;

import org.apache.cassandra.cql3.CQL3Type;
import org.apache.cassandra.cql3.Constants;
import org.apache.cassandra.cql3.Term;
import org.apache.cassandra.serializers.TypeSerializer;
import org.apache.cassandra.serializers.MarshalException;
import org.apache.cassandra.serializers.UUIDSerializer;
import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.cassandra.utils.UUIDGen;

/**
 * Compares UUIDs using the following criteria:<br>
 * - if count of supplied bytes is less than 16, compare counts<br>
 * - compare UUID version fields<br>
 * - nil UUID is always lesser<br>
 * - compare timestamps if both are time-based<br>
 * - compare lexically, unsigned msb-to-lsb comparison<br>
 *
 * @see "com.fasterxml.uuid.UUIDComparator"
 */
public class UUIDType extends AbstractType<UUID>
{
    public static final UUIDType instance = new UUIDType();

    UUIDType()
    {
        super(ComparisonType.CUSTOM);
    }

    public boolean isEmptyValueMeaningless()
    {
        return true;
    }

    public <VL, VR> int compareCustom(VL left, ValueAccessor<VL> accessorL, VR right, ValueAccessor<VR> accessorR)
    {

        // Compare for length
        boolean p1 = accessorL.size(left) == 16, p2 = accessorR.size(right) == 16;
        if (!(p1 & p2))
        {
            // should we assert exactly 16 bytes (or 0)? seems prudent
            assert p1 || accessorL.isEmpty(left);
            assert p2 || accessorR.isEmpty(right);
            return p1 ? 1 : p2 ? -1 : 0;
        }

        // Compare versions
        long msb1 = accessorL.getLong(left, 0);
        long msb2 = accessorR.getLong(right, 0);

        int version1 = (int) ((msb1 >>> 12) & 0xf);
        int version2 = (int) ((msb2 >>> 12) & 0xf);
        if (version1 != version2)
            return version1 - version2;

        // bytes: version is top 4 bits of byte 6
        // then: [6.5-8), [4-6), [0-4)
        if (version1 == 1)
        {
            long reorder1 = TimeUUIDType.reorderTimestampBytes(msb1);
            long reorder2 = TimeUUIDType.reorderTimestampBytes(msb2);
            // we know this is >= 0, since the top 3 bits will be 0
            int c = Long.compare(reorder1, reorder2);
            if (c != 0)
                return c;
        }
        else
        {
            int c = UnsignedLongs.compare(msb1, msb2);
            if (c != 0)
                return c;
        }

        return UnsignedLongs.compare(accessorL.getLong(left, 8), accessorR.getLong(right, 8));
    }

    @Override
    public boolean isValueCompatibleWithInternal(AbstractType<?> otherType)
    {
        return otherType instanceof UUIDType || otherType instanceof TimeUUIDType;
    }

    @Override
    public ByteBuffer fromString(String source) throws MarshalException
    {
        // Return an empty ByteBuffer for an empty string.
        ByteBuffer parsed = parse(source);
        if (parsed != null)
            return parsed;

        throw new MarshalException(String.format("Unable to make UUID from '%s'", source));
    }

    @Override
    public CQL3Type asCQL3Type()
    {
        return CQL3Type.Native.UUID;
    }

    public TypeSerializer<UUID> getSerializer()
    {
        return UUIDSerializer.instance;
    }

    static final Pattern regexPattern = Pattern.compile("[A-Fa-f0-9]{8}\\-[A-Fa-f0-9]{4}\\-[A-Fa-f0-9]{4}\\-[A-Fa-f0-9]{4}\\-[A-Fa-f0-9]{12}");

    static ByteBuffer parse(String source)
    {
        if (source.isEmpty())
            return ByteBufferUtil.EMPTY_BYTE_BUFFER;

        if (regexPattern.matcher(source).matches())
        {
            try
            {
                return UUIDGen.toByteBuffer(UUID.fromString(source));
            }
            catch (IllegalArgumentException e)
            {
                throw new MarshalException(String.format("Unable to make UUID from '%s'", source), e);
            }
        }

        return null;
    }

    @Override
    public Term fromJSONObject(Object parsed) throws MarshalException
    {
        try
        {
            return new Constants.Value(fromString((String) parsed));
        }
        catch (ClassCastException exc)
        {
            throw new MarshalException(String.format(
                    "Expected a string representation of a uuid, but got a %s: %s", parsed.getClass().getSimpleName(), parsed));
        }
    }

    static int version(ByteBuffer uuid)
    {
        return (uuid.get(6) & 0xf0) >> 4;
    }

    @Override
    public int valueLengthIfFixed()
    {
        return 16;
    }
}
