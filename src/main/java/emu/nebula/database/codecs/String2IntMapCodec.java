package emu.nebula.database.codecs;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import emu.nebula.util.ints.String2IntMap;

/**
 * Custom mongodb codec for encoding/decoding fastutil int2int maps.
 */
public class String2IntMapCodec implements Codec<String2IntMap> {

    @Override
    public Class<String2IntMap> getEncoderClass() {
        return String2IntMap.class;
    }

    @Override
    public void encode(BsonWriter writer, String2IntMap collection, EncoderContext encoderContext) {
        writer.writeStartDocument();
        for (var entry : collection.object2IntEntrySet()) {
            writer.writeName(entry.getKey());
            writer.writeInt32(entry.getIntValue());
        }
        writer.writeEndDocument();
    }

    @Override
    public String2IntMap decode(BsonReader reader, DecoderContext decoderContext) {
        String2IntMap collection = new String2IntMap();
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            collection.put(reader.readName(), reader.readInt32());
        }
        reader.readEndDocument();
        return collection;
    }
}