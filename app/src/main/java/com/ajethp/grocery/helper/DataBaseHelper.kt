package com.ajethp.grocery.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ajethp.grocery.classes.Food
import com.ajethp.grocery.classes.User

val DATABASE_NAME = "USER DATABASE"

// Table names
const val TABLE_USER = "Users"
const val TABLE_INVENTORY = "Inventory"
const val TABLE_PURCHASED = "Purchased"
const val TABLE_SHOPPING = "Shopping"

const val USER_COL_EMAIL = "email"
const val USER_COL_NAME = "username"
const val USER_COL_PASSWORD = "password"
const val USER_COL_FAMILY = "family_id"
const val USER_COL_RESTRICTIONS = "restrictions"

const val INVENTORY_COL_NAME = "food_name"
const val INVENTORY_COL_DATE = "expiry_date"
const val INVENTORY_COL_QUANTITY = "quantity"

const val PURCHASED_COL_NAME = "food_name"
const val PURCHASED_COL_QUANTITY = "quantity"

const val SHOPPING_COL_NAME = "food_name"
const val SHOPPING_COL_QUANTITY = "quantity"

class DataBaseHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTable = "CREATE TABLE $TABLE_USER (" +
                "$USER_COL_EMAIL TEXT PRIMARY KEY, " +
                "$USER_COL_NAME TEXT UNIQUE, " +
                "$USER_COL_PASSWORD TEXT, " +
                "$USER_COL_FAMILY TEXT, " +
                "$USER_COL_RESTRICTIONS TEXT" +
                ");"

        val createInventoryTable = "CREATE TABLE $TABLE_INVENTORY ( " +
                "$USER_COL_NAME TEXT, " +
                "$INVENTORY_COL_NAME TEXT, " +
                "$INVENTORY_COL_DATE TEXT, " +
                "$INVENTORY_COL_QUANTITY INTEGER, " +
                "FOREIGN KEY ($USER_COL_NAME) REFERENCES $TABLE_USER($USER_COL_NAME));"

        val createShoppingTable = "CREATE TABLE $TABLE_SHOPPING ( " +
                "$USER_COL_NAME TEXT, " +
                "$SHOPPING_COL_NAME TEXT, " +
                "$SHOPPING_COL_QUANTITY INTEGER, " +
                "FOREIGN KEY ($USER_COL_NAME) REFERENCES $TABLE_USER($USER_COL_NAME));"

        val createPurchasedTable = "CREATE TABLE $TABLE_PURCHASED ( " +
                "$USER_COL_NAME TEXT, " +
                "$PURCHASED_COL_NAME TEXT, " +
                "$PURCHASED_COL_QUANTITY INTEGER, " +
                "FOREIGN KEY ($USER_COL_NAME) REFERENCES $TABLE_USER($USER_COL_NAME));"

        db?.execSQL(createUserTable)
        db?.execSQL(createInventoryTable)
        db?.execSQL(createPurchasedTable)
        db?.execSQL(createShoppingTable)
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     *
     *
     * The SQLite ALTER TABLE documentation can be found
     * [here](http://sqlite.org/lang_altertable.html). If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     *
     *
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     *
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }

    fun insertUserData(user: User) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_COL_EMAIL, user.userEmail)
        contentValues.put(USER_COL_NAME, user.username)
        contentValues.put(USER_COL_PASSWORD, user.password)
        contentValues.put(USER_COL_FAMILY, user.familyId)
        contentValues.put(USER_COL_RESTRICTIONS, user.dietaryRestriction.joinToString())
        database.insert(TABLE_USER, null, contentValues)
    }

    fun insertInventoryData(food: Food, username: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_COL_NAME, username)
        contentValues.put(INVENTORY_COL_NAME, food.foodName)
        contentValues.put(INVENTORY_COL_DATE, food.expiryDate)
        contentValues.put(INVENTORY_COL_QUANTITY, food.quantity)
        database.insert(TABLE_INVENTORY, null, contentValues)
    }

    fun insertShoppingData(food: Food, username: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_COL_NAME, username)
        contentValues.put(SHOPPING_COL_NAME, food.foodName)
        contentValues.put(SHOPPING_COL_QUANTITY, food.quantity)
        database.insert(TABLE_SHOPPING, null, contentValues)
    }

    fun deleteShoppingData(food: Food, username: String) {
        val deleteShoppingQuery = "DELETE FROM $TABLE_SHOPPING " +
                "WHERE $USER_COL_NAME = '$username' " +
                "AND $SHOPPING_COL_NAME = '${food.foodName}' " +
                "AND $SHOPPING_COL_QUANTITY = '${food.quantity}';"
        this.writableDatabase.execSQL(deleteShoppingQuery)
    }

    fun insertPurchasedData(food: Food, username: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_COL_NAME, username)
        contentValues.put(PURCHASED_COL_NAME, food.foodName)
        contentValues.put(PURCHASED_COL_QUANTITY, food.quantity)
        database.insert(TABLE_PURCHASED, null, contentValues)
    }

    fun verifyUserExists(username: String) : Boolean {
        val db = this.readableDatabase
        val usernameQuery = "SELECT $USER_COL_NAME FROM $TABLE_USER " +
                "WHERE $USER_COL_NAME = '$username';"
        return db.rawQuery(usernameQuery, null).count != 0
    }

    fun verifyUserPassword(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val userPasswordQuery = "SELECT $USER_COL_NAME FROM $TABLE_USER " +
                "WHERE $USER_COL_NAME = '$username' "+
                "AND $USER_COL_PASSWORD = '$password';"
        return db.rawQuery(userPasswordQuery, null).count != 0
    }

    fun readUserData(username: String): User {
        val db = this.readableDatabase
        var currentUser = User(null, null, null)
        // get the username and password
        val userTableQuery = "SELECT * FROM $TABLE_USER " +
                "WHERE $USER_COL_NAME = '$username' ;"
        val userResult = db.rawQuery(userTableQuery, null)
        if (userResult.moveToFirst()) {
            val resultEmail: String = userResult.getString(userResult.getColumnIndex(USER_COL_EMAIL))
            val resultName: String = userResult.getString(userResult.getColumnIndex(USER_COL_NAME))
            val resultPassword: String = userResult.getString(userResult.getColumnIndex(USER_COL_PASSWORD))
            val resultRestriction: MutableList<Boolean> = ArrayList()
            userResult.getString(userResult.getColumnIndex(USER_COL_RESTRICTIONS)).split(", ").forEach{ resultRestriction.add(it.toBoolean()) }
            val resultFamily: Int = userResult.getInt(userResult.getColumnIndex(USER_COL_FAMILY))
            val userIsInFamily = (resultFamily != 0)
            currentUser = User(resultEmail, resultName, resultPassword, resultRestriction, resultFamily, userIsInFamily)
        }
        // get the inventory
        val inventoryTableQuery = "SELECT * FROM $TABLE_INVENTORY " +
                "WHERE $USER_COL_NAME = '$username';"
        val inventoryResult = db.rawQuery(inventoryTableQuery, null)
        if (inventoryResult.moveToFirst()) {
            do {
                val foodName = inventoryResult.getString(inventoryResult.getColumnIndex(INVENTORY_COL_NAME))
                val foodDate = inventoryResult.getString(inventoryResult.getColumnIndex(INVENTORY_COL_DATE))
                val foodQuantity = inventoryResult.getString(inventoryResult.getColumnIndex(INVENTORY_COL_QUANTITY)).toInt()
                val inventoryFood = Food(foodName, foodDate, foodQuantity)
                currentUser.inventoryList.add(inventoryFood)
            } while (inventoryResult.moveToNext())
        }
        // get the shopping list
        val shoppingTableQuery = "SELECT * FROM $TABLE_SHOPPING " +
                "WHERE $USER_COL_NAME = '$username';"
        val shoppingResult = db.rawQuery(shoppingTableQuery, null)
        if (shoppingResult.moveToFirst()) {
            do {
                val foodName = shoppingResult.getString(shoppingResult.getColumnIndex(SHOPPING_COL_NAME))
                val foodQuantity = shoppingResult.getString(shoppingResult.getColumnIndex(
                    SHOPPING_COL_QUANTITY)).toInt()
                val shoppingFood = Food(foodName, null, foodQuantity)
                currentUser.shoppingList.add(shoppingFood)
            } while (shoppingResult.moveToNext())
        }
        // get the purchased list
        val purchasedTableQuery = "SELECT * FROM $TABLE_PURCHASED " +
                "WHERE $USER_COL_NAME = '$username';"

        val purchasedResult = db.rawQuery(purchasedTableQuery, null)
        if (purchasedResult.moveToFirst()) {
            do {
                val foodName = purchasedResult.getString(purchasedResult.getColumnIndex(PURCHASED_COL_NAME))
                val foodQuantity = purchasedResult.getString(purchasedResult.getColumnIndex(PURCHASED_COL_QUANTITY)).toInt()
                val purchasedFood = Food(foodName, null, foodQuantity)
                currentUser.purchasedList.add(purchasedFood)
            } while (purchasedResult.moveToNext())
        }
        return currentUser
    }

    // don't use unless there's a major mistake

    fun clearDatabase() {
        this.writableDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        this.writableDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_INVENTORY")
        this.writableDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_SHOPPING")
        this.writableDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_PURCHASED")

        val createUserTable = "CREATE TABLE $TABLE_USER (" +
                "$USER_COL_EMAIL TEXT PRIMARY KEY, " +
                "$USER_COL_NAME TEXT UNIQUE, " +
                "$USER_COL_PASSWORD TEXT, " +
                "$USER_COL_FAMILY TEXT, " +
                "$USER_COL_RESTRICTIONS TEXT" +
                ");"

        val createInventoryTable = "CREATE TABLE $TABLE_INVENTORY ( " +
                "$USER_COL_NAME TEXT, " +
                "$INVENTORY_COL_NAME TEXT, " +
                "$INVENTORY_COL_DATE TEXT, " +
                "$INVENTORY_COL_QUANTITY INTEGER, " +
                "FOREIGN KEY ($USER_COL_NAME) REFERENCES $TABLE_USER($USER_COL_NAME));"

        val createShoppingTable = "CREATE TABLE $TABLE_SHOPPING ( " +
                "$USER_COL_NAME TEXT, " +
                "$SHOPPING_COL_NAME TEXT, " +
                "$SHOPPING_COL_QUANTITY INTEGER, " +
                "FOREIGN KEY ($USER_COL_NAME) REFERENCES $TABLE_USER($USER_COL_NAME));"

        val createPurchasedTable = "CREATE TABLE $TABLE_PURCHASED ( " +
                "$USER_COL_NAME TEXT, " +
                "$PURCHASED_COL_NAME TEXT, " +
                "$PURCHASED_COL_QUANTITY INTEGER, " +
                "FOREIGN KEY ($USER_COL_NAME) REFERENCES $TABLE_USER($USER_COL_NAME));"

        this.writableDatabase.execSQL(createUserTable)
        this.writableDatabase.execSQL(createInventoryTable)
        this.writableDatabase.execSQL(createShoppingTable)
        this.writableDatabase.execSQL(createPurchasedTable)
    }
}