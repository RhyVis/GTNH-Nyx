package rhynia.nyx.api.util

import net.minecraft.nbt.NBTTagCompound

/** Mode Container for switching between modes. */
open class ModeContainer(
    val size: Int,
) {
    /** Current index of the mode. */
    var cursor = 0
        protected set

    /** Get the next index, return to 0 if reached the end. */
    fun next(): Int {
        cursor = (cursor + 1) % size
        return cursor
    }

    /** Get the previous index, return to the end if reached 0. */
    fun prev(): Int {
        cursor = (cursor - 1 + size) % size
        return cursor
    }

    /** Save the mode index to NBT. */
    fun saveNBTData(
        aNBT: NBTTagCompound,
        key: String,
    ) {
        aNBT.setInteger(key, cursor)
    }

    /** Load the mode index from NBT. */
    fun loadNBTData(
        aNBT: NBTTagCompound?,
        key: String,
    ) {
        cursor = aNBT?.getInteger(key) ?: 0
    }
}

/**
 * ref Container for switching between refs, with additional type support.
 *
 * @param T Type of the ref.
 */
open class RefContainer<T>(
    private val refs: List<T>,
) : ModeContainer(refs.size) {
    companion object {
        /** Create a RefContainer with vararg refs. */
        inline fun <reified T> of(vararg modes: T): RefContainer<T> = RefContainer(modes.toList())
    }

    /** Current mode. */
    val current: T
        get() = refs[cursor]

    /** Get all the refs */
    val all: Collection<T>
        get() = refs

    /** Get the next ref, return to the first ref if reached the end. */
    fun nextRef(): T {
        next()
        return refs[cursor]
    }

    /** Get the previous ref, return to the last ref if reached the first. */
    fun prevRef(): T {
        prev()
        return refs[cursor]
    }
}
