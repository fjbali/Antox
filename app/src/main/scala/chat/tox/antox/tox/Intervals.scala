package chat.tox.antox.tox

/**
  * Trait for Tox interval times
  */
trait Intervals {
  /**
    * Returns how many milliseconds should be used as an interval between tox iterations
    */
  def interval: Int
}

/**
  * An enumeration to store the different possible levels the app can be 'working' at.
  * Enumeration makes it extensible
  */
// scalastyle:off
object IntervalLevels extends Enumeration {
  type IntervalLevels = Value
  //
  // try [1 secs / 2 secs]
  val WORKING = Value(1000) // Orig: Value(50) // only in filetransfers it seems
  val AWAKE = Value(2000) // Orig: Value(1000) // everywhere else
  //
}
