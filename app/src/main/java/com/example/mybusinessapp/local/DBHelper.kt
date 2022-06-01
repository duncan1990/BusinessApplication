package com.example.mybusinessapp.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.mybusinessapp.model.Customer

class DBHelper(val context: Context) : SQLiteOpenHelper(context, DBHelper.DATABASE_NAME, null, DBHelper.DATABASE_VERSION) {
    private val TABLE_NAME= "Customer"
    private val COL_ID = "id"
    private val COL_CLIENT_NAME = "clientName"
    private val COL_COUNTRY = "country"
    private val COL_TOTAL = "total"
    companion object {
        private val DATABASE_NAME = "MY_DB"//database adı
        private val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_CLIENT_NAME  VARCHAR(50),$COL_COUNTRY  VARCHAR(50),$COL_TOTAL  DOUBLE)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertData(customer: Customer){
        val sqliteDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_CLIENT_NAME , customer.clientName)
        contentValues.put(COL_COUNTRY, customer.country)
        contentValues.put(COL_TOTAL, customer.total)

        val result = sqliteDB.insert(TABLE_NAME,null,contentValues)

        Toast.makeText(context, if(result != -1L) "Kayıt Başarılı" else "Kayıt yapılamadı.", Toast.LENGTH_SHORT).show()
    }

    fun readData():MutableList<Customer>{
        val customerList = mutableListOf<Customer>()
        val sqliteDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = sqliteDB.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                val customer = Customer()
                customer.id = result.getString(result.getColumnIndexOrThrow(COL_ID)).toInt()
                customer.clientName = result.getString(result.getColumnIndexOrThrow(COL_CLIENT_NAME))
                customer.country = result.getString(result.getColumnIndexOrThrow(COL_COUNTRY))
                customer.total = result.getString(result.getColumnIndexOrThrow(COL_TOTAL)).toDouble()
                customerList.add(customer)
            }while (result.moveToNext())
        }
        result.close()
        sqliteDB.close()
        return customerList
    }

    fun deleteAllData(){
        val sqliteDB = this.writableDatabase
        sqliteDB.delete(TABLE_NAME,null,null)
        sqliteDB.close()

    }

    fun updateTotal(total:Double) {
        val db = this.writableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                val cv = ContentValues()
                cv.put(COL_TOTAL,(result.getInt(result.getColumnIndexOrThrow(COL_TOTAL))+total))
                db.update(TABLE_NAME,cv, "$COL_ID=? AND $COL_CLIENT_NAME=?",
                        arrayOf(result.getString(result.getColumnIndexOrThrow(COL_ID)),
                                result.getString(result.getColumnIndexOrThrow(COL_CLIENT_NAME))))
                                //Toast.makeText(context,if(result != -1L) "Kayıt Başarılı" else "Kayıt yapılamadı.", Toast.LENGTH_SHORT).show()
            }while (result.moveToNext())
        }

        result.close()
        db.close()
    }

}