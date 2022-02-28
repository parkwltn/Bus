package com.example.bus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.*
import java.lang.Exception
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var textView = findViewById<TextView>(R.id.textView)

        textView.text = ""

        //1분마다 도착정보 조회
        //timer (period = 60000) {
            val thread = NetworkThread();
            thread.start()
        //}

    }

    inner class NetworkThread: Thread() {

        override fun run() {

            try {
                var serviceKey = "serviceKey=49MtiWZphKESstysEbkcqhO%2Fiwj167tnKLNaWHXzucroDPqMSVAdpal4ncuZkDT7YF9qAtA3kLw3T6VmgX%2F6HA%3D%3D"
                var stationId = "&stationId=200000078"
                var site = "http://apis.data.go.kr/6410000/busarrivalservice/getBusArrivalList?"+serviceKey+stationId

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

                    //routeIdToName
                    when (routeId) {
                        "200000120" -> routeId = "2007"
                        "200000104" -> routeId = "3000"
                        "200000265" -> routeId = "30-1"
                        "200000010" -> routeId = "900"
                        "200000055" -> routeId = "25"
                    }

                    //버스번호 입력 받기 - HW 연동
                    var enteredBusNum = "25"

                    //if enteredBusNum in routeId
                    if (enteredBusNum in routeId) {
                        //탑승 예약 안내
                        println("$enteredBusNum 번 버스 탑승 예약이 완료되었습니다.")

                        //예약 리스트
                        var BusReservationList = mutableListOf<String>()
                        BusReservationList.add(enteredBusNum)
                        //print(BusReservationList)

                        //도착 안내 1: 등록 완료 후 바로 안내
                        when (enteredBusNum) {
                            "3000" -> println("$routeId 번 버스는 $predictTime1 분 후 도착합니다.")
                            "25" -> println("$routeId 번 버스는 $predictTime1 분 후 도착합니다.")
                        }

                        //도착 안내 2: 1, 3, 6, 9분 전 안내
                    }

                    //테스트 - 삭제
                    runOnUiThread {
                        textView.append("노선Id: ${routeId}\n")
                        textView.append("도착예정시간1: ${predictTime1}\n")
                        textView.append("도착예정시간2: ${predictTime2}\n")
                    }
                }
                textView.setText("")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

