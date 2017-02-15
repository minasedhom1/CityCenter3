package com.example.lenovo.citycenter.classes;

/**
 * Created by lenovo on 09/09/2015.
 */
public class DatabaseHandler  {
  /*  private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Imagedb";

    // Contacts table name

    private static final String TABLE_RESTAURANTS = "Resturants";


 //keys of second table (restaurant
    private static final String KEY_ID_REST="Id";
    private static final String KEY_NAME_REST="Name";
    private static final String KEY_ICON_REST= "Icon";
    private static final String KEY_DETAILS_REST = "Details";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_RESTAURANTS_TABLE ="CREATE TABLE " + TABLE_RESTAURANTS + "("
                + KEY_ID_REST + " INTEGER PRIMARY KEY autoincrement," + KEY_NAME_REST + " TEXT not null,"  +
                KEY_DETAILS_REST + " TEXT,"  +
                KEY_ICON_REST + " TEXT," + ")";


                db.execSQL(CREATE_RESTAURANTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTAURANTS);
        // Create tables again
        onCreate(db);
    }
    //METHODS TO HANDLE USER TABLE
    //***************************************
    // Adding new contact





  //METHODS TO HANDLE RESTAURANT TABLE
  //************************************

    public void addRestaurant(category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_REST, category.get_name());
        values.put(KEY_ICON_REST, category.get_icon());

        //values.put(KEY_PHONE_REST,shop.get_phone());
       // values.put(KEY_WEBSITE, shop.get_website());//contact._name??
       // values.put(KEY_LOCATION_REST,restaurant.get_location());



        db.insert(TABLE_RESTAURANTS, null, values);
        db.close(); // Closing database connection
    }

    public List<category> getAllRestaurants() {
        List<category> categoryList = new ArrayList<category>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_RESTAURANTS  ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                category category = new category();
                category.set_id(Integer.parseInt(cursor.getString(0)));
                category.set_name(cursor.getString(1));
                category.set_details(cursor.getString(2));
               category.set_icon(cursor.getInt(3));


// Adding contact to list
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
// close inserting data from database
        db.close();
// return contact list
        return categoryList;

    }


   /* public void updateRestaurant(Restaurant restaurant,int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME_REST, restaurant.get_name());
        values.put(KEY_ICON_REST, restaurant.get_icon());
        values.put(KEY_LONGTUDE, restaurant.get_longtude());
        values.put(KEY_LATITUDE, restaurant.get_latitude());
        values.put(KEY_PHONE_REST,restaurant.get_phone());
        values.put(KEY_WEBSITE, restaurant.get_website());//contact._name??
        values.put(KEY_LOCATION_REST,restaurant.get_location());
        values.put(KEY_CATEGORY_REST,restaurant.get_category());
        values.put(KEY_MENU_REST,restaurant.get_menu());
        values.put(KEY_FLAG, restaurant.get_flag());
        db.update(TABLE_RESTAURANTS, values, KEY_ID_REST + "=?", new String[]{String.valueOf(id)});
        db.close();

    }

    public void deleteRestaurant(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESTAURANTS, KEY_ID_REST + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public List<Restaurant> getFavourites(){
        SQLiteDatabase db = this.getWritableDatabase();

        List<Restaurant> restaurantList = new ArrayList<Restaurant>();

        Cursor cursor = db.query(TABLE_RESTAURANTS, new String[]{KEY_ID_REST,KEY_NAME_REST,
                KEY_PHONE_REST,KEY_LOCATION_REST,KEY_CATEGORY_REST,
                KEY_MENU_REST,KEY_ICON_REST,KEY_LATITUDE,KEY_LONGTUDE,KEY_WEBSITE,KEY_FLAG},KEY_FLAG +" =1",null, null, null, null);

        //String query = "select * from "+TABLE_RESTAURANTS +" where "+ KEY_FLAG +" = T";
        //Cursor cursor = db.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                Restaurant myRestaurant = new Restaurant();
                myRestaurant.set_id(Integer.parseInt(cursor.getString(0)));
                myRestaurant.set_name(cursor.getString(1));
                myRestaurant.set_phone(cursor.getString(2));
                myRestaurant.set_location(cursor.getString(3));
                myRestaurant.set_category(cursor.getString(4));
                myRestaurant.set_menu(cursor.getString(5));
                myRestaurant.set_icon(cursor.getString(6));
                myRestaurant.set_latitude(cursor.getString(7));
                myRestaurant.set_longtude(cursor.getString(8));
                myRestaurant.set_website(cursor.getString(9));
                myRestaurant.set_flag(cursor.getInt(10));

                restaurantList.add(myRestaurant);

            } while (cursor.moveToNext());
        }
        db.close();
        return restaurantList;

    }

    public void updateFlag(int id,boolean type){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (type)
        values.put(KEY_FLAG, 1);
        else
        values.put(KEY_FLAG, 0);

        db.update(TABLE_RESTAURANTS, values, KEY_ID_REST + "= ?", new String[]{String.valueOf(id)});
        db.close();
    }

    /*public Restaurant getResturant(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from "+TABLE_RESTAURANTS +" where "+ KEY_ID_REST +" = "+id;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)
            cursor.moveToFirst();

        Restaurant restaurant = new Restaurant(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2),cursor.getString(3));
        db.close();
// return contact
        return user;*/

