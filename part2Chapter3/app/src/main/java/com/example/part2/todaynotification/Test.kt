package com.example.part2.todaynotification

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket

// JVM Java Build
fun main(){
    Thread {
        val port = 8080
        val server = ServerSocket(port)
        while(true){
            // 서버는 계속 리스닝 작업을 함  (블록 상태)
            val socket = server.accept() // 소켓이 서버로부터 받음
            // Client -> Server 요청 (클, 서 -> 소켓 각각 생김) EndPoint

            // 데이터 받을라면 스트리밍 생성
            // socket.getInputStream()  // 일방통행 > 들어오는 스트림 : 클라이언트 -> 서버 (Client의 OutputStream)  == socket 입장 아웃풋 스트림
            // socket.getOutputStream() // 일방통행 > 나가는 스트림  : 서버 -> 클라이언트 (Client에게 데이터를 주는 스트림) == socket 입장 인풋 스트림

            // client -> data -> buffer 넣기
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            // 서버 입장에서 이거 받으면 데이터 응답 내려줘야함
            val printer = PrintWriter(socket.getOutputStream()) // 오케이 받은거 프린트해서 보내줄게!

            // reader를 통해 데이터 읽기
            var input: String = "-1"
            // input = reader.readLine() // 한 줄씩 데이터 읽기 -> 데이터가 다 들어올 때 까지 읽어야하므로
            while(input.isNotEmpty()){
                input = reader.readLine()
            }
            println("READ DATA  $input")
            // Log.e("SERVER", "READ DATA $input")

            // http 통신 규격 사용 -> 1.1 HTTP 규격 사용 + 200 상태코드 사용 응답: OK
            // Header
            printer.println("HTTP/1.1 200 OK")
            printer.println("Content-Type: text/html\r\n")

            // Body
            printer.println("{\"message\" : \"Hello World!!!\"}")
            printer.println("\r\n")
            printer.flush() // 잔여 데이터 배출하기
            // 서버로부터 클라이언트에게 데이터를 쭉 다 보내주고 flush 처리 까지 하기

            printer.close() // outputStream 끊기

            // Client 측에서도 보내는거 그만!
            reader.close() // 읽기도 닫아주기

            // EndPoint 끝내기
            socket.close() // 소켓도 끊기
        }

    }.start()
}