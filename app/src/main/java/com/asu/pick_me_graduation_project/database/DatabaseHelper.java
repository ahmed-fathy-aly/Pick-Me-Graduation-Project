package com.asu.pick_me_graduation_project.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.asu.pick_me_graduation_project.model.Location;
import com.asu.pick_me_graduation_project.model.Ride;
import com.asu.pick_me_graduation_project.model.RideDetails;
import com.asu.pick_me_graduation_project.model.User;
import com.asu.pick_me_graduation_project.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by ahmed on 6/24/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    /* general constants */
    public static String DATABASE_NAME = "pickMeDatabase";
    public static int DATABASE_VERSION = 8;

    /* users table */
    public static String USER_TABLE = "userTable";
    public static String ID = "id";
    public static String FIRST_NAME = "firstName";
    public static String LAST_NAME = "lastName";
    public static String PROFILE_PICTURE_URL = "profilePictureUrl";

    /* locations table */
    public static String LOCATION_TABLE = "locationTable";
    public static String RIDE_ID = "rideId";
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
                        + RIDE_ID + " INT , "
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


    /**
     * clears all contents of the tables
     */
    public void clearTables()
    {
        String clearUserTable = "DELETE  FROM " + USER_TABLE;
        String clearLocationTable = "DELETE  FROM " + LOCATION_TABLE;
        String clearRideTable = "DELETE  FROM " + RIDE_TABLE;

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(clearUserTable);
        db.execSQL(clearLocationTable);
        db.execSQL(clearRideTable);
    }

    /**
     * adds the list of ride to the db
     * make sure you clear the db first
     */
    public void insertRides(List<Ride> rideList)
    {
        //Database
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        // the users
        HashSet<Integer> userIds = new HashSet<>();
        for (Ride ride : rideList)
        {
            for (User user : ride.getMembers())
                if (!userIds.contains(Integer.parseInt(user.getUserId())))
                {
                    userIds.add(Integer.parseInt(user.getUserId()));
                    insertUser(user, db);
                }
        }
        userIds.clear();

        // the locations
        for (Ride ride : rideList)
            for (Location location : ride.getLocations())
            {
                location.setRideId(ride.getId());
                insertLocation(location, db);
            }

        // the ride details
        for (Ride ride : rideList)
            insertRideDetails(ride, db);

        db.setTransactionSuccessful();
        db.endTransaction();
    }


    /**
     * returns a ride along with its details, locations and driver
     * returns null if that ride doesn't exist
     */
    public Ride getRide(int id)
    {
        Ride ride = new Ride();
        int driverId;

        // get the ride details
        String selectDetails =
                "SELECT * FROM " + RIDE_TABLE
                        + " WHERE " + ID + " = " + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectDetails, null);
        if (cursor.moveToFirst())
        {
            ride.setId("" + cursor.getInt(cursor.getColumnIndex(ID)));
            ride.setTime(TimeUtils.getDatabaseTime(cursor.getString(cursor.getColumnIndex(TIME_STR))));
            ride.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
            ride.setNotes(cursor.getString(cursor.getColumnIndex(NOTES)));
            RideDetails rideDetails = new RideDetails();
            rideDetails.setNoSmoking(cursor.getInt(cursor.getColumnIndex(NO_SMOKING)) == 1);
            rideDetails.setNumberOfFreeSeats(cursor.getInt(cursor.getColumnIndex(FREE_SEATS)));
            rideDetails.setLadiesOnly(cursor.getInt(cursor.getColumnIndex(LADIES_ONLY)) == 1);
            ride.setRideDetails(rideDetails);
            driverId = cursor.getInt(cursor.getColumnIndex(DRIVER_ID));
        } else
            return null;

        Log.e("Game", "found its details ");

        // get the driver
        User driver = getUser(driverId, db);
        if (driver != null)
            ride.setDriver(driver);
        else
            return null;

        // get the locations
        List<Location> locationList = getLocations(id, db);
        if (locationList != null)
            ride.setLocations(locationList);
        else
            return null;

        return ride;
    }

    /**
     * returns all the rides in the database
     */
    public List<Ride> getAllRides()
    {
        String selectRideIds =
                "SELECT " + ID + " FROM " + RIDE_TABLE;
        Cursor cursor = getReadableDatabase().rawQuery(selectRideIds, null);

        List<Ride> rides = new ArrayList<>();
        if (cursor.moveToFirst())
            do
            {
                int rideId = cursor.getInt(cursor.getColumnIndex(ID));
                Ride ride = getRide(rideId);
                if (ride == null)
                    return null;
                else
                    rides.add(ride);
            } while (cursor.moveToNext());
        return rides;
    }

    private void insertUser(User user, SQLiteDatabase database)
    {
        String keysString = String.format(" ( %s , %s, %s, %s ) "
                , ID
                , FIRST_NAME
                , LAST_NAME
                , PROFILE_PICTURE_URL);
        String valuesString = String.format(" ( %d, \"%s\", \"%s\", \"%s\" ) ",
                Integer.parseInt(user.getUserId())
                , user.getFirstName()
                , user.getLastName()
                , user.getProfilePictureUrl());
        String insertUser =
                "INSERT INTO " + USER_TABLE
                        + keysString
                        + " VALUES " + valuesString;
        database.execSQL(insertUser);
    }


    private void insertLocation(Location location, SQLiteDatabase database)
    {
        String keysString = String.format(" ( %s , %s , %s, %s, %s , %s , %s ) "
                , ID
                , RIDE_ID
                , LONGITUDE
                , LATITUDE
                , ORDER
                , USER_ID
                , TYPE);
        String valuesString = String.format(" ( %d, %d,  %.8f, %.8f, %d , %d , %d ) ",
                Integer.parseInt(location.getId())
                , Integer.parseInt(location.getRideId())
                , location.getLongitude()
                , location.getLatitude()
                , location.getOrder()
                , Integer.parseInt(location.getUser().getUserId())
                , location.getType() == Location.LocationType.SOURCE ? 0 : 1);
        String insertLocation =
                "INSERT INTO " + LOCATION_TABLE
                        + keysString
                        + " VALUES " + valuesString;
        database.execSQL(insertLocation);
    }

    private void insertRideDetails(Ride ride, SQLiteDatabase database)
    {
        String keysString = String.format(" ( %s , %s, %s, %s , %s , %s , %s , %s ) "
                , ID
                , DRIVER_ID
                , DESCRIPTION
                , NOTES
                , TIME_STR
                , FREE_SEATS
                , NO_SMOKING
                , LADIES_ONLY);
        String valuesString = String.format(" ( %d, %d, \"%s\" , \"%s\", \"%s\",  %d , %d , %d ) ",
                Integer.parseInt(ride.getId())
                , Integer.parseInt(ride.getRider().getUserId())
                , ride.getDescription()
                , ride.getNotes()
                , TimeUtils.convertToDatabaseTime(ride.getTime())
                , ride.getRideDetails().getNumberOfFreeSeats()
                , ride.getRideDetails().isNoSmoking() ? 1 : 0
                , ride.getRideDetails().isLadiesOnly() ? 1 : 0
        );
        String insertRide =
                "INSERT INTO " + RIDE_TABLE
                        + keysString
                        + " VALUES " + valuesString;
        database.execSQL(insertRide);
    }


    private User getUser(int driverId, SQLiteDatabase db)
    {
        String selectDriver =
                "SELECT * FROM " + USER_TABLE
                        + " WHERE " + ID + " = " + driverId;
        Cursor cursor = db.rawQuery(selectDriver, null);
        if (cursor.moveToFirst())
        {
            User user = new User();
            user.setUserId("" + cursor.getInt(cursor.getColumnIndex(ID)));
            user.setFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
            user.setLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
            user.setProfilePictureUrl(cursor.getString(cursor.getColumnIndex(PROFILE_PICTURE_URL)));
            return user;
        }
        return null;

    }


    private List<Location> getLocations(int id, SQLiteDatabase db)
    {
        String selectLocations =
                "SELECT * FROM " + LOCATION_TABLE
                        + " JOIN " + USER_TABLE
                        + " ON " + LOCATION_TABLE + "." + USER_ID + " = " + USER_TABLE + "." + ID
                        + " WHERE " + RIDE_ID + " = " + id;
        Cursor cursor = db.rawQuery(selectLocations, null);
        List<Location> locationList = new ArrayList<>();
        if (cursor.moveToFirst())
            do
            {
                // locations details
                Location location = new Location();
                location.setId("" + cursor.getInt(cursor.getColumnIndex(ID)));
                location.setRideId("" + cursor.getInt(cursor.getColumnIndex(RIDE_ID)));
                location.setLatitude(cursor.getDouble(cursor.getColumnIndex(LATITUDE)));
                location.setLongitude(cursor.getDouble(cursor.getColumnIndex(LONGITUDE)));
                location.setOrder(cursor.getInt(cursor.getColumnIndex(ORDER)));
                location.setType(cursor.getInt(cursor.getColumnIndex(TYPE)) == 0 ?
                        Location.LocationType.SOURCE : Location.LocationType.DESTINATION);

                // its user
                User user = new User();
                user.setUserId("" + cursor.getInt(cursor.getColumnIndex(ID)));
                user.setFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
                user.setLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
                user.setProfilePictureUrl(cursor.getString(cursor.getColumnIndex(PROFILE_PICTURE_URL)));
                location.setUser(user);

                locationList.add(location);
            }
            while (cursor.moveToNext());

        return locationList;
    }
}
