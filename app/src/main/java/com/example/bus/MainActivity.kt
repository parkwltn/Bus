package com.example.bus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.*
import java.lang.Exception
import javax.xml.parsers.DocumentBuilderFactory

var text=""

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)

        textView.text = ""

        val key = "serviceKey=49MtiWZphKESstysEbkcqhO%2Fiwj167tnKLNaWHXzucroDPqMSVAdpal4ncuZkDT7YF9qAtA3kLw3T6VmgX%2F6HA%3D%3D"
        val stationId = "&stationId=200000078"
        val url = "http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList?"+key+stationId

        button.setOnClickListener {

            val thread = Thread(NetworkThread(url))
            thread.start()
            thread.join()

            textView.text = text
        }
    }
}

class NetworkThread(
    val url: String): Runnable {

        override fun run() {

            try {
                val xml: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)
                xml.documentElement.normalize()

                val list: NodeList = xml.getElementsByTagName("item")

                for(i in 0..list.length-1) {
                    val n: Node = list.item(i)
                    if(n.nodeType == Node.ELEMENT_NODE) {
                        val element = n as Element
                        val map = mutableMapOf<String, String>()

                        for(j in 0..element.attributes.length-1) {
                            map.putIfAbsent(
                                element.attributes.item(j).nodeName,
                                element.attributes.item(j).nodeValue
                            )
                        }

                        var routeId = element.getElementsByTagName("routeId").item(0).textContent
                        var predictTime1 = element.getElementsByTagName("predictTime1").item(0).textContent
                        var predictTime2 = element.getElementsByTagName("predictTime2").item(0).textContent

                        println("1. 노선Id: $routeId\n 2. 도착예정시간: $predictTime1\n 3. 도착예정시간: $predictTime2\n")
                        text = "1. 노선Id: $routeId\n 2. 도착예정시간: $predictTime1\n 3. 도착예정시간: $predictTime2\n"
                        println("hi~")
                        text = "안뇽"
                    }
                }
            } catch (e: Exception) {
                Log.d("TTT", "openAPI"+e.toString())
            }
        }
    }