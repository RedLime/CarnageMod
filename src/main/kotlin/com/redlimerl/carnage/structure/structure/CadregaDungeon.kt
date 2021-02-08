package com.redlimerl.carnage.structure.structure

import com.redlimerl.carnage.CarnageMod
import com.redlimerl.carnage.structure.CarnageStructures
import net.minecraft.nbt.CompoundTag
import net.minecraft.structure.*
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor
import net.minecraft.util.BlockMirror
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkSectionPos
import net.minecraft.util.registry.DynamicRegistryManager
import net.minecraft.world.HeightLimitView
import net.minecraft.world.Heightmap
import net.minecraft.world.ServerWorldAccess
import net.minecraft.world.WorldView
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.SpawnSettings
import net.minecraft.world.chunk.ChunkStatus
import net.minecraft.world.gen.ChunkRandom
import net.minecraft.world.gen.StructureAccessor
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.chunk.StructureConfig
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.StructureFeature.StructureStartFactory
import java.util.*


class CadregaDungeon : StructureFeature<DefaultFeatureConfig>(DefaultFeatureConfig.CODEC) {

    override fun getStructureStartFactory(): StructureStartFactory<DefaultFeatureConfig?>? {
        return StructureStartFactory {
            feature: StructureFeature<DefaultFeatureConfig?>?, chunkX: Int, chunkZ: Int, box: BlockBox?, references: Int, seed: Long ->
            Start(feature, chunkX, chunkZ, box, references, seed)
        }
    }

    override fun getCreatureSpawns(): MutableList<SpawnSettings.SpawnEntry> {
        return mutableListOf()
    }

    override fun getMonsterSpawns(): MutableList<SpawnSettings.SpawnEntry> {
        return mutableListOf()
    }

    override fun locateStructure(world: WorldView?, structureAccessor: StructureAccessor?, searchStartPos: BlockPos?, searchRadius: Int, skipExistingChunks: Boolean, worldSeed: Long, config: StructureConfig?): BlockPos? {
        val i = config!!.spacing
        val j = ChunkSectionPos.getSectionCoord(searchStartPos!!.x)
        val k = ChunkSectionPos.getSectionCoord(searchStartPos.z)
        var l = 0

        val chunkRandom = ChunkRandom()
        while (l <= searchRadius) {
            for (m in -l..l) {
                val bl = m == -l || m == l
                for (n in -l..l) {
                    val bl2 = n == -l || n == l
                    if (bl || bl2) {
                        val o = j + i * m
                        val p = k + i * n
                        val chunkPos = getStartChunk(config, worldSeed, chunkRandom, o, p)
                        val bl3 = world!!.biomeAccess.method_31608(chunkPos).generationSettings.hasStructureFeature(this)
                        if (bl3) {
                            val chunk = world.getChunk(chunkPos.x, chunkPos.z, ChunkStatus.STRUCTURE_STARTS)
                            val structureStart = structureAccessor!!.getStructureStart(ChunkSectionPos.from(chunk.pos, 0), this, chunk)
                            if (structureStart != null && structureStart.hasChildren()) {
                                return if (skipExistingChunks) {
                                    structureStart.incrementReferences()
                                    structureStart.pos
                                } else {
                                    structureStart.pos
                                }
                            }
                        }
                        if (l == 0) {
                            break
                        }
                    }
                }
                if (l == 0) {
                    break
                }
            }
            ++l
        }


        return null
    }

    class Start(feature: StructureFeature<DefaultFeatureConfig?>?, chunkX: Int, chunkZ: Int, box: BlockBox?, references: Int,
                seed: Long) : StructureStart<DefaultFeatureConfig?>(feature, chunkX, chunkZ, box, references, seed) {

        override fun init(registryManager: DynamicRegistryManager?, chunkGenerator: ChunkGenerator, manager: StructureManager, chunkX: Int, chunkZ: Int, biome: Biome?, config: DefaultFeatureConfig?, heightLimitView: HeightLimitView) {
            val x = chunkX * 16
            val z = chunkZ * 16
            val y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, heightLimitView)
            val pos = BlockPos(x, y, z)
            val rotation = BlockRotation.random(random)
            CadregaDungeonGenerator.addPieces(manager, pos, rotation, children)
            setBoundingBoxFromChildren()
        }
    }
}

object CadregaDungeonGenerator {
    private val FIRST_FLOOR: Identifier = CarnageMod.identifier("cadrega_dungeon/first_floor")
    private val CARCANO_FLOOR: Identifier = CarnageMod.identifier("cadrega_dungeon/carcano_floor")
    private val COGNITION_FLOOR: Identifier = CarnageMod.identifier("cadrega_dungeon/cognition_floor")
    private val LAST_FLOOR: Identifier = CarnageMod.identifier("cadrega_dungeon/last_floor")

    fun addPieces(manager: StructureManager, pos: BlockPos, rotation: BlockRotation, pieces: MutableList<StructurePiece>) {
        if (pos.y > 15) {
            pieces.add(Piece(manager, pos, FIRST_FLOOR, rotation))
            pieces.add(Piece(manager, pos.add(0,5,0), CARCANO_FLOOR, rotation))
            pieces.add(Piece(manager, pos.add(0,10,0), COGNITION_FLOOR, rotation))
            pieces.add(Piece(manager, pos.add(0,15,0), LAST_FLOOR, rotation))
        }
    }

    class Piece : SimpleStructurePiece {
        private val blockRotation: BlockRotation
        private val template: Identifier

        constructor(structureManager: StructureManager, compoundTag: CompoundTag) : super(CarnageStructures.CADREGA_DUNGEON_PIECE, compoundTag) {
            template = Identifier(compoundTag.getString("Template"))
            this.blockRotation = BlockRotation.valueOf(compoundTag.getString("Rot"))
            initializeStructureData(structureManager)
        }

        constructor(structureManager: StructureManager, pos: BlockPos?, template: Identifier, rotation: BlockRotation) : super(CarnageStructures.CADREGA_DUNGEON_PIECE, 0) {
            this.pos = pos
            this.blockRotation = rotation
            this.template = template
            initializeStructureData(structureManager)
        }

        private fun initializeStructureData(structureManager: StructureManager) {
            val structure: Structure = structureManager.getStructureOrBlank(template)
            val placementData = StructurePlacementData()
                    .setRotation(this.blockRotation)
                    .setMirror(BlockMirror.NONE)
                    .addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS)
            setStructureData(structure, pos, placementData)
        }

        override fun toNbt(tag: CompoundTag) {
            super.toNbt(tag)
            tag.putString("Template", template.toString())
            tag.putString("Rot", this.blockRotation.name)
        }

        override fun handleMetadata(metadata: String?, pos: BlockPos?, serverWorldAccess: ServerWorldAccess?, random: Random?,
                                    boundingBox: BlockBox?) {
        }
    }
}