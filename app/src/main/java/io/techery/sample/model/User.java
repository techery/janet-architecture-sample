package io.techery.sample.model;

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
public abstract class User implements Indexable{
    public abstract Long id();
    public abstract String login();
    @SerializedName("avatar_url") public abstract String avatarUrl();
    @SerializedName("html_url") public abstract String url();

    @Gson.Ignore
    @Value.Default
    @Override
    public byte[] index() {
        return ByteBuffer.allocate(8).putLong(id()).array();
    }
}
