package org.jared.dungeoncrawler.v1_13_R2;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftMagicNumbers;
import org.bukkit.util.Vector;
import org.jared.dungeoncrawler.api.generation.block.IMaterialAndData;
import org.jared.dungeoncrawler.api.generation.block.MaterialAndData;
import org.jared.dungeoncrawler.api.plugin.DungeonCrawler;
import org.jared.dungeoncrawler.api.structures.AbstractStructure;
import org.jared.dungeoncrawler.api.structures.nbt.Tag;
import org.jared.dungeoncrawler.api.structures.nbt.*;
import org.jared.dungeoncrawler.api.structures.nbtTest.CompressedStreamTools;
import org.jared.dungeoncrawler.api.structures.nbtTest.NBTDynamicOps;
import org.jared.dungeoncrawler.api.structures.nbtTest.NBTDynamicOpsTest;
import org.jared.dungeoncrawler.api.util.VectorUtil;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiFunction;

public class Structure extends AbstractStructure
{
    public Structure(DefinedStructure definedStructure)
    {
        test();

        NBTTagCompound tag = definedStructure.a(new NBTTagCompound());

        this.dimensions = new int[]{tag.getList("size", 3).h(0), tag.getList("size", 3).h(1), tag.getList("size", 3).h(2)};

        NBTTagList states = tag.getList("palette", 10);
        NBTTagList blocks = tag.getList("blocks", 10);

        for (int i = 0; i < blocks.size(); i++)
        {
            NBTTagCompound blockTag = blocks.getCompound(i);

            Vector position = new Vector(blockTag.getList("pos", 3).h(0), blockTag.getList("pos", 3).h(1), blockTag.getList("pos", 3).h(2));

            //NBTTagCompound state = states.getCompound(blockTag.getInt("state"));
            //DungeonCrawler.LOG.debug(state.asString("Name"));

            IBlockData data = GameProfileSerializer.d(states.getCompound(blockTag.getInt("state")));
            Block block = IRegistry.BLOCK.get(new MinecraftKey(states.getCompound(blockTag.getInt("state")).getString("Name")));

            Material material = CraftMagicNumbers.getMaterial(block);

            if (material.name().contains("SIGN"))
            {
                this.edgeCases.add(position);
            }
            else
            {
                String dataString = data.toString();
                dataString = dataString.contains("[") ? dataString.substring(dataString.indexOf("["), dataString.indexOf("]") + 1) : "";

                Block block1 = IRegistry.BLOCK.get(new MinecraftKey("minecraft:stone_slab"));
                BlockStateList<Block, IBlockData> states1 = block1.getStates();
                try
                {
                    Field listField = BlockStateList.class.getDeclaredField("c");
                    listField.setAccessible(true);

                    ImmutableSortedMap<String, IBlockState<?>> map = (ImmutableSortedMap<String, IBlockState<?>>) listField.get(states1);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                this.blockMap.put(position, new MaterialAndData(material, dataString, (byte) 0));
            }
        }
    }

    public void test()
    {
        try
        {
            FileInputStream fileInputStream = new FileInputStream(DungeonCrawler.getPlugin().getDataFolder() + "\\Decorations\\Floor\\Wall\\bigtest.nbt");
            //NBTInputStream inputStream = new NBTInputStream(fileInputStream, true);

            CompoundTag compoundTag = null;
            //CompoundTag compoundTag = (CompoundTag) inputStream.readNamedTag().getTag();

            //compoundTag = updateTest(getDataFixer(), compoundTag, compoundTag.getInt("DataVersion"));

           // DungeonCrawler.LOG.warning(((CompoundTag) compoundTag.getList("palette").get(0)).asString("Name"));

            org.jared.dungeoncrawler.api.structures.nbtTest.NBTTagCompound previous = CompressedStreamTools.readCompressed(fileInputStream);

            org.jared.dungeoncrawler.api.structures.nbtTest.NBTTagCompound updated = update(getDataFixer(), previous, previous.getInt("DataVersion"));

            DungeonCrawler.LOG.console(updated.getList("palette", 10).getCompound(0).getString("Name"));


            //NBTTagCompound previous1 = NBTCompressedStreamTools.a(fileInputStream);

            //NBTTagCompound updated1 = updateTest(getDataFixer(), previous1, previous1.getInt("DataVersion"));

            //DungeonCrawler.LOG.warning(updated1.getList("palette", 10).getCompound(0).asString("Name"));

            ListTag sizeTag = compoundTag.getListTag("size");
            int dimensions[] = {sizeTag.getInt(0), sizeTag.getInt(1), sizeTag.getInt(2)};

            ListTag blockTags = compoundTag.getListTag("blocks");
            ListTag paletteTags;

            if (compoundTag.containsKey("palette"))
            {
                paletteTags = compoundTag.getListTag("palette");

                for (int i = 0; i < blockTags.getValue().size(); i++)
                {
                    CompoundTag blockTag = (CompoundTag) blockTags.getValue().get(i);

                    ListTag positionTag = blockTag.getListTag("pos");

                    Vector position = new Vector(positionTag.getInt(0), positionTag.getInt(1), positionTag.getInt(2));

                    CompoundTag stateTag = (CompoundTag) paletteTags.getValue().get(blockTag.getInt("state"));

                    getBlockData(stateTag);

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String getBlockData(CompoundTag compoundTag)
    {
        if (!compoundTag.containsKey("Name"))
        {
            return Material.AIR.createBlockData().getAsString();
        }
        else
        {
            //BlockData blockData = Bukkit.createBlockData(compoundTag.asString("Name"));
            //Material material = blockData.getMaterial();

            if (compoundTag.containsKey("Properties"))
            {
                CompoundTag propertyTag = (CompoundTag) compoundTag.getValue().get("Properties");

                String data = compoundTag.getString("Name") + toBlockData(propertyTag.getValue());

                DungeonCrawler.LOG.severe(data);

            }
        }

        return null;
    }

    public NBTTagCompound updateTest(DataFixer dataFixer, NBTTagCompound compoundTag, int version)
    {
        return (NBTTagCompound) dataFixer.update(DataFixTypes.STRUCTURE, new Dynamic<>(DynamicOpsNBT.a, compoundTag), version, 1631).getValue();
    }

    public org.jared.dungeoncrawler.api.structures.nbtTest.NBTTagCompound update(DataFixer dataFixer, org.jared.dungeoncrawler.api.structures.nbtTest.NBTTagCompound tagCompound, int version)
    {
        return (org.jared.dungeoncrawler.api.structures.nbtTest.NBTTagCompound) dataFixer.update(DataFixTypes.STRUCTURE, new Dynamic<>(DynamicOpsNBT.a, tagCompound), version, 1631).getValue();
    }

    DataFixer getDataFixer()
    {
        DataFixerBuilder dataFixerBuilder = new DataFixerBuilder(1631);
        populateDataFixer(dataFixerBuilder);
        ForkJoinPool pool = new ForkJoinPool(Integer.getInteger("net.minecraft.server.v1_13_R2.DataConverterRegistry.bootstrapThreads", Math.min(Runtime.getRuntime().availableProcessors(), 2)));
        DataFixer fixer = dataFixerBuilder.build(pool);
        pool.shutdown();
        return fixer;
    }

    private static final BiFunction<Integer, Schema, Schema> a = new BiFunction<Integer, Schema, Schema>()
    {
        @Override
        public Schema apply(Integer arg1, Schema arg2)
        {
            return new Schema(arg1, arg2);
        }
    };

    private static final BiFunction<Integer, Schema, Schema> b = new BiFunction<Integer, Schema, Schema>()
    {
        @Override
        public Schema apply(Integer arg_0, Schema arg_1)
        {
            return new DataConverterSchemaNamed(arg_0, arg_1);
        }
    };

    void populateDataFixer(DataFixerBuilder dataFixerBuilder)
    {
        dataFixerBuilder.addSchema(99, DataConverterSchemaV99::new);

        Schema equipmentSchema = dataFixerBuilder.addSchema(100, DataConverterSchemaV100::new);
        dataFixerBuilder.addFixer(new DataConverterEquipment(equipmentSchema, true));

        Schema signSchema = dataFixerBuilder.addSchema(101, a);
        dataFixerBuilder.addFixer(new DataConverterSignText(signSchema, false));

        Schema materialPotionSchema = dataFixerBuilder.addSchema(102, DataConverterSchemaV102::new);
        dataFixerBuilder.addFixer(new DataConverterMaterialId(materialPotionSchema, true));
        dataFixerBuilder.addFixer(new DataConverterPotionId(materialPotionSchema, false));

        Schema spawnEggSchema = dataFixerBuilder.addSchema(105, a);
        dataFixerBuilder.addFixer(new DataConverterSpawnEgg(spawnEggSchema, true));

        Schema mobSpawnerSchema = dataFixerBuilder.addSchema(106, DataConverterSchemaV106::new);
        dataFixerBuilder.addFixer(new DataConverterMobSpawner(mobSpawnerSchema, true));

        Schema minecartSchema = dataFixerBuilder.addSchema(107, DataConverterSchemaV107::new);
        dataFixerBuilder.addFixer(new DataConverterMinecart(minecartSchema, true));

        Schema uuidSchema = dataFixerBuilder.addSchema(108, a);
        dataFixerBuilder.addFixer(new DataConverterUUID(uuidSchema, true));

        Schema healthSchema = dataFixerBuilder.addSchema(109, a);
        dataFixerBuilder.addFixer(new DataConverterHealth(healthSchema, true));

        Schema saddleSchema = dataFixerBuilder.addSchema(110, a);
        dataFixerBuilder.addFixer(new DataConverterSaddle(saddleSchema, true));

        Schema hangingSchema = dataFixerBuilder.addSchema(111, a);
        dataFixerBuilder.addFixer(new DataConverterHanging(hangingSchema, true));

        Schema dropChanceSchema = dataFixerBuilder.addSchema(113, a);
        dataFixerBuilder.addFixer(new DataConverterDropChances(dropChanceSchema, true));

        Schema ridingSchema = dataFixerBuilder.addSchema(135, DataConverterSchemaV135::new);
        dataFixerBuilder.addFixer(new DataConverterRiding(ridingSchema, true));

        Schema tippedArrowSchema = dataFixerBuilder.addSchema(143, DataConverterSchemaV143::new);
        dataFixerBuilder.addFixer(new DataConverterEntityTippedArrow(tippedArrowSchema, true));

        Schema armourStandSchema = dataFixerBuilder.addSchema(147, a);
        dataFixerBuilder.addFixer(new DataConverterArmorStand(armourStandSchema, true));

        Schema bookSchema = dataFixerBuilder.addSchema(165, a);
        dataFixerBuilder.addFixer(new DataConverterBook(bookSchema, true));

        Schema addChoicesSchemaV1_10 = dataFixerBuilder.addSchema(501, DataConverterSchemaV501::new);
        dataFixerBuilder.addFixer(new DataConverterAddChoices(addChoicesSchemaV1_10, "Add 1.10 entities fix", DataConverterTypes.ENTITY));

        Schema zombieFishSchema = dataFixerBuilder.addSchema(502, a);
        dataFixerBuilder.addFixer(DataConverterItemName.a(zombieFishSchema, "cooked_fished item renamer", s -> Objects.equals(DataConverterSchemaNamed.a(s), "minecraft:cooked_fished") ? "minecraft:cooked_fish" : s));
        dataFixerBuilder.addFixer(new DataConverterZombie(zombieFishSchema, false));

        Schema vboSchema = dataFixerBuilder.addSchema(505, a);
        dataFixerBuilder.addFixer(new DataConverterVBO(vboSchema, false));

        Schema guardianSchema = dataFixerBuilder.addSchema(700, DataConverterSchemaV700::new);
        dataFixerBuilder.addFixer(new DataConverterGuardian(guardianSchema, true));

        Schema skeletonSchema = dataFixerBuilder.addSchema(701, DataConverterSchemaV701::new);
        dataFixerBuilder.addFixer(new DataConverterSkeleton(skeletonSchema, true));

        Schema zombieTypeSchema = dataFixerBuilder.addSchema(702, DataConverterSchemaV702::new);
        dataFixerBuilder.addFixer(new DataConverterZombieType(zombieTypeSchema, true));

        Schema horseSchema = dataFixerBuilder.addSchema(703, DataConverterSchemaV703::new);
        dataFixerBuilder.addFixer(new DataConverterHorse(horseSchema, true));

        Schema tileEntitySchema = dataFixerBuilder.addSchema(704, DataConverterSchemaV704::new);
        dataFixerBuilder.addFixer(new DataConverterTileEntity(tileEntitySchema, true));

        Schema entitySchema = dataFixerBuilder.addSchema(705, DataConverterSchemaV705::new);
        dataFixerBuilder.addFixer(new DataConverterEntity(entitySchema, true));

        Schema bannerSchema = dataFixerBuilder.addSchema(804, b);
        dataFixerBuilder.addFixer(new DataConverterBanner(bannerSchema, true));

        Schema potionWaterSchema = dataFixerBuilder.addSchema(806, b);
        dataFixerBuilder.addFixer(new DataConverterPotionWater(potionWaterSchema, false));

        Schema addChoicesSchemaShulkerBox = dataFixerBuilder.addSchema(808, DataConverterSchemaV808::new);
        dataFixerBuilder.addFixer(new DataConverterAddChoices(addChoicesSchemaShulkerBox, "added shulker box", DataConverterTypes.j));

        Schema shulkerSchema = dataFixerBuilder.addSchema(813, 1, b);
        dataFixerBuilder.addFixer(new DataConverterShulker(shulkerSchema, false));

        Schema shulkerBoxSchema = dataFixerBuilder.addSchema(813, b);
        dataFixerBuilder.addFixer(new DataConverterShulkerBoxItem(shulkerBoxSchema, false));
        dataFixerBuilder.addFixer(new DataConverterShulkerBoxBlock(shulkerBoxSchema, false));

        Schema langSchema = dataFixerBuilder.addSchema(816, b);
        dataFixerBuilder.addFixer(new DataConverterLang(langSchema, false));

        Schema totemItemSchema = dataFixerBuilder.addSchema(820, b);
        dataFixerBuilder.addFixer(DataConverterItemName.a(totemItemSchema, "totem item renamer", s -> Objects.equals(s, "minecraft:totem") ? "minecraft:totem_of_undying" : s));

        Schema shoulderEntitySchema = dataFixerBuilder.addSchema(1022, DataConverterSchemaV1022::new);
        dataFixerBuilder.addFixer(new DataConverterShoulderEntity(shoulderEntitySchema, "added shoulder entities to players", DataConverterTypes.PLAYER));

        Schema bedSchema = dataFixerBuilder.addSchema(1125, DataConverterSchemaV1125::new);
        dataFixerBuilder.addFixer(new DataConverterBedBlock(bedSchema, true));
        dataFixerBuilder.addFixer(new DataConverterBedItem(bedSchema, false));

        Schema keybindSchema = dataFixerBuilder.addSchema(1344, b);
        dataFixerBuilder.addFixer(new DataConverterKeybind(keybindSchema, false));

        Schema keybind2Schema = dataFixerBuilder.addSchema(1446, b);
        dataFixerBuilder.addFixer(new DataConverterKeybind2(keybind2Schema, false));

        Schema flattenStateSchema = dataFixerBuilder.addSchema(1450, b);
        dataFixerBuilder.addFixer(new DataConverterFlattenState(flattenStateSchema, false));

        Schema addChoicesSchemaTrappedChest = dataFixerBuilder.addSchema(1451, DataConverterSchemaV1451::new);
        dataFixerBuilder.addFixer(new DataConverterAddChoices(addChoicesSchemaTrappedChest, "AddTrappedChestFix", DataConverterTypes.j));

        Schema paletteSchema = dataFixerBuilder.addSchema(1451, 1, DataConverterSchemaV1451_1::new);
        dataFixerBuilder.addFixer(new ChunkConverterPalette(paletteSchema, true));

        Schema pistonSchema = dataFixerBuilder.addSchema(1451, 2, DataConverterSchemaV1451_2::new);
        dataFixerBuilder.addFixer(new DataConverterPiston(pistonSchema, true));

        Schema entityBlockStateAndMapSchema = dataFixerBuilder.addSchema(1451, 3, DataConverterSchemaV1451_3::new);
        dataFixerBuilder.addFixer(new DataConverterEntityBlockState(entityBlockStateAndMapSchema, true));
        dataFixerBuilder.addFixer(new DataConverterMap(entityBlockStateAndMapSchema, false));

        Schema blockNameFlattenSchema = dataFixerBuilder.addSchema(1451, 4, DataConverterSchemaV1451_4::new);
        dataFixerBuilder.addFixer(new DataConverterBlockName(blockNameFlattenSchema, true));
        dataFixerBuilder.addFixer(new DataConverterFlatten(blockNameFlattenSchema, false));

        Schema sub5Schema = dataFixerBuilder.addSchema(1451, 5, DataConverterSchemaV1451_5::new);
        dataFixerBuilder.addFixer(new DataConverterAddChoices(sub5Schema, "RemoveNoteBlockFlowerPotFix", DataConverterTypes.j));
        dataFixerBuilder.addFixer(new DataConverterFlattenSpawnEgg(sub5Schema, false));
        dataFixerBuilder.addFixer(new DataConverterWolf(sub5Schema, false));
        dataFixerBuilder.addFixer(new DataConverterBannerColour(sub5Schema, false));
        dataFixerBuilder.addFixer(new DataConverterWorldGenSettings(sub5Schema, false));

        Schema sub6Schema = dataFixerBuilder.addSchema(1451, 7, DataConverterSchemaV1451_6::new);
        dataFixerBuilder.addFixer(new DataConverterStatistic(sub6Schema, true));
        dataFixerBuilder.addFixer(new DataConverterJukeBox(sub6Schema, false));

        Schema villageSchema = dataFixerBuilder.addSchema(1451, 7, DataConverterSchemaV1451_7::new);
        dataFixerBuilder.addFixer(new DataConverterVillage(villageSchema, true));

        Schema villagerTradeSchema = dataFixerBuilder.addSchema(1451, 7, b);
        dataFixerBuilder.addFixer(new DataConverterVillagerTrade(villagerTradeSchema, false));

        Schema itemFrameSchema = dataFixerBuilder.addSchema(1456, b);
        dataFixerBuilder.addFixer(new DataConverterItemFrame(itemFrameSchema, false));

        Schema customNameSchema = dataFixerBuilder.addSchema(1458, b);
        dataFixerBuilder.addFixer(new DataFix(customNameSchema, false)
        {
            @Override
            protected TypeRewriteRule makeRule()
            {
                return this.fixTypeEverywhereTyped("Player CustomName", this.getInputSchema().getType(DataConverterTypes.PLAYER), typed -> typed.update(DSL.remainderFinder(), dynamic -> DataConverterCustomNameEntity.a(dynamic)));
            }
        });
        dataFixerBuilder.addFixer(new DataConverterCustomNameEntity(customNameSchema, false));
        dataFixerBuilder.addFixer(new DataConverterCustomNameItem(customNameSchema, false));
        dataFixerBuilder.addFixer(new DataConverterCustomNameTile(customNameSchema, false));

        Schema paintingSchema = dataFixerBuilder.addSchema(1460, DataConverterSchemaV1460::new);
        dataFixerBuilder.addFixer(new DataConverterPainting(paintingSchema, false));

        Schema protoChunkSchema = dataFixerBuilder.addSchema(1466, DataConverterSchemaV1466::new);
        dataFixerBuilder.addFixer(new DataConverterProtoChunk(protoChunkSchema, true));

        Schema addChoicesSchemaV1_13 = dataFixerBuilder.addSchema(1470, DataConverterSchemaV1470::new);
        dataFixerBuilder.addFixer(new DataConverterAddChoices(addChoicesSchemaV1_13, "Add 1.13 entities fix", DataConverterTypes.ENTITY));

        Schema shulkerColorSchema = dataFixerBuilder.addSchema(1474, b);
        dataFixerBuilder.addFixer(new DataConverterColorlessShulkerEntity(shulkerColorSchema, false));
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(shulkerColorSchema, "Colorless shulker block fixer", s -> Objects.equals(DataConverterSchemaNamed.a(s), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : s));
        dataFixerBuilder.addFixer(DataConverterItemName.a(shulkerColorSchema, "Colorless shulker item fixer", s -> Objects.equals(DataConverterSchemaNamed.a(s), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : s));

        Schema flowingSchema = dataFixerBuilder.addSchema(1475, b);
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(flowingSchema, "Flowing fixer", s -> ImmutableMap.of("minecraft:flowing_water", "minecraft:water", "minecraft:flowing_lava", "minecraft:lava").getOrDefault(s, s)));

        Schema coralBlocksSchema = dataFixerBuilder.addSchema(1480, b);
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(coralBlocksSchema, "Rename coral blocks", s -> DataConverterCoral.a.getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(DataConverterItemName.a(coralBlocksSchema, "Rename coral items", s -> DataConverterCoral.a.getOrDefault(s, (String) s)));

        Schema conduitSchema = dataFixerBuilder.addSchema(1481, DataConverterSchemaV1481::new);
        dataFixerBuilder.addFixer(new DataConverterAddChoices(conduitSchema, "Add conduit", DataConverterTypes.j));

        Schema pufferFishSchema = dataFixerBuilder.addSchema(1483, DataConverterSchemaV1483::new);
        dataFixerBuilder.addFixer(new DataConverterEntityPufferfish(pufferFishSchema, true));
        dataFixerBuilder.addFixer(DataConverterItemName.a(pufferFishSchema, "Rename pufferfish egg item", s -> DataConverterEntityPufferfish.a.getOrDefault(s, (String) s)));

        Schema seagrassSchema = dataFixerBuilder.addSchema(1484, b);
        dataFixerBuilder.addFixer(DataConverterItemName.a(seagrassSchema, "Rename seagrass items", s -> ImmutableMap.of("minecraft:sea_grass", "minecraft:seagrass", "minecraft:tall_sea_grass", "minecraft:tall_seagrass").getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(seagrassSchema, "Rename seagrass blocks", s -> ImmutableMap.of("minecraft:sea_grass", "minecraft:seagrass", "minecraft:tall_sea_grass", "minecraft:tall_seagrass").getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(new DataConverterHeightmapRenaming(seagrassSchema, false));

        Schema codSalmonSchema = dataFixerBuilder.addSchema(1486, DataConverterSchemaV1486::new);
        dataFixerBuilder.addFixer(new DataConverterEntityCodSalmon(codSalmonSchema, true));
        dataFixerBuilder.addFixer(DataConverterItemName.a(codSalmonSchema, "Rename cod/salmon egg items", s -> DataConverterEntityCodSalmon.b.getOrDefault(s, (String) s)));

        Schema prismarineSchema = dataFixerBuilder.addSchema(1487, b);
        dataFixerBuilder.addFixer(DataConverterItemName.a(prismarineSchema, "Rename prismarine_brick(s)_* blocks", s -> ImmutableMap.of("minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab", "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs").getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(prismarineSchema, "Rename prismarine_brick(s)_* items", s -> ImmutableMap.of("minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab", "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs").getOrDefault(s, (String) s)));

        Schema kelpSchema = dataFixerBuilder.addSchema(1488, b);
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(kelpSchema, "Rename kelp/kelptop", s -> ImmutableMap.of("minecraft:kelp_top", "minecraft:kelp", "minecraft:kelp", "minecraft:kelp_plant").getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(DataConverterItemName.a(kelpSchema, "Rename kelptop", s -> Objects.equals(s, "minecraft:kelp_top") ? "minecraft:kelp" : s));
        dataFixerBuilder.addFixer(new DataConverterNamedEntity(kelpSchema, false, "Command block block entity custom name fix", DataConverterTypes.j, "minecraft:command_block")
        {

            @Override
            protected Typed<?> a(Typed<?> typed)
            {
                return typed.update(DSL.remainderFinder(), DataConverterCustomNameEntity::a);
            }
        });
        dataFixerBuilder.addFixer(new DataConverterNamedEntity(kelpSchema, false, "Command block minecart custom name fix", DataConverterTypes.ENTITY, "minecraft:commandblock_minecart")
        {

            @Override
            protected Typed<?> a(Typed<?> typed)
            {
                return typed.update(DSL.remainderFinder(), DataConverterCustomNameEntity::a);
            }
        });
        dataFixerBuilder.addFixer(new DataConverterIglooMetadataRemoval(kelpSchema, false));

        Schema melonSchema = dataFixerBuilder.addSchema(1490, b);
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(melonSchema, "Rename melon_block", s -> Objects.equals(s, "minecraft:melon_block") ? "minecraft:melon" : s));
        dataFixerBuilder.addFixer(DataConverterItemName.a(melonSchema, "Rename melon_block/melon/speckled_melon", s -> ImmutableMap.of("minecraft:melon_block", "minecraft:melon", "minecraft:melon", "minecraft:melon_slice", "minecraft:speckled_melon", "minecraft:glistering_melon_slice").getOrDefault(s, (String) s)));

        Schema templateRenameSchema = dataFixerBuilder.addSchema(1492, b);
        dataFixerBuilder.addFixer(new DataConverterChunkStructuresTemplateRename(templateRenameSchema, false));

        Schema itemEnchant = dataFixerBuilder.addSchema(1494, b);
        dataFixerBuilder.addFixer(new DataConverterItemStackEnchantment(itemEnchant, false));

        Schema leavesSchema = dataFixerBuilder.addSchema(1496, b);
        dataFixerBuilder.addFixer(new DataConverterLeaves(leavesSchema, false));

        Schema entityPackedSchema = dataFixerBuilder.addSchema(1500, b);
        dataFixerBuilder.addFixer(new DataConverterBlockEntityKeepPacked(entityPackedSchema, false));

        Schema advancementSchema = dataFixerBuilder.addSchema(1501, b);
        dataFixerBuilder.addFixer(new DataConverterAdvancement(advancementSchema, false));

        Schema recipesSchema = dataFixerBuilder.addSchema(1502, b);
        dataFixerBuilder.addFixer(new DataConverterRecipes(recipesSchema, false));

        Schema levelSchema = dataFixerBuilder.addSchema(1506, b);
        dataFixerBuilder.addFixer(new DataConverterLevelDataGeneratorOptions(leavesSchema, false));

        Schema biomeSchema = dataFixerBuilder.addSchema(1508, b);
        dataFixerBuilder.addFixer(new DataConverterBiome(biomeSchema, false));

        Schema V1510Schema = dataFixerBuilder.addSchema(1510, DataConverterSchemaV1510::new);
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(V1510Schema, "Block renamening fix", s -> DataConverterEntityRename.b.getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(DataConverterItemName.a(V1510Schema, "Item renamening fix", s -> DataConverterEntityRename.c.getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(new DataConverterRecipeRename(V1510Schema, false));
        dataFixerBuilder.addFixer(new DataConverterEntityRename(V1510Schema, true));
        dataFixerBuilder.addFixer(new DataConverterSwimStats(V1510Schema, false));

        Schema displayNameSchema = dataFixerBuilder.addSchema(1514, b);
        dataFixerBuilder.addFixer(new DataConverterObjectiveDisplayName(displayNameSchema, false));
        dataFixerBuilder.addFixer(new DataConverterTeamDisplayName(displayNameSchema, false));
        dataFixerBuilder.addFixer(new DataConverterObjectiveRenderType(displayNameSchema, false));

        Schema coralFanBlockSchema = dataFixerBuilder.addSchema(1515, b);
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(coralFanBlockSchema, "Rename coral fan blocks", s -> DataConverterCoralFan.a.getOrDefault(s, (String) s)));

        Schema trappedChestSchema = dataFixerBuilder.addSchema(1624, b);
        dataFixerBuilder.addFixer(new DataConverterTrappedChest(trappedChestSchema, false));
    }

    public String toBlockData(Map<String, Tag> map)
    {
        StringBuilder stringBuilder = new StringBuilder("[");

        Iterator<Map.Entry<String, Tag>> entryIterator = map.entrySet().iterator();
        while (entryIterator.hasNext())
        {
            Map.Entry<String, Tag> entry = entryIterator.next();
            String key = entry.getKey();
            StringTag value = (StringTag) entry.getValue();

            stringBuilder.append(entry.getKey()).append("=").append(value.getValue());

            if (entryIterator.hasNext())
                stringBuilder.append(",");
        }

        return stringBuilder.append("]").toString();
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

            BlockData blockData = material.createBlockData(materialAndData.getBlockData());

            if (blockData instanceof Directional)
            {
                Directional directional = (Directional) blockData;
                Vector directionVector = VectorUtil.toVector(directional.getFacing());
                directionVector = VectorUtil.rotateVector(directionVector, angle);
                ((Directional) blockData).setFacing(VectorUtil.fromVector(directionVector));
            }

            Vector offset = VectorUtil.rotateVector(vector, angle);

            String dataString = blockData.getAsString();
            dataString = dataString.contains("[") ? dataString.substring(dataString.indexOf("["), dataString.indexOf("]") + 1) : "";

            blockMapCopy.put(offset, new MaterialAndData(material, dataString, (byte) 0));
        }

        this.blockMap = blockMapCopy;
    }
}
