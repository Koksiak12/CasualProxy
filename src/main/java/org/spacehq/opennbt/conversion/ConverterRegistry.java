// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt.conversion;

import org.spacehq.opennbt.conversion.builtin.custom.StringArrayTagConverter;
import org.spacehq.opennbt.tag.builtin.custom.StringArrayTag;
import org.spacehq.opennbt.conversion.builtin.custom.ShortArrayTagConverter;
import org.spacehq.opennbt.tag.builtin.custom.ShortArrayTag;
import org.spacehq.opennbt.conversion.builtin.custom.SerializableTagConverter;
import org.spacehq.opennbt.tag.builtin.custom.SerializableTag;
import org.spacehq.opennbt.conversion.builtin.custom.SerializableArrayTagConverter;
import org.spacehq.opennbt.tag.builtin.custom.SerializableArrayTag;
import org.spacehq.opennbt.conversion.builtin.custom.LongArrayTagConverter;
import org.spacehq.opennbt.tag.builtin.custom.LongArrayTag;
import org.spacehq.opennbt.conversion.builtin.custom.FloatArrayTagConverter;
import org.spacehq.opennbt.tag.builtin.custom.FloatArrayTag;
import org.spacehq.opennbt.conversion.builtin.custom.DoubleArrayTagConverter;
import org.spacehq.opennbt.tag.builtin.custom.DoubleArrayTag;
import org.spacehq.opennbt.conversion.builtin.IntArrayTagConverter;
import org.spacehq.opennbt.tag.builtin.IntArrayTag;
import org.spacehq.opennbt.conversion.builtin.CompoundTagConverter;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.conversion.builtin.ListTagConverter;
import java.util.List;
import org.spacehq.opennbt.tag.builtin.ListTag;
import org.spacehq.opennbt.conversion.builtin.StringTagConverter;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.opennbt.conversion.builtin.ByteArrayTagConverter;
import org.spacehq.opennbt.tag.builtin.ByteArrayTag;
import org.spacehq.opennbt.conversion.builtin.DoubleTagConverter;
import org.spacehq.opennbt.tag.builtin.DoubleTag;
import org.spacehq.opennbt.conversion.builtin.FloatTagConverter;
import org.spacehq.opennbt.tag.builtin.FloatTag;
import org.spacehq.opennbt.conversion.builtin.LongTagConverter;
import org.spacehq.opennbt.tag.builtin.LongTag;
import org.spacehq.opennbt.conversion.builtin.IntTagConverter;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.conversion.builtin.ShortTagConverter;
import org.spacehq.opennbt.tag.builtin.ShortTag;
import org.spacehq.opennbt.conversion.builtin.ByteTagConverter;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import java.util.HashMap;
import java.util.HashSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Iterator;
import org.spacehq.opennbt.tag.TagRegisterException;
import org.spacehq.opennbt.tag.builtin.Tag;
import java.util.Map;

public class ConverterRegistry
{
    private static final Map<Class<? extends Tag>, TagConverter<? extends Tag, ?>> tagToConverter;
    private static final Map<Class<?>, TagConverter<? extends Tag, ?>> typeToConverter;
    
    public static <T extends Tag, V> void register(final Class<T> tag, final Class<V> type, final TagConverter<T, V> converter) throws ConverterRegisterException {
        if (ConverterRegistry.tagToConverter.containsKey(tag)) {
            throw new TagRegisterException("Type conversion to tag " + tag.getName() + " is already registered.");
        }
        if (ConverterRegistry.typeToConverter.containsKey(type)) {
            throw new TagRegisterException("Tag conversion to type " + type.getName() + " is already registered.");
        }
        ConverterRegistry.tagToConverter.put(tag, converter);
        ConverterRegistry.typeToConverter.put(type, converter);
    }
    
    public static <T extends Tag, V> V convertToValue(final T tag) throws ConversionException {
        if (tag == null || tag.getValue() == null) {
            return null;
        }
        if (!ConverterRegistry.tagToConverter.containsKey(tag.getClass())) {
            throw new ConversionException("Tag type " + tag.getClass().getName() + " has no converter.");
        }
        final TagConverter<T, ?> converter = (TagConverter<T, ?>)ConverterRegistry.tagToConverter.get(tag.getClass());
        return (V)converter.convert(tag);
    }
    
    public static <V, T extends Tag> T convertToTag(final String name, final V value) throws ConversionException {
        if (value == null) {
            return null;
        }
        TagConverter<T, V> converter = (TagConverter<T, V>)ConverterRegistry.typeToConverter.get(value.getClass());
        if (converter == null) {
            for (final Class<?> clazz : getAllClasses(value.getClass())) {
                if (ConverterRegistry.typeToConverter.containsKey(clazz)) {
                    try {
                        converter = (TagConverter<T, V>)ConverterRegistry.typeToConverter.get(clazz);
                        break;
                    }
                    catch (ClassCastException ex) {}
                }
            }
        }
        if (converter == null) {
            throw new ConversionException("Value type " + value.getClass().getName() + " has no converter.");
        }
        return converter.convert(name, value);
    }
    
    private static Set<Class<?>> getAllClasses(final Class<?> clazz) {
        final Set<Class<?>> ret = new LinkedHashSet<Class<?>>();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            ret.add(c);
            ret.addAll(getAllSuperInterfaces(c));
        }
        if (ret.contains(Serializable.class)) {
            ret.remove(Serializable.class);
            ret.add(Serializable.class);
        }
        return ret;
    }
    
    private static Set<Class<?>> getAllSuperInterfaces(final Class<?> clazz) {
        final Set<Class<?>> ret = new HashSet<Class<?>>();
        for (final Class<?> c : clazz.getInterfaces()) {
            ret.add(c);
            ret.addAll(getAllSuperInterfaces(c));
        }
        return ret;
    }
    
    static {
        tagToConverter = new HashMap<Class<? extends Tag>, TagConverter<? extends Tag, ?>>();
        typeToConverter = new HashMap<Class<?>, TagConverter<? extends Tag, ?>>();
        register(ByteTag.class, Byte.class, new ByteTagConverter());
        register(ShortTag.class, Short.class, new ShortTagConverter());
        register(IntTag.class, Integer.class, new IntTagConverter());
        register(LongTag.class, Long.class, new LongTagConverter());
        register(FloatTag.class, Float.class, new FloatTagConverter());
        register(DoubleTag.class, Double.class, new DoubleTagConverter());
        register(ByteArrayTag.class, byte[].class, new ByteArrayTagConverter());
        register(StringTag.class, String.class, new StringTagConverter());
        register(ListTag.class, List.class, new ListTagConverter());
        register(CompoundTag.class, Map.class, new CompoundTagConverter());
        register(IntArrayTag.class, int[].class, new IntArrayTagConverter());
        register(DoubleArrayTag.class, double[].class, new DoubleArrayTagConverter());
        register(FloatArrayTag.class, float[].class, new FloatArrayTagConverter());
        register(LongArrayTag.class, long[].class, new LongArrayTagConverter());
        register(SerializableArrayTag.class, Serializable[].class, new SerializableArrayTagConverter());
        register(SerializableTag.class, Serializable.class, new SerializableTagConverter());
        register(ShortArrayTag.class, short[].class, new ShortArrayTagConverter());
        register(StringArrayTag.class, String[].class, new StringArrayTagConverter());
    }
}
