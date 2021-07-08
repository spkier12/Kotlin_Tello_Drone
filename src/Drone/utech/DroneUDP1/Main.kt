package Drone.utech.DroneUDP1

import ReciveData.Received
import java.lang.Thread
import Automation.AutoFlight
import java.lang.Exception
import java.net.*
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.jvm.JvmStatic

class Main {
    private var socket: DatagramSocket = DatagramSocket()
    private var address: InetAddress = InetAddress.getByName("192.168.10.1")
    private var port: Int = 8889
    private var attempts: Int = 0

    // Setup a Configuration to the UDP Server
    fun connect() {
        attempts = 0
        attempts++
        try {
            val b = Received(socket)
            val b0 = Thread(b)
            b0.start()
            getMSG()
        } catch (e: Exception) {
            println("\nException in Connecting: {} $attempts")
            connect()
        }
    }

    // Input commands if you write a command it will trigger a packet send
    private fun getMSG() {
        println("Emergency shutdown Key: 0 \nCommand Takeover terminal Key: 1")
        println("Takeoff Key: 2 \nLand Key: 3 \nProgram Flight Key: 4 -> Commands here")
        println("+Enter Key")
        println("Msg: ")
        val msg = Scanner(System.`in`)
        val msg1 = msg.nextLine()
        val msg2Split = msg1.split(" ".toRegex()).toTypedArray()

        when (msg2Split[0]) {
            "0" -> {
                send("emergency")
            }
            "1" -> {
                send("command")
            }
            "2" -> {
                send("takeoff")
            }
            "3" -> {
                send("land")
            }
            "4" -> {
                val a = AutoFlight(msg2Split, socket)
                val b = Thread(a)
                b.start()
                println("Started AutoFlight")
                send(msg1)
            }
            else -> send(msg1)
        }
    }

    // send a Message packet to the specified UDP Server
    private fun send(msg: String) {
        val buf: ByteArray
        try {
            buf = msg.toByteArray(StandardCharsets.UTF_8)
            val packet = DatagramPacket(buf, buf.size, address, port)
            socket.send(packet)
            getMSG()
        } catch (e: Exception) {
            getMSG()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val a = Main()
            a.connect()
        }
    }
}