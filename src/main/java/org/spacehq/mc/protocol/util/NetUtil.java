// 
// Decompiled by Procyon v0.5.30
// 

package org.spacehq.mc.protocol.util;

import org.spacehq.mc.protocol.data.game.NibbleArray3d;
import org.spacehq.mc.protocol.data.game.ShortArray3d;
import java.nio.ShortBuffer;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import org.spacehq.mc.protocol.data.game.Chunk;
import java.util.List;
import org.spacehq.mc.protocol.data.game.Rotation;
import org.spacehq.mc.protocol.data.game.values.MagicValues;
import org.spacehq.mc.protocol.data.game.values.entity.MetadataType;
import java.util.ArrayList;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.opennbt.tag.builtin.Tag;
import java.io.OutputStream;
import java.io.DataOutputStream;
import org.spacehq.packetlib.io.NetOutput;
import java.io.IOException;
import org.spacehq.opennbt.NBTIO;
import java.io.InputStream;
import java.io.DataInputStream;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.io.NetInput;

public class NetUtil
{
    private static final int POSITION_X_SIZE = 38;
    private static final int POSITION_Y_SIZE = 26;
    private static final int POSITION_Z_SIZE = 38;
    private static final int POSITION_Y_SHIFT = 4095;
    private static final int POSITION_WRITE_SHIFT = 67108863;
    
    public static CompoundTag readNBT(final NetInput in) throws IOException {
        final byte b = in.readByte();
        if (b == 0) {
            return null;
        }
        return (CompoundTag)NBTIO.readTag(new DataInputStream(new NetInputStream(in, b)));
    }
    
    public static void writeNBT(final NetOutput out, final CompoundTag tag) throws IOException {
        if (tag == null) {
            out.writeByte(0);
        }
        else {
            NBTIO.writeTag(new DataOutputStream(new NetOutputStream(out)), tag);
        }
    }
    
    public static Position readPosition(final NetInput in) throws IOException {
        final long val = in.readLong();
        final int x = (int)(val >> 38);
        final int y = (int)(val >> 26 & 0xFFFL);
        final int z = (int)(val << 38 >> 38);
        return new Position(x, y, z);
    }
    
    public static void writePosition(final NetOutput out, final Position pos) throws IOException {
        final long x = pos.getX() & 0x3FFFFFF;
        final long y = pos.getY() & 0xFFF;
        final long z = pos.getZ() & 0x3FFFFFF;
        out.writeLong(x << 38 | y << 26 | z);
    }
    
    public static ItemStack readItem(final NetInput in) throws IOException {
        final short item = in.readShort();
        if (item < 0) {
            return null;
        }
        return new ItemStack(item, in.readByte(), in.readShort(), readNBT(in));
    }
    
    public static void writeItem(final NetOutput out, final ItemStack item) throws IOException {
        if (item == null) {
            out.writeShort(-1);
        }
        else {
            out.writeShort(item.getId());
            out.writeByte(item.getAmount());
            out.writeShort(item.getData());
            writeNBT(out, item.getNBT());
        }
    }
    
    public static EntityMetadata[] readEntityMetadata(final NetInput in) throws IOException {
        final List<EntityMetadata> ret = new ArrayList<EntityMetadata>();
        byte b;
        while ((b = in.readByte()) != 127) {
            final int typeId = (b & 0xE0) >> 5;
            final int id = b & 0x1F;
            final MetadataType type = MagicValues.key(MetadataType.class, typeId);
            Object value = null;
            switch (type) {
                case BYTE: {
                    value = in.readByte();
                    break;
                }
                case SHORT: {
                    value = in.readShort();
                    break;
                }
                case INT: {
                    value = in.readInt();
                    break;
                }
                case FLOAT: {
                    value = in.readFloat();
                    break;
                }
                case STRING: {
                    value = in.readString();
                    break;
                }
                case ITEM: {
                    value = readItem(in);
                    break;
                }
                case POSITION: {
                    value = new Position(in.readInt(), in.readInt(), in.readInt());
                    break;
                }
                case ROTATION: {
                    value = new Rotation(in.readFloat(), in.readFloat(), in.readFloat());
                    break;
                }
                default: {
                    throw new IOException("Unknown metadata type id: " + typeId);
                }
            }
            ret.add(new EntityMetadata(id, type, value));
        }
        return ret.toArray(new EntityMetadata[ret.size()]);
    }
    
    public static void writeEntityMetadata(final NetOutput out, final EntityMetadata[] metadata) throws IOException {
        for (final EntityMetadata meta : metadata) {
            final int id = MagicValues.value(Integer.class, meta.getType()) << 5 | (meta.getId() & 0x1F);
            out.writeByte(id);
            switch (meta.getType()) {
                case BYTE: {
                    out.writeByte((byte)meta.getValue());
                    break;
                }
                case SHORT: {
                    out.writeShort((short)meta.getValue());
                    break;
                }
                case INT: {
                    out.writeInt((int)meta.getValue());
                    break;
                }
                case FLOAT: {
                    out.writeFloat((float)meta.getValue());
                    break;
                }
                case STRING: {
                    out.writeString((String)meta.getValue());
                    break;
                }
                case ITEM: {
                    writeItem(out, (ItemStack)meta.getValue());
                    break;
                }
                case POSITION: {
                    final Position pos = (Position)meta.getValue();
                    out.writeInt(pos.getX());
                    out.writeInt(pos.getY());
                    out.writeInt(pos.getZ());
                    break;
                }
                case ROTATION: {
                    final Rotation rot = (Rotation)meta.getValue();
                    out.writeFloat(rot.getPitch());
                    out.writeFloat(rot.getYaw());
                    out.writeFloat(rot.getRoll());
                    break;
                }
                default: {
                    throw new IOException("Unmapped metadata type: " + meta.getType());
                }
            }
        }
        out.writeByte(127);
    }
    
