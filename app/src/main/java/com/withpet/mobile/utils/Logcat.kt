package com.withpet.mobile.utils

import android.os.Environment
import android.util.Log
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class Logcat {
    companion object {
        // 로그의 태그로 사용될 상수
        private const val TAG: String = "#####[위드팻 로그캣]#####"

        // 디버그 로그 출력
        fun d(message: String) {
            Log.d(TAG, buildLogMessage(message))
        }

        // 에러 로그 출력
        fun e(message: String) {
            Log.e(TAG, buildLogMessage(message))
        }

        // 정보 로그 출력
        fun i(message: String) {
            Log.i(TAG, buildLogMessage(message))
        }

        // 경고 로그 출력
        fun w(message: String, exception: Exception? = null) {
            Log.w(TAG, buildLogMessage(message))
            exception?.let { Log.w(TAG, it) }
        }

        // 상세 로그 출력
        fun v(message: String) {
            Log.v(TAG, buildLogMessage(message))
        }

        // 로그 메시지 구성
        private fun buildLogMessage(message: String): String {
            val ste = Thread.currentThread().stackTrace[4]
            val sb = StringBuilder()

            sb.append("[")
            sb.append(ste.fileName)
            sb.append("] ")
            sb.append(ste.methodName)
            sb.append(" #")
            sb.append(ste.lineNumber)
            sb.append(": ")
            sb.append(message)

            // writeLog(sb.toString())
            return sb.toString()
        }

        // 로그를 파일로 저장
        private fun writeLog(str: String) {
            var result = 0
            val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/log/$TAG/"
            val file = File(dirPath)

            if (!file.exists())
                file.mkdirs()

            val nowDate =
                SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))

            val savefile = File("$dirPath$nowDate.txt")
            try {
                val nowTime =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date(System.currentTimeMillis()))

                val bfw = BufferedWriter(FileWriter("$dirPath$nowDate.txt", true))
                bfw.write("++ Time: $nowTime\n")
                bfw.write(str)
                bfw.write("\n")
                bfw.flush()
                bfw.close()
            } catch (e: FileNotFoundException) {
                result = 1
                e.printStackTrace()
            } catch (e: IOException) {
                result = 1
                e.printStackTrace()
            }
        }
    }
}

