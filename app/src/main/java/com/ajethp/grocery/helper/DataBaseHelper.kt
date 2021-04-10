package com.ajethp.grocery.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ajethp.grocery.classes.Food
import com.ajethp.grocery.classes.User

const val DATABASE_NAME = "USER DATABASE"

// Table names
const val TABLE_USER = "Users"
const val TABLE_FAMILY = "Family"
const val TABLE_INVENTORY = "Inventory"
const val TABLE_PURCHASED = "Purchased"
const val TABLE_SHOPPING = "Shopping"

const val FAMILY_COL_NAME = "family_id"
const val FAMILY_COL_PASSWORD = "family_password"

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

/**
 * This class implements the SQLite database needed in order to
 * store the user information, along with their food list and
 * family information
 *
 * @author jethro
 * @author claudia
 */

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
                "$USER_COL_FAMILY TEXT REFERENCES $TABLE_FAMILY($FAMILY_COL_NAME), " +
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

        val createFamilyTable = "CREATE TABLE $TABLE_FAMILY ( " +
                "$FAMILY_COL_NAME TEXT PRIMARY KEY, " +
                "$FAMILY_COL_PASSWORD TEXT );"

        db?.execSQL(createUserTable)
        db?.execSQL(createInventoryTable)
        db?.execSQL(createPurchasedTable)
        db?.execSQL(createShoppingTable)
        db?.execSQL(createFamilyTable)
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

    /**
     * This method inserts the user data into the user table
     * It is invoked when creating a new user account
     */
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

    fun modifyUserRestriction(user: User) {
        val database = this.writableDatabase
        val modifyUserRestrictionQuery = "UPDATE $TABLE_USER " +
                "SET $USER_COL_RESTRICTIONS = '${user.dietaryRestriction.joinToString()}' " +
                "WHERE $USER_COL_NAME = '${user.username}';"
        database.execSQL(modifyUserRestrictionQuery)
    }

    fun modifyUserFamily(username: String, familyId: String) {
        val database = this.writableDatabase
        val modifyUserFamilyQuery = "UPDATE $TABLE_USER " +
                "SET $USER_COL_FAMILY = '$familyId' " +
                "WHERE $USER_COL_NAME = '$username';"
        database.execSQL(modifyUserFamilyQuery)
    }

    /**
     * This method is invoked when inserting inventory data
     * for a particular user, the data is stored in the
     * Inventory table
     */
    fun insertInventoryData(food: Food, username: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_COL_NAME, username)
        contentValues.put(INVENTORY_COL_NAME, food.foodName)
        contentValues.put(INVENTORY_COL_DATE, food.expiryDate)
        contentValues.put(INVENTORY_COL_QUANTITY, food.quantity)
        database.insert(TABLE_INVENTORY, null, contentValues)
    }

    /**
     * This method is invoked when an inventory item has been used
     * with some remaining amount of item
     */

    fun modifyInventoryData(food: Food, quantityRemaining: Int, username: String) {
        val modifyInventoryDataQuery = "UPDATE $TABLE_INVENTORY " +
                "SET $INVENTORY_COL_QUANTITY = $quantityRemaining " +
                "WHERE $USER_COL_NAME = '$username' " +
                "AND $INVENTORY_COL_NAME = '${food.foodName}' " +
                "AND $INVENTORY_COL_DATE = '${food.expiryDate}';"
        this.writableDatabase.execSQL(modifyInventoryDataQuery)
    }

    /**
     * This method is invoked when an inventory item has been used
     * with no item remaining
     */

    fun deleteInventoryData(food: Food, username: String) {
        val deleteInventoryQuery = "DELETE FROM $TABLE_INVENTORY " +
                "WHERE $USER_COL_NAME = '$username' " +
                "AND $INVENTORY_COL_NAME = \"${food.foodName}\" " +
                "AND $INVENTORY_COL_DATE = '${food.expiryDate}';"
        this.writableDatabase.execSQL(deleteInventoryQuery)
    }

    /**
     * This method is invoked when inserting shopping data
     * for a particular user, the data is stored in the
     * Shopping table
     */
    fun insertShoppingData(food: Food, username: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_COL_NAME, username)
        contentValues.put(SHOPPING_COL_NAME, food.foodName)
        contentValues.put(SHOPPING_COL_QUANTITY, food.quantity)
        database.insert(TABLE_SHOPPING, null, contentValues)
    }

    /**
     * This method is invoked when the shopping data is deleted
     * or when it has been purchased and moved to the purchased
     * list
     */
    fun deleteShoppingData(food: Food, username: String) {
        val deleteShoppingQuery = "DELETE FROM $TABLE_SHOPPING " +
                "WHERE $USER_COL_NAME = '$username' " +
                "AND $SHOPPING_COL_NAME = \"${food.foodName}\" " +
                "AND $SHOPPING_COL_QUANTITY = '${food.quantity}';"
        this.writableDatabase.execSQL(deleteShoppingQuery)
    }

    /**
     * This method is invoked when a shopping item has been purchased
     * and moved to the purchased item table
     */

    fun insertPurchasedData(food: Food, username: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(USER_COL_NAME, username)
        contentValues.put(PURCHASED_COL_NAME, food.foodName)
        contentValues.put(PURCHASED_COL_QUANTITY, food.quantity)
        database.insert(TABLE_PURCHASED, null, contentValues)
    }

    /**
     * This method is invoked when a purchased item is deleted
     * and moved to the Inventory table or when it is deleted
     * by the user. It deletes the row from the Purchased table
     */

    fun deletePurchasedData(food: Food, username: String) {
        val deletePurchasedQuery = "DELETE FROM $TABLE_PURCHASED " +
                "WHERE $USER_COL_NAME = '$username' " +
                "AND $PURCHASED_COL_NAME = \"${food.foodName}\" " +
                "AND $PURCHASED_COL_QUANTITY = '${food.quantity}';"
        this.writableDatabase.execSQL(deletePurchasedQuery)
    }

    fun insertFamilyData(familyId: String, familyPassword: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(FAMILY_COL_NAME, familyId)
        contentValues.put(FAMILY_COL_PASSWORD, familyPassword)
        database.insert(TABLE_FAMILY, null, contentValues)
    }

    fun selectFamilyInventory(familyId: String) {
        val familyInventoryQuery = "SELECT * FROM $TABLE_INVENTORY " +
                "WHERE $USER_COL_NAME IN ( SELECT $USER_COL_NAME FROM $TABLE_USER " +
                "WHERE $USER_COL_FAMILY = '$familyId');"
        this.readableDatabase.execSQL(familyInventoryQuery)
    }

    fun verifyFamilyExists(familyId: String): Boolean {
        val db = this.readableDatabase
        val familyQuery = "SELECT $FAMILY_COL_NAME FROM $TABLE_FAMILY " +
                "WHERE $FAMILY_COL_NAME = '$familyId';"
        return db.rawQuery(familyQuery, null).count != 0
    }

    fun verifyFamilyPassword(familyId: String, password: String): Boolean {
        val db = this.readableDatabase
        val familyPasswordQuery = "SELECT $FAMILY_COL_NAME FROM $TABLE_FAMILY " +
                "WHERE $FAMILY_COL_NAME = '$familyId' "+
                "AND $FAMILY_COL_PASSWORD = '$password';"
        return db.rawQuery(familyPasswordQuery, null).count != 0
    }

    /**
     * This method is used to check if a user with that user ID
     * already exists in the user table. It is invoked when creating
     * a new user account
     */

    fun verifyUserExists(username: String) : Boolean {
        val db = this.readableDatabase
        val usernameQuery = "SELECT $USER_COL_NAME FROM $TABLE_USER " +
                "WHERE $USER_COL_NAME = '$username';"
        return db.rawQuery(usernameQuery, null).count != 0
    }

    /**
     * This method is used to check if a user with that user ID
     * and password exists. It is used to verify user password when
     * logging in
     */

    fun verifyUserPassword(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val userPasswordQuery = "SELECT $USER_COL_NAME FROM $TABLE_USER " +
                "WHERE $USER_COL_NAME = '$username' "+
                "AND $USER_COL_PASSWORD = '$password';"
        return db.rawQuery(userPasswordQuery, null).count != 0
    }

    /**
     * This method is used to initialise a User object based on the
     * data that already exists on the database.It takes in the username
     * as a string and returns a User object tied to that particular
     * username
     */

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
            val resultFamily: String? = userResult.getString(userResult.getColumnIndex(USER_COL_FAMILY))
            // CLOUDDDD remind me to check this
            val userIsInFamily = (resultFamily != null)
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
        this.writableDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_FAMILY")

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

        val createFamilyTable = "CREATE TABLE $TABLE_FAMILY ( " +
                "$FAMILY_COL_NAME INTEGER PRIMARY KEY, " +
                "$FAMILY_COL_PASSWORD TEXT );"

        this.writableDatabase.execSQL(createUserTable)
        this.writableDatabase.execSQL(createInventoryTable)
        this.writableDatabase.execSQL(createShoppingTable)
        this.writableDatabase.execSQL(createPurchasedTable)
        this.writableDatabase.execSQL(createFamilyTable)
    }
}