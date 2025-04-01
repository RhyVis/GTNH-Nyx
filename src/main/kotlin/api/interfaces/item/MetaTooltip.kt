package rhynia.nyx.api.interfaces.item

/**
 * Specify the item that uses meta value to represent different tooltips. This is used to provide
 * tooltips information on registration.
 */
interface MetaTooltip {
    /**
     * Set or clear the tooltips of given meta.
     *
     * @param meta the meta value
     * @param tooltips the tooltips to set, `null` to clear.
     */
    fun setTooltips(
        meta: Int,
        tooltips: Array<out String>?,
    )

    /**
     * Get the tooltips of given meta.
     *
     * @param meta the meta value
     * @return the tooltips, or `null` if not set.
     */
    fun getTooltips(meta: Int): Array<out String>?
}
