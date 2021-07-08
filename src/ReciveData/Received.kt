package ReciveData

import java.net.DatagramSocket
import java.lang.Runnable
import java.net.DatagramPacket
import java.lang.Exception

class Received(private val `var`: DatagramSocket) : Runnable {
    override fun run() {
        while (true) try {
            val buf = ByteArray(1024)
            val packet = DatagramPacket(buf, buf.size)
            `var`.receive(packet)
            val data = String(
                packet.data, 0, packet.length
            )
            if (data.isNotEmpty()) {
                println(data)
            }
        } catch (e: Exception) {
            println("Error in getting data ${e.cause}")
        }
    }
}