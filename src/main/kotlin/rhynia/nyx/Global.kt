package rhynia.nyx

import org.apache.logging.log4j.Logger

internal val Any.Log: Logger
  get() = Nyx.ModLogger

internal const val MOD_ID = "nyx"
internal const val MOD_NAME = "Nyx"
