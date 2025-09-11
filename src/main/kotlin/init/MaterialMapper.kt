package rhynia.nyx.init

import bartworks.system.material.Werkstoff
import bartworks.system.material.WerkstoffLoader
import gregtech.api.enums.Materials
import gregtech.api.enums.OrePrefixes
import gregtech.api.util.GTOreDictUnificator
import gtPlusPlus.core.material.Material
import net.minecraft.item.ItemStack
import rhynia.nyx.ModLogger
import rhynia.nyx.api.item.MetaItemToken
import rhynia.nyx.api.item.asToken
import rhynia.nyx.api.util.copyAmountUnsafe
import rhynia.nyx.mixins.gt.AccessorGTMaterial
import kotlin.time.measureTime

/**
 * Maps materials from different mods (GT, BW, PP) into a unified structure for utility use.
 *
 * Provides methods to query materials by their ore prefixes and retrieve corresponding ItemStacks.
 */
object MaterialMapper {
    private val mapGT = AccessorGTMaterial.getMaterialMap()
    private val mapBW = Werkstoff.werkstoffNameHashMap
    private val mapPP = Material.mMaterialMap.associateBy { it.unlocalizedName }

    private val _data: MutableMap<String, MaterialData> = mutableMapOf()
    val data: Map<String, MaterialData> get() = _data

    private val _reversedIndex: MutableMap<MetaItemToken, Pair<MaterialData, OrePrefixes>> = mutableMapOf()
    val reversedIndex: Map<MetaItemToken, Pair<MaterialData, OrePrefixes>> get() = _reversedIndex

    fun init() {
        measureTime {
            mapGT.forEach {
                _data[it.key] = MaterialData.see(it.value)
            }
            mapBW.forEach {
                _data[it.key] = MaterialData.see(it.value)
            }
            mapPP.forEach {
                _data[it.key] = MaterialData.see(it.value)
            }

            buildReserveIndex()
        }.also {
            ModLogger.info("Mapped ${data.size} materials with ${reversedIndex.size} reverse entries in ${it.inWholeMilliseconds} ms")
            data.forEach { (key, value) ->
                ModLogger.debug(" - $key: ${value.name} (${value.validOrePrefixes.size} prefixes)")
            }
        }
    }

    /**
     * Looks up the material data and ore prefix for the given [ItemStack].
     */
    operator fun get(itemStack: ItemStack): Pair<MaterialData, OrePrefixes>? = reversedIndex[itemStack.asToken()]

    /**
     * Looks up the material data and ore prefix for the given [MetaItemToken].
     */
    operator fun get(token: MetaItemToken): Pair<MaterialData, OrePrefixes>? = reversedIndex[token]

    private fun buildReserveIndex() {
        data.values.forEach { materialData ->
            materialData.validOrePrefixes.forEach { prefix ->
                materialData.getByOrePrefix(prefix, 1)?.let { itemStack ->
                    val token = itemStack.asToken()
                    _reversedIndex[token] = materialData to prefix
                }
            }
        }
    }

    /**
     * Represents material data from different mods (GT, BW, PP).
     */
    interface MaterialData {
        val name: String
        val validOrePrefixes: List<OrePrefixes>

        val anyStack: ItemStack? get() =
            validOrePrefixes.firstOrNull()?.let {
                getByOrePrefix(it, 1)
            }

        /**
         * Checks if the material has the specified ore prefix.
         */
        fun hasOrePrefix(prefix: OrePrefixes): Boolean = prefix in validOrePrefixes

        /**
         * Gets an ItemStack of the material with the specified ore prefix and count.
         */
        fun getByOrePrefix(
            prefix: OrePrefixes,
            count: Int,
        ): ItemStack?

        /**
         * Gets an ItemStack of the material with the specified ore prefix and count larger than 64.
         *
         * Like [gregtech.api.util.GTUtility.copyAmountUnsafe]
         */
        fun getByOrePrefixUnsafe(
            prefix: OrePrefixes,
            count: Int,
        ): ItemStack? = getByOrePrefix(prefix, 1)?.copyAmountUnsafe(count)

        companion object {
            fun see(obj: Any): MaterialData =
                when (obj) {
                    is Materials -> ImplGT(obj)
                    is Werkstoff -> ImplBW(obj)
                    is Material -> ImplPP(obj)
                    else -> throw IllegalArgumentException("Unsupported material type: ${obj::class.java}")
                }
        }

        private class ImplGT(
            private val material: Materials,
        ) : MaterialData {
            override val name: String = material.mName
            override val validOrePrefixes: List<OrePrefixes> =
                OrePrefixes.entries.filter { prefix ->
                    GTOreDictUnificator.get(prefix, material, 1) != null
                }

            override fun getByOrePrefix(
                prefix: OrePrefixes,
                count: Int,
            ): ItemStack? =
                if (hasOrePrefix(prefix)) {
                    GTOreDictUnificator.get(prefix, material, count.toLong())
                } else {
                    null
                }
        }

        private class ImplBW(
            private val werkstoff: Werkstoff,
        ) : MaterialData {
            override val name: String = werkstoff.defaultName
            override val validOrePrefixes: List<OrePrefixes> =
                OrePrefixes.entries.filter { prefix ->
                    WerkstoffLoader.getCorrespondingItemStackUnsafe(prefix, werkstoff, 1) != null
                }

            override fun getByOrePrefix(
                prefix: OrePrefixes,
                count: Int,
            ): ItemStack? =
                if (hasOrePrefix(prefix)) {
                    werkstoff.get(prefix, count)
                } else {
                    null
                }
        }

        private class ImplPP(
            private val material: Material,
        ) : MaterialData {
            override val name: String = material.unlocalizedName
            override val validOrePrefixes: List<OrePrefixes> =
                OrePrefixes.entries.filter { prefix ->
                    material.getComponentByPrefix(prefix, 1) != null
                }

            override fun getByOrePrefix(
                prefix: OrePrefixes,
                count: Int,
            ): ItemStack? =
                if (hasOrePrefix(prefix)) {
                    material.getComponentByPrefix(prefix, count)
                } else {
                    null
                }
        }
    }
}
