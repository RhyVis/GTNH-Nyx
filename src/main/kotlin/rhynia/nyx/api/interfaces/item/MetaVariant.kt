package rhynia.nyx.api.interfaces.item

import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.item.ItemStack

/** Specify the item that uses meta value to represent different variants. */
interface MetaVariant {
  /**
   * Create a copy of this with given meta value.
   *
   * @param meta the meta value
   * @return the copy of this with given meta value
   * @throws IllegalArgumentException if the meta is invalid.
   */
  @Throws(IllegalArgumentException::class) fun getVariant(meta: Int): ItemStack

  /**
   * Create an array of copies of this with different meta values.
   *
   * @return the copies of this with different meta values
   */
  fun getVariants(): Array<ItemStack>

  /**
   * Register a variant of this with given meta value, and return the instance of registered variant
   * item.
   *
   * @param meta the meta value
   * @return the instance of the registered variant item
   * @throws IllegalArgumentException if the meta value is taken.
   */
  @Throws(IllegalArgumentException::class) fun registerVariant(meta: Int): ItemStack

  /**
   * Create a copy of allowed meta IDs.
   *
   * @return a copy of allowed meta IDs.
   */
  fun getVariantIds(): Set<Int>

  /**
   * Check if the meta is a valid variant.
   *
   * @param meta the meta value
   * @return `true` if valid
   */
  fun isValidVariant(meta: Int): Boolean = getVariantIds().contains(meta)

  /**
   * Register the icons of all variants.
   *
   * @param register the icon register
   * @param iconFunc the function to get the icon name of given meta
   */
  fun MetaVariant.registerVariantIcon(register: IIconRegister, iconFunc: (Int) -> String) =
      this.getVariantIds().associate { it to register.registerIcon(iconFunc(it)) }
}
