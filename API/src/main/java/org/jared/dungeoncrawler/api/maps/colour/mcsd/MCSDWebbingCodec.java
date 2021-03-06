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

package org.jared.dungeoncrawler.api.maps.colour.mcsd;

import org.jared.dungeoncrawler.api.maps.util.BitInputStream;
import org.jared.dungeoncrawler.api.maps.util.BitPacket;

import java.io.IOException;

public class MCSDWebbingCodec
{
    private int written_cells;
    private int last_x, last_y;
    private int last_dx, last_dy;
    public boolean[] strands = new boolean[1 << 16];
    private BitPacket[] packets = new BitPacket[1024];
    private int packets_count = 0;

    public MCSDWebbingCodec()
    {
        for (int i = 0; i < this.packets.length; i++)
        {
            this.packets[i] = new BitPacket();
        }
    }


    public void reset(boolean[] cells, boolean copyCells)
    {
        if (copyCells)
        {
            System.arraycopy(cells, 0, this.strands, 0, cells.length);
        }
        else
        {
            this.strands = cells;
        }
        this.written_cells = 0;
        this.last_x = -1000;
        this.last_y = -1000;
        this.last_dx = 1;
        this.last_dy = 1;
        this.packets_count = 0;
    }


    public boolean readNext(BitInputStream stream) throws IOException
    {
        int op = stream.readBits(2);
        if (op == 0b11)
        {
            if (stream.readBits(1) == 1)
            {
                // Set DX/DY increment/decrement
                int sub = stream.readBits(2);
                if (sub == 0b00)
                {
                    last_dx = -1;
                }
                else if (sub == 0b01)
                {
                    last_dx = 1;
                }
                else if (sub == 0b10)
                {
                    last_dy = -1;
                }
                else if (sub == 0b11)
                {
                    last_dy = 1;
                }
            }
            else
            {
                // Command codes
                if (stream.readBits(1) == 1)
                {
                    // End of slice
                    return false;
                }
                else
                {
                    // Reset position
                    last_x = stream.readBits(8);
                    last_y = stream.readBits(8);
                    strands[last_x | (last_y << 8)] = true;
                }
            }
        }
        else
        {
            // Write next pixel
            if (op == 0b00)
            {
                last_x += last_dx;
            }
            else if (op == 0b01)
            {
                last_y += last_dy;
            }
            else if (op == 0b10)
            {
                last_x += last_dx;
                last_y += last_dy;
            }
            strands[last_x | (last_y << 8)] = true;
        }
        return true;
    }

}