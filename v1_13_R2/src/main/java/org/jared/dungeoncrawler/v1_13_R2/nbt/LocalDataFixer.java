package org.jared.dungeoncrawler.v1_13_R2.nbt;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.*;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.server.v1_13_R2.*;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiFunction;

public class LocalDataFixer
{
    private DataFixer attachedDataFixer;

    public LocalDataFixer()
    {
        this.attachedDataFixer = createDataFixer();
    }

    public DataFixer getAttachedDataFixer()
    {
        return attachedDataFixer;
    }

    private DataFixer createDataFixer()
    {
        DataFixerBuilder dataFixerBuilder = new DataFixerBuilder(1631);
        populateDataFixer(dataFixerBuilder);
        ForkJoinPool pool = new ForkJoinPool(Integer.getInteger("net.minecraft.server.v1_13_R2.DataConverterRegistry.bootstrapThreads", Math.min(Runtime.getRuntime().availableProcessors(), 2)));
        DataFixer fixer = dataFixerBuilder.build(pool);
        pool.shutdown();
        return fixer;
    }

    private static final BiFunction<Integer, Schema, Schema> schemaBiFunction = Schema::new;
    private static final BiFunction<Integer, Schema, Schema> namedSchemaBiFunction = DataConverterSchemaNamed::new;

