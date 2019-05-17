package org.jared.dungeoncrawler.v1_12_R1;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.block.IMaterialAndData;
import org.jared.dungeoncrawler.api.generation.block.MaterialAndData;
import org.jared.dungeoncrawler.api.structures.AbstractStructure;
import org.jared.dungeoncrawler.api.util.VectorUtil;

import java.util.Map;

public class Structure extends AbstractStructure implements Cloneable
{
    public Structure(DefinedStructure definedStructure)
    {
        NBTTagCompound tag = definedStructure.a(new NBTTagCompound());

        this.dimensions = new int[] { tag.getList("size", 3).c(0), tag.getList("size", 3).c(1), tag.getList("size", 3).c(2) };

        NBTTagList states = tag.getList("palette", 10);
        NBTTagList blocks = tag.getList("blocks", 10);
        for (int i = 0; i < blocks.size(); i++)
        {
            NBTTagCompound blockTag = blocks.get(i);

            Vector position = new Vector(blockTag.getList("pos", 3).c(0), blockTag.getList("pos", 3).c(1), blockTag.getList("pos", 3).c(2));

            IBlockData data = GameProfileSerializer.d(states.get(blockTag.getInt("state")));
            Block block = Block.REGISTRY.get(new MinecraftKey(states.get(blockTag.getInt("state")).getString("Name")));

            Material material = CraftMagicNumbers.getMaterial(block);

            if (material.name().contains("SIGN"))
            {
                this.edgeCases.add(position);
            }
            else
            {
                this.blockMap.put(position, new MaterialAndData(material, "", (byte)block.toLegacyData(data)));
            }
        }
    }

    @Override
    public void rotate(int angle)
    {
        Map<Vector, IMaterialAndData> blockMapCopy = Maps.newHashMap();

        for (Map.Entry<Vector, IMaterialAndData> blockEntry : blockMap.entrySet())
        {
            Vector vector = blockEntry.getKey();
            IMaterialAndData materialAndData = blockEntry.getValue();
            Material material = materialAndData.getMaterial();
            byte data = materialAndData.getBlockDataLegacy();


            if (angle == 90)
            {
                data = (byte) rotate90(material.getId(), data);
            }
            else if (angle == 180)
            {
                data = (byte) rotate90(material.getId(), data);
                data = (byte) rotate90(material.getId(), data);
            }
            else if (angle == 270)
            {
                data = (byte) rotate90(material.getId(), data);
                data = (byte) rotate90(material.getId(), data);
                data = (byte) rotate90(material.getId(), data);
            }

            Vector offset = VectorUtil.rotateVector(vector, angle);
            blockMapCopy.put(offset, new MaterialAndData(material, "", data));
        }

        this.blockMap = blockMapCopy;
    }

