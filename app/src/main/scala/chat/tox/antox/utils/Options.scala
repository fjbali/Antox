package chat.tox.antox.utils

object Options {
  var ipv6Enabled: Boolean = true

  var udpEnabled: Boolean = false
  var autoAcceptFt: Boolean = false
  var videoCallStartWithNoVideo = false
  // TODO: hardcoded for now
  var batterySavingMode = true
  // TODO: hardcoded for now
  var proxyEnabled: Boolean = false
  var proxyAddress: String = "127.0.0.1"
  var proxyPort: String = "9050"
}
