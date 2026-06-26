package com.crud.aws.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.springframework.stereotype.Service;

@Service
public class AvroDeserializer {

    public GenericRecord deserialize(byte[] payload, Schema schema) {
        try {
            SpecificDatumReader<GenericRecord> reader = new SpecificDatumReader<>(schema);
            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(payload, null);
            return reader.read(null, decoder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
