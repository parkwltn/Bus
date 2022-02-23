package com.example.bus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.*
import java.lang.Exception
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var textView = findViewById<TextView>(R.id.textView)

        textView.text = ""

        button.setOnClickListener {

            val thread = NetworkThread()
            thread.start()
        }
    }

    inner class NetworkThread: Thread() {

        override fun run() {

            try {

                var site = "http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList?serviceKey=49MtiWZphKESstysEbkcqhO%2Fiwj167tnKLNaWHXzucroDPqMSVAdpal4ncuZkDT7YF9qAtA3kLw3T6VmgX%2F6HA%3D%3D&stationId=200000078"
                var url = URL(site)
                var conn = url.openConnection()
                var input = conn.getInputStream()

                var factory = DocumentBuilderFactory.newInstance()
                var builder = factory.newDocumentBuilder()

                var doc = builder.parse(input)

                var root = doc.documentElement

                var itemNodeList = root.getElementsByTagName("busArrivalList")

                for (i in 0 until itemNodeList.length) {
                    var itemElement = itemNodeList.item(i) as Element

                    var routeIdList = itemElement.getElementsByTagName("routeId")
                    var predictTime1List = itemElement.getElementsByTagName("predictTime1")
                    var predictTime2List = itemElement.getElementsByTagName("predictTime2")

                    var routeIdNode = routeIdList.item(0) as Element
                    var predictTime1Node = predictTime1List.item(0) as Element
                    var predictTime2Node = predictTime2List.item(0) as Element

                    var routeId = routeIdNode.textContent
                    var predictTime1 = predictTime1Node.textContent
                    var predictTime2 = predictTime2Node.textContent

                    runOnUiThread {
                        textView.append("노선Id: ${routeId}\n")
                        textView.append("도착예정시간1: ${predictTime1}\n")
                        textView.append("도착예정시간2: ${predictTime2}\n")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