   /* public ArrayList<String> search(String search_word)
    {
        ArrayList<String> mylist =new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // String SelectQuery ="select "+ search_word+ " from Resturant" ;
        // db.query(TABLE_RESTAURANTS,new String[]{search_word},null,null,null,null,null );

        Cursor cursor= db.query(TABLE_RESTAURANTS, new String[]{search_word}, null, null, null, null, null);

        if (search_word.matches("Location")) {
            if (cursor.moveToFirst()) {
                do {
                    mylist.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }

        else if (search_word.matches("Category")){
            if (cursor.moveToFirst()) {
                do {
                    mylist.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }

        else if (search_word.matches("Name")){
            if (cursor.moveToFirst()) {
                do {
                    mylist.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return  mylist;
    }

    public ArrayList<Restaurant> getFromSearch(String column , String search_word)
    {
        ArrayList<Restaurant> mylist =new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // String SelectQuery ="select "+ search_word+ " from Resturant" ;
        // db.query(TABLE_RESTAURANTS,new String[]{search_word},null,null,null,null,null );

        Cursor cursor = db.query(TABLE_RESTAURANTS, new String[]{KEY_ID_REST,KEY_NAME_REST,
                KEY_PHONE_REST,KEY_LOCATION_REST,KEY_CATEGORY_REST,
                KEY_MENU_REST,KEY_ICON_REST,KEY_LATITUDE,KEY_LONGTUDE,KEY_WEBSITE,KEY_FLAG},column+"=?",new String[]{search_word}, null, null, null);
        if (column.matches("Location")) {
            if (cursor.moveToFirst()) {
                do {
                    Restaurant myRestaurant = new Restaurant();
                    myRestaurant.set_id(Integer.parseInt(cursor.getString(0)));
                    myRestaurant.set_name(cursor.getString(1));
                    myRestaurant.set_phone(cursor.getString(2));
                    myRestaurant.set_location(cursor.getString(3));
                    myRestaurant.set_category(cursor.getString(4));
                    myRestaurant.set_menu(cursor.getString(5));
                    myRestaurant.set_icon(cursor.getString(6));
                    myRestaurant.set_latitude(cursor.getString(7));
                    myRestaurant.set_longtude(cursor.getString(8));
                    myRestaurant.set_website(cursor.getString(9));
                    myRestaurant.set_flag(cursor.getInt(10));

                    mylist.add(myRestaurant);

                } while (cursor.moveToNext());
            }
        }

        else if (column.matches("Category")){
            if (cursor.moveToFirst()) {
                do {
                    Restaurant myRestaurant = new Restaurant();
                    myRestaurant.set_id(Integer.parseInt(cursor.getString(0)));
                    myRestaurant.set_name(cursor.getString(1));
                    myRestaurant.set_phone(cursor.getString(2));
                    myRestaurant.set_location(cursor.getString(3));
                    myRestaurant.set_category(cursor.getString(4));
                    myRestaurant.set_menu(cursor.getString(5));
                    myRestaurant.set_icon(cursor.getString(6));
                    myRestaurant.set_latitude(cursor.getString(7));
                    myRestaurant.set_longtude(cursor.getString(8));
                    myRestaurant.set_website(cursor.getString(9));
                    myRestaurant.set_flag(cursor.getInt(10));

                    mylist.add(myRestaurant);
                } while (cursor.moveToNext());
            }
        }

        else if (column.matches("Name")){
            if (cursor.moveToFirst()) {
                do {
                    Restaurant myRestaurant = new Restaurant();
                    myRestaurant.set_id(Integer.parseInt(cursor.getString(0)));
                    myRestaurant.set_name(cursor.getString(1));
                    myRestaurant.set_phone(cursor.getString(2));
                    myRestaurant.set_location(cursor.getString(3));
                    myRestaurant.set_category(cursor.getString(4));
                    myRestaurant.set_menu(cursor.getString(5));
                    myRestaurant.set_icon(cursor.getString(6));
                    myRestaurant.set_latitude(cursor.getString(7));
                    myRestaurant.set_longtude(cursor.getString(8));
                    myRestaurant.set_website(cursor.getString(9));
                    myRestaurant.set_flag(cursor.getInt(10));

                    mylist.add(myRestaurant);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return  mylist;
    }*/

}