    public static ParsedChunkData dataToChunks(final NetworkChunkData data, final boolean checkForSky) {
        final Chunk[] chunks = new Chunk[16];
        int pos = 0;
        int expected = 0;
        boolean sky = false;
        final ShortBuffer buf = ByteBuffer.wrap(data.getData()).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        for (int pass = 0; pass < 4; ++pass) {
            for (int ind = 0; ind < 16; ++ind) {
                if ((data.getMask() & 1 << ind) != 0x0) {
                    if (pass == 0) {
                        expected += 10240;
                    }
                    if (pass == 1) {
                        chunks[ind] = new Chunk(sky || data.hasSkyLight());
                        final ShortArray3d blocks = chunks[ind].getBlocks();
                        buf.position(pos / 2);
                        buf.get(blocks.getData(), 0, blocks.getData().length);
                        pos += blocks.getData().length * 2;
                    }
                    if (pass == 2) {
                        final NibbleArray3d blocklight = chunks[ind].getBlockLight();
                        System.arraycopy(data.getData(), pos, blocklight.getData(), 0, blocklight.getData().length);
                        pos += blocklight.getData().length;
                    }
                    if (pass == 3 && (sky || data.hasSkyLight())) {
                        final NibbleArray3d skylight = chunks[ind].getSkyLight();
                        System.arraycopy(data.getData(), pos, skylight.getData(), 0, skylight.getData().length);
                        pos += skylight.getData().length;
                    }
                }
            }
            if (pass == 0 && data.getData().length > expected) {
                sky = checkForSky;
            }
        }
        byte[] biomeData = null;
        if (data.isFullChunk()) {
            biomeData = new byte[256];
           // try {
                //System.arraycopy(data.getData(), pos, biomeData, 0, biomeData.length);
           // }
            //catch (ArrayIndexOutOfBoundsException ex){
           // System.out.println("dzialawtf123871872387123618381268326186831263187\n\n");
        //}
            pos += biomeData.length;
        }
        return new ParsedChunkData(chunks, biomeData);
    }
    
    public static NetworkChunkData chunksToData(final ParsedChunkData chunks) {
        int chunkMask = 0;
        final boolean fullChunk = chunks.getBiomes() != null;
        boolean sky = false;
        int length = fullChunk ? chunks.getBiomes().length : 0;
        byte[] data = null;
        int pos = 0;
        ShortBuffer buf = null;
        for (int pass = 0; pass < 4; ++pass) {
            for (int ind = 0; ind < chunks.getChunks().length; ++ind) {
                final Chunk chunk = chunks.getChunks()[ind];
                if (chunk != null && (!fullChunk || !chunk.isEmpty())) {
                    if (pass == 0) {
                        chunkMask |= 1 << ind;
                        length += chunk.getBlocks().getData().length * 2;
                        length += chunk.getBlockLight().getData().length;
                        if (chunk.getSkyLight() != null) {
                            length += chunk.getSkyLight().getData().length;
                        }
                    }
                    if (pass == 1) {
                        final short[] blocks = chunk.getBlocks().getData();
                        buf.position(pos / 2);
                        buf.put(blocks, 0, blocks.length);
                        pos += blocks.length * 2;
                    }
                    if (pass == 2) {
                        final byte[] blocklight = chunk.getBlockLight().getData();
                        System.arraycopy(blocklight, 0, data, pos, blocklight.length);
                        pos += blocklight.length;
                    }
                    if (pass == 3 && chunk.getSkyLight() != null) {
                        final byte[] skylight = chunk.getSkyLight().getData();
                        System.arraycopy(skylight, 0, data, pos, skylight.length);
                        pos += skylight.length;
                        sky = true;
                    }
                }
            }
            if (pass == 0) {
                data = new byte[length];
                buf = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
            }
        }
        if (fullChunk) {
            System.arraycopy(chunks.getBiomes(), 0, data, pos, chunks.getBiomes().length);
            pos += chunks.getBiomes().length;
        }
        return new NetworkChunkData(chunkMask, fullChunk, sky, data);
    }
    
    private static class NetInputStream extends InputStream
    {
        private NetInput in;
        private boolean readFirst;
        private byte firstByte;
        
        public NetInputStream(final NetInput in, final byte firstByte) {
            this.in = in;
            this.firstByte = firstByte;
        }
        
        @Override
        public int read() throws IOException {
            if (!this.readFirst) {
                this.readFirst = true;
                return this.firstByte;
            }
            return this.in.readUnsignedByte();
        }
    }
    
    private static class NetOutputStream extends OutputStream
    {
        private NetOutput out;
        
        public NetOutputStream(final NetOutput out) {
            this.out = out;
        }
        
        @Override
        public void write(final int b) throws IOException {
            this.out.writeByte(b);
        }
    }
}
