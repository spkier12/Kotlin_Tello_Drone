package Automation

import java.net.DatagramSocket
import java.lang.Runnable
import java.net.DatagramPacket
import java.net.InetAddress
import java.io.IOException
import java.lang.InterruptedException
import java.nio.charset.StandardCharsets

class AutoFlight(private val Directions: Array<String>, private val Socket: DatagramSocket) : Runnable {
    var Tries = 0
    override fun run() {
        for (direction in Directions) {
            try {
                var buffer: ByteArray
                if (direction.contains("wait")) {
                    Thread.sleep(2000)
                } else if (direction.contains("repeat")) {
                    Tries++
                    if (Tries < 10) {
                        run()
                    }
                } else {
                    buffer = direction.toByteArray(StandardCharsets.UTF_8)
                    val Packet = DatagramPacket(buffer, buffer.size, InetAddress.getByName("192.168.10.1"), 8889)
                    Socket.send(Packet)
                }
            } catch (e: IOException) {
                println(e.cause)
            } catch (e: InterruptedException) {
                println(e.cause)
            }
        }
    }
}