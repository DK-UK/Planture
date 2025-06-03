package com.dkdevs.testing2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlantEntity::class], version = 1, exportSchema = false)
abstract class PlantDatabase : RoomDatabase() {

    abstract fun plantDao() : PlantDao

    companion object {
        private var INSTANCE : PlantDatabase? = null

        fun getInstance(context : Context) : PlantDatabase {
            if (INSTANCE == null){

                INSTANCE = Room.databaseBuilder(
                    context,
                    PlantDatabase::class.java,
                    "plant.db"
                ).build()
            }
            return INSTANCE!!
        }
    }
}