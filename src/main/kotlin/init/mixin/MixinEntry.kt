package rhynia.nyx.init.mixin

import gregtech.api.enums.Mods
import gregtech.api.enums.Mods.AppliedEnergistics2
import gregtech.api.enums.Mods.BartWorks
import rhynia.nyx.Log

/** Store all late mixins, will be loaded by MixinManager */
@Suppress("UNUSED", "SpellCheckingInspection")
enum class MixinEntry(
    actions: MixinBuilder.() -> Unit,
) {
    PortableCell({
        ofClasses("ae.MixinPortableCell")
        toMod(AppliedEnergistics2)
    }),
    QuantumCell({
        ofClasses("ae.MixinQuantumCell")
        toMod(AppliedEnergistics2)
    }),

    MegaMultiBase({
        ofClasses("bw.MixinMegaMultiBase")
        toMod(BartWorks)
    }),

    // spotless:on
    ;

    private val builder = MixinBuilder().apply(actions)

    val modId: String =
        builder.targetModId ?: throw IllegalStateException("Target mod ID not set for $name")
    val mixinClasses: List<String> = builder.classes
    val condition: (() -> Boolean) = builder.condition ?: { true }

    companion object {
        fun findLateMixins(loadedMods: Set<String>): List<String> {
            val mixinsToLoad = mutableListOf<String>()
            val mininsNotLoad = mutableListOf<String>()
            MixinEntry.entries.forEach {
                if (it.modId in loadedMods && (it.condition())) {
                    mixinsToLoad.addAll(it.mixinClasses)
                } else {
                    mininsNotLoad.addAll(it.mixinClasses)
                }
            }
            Log.info("Mixins to load: $mixinsToLoad")
            if (mininsNotLoad.isNotEmpty()) Log.info("Mixins not load: $mininsNotLoad")
            return mixinsToLoad
        }
    }

    private class MixinBuilder {
        val classes = mutableListOf<String>()
        var targetModId: String? = null
        var condition: (() -> Boolean)? = null

        /**
         * Add mixin classes to the builder
         *
         * Note that the class name is under the package `rhynia.nyx.mixins`, the prefix is NOT needed
         */
        fun ofClasses(vararg mixinClasses: String): MixinBuilder =
            apply {
                classes.addAll(mixinClasses)
            }

        /** Convert Mods to mod ID */
        fun toMod(targetMod: Mods) = apply { targetModId = targetMod.ID }

        /** Extra condition checked when loading the mixin */
        fun applyIf(ai: () -> Boolean) = apply { condition = ai }
    }
}
