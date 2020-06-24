package common.data.local

import java.time.ZonedDateTime


data class DiaryItem(val id: Int, val title: String, val contents: String, val dateTime: ZonedDateTime)