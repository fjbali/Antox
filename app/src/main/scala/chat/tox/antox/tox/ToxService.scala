
package chat.tox.antox.tox

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.preference.PreferenceManager
import chat.tox.antox.av.CallService
import chat.tox.antox.callbacks.{AntoxOnSelfConnectionStatusCallback, ToxCallbackListener, ToxavCallbackListener}
import chat.tox.antox.utils.AntoxLog
import im.tox.tox4j.core.enums.ToxConnection
import im.tox.tox4j.impl.jni.ToxJniLog
import rx.lang.scala.schedulers.{AndroidMainThreadScheduler}
import rx.lang.scala.{Observable, Subscription}
import scala.concurrent.duration._

class ToxService extends Service {

  private var serviceThread: Thread = _

  private var keepRunning: Boolean = true

  private val connectionCheckInterval =  30000 // 10000 //in ms

  private val reconnectionIntervalSeconds = 120 // 60

  private var callService: CallService = _

  override def onCreate() {
    if (!ToxSingleton.isInited) {
      ToxSingleton.initTox(getApplicationContext)
      AntoxLog.debug("Initting ToxSingleton")
    }

    keepRunning = true
    val thisService = this

    val start = new Runnable() {

      override def run() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext)

        callService = new CallService(thisService)
        callService.start()

        val toxCallbackListener = new ToxCallbackListener(thisService)
        val toxAvCallbackListener = new ToxavCallbackListener(thisService)

        var reconnection: Subscription = null

        val connectionSubscription = AntoxOnSelfConnectionStatusCallback.connectionStatusSubject
          .observeOn(AndroidMainThreadScheduler())
          .distinctUntilChanged
          .subscribe(toxConnection => {
            if (toxConnection != ToxConnection.NONE) {
              if (reconnection != null && !reconnection .isUnsubscribed) {
                reconnection.unsubscribe()
              }
              AntoxLog.debug("Tox connected. Stopping reconnection")
            } else {
              reconnection = Observable
                .interval(reconnectionIntervalSeconds seconds)
                .subscribe(x => {
                    AntoxLog.debug("Reconnecting")
                    ToxSingleton.bootstrap(getApplicationContext).subscribe()
                  })
              AntoxLog.debug(s"Tox disconnected. Scheduled reconnection every $reconnectionIntervalSeconds seconds")
            }
          })

        var ticks = 0
        // val toxAv_interval_longer = ToxSingleton.toxAv.interval * 20
        val toxAv_interval_longer = 2000 // 2 secs
        val toxCoreIterationRatio = Math.ceil(ToxSingleton.tox.interval/toxAv_interval_longer).toInt
        System.out.println("ToxService:" + "ToxSingleton.tox.interval = " + ToxSingleton.tox.interval)
        System.out.println("ToxService:" + "ToxSingleton.toxAv.interval = " + ToxSingleton.toxAv.interval)
        System.out.println("ToxService:" + "toxAv_interval_longer = " + toxAv_interval_longer)
        System.out.println("ToxService:" + "toxCoreIterationRatio = " + toxCoreIterationRatio)
        System.out.println("ToxService:" + "connectionCheckInterval = " + connectionCheckInterval)

        while (keepRunning) {
          if (!ToxSingleton.isToxConnected(preferences, thisService)) {
            try {
              Thread.sleep(connectionCheckInterval)
            } catch {
              case e: Exception =>
            }
          } else {
            try {
              if(ticks % toxCoreIterationRatio == 0) {
                // System.out.println("ToxService:" + "ToxSingleton.tox.iterate")
                ToxSingleton.tox.iterate(toxCallbackListener)
              }
              // System.out.println("ToxService:" + "ToxSingleton *+ toxAv +* iterate")
              ToxSingleton.toxAv.iterate(toxAvCallbackListener)

              val time = toxAv_interval_longer
              Thread.sleep(time)
              ticks += 1
            } catch {
              case e: Exception =>
                e.printStackTrace()
            }
          }
        }

        connectionSubscription.unsubscribe()
      }
    }

    serviceThread = new Thread(start)
    serviceThread.start()
  }

  override def onBind(intent: Intent): IBinder = null

  override def onStartCommand(intent: Intent, flags: Int, id: Int): Int = Service.START_STICKY

  override def onDestroy() {
    super.onDestroy()

    System.out.println("ToxService:" + "onDestroy(): enter")

    keepRunning = false

    System.out.println("ToxService:" + "onDestroy(): serviceThread.interrupt")
    serviceThread.interrupt()
    System.out.println("ToxService:" + "onDestroy(): serviceThread waiting to end ...")
    serviceThread.join()
    System.out.println("ToxService:" + "onDestroy(): serviceThread: ended")

    callService.destroy()

    ToxSingleton.save()
    ToxSingleton.isInited = false
    ToxSingleton.tox.close()
    AntoxLog.debug("onDestroy() called for Tox service")
    System.out.println("ToxService:" + "onDestroy(): ready")
  }
}
