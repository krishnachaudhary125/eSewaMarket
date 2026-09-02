import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_4_5 = object : Migration(4, 5) {

    override suspend fun migrate(connection: SQLiteConnection) {

// Added province, district, city, postalCode and removed formatted address from version 4_5

        connection.execSQL(
            """
            CREATE TABLE addresses_new (
                id INTEGER NOT NULL,
                userId INTEGER NOT NULL,
                fullName TEXT NOT NULL,
                phone TEXT NOT NULL,
                province TEXT NOT NULL DEFAULT '',
                district TEXT NOT NULL DEFAULT '',
                city TEXT NOT NULL DEFAULT '',
                postalCode TEXT NOT NULL DEFAULT '',
                addressName TEXT NOT NULL,
                isDefaultAddress INTEGER NOT NULL,
                isBillingAddress INTEGER NOT NULL,
                label TEXT,
                PRIMARY KEY(id, userId)
            )
            """.trimIndent()
        )

        connection.execSQL(
            """
            INSERT INTO addresses_new (
                id,
                userId,
                fullName,
                phone,
                province,
                district,
                city,
                postalCode,
                addressName,
                isDefaultAddress,
                isBillingAddress,
                label
            )
            SELECT
                id,
                userId,
                fullName,
                phone,
                '',
                '',
                '',
                '',
                addressName,
                isDefaultAddress,
                isBillingAddress,
                label
            FROM addresses
            """.trimIndent()
        )

        connection.execSQL("DROP TABLE addresses")

        connection.execSQL(
            "ALTER TABLE addresses_new RENAME TO addresses"
        )
    }
}