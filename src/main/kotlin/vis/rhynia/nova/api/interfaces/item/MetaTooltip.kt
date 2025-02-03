package vis.rhynia.nova.api.interfaces.item

interface MetaTooltip {
  /**
   * Set or clear the tooltips of given meta.
   *
   * @param meta the meta value
   * @param tooltips the tooltips to set, `null` to clear.
   */
  fun setTooltips(meta: Int, tooltips: Array<String>?)

  /**
   * Get the tooltips of given meta.
   *
   * @param meta the meta value
   * @return the tooltips, or `null` if not set.
   */
  fun getTooltips(meta: Int): Array<String>?
}
