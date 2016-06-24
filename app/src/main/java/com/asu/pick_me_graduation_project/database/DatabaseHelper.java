package com.asu.pick_me_graduation_project.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.asu.pick_me_graduation_project.model.Location;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.TimeUtils;

import java.util.HashSet;
import java.util.List;

/**
 * Created by ahmed on 6/24/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    /* general constants */
    public static String DATABASE_NAME = "pickMeDatabase";
    public static int DATABASE_VERSION = 7;

    /* users table */
    public static String USER_TABLE = "userTable";
    public static String ID = "id";
    public static String FIRST_NAME = "firstName";
    public static String LAST_NAME = "lastName";
    public static String PROFILE_PICTURE_URL = "profilePictureUrl";

    /* locations table */
    public static String LOCATION_TABLE = "locationTable";
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";
    public static String ORDER = "locationOrder";
    public static String USER_ID = "userID";
    public static String TYPE = "type";

    /* rides table */
    public static String RIDE_TABLE = "rideTable";
    public static String DESCRIPTION = "description";
    public static String DRIVER_ID = "driverId";
    public static String TIME_STR = "timeStr";
    public static String NOTES = "notes";
    public static String FREE_SEATS = "freeSeats";
    public static String NO_SMOKING = "noSmoking";
    public static String LADIES_ONLY = "ladiesOnly";


    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String createUserTable =
                "CREATE TABLE " + USER_TABLE
                        + " ( "
                        + ID + " INT PRIMARY KEY , "
                        + FIRST_NAME + " TEXT , "
                        + LAST_NAME + " TEXT , "
                        + PROFILE_PICTURE_URL + " TEXT"
                        + " )";
        String createLocationsTable =
                "CREATE TABLE " + LOCATION_TABLE
                        + " ( "
                        + ID + " INT PRIMARY KEY , "
                        + LATITUDE + " REAL , "
                        + LONGITUDE + " REAL , "
                        + ORDER + " INT , "
                        + USER_ID + " INT , "
                        + TYPE + " INT "
                        + " )";
        String createRidesTable =
                "CREATE TABLE " + RIDE_TABLE
                        + " ( "
                        + ID + " INT PRIMARY KEY , "
                        + DESCRIPTION + " TEXT , "
                        + DRIVER_ID + " INT , "
                        + TIME_STR + " TEXT , "
                        + NOTES + " TEXT ,"
                        + FREE_SEATS + " INT ,"
                        + NO_SMOKING + " INT ,"
                        + LADIES_ONLY + " INT"
                        + " )";
        db.execSQL(createUserTable);
        db.execSQL(createLocationsTable);
        db.execSQL(createRidesTable);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String dropUserTable =
                "DROP TABLE IF EXISTS " + USER_TABLE;
        String dropLocationsTable =
                "DROP TABLE IF EXISTS " + LOCATION_TABLE;
        String dropRidesTable =
                "DROP TABLE IF EXISTS " + RIDE_TABLE;

        db.execSQL(dropUserTable);
        db.execSQL(dropLocationsTable);
        db.execSQL(dropRidesTable);
        onCreate(db);
    }



}
