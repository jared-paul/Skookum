package org.jared.dungeoncrawler.api.nbt;

public class NBTSizeTracker
{
    public static final NBTSizeTracker INFINITE = new NBTSizeTracker(0L)
    {
        public void read(long bits)
        {
        }
    };
    private final long max;
    private long read;

    public NBTSizeTracker(long max)
    {
        this.max = max;
    }

    public void read(long bits)
    {
        this.read += bits / 8L;
        if (this.read > this.max)
        {
            throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.read + "bytes where max allowed: " + this.max);
        }
    }

    public static void readUTF(NBTSizeTracker tracker, String data)
    {
        tracker.read(16L);
        if (data != null)
        {
            int len = data.length();
            int utflen = 0;

            for (int i = 0; i < len; ++i)
            {
                int c = data.charAt(i);
                if (c >= 1 && c <= 127)
                {
                    ++utflen;
                }
                else if (c > 2047)
                {
                    utflen += 3;
                }
                else
                {
                    utflen += 2;
                }
            }

            tracker.read((long) (8 * utflen));
        }
    }
}
