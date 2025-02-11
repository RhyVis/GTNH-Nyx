package rhynia.nyx.api.process

enum class OverclockType(val timeDec: Double, val powerInc: Double) {
  Never(1.0, 1.0),
  Normal(2.0, 4.0),
  PerfectHalfVol(2.0, 2.0),
  Perfect(4.0, 4.0);

  val efficiency: Double = timeDec / powerInc
  val isEfficient: Boolean = timeDec >= powerInc
}
