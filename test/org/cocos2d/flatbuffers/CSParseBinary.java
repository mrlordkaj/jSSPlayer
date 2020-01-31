/*
 * Copyright 2018 Thinh Pham.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cocos2d.flatbuffers;

import com.google.flatbuffers.Table;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Thinh Pham
 */
public class CSParseBinary extends Table {
    public static CSParseBinary getRootAsMonster(ByteBuffer _bb) { return getRootAsMonster(_bb, new CSParseBinary()); }
    public static CSParseBinary getRootAsMonster(ByteBuffer _bb, CSParseBinary obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
    public void __init(int _i, ByteBuffer _bb) { bb_pos = _i; bb = _bb; }
    public CSParseBinary __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }
}
