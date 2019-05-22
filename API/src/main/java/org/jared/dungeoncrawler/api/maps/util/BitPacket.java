/*
 MIT License

 Copyright (c) inventivetalent

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
*/

// From https://github.com/bergerhealer/BKCommonLib

package org.jared.dungeoncrawler.api.maps.util;

public class BitPacket implements Cloneable
{
    public int data, bits;

    public BitPacket()
    {
        this.data = 0;
        this.bits = 0;
    }

    public BitPacket(int data, int bits)
    {
        this.data = data;
        this.bits = bits;
    }


    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }
        else if (o instanceof BitPacket)
        {
            BitPacket other = (BitPacket) o;
            if (other.bits == bits)
            {
                int mask = ((1 << bits) - 1);
                return (data & mask) == (other.data & mask);
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    @Override
    public BitPacket clone()
    {
        return new BitPacket(this.data, this.bits);
    }

    @Override
    public String toString()
    {
        String str = Integer.toBinaryString(data & ((1 << bits) - 1));
        while (str.length() < this.bits)
        {
            str = "0" + str;
        }
        return str;
    }
}