    private void populateDataFixer(DataFixerBuilder dataFixerBuilder)
    {
        dataFixerBuilder.addSchema(99, DataConverterSchemaV99::new);

        Schema equipmentSchema = dataFixerBuilder.addSchema(100, DataConverterSchemaV100::new);
        dataFixerBuilder.addFixer(new DataConverterEquipment(equipmentSchema, true));

        Schema signSchema = dataFixerBuilder.addSchema(101, schemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterSignText(signSchema, false));

        Schema materialPotionSchema = dataFixerBuilder.addSchema(102, DataConverterSchemaV102::new);
        dataFixerBuilder.addFixer(new DataConverterMaterialId(materialPotionSchema, true));
        dataFixerBuilder.addFixer(new DataConverterPotionId(materialPotionSchema, false));

        Schema spawnEggSchema = dataFixerBuilder.addSchema(105, schemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterSpawnEgg(spawnEggSchema, true));

        Schema mobSpawnerSchema = dataFixerBuilder.addSchema(106, DataConverterSchemaV106::new);
        dataFixerBuilder.addFixer(new DataConverterMobSpawner(mobSpawnerSchema, true));

        Schema minecartSchema = dataFixerBuilder.addSchema(107, DataConverterSchemaV107::new);
        dataFixerBuilder.addFixer(new DataConverterMinecart(minecartSchema, true));

        Schema uuidSchema = dataFixerBuilder.addSchema(108, schemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterUUID(uuidSchema, true));

        Schema healthSchema = dataFixerBuilder.addSchema(109, schemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterHealth(healthSchema, true));

        Schema saddleSchema = dataFixerBuilder.addSchema(110, schemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterSaddle(saddleSchema, true));

        Schema hangingSchema = dataFixerBuilder.addSchema(111, schemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterHanging(hangingSchema, true));

        Schema dropChanceSchema = dataFixerBuilder.addSchema(113, schemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterDropChances(dropChanceSchema, true));

        Schema ridingSchema = dataFixerBuilder.addSchema(135, DataConverterSchemaV135::new);
        dataFixerBuilder.addFixer(new DataConverterRiding(ridingSchema, true));

        Schema tippedArrowSchema = dataFixerBuilder.addSchema(143, DataConverterSchemaV143::new);
        dataFixerBuilder.addFixer(new DataConverterEntityTippedArrow(tippedArrowSchema, true));

        Schema armourStandSchema = dataFixerBuilder.addSchema(147, schemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterArmorStand(armourStandSchema, true));

        Schema bookSchema = dataFixerBuilder.addSchema(165, schemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterBook(bookSchema, true));

        Schema addChoicesSchemaV1_10 = dataFixerBuilder.addSchema(501, DataConverterSchemaV501::new);
        dataFixerBuilder.addFixer(new DataConverterAddChoices(addChoicesSchemaV1_10, "Add 1.10 entities fix", DataConverterTypes.ENTITY));

        Schema zombieFishSchema = dataFixerBuilder.addSchema(502, schemaBiFunction);
        dataFixerBuilder.addFixer(DataConverterItemName.a(zombieFishSchema, "cooked_fished item renamer", s -> Objects.equals(DataConverterSchemaNamed.a(s), "minecraft:cooked_fished") ? "minecraft:cooked_fish" : s));
        dataFixerBuilder.addFixer(new DataConverterZombie(zombieFishSchema, false));

        Schema vboSchema = dataFixerBuilder.addSchema(505, schemaBiFunction);
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

        Schema bannerSchema = dataFixerBuilder.addSchema(804, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterBanner(bannerSchema, true));

        Schema potionWaterSchema = dataFixerBuilder.addSchema(806, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterPotionWater(potionWaterSchema, false));

        Schema addChoicesSchemaShulkerBox = dataFixerBuilder.addSchema(808, DataConverterSchemaV808::new);
        dataFixerBuilder.addFixer(new DataConverterAddChoices(addChoicesSchemaShulkerBox, "added shulker box", DataConverterTypes.j));

        Schema shulkerSchema = dataFixerBuilder.addSchema(813, 1, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterShulker(shulkerSchema, false));

        Schema shulkerBoxSchema = dataFixerBuilder.addSchema(813, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterShulkerBoxItem(shulkerBoxSchema, false));
        dataFixerBuilder.addFixer(new DataConverterShulkerBoxBlock(shulkerBoxSchema, false));

        Schema langSchema = dataFixerBuilder.addSchema(816, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterLang(langSchema, false));

        Schema totemItemSchema = dataFixerBuilder.addSchema(820, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(DataConverterItemName.a(totemItemSchema, "totem item renamer", s -> Objects.equals(s, "minecraft:totem") ? "minecraft:totem_of_undying" : s));

        Schema shoulderEntitySchema = dataFixerBuilder.addSchema(1022, DataConverterSchemaV1022::new);
        dataFixerBuilder.addFixer(new DataConverterShoulderEntity(shoulderEntitySchema, "added shoulder entities to players", DataConverterTypes.PLAYER));

        Schema bedSchema = dataFixerBuilder.addSchema(1125, DataConverterSchemaV1125::new);
        dataFixerBuilder.addFixer(new DataConverterBedBlock(bedSchema, true));
        dataFixerBuilder.addFixer(new DataConverterBedItem(bedSchema, false));

        Schema keybindSchema = dataFixerBuilder.addSchema(1344, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterKeybind(keybindSchema, false));

        Schema keybind2Schema = dataFixerBuilder.addSchema(1446, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterKeybind2(keybind2Schema, false));

        Schema flattenStateSchema = dataFixerBuilder.addSchema(1450, namedSchemaBiFunction);
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

        Schema villagerTradeSchema = dataFixerBuilder.addSchema(1451, 7, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterVillagerTrade(villagerTradeSchema, false));

        Schema itemFrameSchema = dataFixerBuilder.addSchema(1456, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterItemFrame(itemFrameSchema, false));

        Schema customNameSchema = dataFixerBuilder.addSchema(1458, namedSchemaBiFunction);
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

        Schema shulkerColorSchema = dataFixerBuilder.addSchema(1474, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterColorlessShulkerEntity(shulkerColorSchema, false));
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(shulkerColorSchema, "Colorless shulker block fixer", s -> Objects.equals(DataConverterSchemaNamed.a(s), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : s));
        dataFixerBuilder.addFixer(DataConverterItemName.a(shulkerColorSchema, "Colorless shulker item fixer", s -> Objects.equals(DataConverterSchemaNamed.a(s), "minecraft:purple_shulker_box") ? "minecraft:shulker_box" : s));

        Schema flowingSchema = dataFixerBuilder.addSchema(1475, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(flowingSchema, "Flowing fixer", s -> ImmutableMap.of("minecraft:flowing_water", "minecraft:water", "minecraft:flowing_lava", "minecraft:lava").getOrDefault(s, s)));

        Schema coralBlocksSchema = dataFixerBuilder.addSchema(1480, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(coralBlocksSchema, "Rename coral blocks", s -> DataConverterCoral.a.getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(DataConverterItemName.a(coralBlocksSchema, "Rename coral items", s -> DataConverterCoral.a.getOrDefault(s, (String) s)));

        Schema conduitSchema = dataFixerBuilder.addSchema(1481, DataConverterSchemaV1481::new);
        dataFixerBuilder.addFixer(new DataConverterAddChoices(conduitSchema, "Add conduit", DataConverterTypes.j));

        Schema pufferFishSchema = dataFixerBuilder.addSchema(1483, DataConverterSchemaV1483::new);
        dataFixerBuilder.addFixer(new DataConverterEntityPufferfish(pufferFishSchema, true));
        dataFixerBuilder.addFixer(DataConverterItemName.a(pufferFishSchema, "Rename pufferfish egg item", s -> DataConverterEntityPufferfish.a.getOrDefault(s, (String) s)));

        Schema seagrassSchema = dataFixerBuilder.addSchema(1484, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(DataConverterItemName.a(seagrassSchema, "Rename seagrass items", s -> ImmutableMap.of("minecraft:sea_grass", "minecraft:seagrass", "minecraft:tall_sea_grass", "minecraft:tall_seagrass").getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(seagrassSchema, "Rename seagrass blocks", s -> ImmutableMap.of("minecraft:sea_grass", "minecraft:seagrass", "minecraft:tall_sea_grass", "minecraft:tall_seagrass").getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(new DataConverterHeightmapRenaming(seagrassSchema, false));

        Schema codSalmonSchema = dataFixerBuilder.addSchema(1486, DataConverterSchemaV1486::new);
        dataFixerBuilder.addFixer(new DataConverterEntityCodSalmon(codSalmonSchema, true));
        dataFixerBuilder.addFixer(DataConverterItemName.a(codSalmonSchema, "Rename cod/salmon egg items", s -> DataConverterEntityCodSalmon.b.getOrDefault(s, (String) s)));

        Schema prismarineSchema = dataFixerBuilder.addSchema(1487, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(DataConverterItemName.a(prismarineSchema, "Rename prismarine_brick(s)_* blocks", s -> ImmutableMap.of("minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab", "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs").getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(prismarineSchema, "Rename prismarine_brick(s)_* items", s -> ImmutableMap.of("minecraft:prismarine_bricks_slab", "minecraft:prismarine_brick_slab", "minecraft:prismarine_bricks_stairs", "minecraft:prismarine_brick_stairs").getOrDefault(s, (String) s)));

        Schema kelpSchema = dataFixerBuilder.addSchema(1488, namedSchemaBiFunction);
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

        Schema melonSchema = dataFixerBuilder.addSchema(1490, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(melonSchema, "Rename melon_block", s -> Objects.equals(s, "minecraft:melon_block") ? "minecraft:melon" : s));
        dataFixerBuilder.addFixer(DataConverterItemName.a(melonSchema, "Rename melon_block/melon/speckled_melon", s -> ImmutableMap.of("minecraft:melon_block", "minecraft:melon", "minecraft:melon", "minecraft:melon_slice", "minecraft:speckled_melon", "minecraft:glistering_melon_slice").getOrDefault(s, (String) s)));

        Schema templateRenameSchema = dataFixerBuilder.addSchema(1492, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterChunkStructuresTemplateRename(templateRenameSchema, false));

        Schema itemEnchant = dataFixerBuilder.addSchema(1494, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterItemStackEnchantment(itemEnchant, false));

        Schema leavesSchema = dataFixerBuilder.addSchema(1496, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterLeaves(leavesSchema, false));

        Schema entityPackedSchema = dataFixerBuilder.addSchema(1500, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterBlockEntityKeepPacked(entityPackedSchema, false));

        Schema advancementSchema = dataFixerBuilder.addSchema(1501, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterAdvancement(advancementSchema, false));

        Schema recipesSchema = dataFixerBuilder.addSchema(1502, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterRecipes(recipesSchema, false));

        Schema levelSchema = dataFixerBuilder.addSchema(1506, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterLevelDataGeneratorOptions(levelSchema, false));

        Schema biomeSchema = dataFixerBuilder.addSchema(1508, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterBiome(biomeSchema, false));

        Schema V1510Schema = dataFixerBuilder.addSchema(1510, DataConverterSchemaV1510::new);

        //yes mojang has the typo too lol
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(V1510Schema, "Block renamening fix", s -> DataConverterEntityRename.b.getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(DataConverterItemName.a(V1510Schema, "Item renamening fix", s -> DataConverterEntityRename.c.getOrDefault(s, (String) s)));
        dataFixerBuilder.addFixer(new DataConverterRecipeRename(V1510Schema, false));
        dataFixerBuilder.addFixer(new DataConverterEntityRename(V1510Schema, true));
        dataFixerBuilder.addFixer(new DataConverterSwimStats(V1510Schema, false));

        Schema displayNameSchema = dataFixerBuilder.addSchema(1514, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterObjectiveDisplayName(displayNameSchema, false));
        dataFixerBuilder.addFixer(new DataConverterTeamDisplayName(displayNameSchema, false));
        dataFixerBuilder.addFixer(new DataConverterObjectiveRenderType(displayNameSchema, false));

        Schema coralFanBlockSchema = dataFixerBuilder.addSchema(1515, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(DataConverterBlockRename.a(coralFanBlockSchema, "Rename coral fan blocks", s -> DataConverterCoralFan.a.getOrDefault(s, (String) s)));

        Schema trappedChestSchema = dataFixerBuilder.addSchema(1624, namedSchemaBiFunction);
        dataFixerBuilder.addFixer(new DataConverterTrappedChest(trappedChestSchema, false));
    }
}
