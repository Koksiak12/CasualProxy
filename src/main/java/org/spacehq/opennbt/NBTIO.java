// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.opennbt;

import org.spacehq.opennbt.tag.TagCreateException;
import org.spacehq.opennbt.tag.TagRegistry;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.FileOutputStream;
import org.spacehq.opennbt.tag.builtin.Tag;
import java.io.InputStream;
import java.io.DataInputStream;
import java.util.zip.GZIPInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

public class NBTIO
{
    public static CompoundTag readFile(final String path) throws IOException {
        return readFile(new File(path));
    }
    
    public static CompoundTag readFile(final File file) throws IOException {
        return readFile(file, true);
    }
    
    public static CompoundTag readFile(final String path, final boolean compressed) throws IOException {
        return readFile(new File(path), compressed);
    }
    
    public static CompoundTag readFile(final File file, final boolean compressed) throws IOException {
        InputStream in = new FileInputStream(file);
        if (compressed) {
            in = new GZIPInputStream(in);
        }
        final Tag tag = readTag(new DataInputStream(in));
        if (!(tag instanceof CompoundTag)) {
            throw new IOException("Root tag is not a CompoundTag!");
        }
        return (CompoundTag)tag;
    }
    
    public static void writeFile(final CompoundTag tag, final String path) throws IOException {
        writeFile(tag, new File(path));
    }
    
    public static void writeFile(final CompoundTag tag, final File file) throws IOException {
        writeFile(tag, file, true);
    }
    
    public static void writeFile(final CompoundTag tag, final String path, final boolean compressed) throws IOException {
        writeFile(tag, new File(path), compressed);
    }
    
    public static void writeFile(final CompoundTag tag, final File file, final boolean compressed) throws IOException {
        if (!file.exists()) {
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
        OutputStream out = new FileOutputStream(file);
        if (compressed) {
            out = new GZIPOutputStream(out);
        }
        writeTag(new DataOutputStream(out), tag);
        out.close();
    }
    
    public static Tag readTag(final DataInputStream in) throws IOException {
        final int id = in.readUnsignedByte();
        if (id == 0) {
            return null;
        }
        final String name = in.readUTF();
        Tag tag;
        try {
            tag = TagRegistry.createInstance(id, name);
        }
        catch (TagCreateException e) {
            throw new IOException("Failed to create tag.", e);
        }
        tag.read(in);
        return tag;
    }
    
    public static void writeTag(final DataOutputStream out, final Tag tag) throws IOException {
        out.writeByte(TagRegistry.getIdFor(tag.getClass()));
        out.writeUTF(tag.getName());
        tag.write(out);
    }
}
