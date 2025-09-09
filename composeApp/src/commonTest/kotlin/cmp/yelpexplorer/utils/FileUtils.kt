package cmp.yelpexplorer.utils

import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

class FileUtils {
    companion object {
        fun getStringFromPath(filePath: String): String {
            return FileSystem.SYSTEM.read("src/commonTest/resources/$filePath".toPath()) {
                readUtf8()
            }
        }

        inline fun <reified T> getDataFromPath(filePath: String): T {
            val json = Json {
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            }
            return json.decodeFromString(getStringFromPath(filePath))
        }
    }
}


//@OptIn(ExperimentalSerializationApi::class)
//class FileUtils {
//    companion object {
//        private fun getStringFromFile(file: File): String {
//            file.source().use { source ->
//                source.buffer().use { bufferedSource ->
//                    return bufferedSource.readUtf8()
//                }
//            }
//        }
//
//        fun getStringFromPath(filePath: String): String {
//            val file = File(FileUtils::class.java.classLoader!!.getResource(filePath).path)
//            return getStringFromFile(file)
//        }
//
//        inline fun <reified T> getDataFromPath(filePath: String): T {
//            val json = Json {
//                ignoreUnknownKeys = true
//                explicitNulls = false
//            }
//            return json.decodeFromString(getStringFromPath(filePath))
//        }
//    }
//}