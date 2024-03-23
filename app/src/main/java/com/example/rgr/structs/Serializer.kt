package com.example.rgr.structs

import android.content.Context
import java.io.*


object Serializer {

    @Throws(IOException::class)
    fun write(context: Context, listOfLists: ListOfLists<*>?, fileName: String) {

        val fileOutputStream: FileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        listOfLists.let { // todo what is it ??
            ObjectOutputStream(fileOutputStream).use { oos ->
                oos.writeObject(listOfLists)
            }
        }
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun read(context: Context, fileName: String): ListOfLists<*> {
        val fileInputStream: FileInputStream = context.openFileInput(fileName)
        return ObjectInputStream(fileInputStream).use { ois ->
            ois.readObject() as ListOfLists<*>
        }
    }
}