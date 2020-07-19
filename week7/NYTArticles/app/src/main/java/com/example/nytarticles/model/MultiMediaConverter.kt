package com.example.nytarticles.model

import androidx.room.TypeConverter


class MultiMediaConverter {
    @TypeConverter
    fun multiMediaListToString(multimediaList: List<Multimedia>?): String? =
        multimediaList?.joinToString(separator = ",") { it.url }

    @TypeConverter
    fun stringToMultiMediaList(multimediaListString: String): List<Multimedia>? {
        val array =
            multimediaListString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val list = mutableListOf<Multimedia>()
        array.forEach {
            list.add(Multimedia(url = it))
        }
        return list
    }

}

