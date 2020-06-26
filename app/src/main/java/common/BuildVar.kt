package common

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object BuildVar {

    const val PAGE_LIMIT = 30

    fun longToTime(time: Long): String {
        return Instant.ofEpochSecond(time).atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))
    }

}