    private static int rotate90(int type, int data)
    {
        int extra;
        int withoutFlags;
        switch (type)
        {
            case 17:
            case 162:
            case 170:
                if (data >= 4 && data <= 11)
                {
                    data ^= 12;
                }
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 24:
            case 25:
            case 30:
            case 31:
            case 32:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 51:
            case 52:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 70:
            case 72:
            case 73:
            case 74:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 83:
            case 84:
            case 85:
            case 87:
            case 88:
            case 89:
            case 90:
            case 92:
            case 95:
            case 97:
            case 98:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 110:
            case 111:
            case 112:
            case 113:
            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 129:
            case 132:
            case 133:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 147:
            case 148:
            case 151:
            case 152:
            case 153:
            case 155:
            case 159:
            case 160:
            case 161:
            case 165:
            case 166:
            case 168:
            case 169:
            default:
                break;
            case 23:
            case 158:
                extra = data & 8;
                switch (data & -9)
                {
                    case 2:
                        return 5 | extra;
                    case 3:
                        return 4 | extra;
                    case 4:
                        return 2 | extra;
                    case 5:
                        return 3 | extra;
                    default:
                        return data;
                }
            case 26:
                return data & -4 | data + 1 & 3;
            case 29:
            case 33:
            case 34:
                int rest = data & -8;
                switch (data & 7)
                {
                    case 2:
                        return 5 | rest;
                    case 3:
                        return 4 | rest;
                    case 4:
                        return 2 | rest;
                    case 5:
                        return 3 | rest;
                    default:
                        return data;
                }
            case 50:
            case 75:
            case 76:
                switch (data)
                {
                    case 1:
                        return 3;
                    case 2:
                        return 4;
                    case 3:
                        return 2;
                    case 4:
                        return 1;
                    default:
                        return data;
                }
            case 53:
            case 67:
            case 108:
            case 109:
            case 114:
            case 128:
            case 134:
            case 135:
            case 136:
            case 156:
            case 163:
            case 164:
                switch (data)
                {
                    case 0:
                        return 2;
                    case 1:
                        return 3;
                    case 2:
                        return 1;
                    case 3:
                        return 0;
                    case 4:
                        return 6;
                    case 5:
                        return 7;
                    case 6:
                        return 5;
                    case 7:
                        return 4;
                    default:
                        return data;
                }
            case 54:
            case 61:
            case 62:
            case 65:
            case 68:
            case 130:
            case 146:
            case 154:
                extra = data & 8;
                withoutFlags = data & -9;
                switch (withoutFlags)
                {
                    case 2:
                        return 5 | extra;
                    case 3:
                        return 4 | extra;
                    case 4:
                        return 2 | extra;
                    case 5:
                        return 3 | extra;
                    default:
                        return data;
                }
            case 63:
                return (data + 4) % 16;
            case 64:
            case 71:
                if ((data & 8) != 0)
                {
                    break;
                }
            case 127:
            case 131:
                extra = data & -4;
                withoutFlags = data & 3;
                switch (withoutFlags)
                {
                    case 0:
                        return 1 | extra;
                    case 1:
                        return 2 | extra;
                    case 2:
                        return 3 | extra;
                    case 3:
                        return 0 | extra;
                    default:
                        return data;
                }
            case 66:
                switch (data)
                {
                    case 6:
                        return 7;
                    case 7:
                        return 8;
                    case 8:
                        return 9;
                    case 9:
                        return 6;
                }
            case 27:
            case 28:
            case 157:
                switch (data & 7)
                {
                    case 0:
                        return 1 | data & -8;
                    case 1:
                        return 0 | data & -8;
                    case 2:
                        return 5 | data & -8;
                    case 3:
                        return 4 | data & -8;
                    case 4:
                        return 2 | data & -8;
                    case 5:
                        return 3 | data & -8;
                    default:
                        return data;
                }
            case 69:
                extra = data & 8;
                switch (data & -9)
                {
                    case 0:
                        return 7 | extra;
                    case 1:
                        return 3 | extra;
                    case 2:
                        return 4 | extra;
                    case 3:
                        return 2 | extra;
                    case 4:
                        return 1 | extra;
                    case 5:
                        return 6 | extra;
                    case 6:
                        return 5 | extra;
                    case 7:
                        return 0 | extra;
                    default:
                        return data;
                }
            case 77:
            case 143:
                extra = data & 8;
                switch (data & -9)
                {
                    case 1:
                        return 3 | extra;
                    case 2:
                        return 4 | extra;
                    case 3:
                        return 2 | extra;
                    case 4:
                        return 1 | extra;
                    default:
                        return data;
                }
            case 86:
            case 91:
                switch (data)
                {
                    case 0:
                        return 1;
                    case 1:
                        return 2;
                    case 2:
                        return 3;
                    case 3:
                        return 0;
                    default:
                        return data;
                }
            case 93:
            case 94:
            case 149:
            case 150:
                withoutFlags = data & 3;
                int delay = data - withoutFlags;
                switch (withoutFlags)
                {
                    case 0:
                        return 1 | delay;
                    case 1:
                        return 2 | delay;
                    case 2:
                        return 3 | delay;
                    case 3:
                        return 0 | delay;
                    default:
                        return data;
                }
            case 96:
            case 167:
                int withoutOrientation = data & -4;
                int orientation = data & 3;
                switch (orientation)
                {
                    case 0:
                        return 3 | withoutOrientation;
                    case 1:
                        return 2 | withoutOrientation;
                    case 2:
                        return 0 | withoutOrientation;
                    case 3:
                        return 1 | withoutOrientation;
                    default:
                        return data;
                }
            case 99:
            case 100:
                if (data >= 10)
                {
                    return data;
                }

                return data * 3 % 10;
            case 106:
                return (data << 1 | data >> 3) & 15;
            case 107:
                return data + 1 & 3 | data & -4;
            case 144:
                switch (data)
                {
                    case 2:
                        return 5;
                    case 3:
                        return 4;
                    case 4:
                        return 2;
                    case 5:
                        return 3;
                    default:
                        return data;
                }
            case 145:
                int damage = data & -4;
                switch (data & 3)
                {
                    case 0:
                        return 3 | damage;
                    case 1:
                        return 0 | damage;
                    case 2:
                        return 1 | damage;
                    case 3:
                        return 2 | damage;
                }
        }

        return data;
    }
}
