package io.techery.sample.model;

import android.support.annotation.Nullable;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.nio.ByteBuffer;

import io.techery.snapper.model.Indexable;

@Value.Immutable
@Value.Style(privateNoargConstructor = true,
        defaultAsDefault = true)
@Gson.TypeAdapters
@DefaultSerializer(CompatibleFieldSerializer.class)
public abstract class Repository implements Indexable {
    public abstract Long id();
    public abstract String name();
    @Nullable public abstract String description();
    @SerializedName("html_url") public abstract String url();
    public abstract User owner();

    @Gson.Ignore
    @Value.Default
    @Override
    public byte[] index() {
        return ByteBuffer.allocate(8).putLong(id()).array();
    }
}
