package com.example.vitabu;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Singleton Class to encapsulate all logic for interacting with the database.
 *
 * All methods in this
 * class that are NOT constructors will take 2 Runnable objects as their first 2 arguments.
 * These arguments will be the success and fail callbacks respectively.  All methods should be able
 * to handle null being passed as callbacks without throwing an exception.
 *
 * If a value needs to be returned to the user, It should be saved, as a private variable within
 * this class, then returned useing a getter that is to be called ONLY from within the success
 * callback.
 *
 * An example of how to convert methods to a Runnable is shown below.
 * {@code
public void testMethod(String arg1, int arg2){
Log.d("TEST", "In callback function. arg1 = " + arg1 + " arg2 = " + arg2);
}

...

public void someMethod(){
Runnable testRunnable = new Runnable() {
@Override
public void run() {
testMethod(arg1, arg2);
}
};
Database database = Database.getInstance()
database.testCallback(testRunnable);
}
}
 *
 *
 *
 * @author Tristan Carlson
 * @version 1.0
 */
public class Database {
    private String logTag = "Database Singleton";
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference rootReference;
    private static final Database ourInstance = new Database();
    private ArrayList<Book> searchBooksReturnValue;
    private ArrayList<BorrowRecord> getBorrowRecordsByBookidReturnValue;
    private User fetchUserReturnValue;
    private Iterable<DataSnapshot> queryResult;

    // TODO Move all other database accesses to this class.

    /**
     * Method to get reference to the singleton object.
     * @return Database
     */
    public static Database getInstance() {
        return ourInstance;
    }

    /**
     * Internal Constructor.
     */
    private Database() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        rootReference = database.getReference();
    }

    /**
     * Writes User to the database.
     * @param successCallback Runnable to call on success.
     * @param failCallback Runnable to call on fail.
     * @param user  Local user to write to database.
     */
    public void writeUserToDatabase(final Runnable successCallback, final Runnable failCallback, final LocalUser user){
        rootReference.child("users").child(user.getUserName()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Successfully wrote user to database.");
                        rootReference.child("usernames").child(user.getUserName()).setValue(user.getUserid())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(logTag, "Successfully wrote username to database.");
                                        successCallback.run();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(logTag, "Failed to write Username to database", e);
                                        failCallback.run();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to write User to database", e);
                        failCallback.run();
                    }
                });
    }

    public String getCurUserName(){
        return auth.getCurrentUser().getDisplayName();
    }

    /**
     *
     * @param successCallback Runnable to call on success.
     * @param failCallback Runnable to call on fail.
     * @param email String
     * @param password String
     * @param userName String
     * @param location Location object
     */
    public void createUser(final Runnable successCallback, final Runnable failCallback, final String email, final String password, final String userName, final Location location){
        // Attempt to register user with firebase auth.
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // User successfully created. Update user info.
                        Log.d(logTag, "Successfully created user with email: " + email);

                        final LocalUser user = new LocalUser(location, userName, email, auth.getCurrentUser());

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName)
                                .build();

                        auth.getCurrentUser().updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(logTag, "User profile updated.");
                                            writeUserToDatabase(successCallback, failCallback, user);
                                        }
                                        else{
                                            Log.d(logTag, "User Profile update Failed.  This is bad.");
                                            if (failCallback != null)
                                                failCallback.run();
                                        }
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(logTag, "Failed to create user with email: " + email, e);
                        if (failCallback != null)
                            failCallback.run();
                    }
                });
    }

    /**
     * Convienient method to test if you have passed a callback method correctly.
     * @param callback Runnable to be called back when this method is executed.
     */
    public void testCallback(Runnable callback){
        try {
            callback.run();
        }
        catch (Exception err){
            Log.d(logTag, "Exception in callback", err);
        }
    }

    /**
     * Checks if username is available.  Calls sucsessCallback if it is, and failCallback otherwise.
     * @param successCallback Runnable to call on success.
     * @param failCallback Runnable to call on fail.
     * @param username String username to be checked.
     */
    public void checkUsernameAvailability(final Runnable successCallback, final Runnable failCallback, final String username){
        rootReference.child("usernames").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(logTag, "Got username " + username + " Data from the database");
                if (snapshot.exists()){
                    // Username is taken
                    if (failCallback != null)
                        failCallback.run();

                }
                else{
                    // Username available.
                    if (successCallback != null)
                        successCallback.run();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(logTag, "Database Error", databaseError.toException());
                if (failCallback != null)
                    failCallback.run();
            }
        });

    }

    /**
     * Searches database for books based on the provided search criteria. The list of books matching
     * the criteria can be obtained by calling the method getSearchBooksReturnValue() in the
     * successCallback method.
     *
     * @param successCallback Runnable to be called back on successful search.
     * @param failCallback Runnable to be called if there is a database error.
     * @param author String
     * @param title String
     * @param isbn String
     * @param kwords String
     */
    public void searchBooks(final Runnable successCallback, final Runnable failCallback, final String author, final String title, final String isbn, final String kwords){
        final ArrayList<Book> bookList = new ArrayList<>();
        String initialSearchValue = "";
        String initialSearchField = "";
        if (! author.equals("")){
            initialSearchValue = author;
            initialSearchField = "author";
        }else if (! title.equals("")){
            initialSearchValue = title;
            initialSearchField = "title";
        }
        else if (! isbn.equals("")){
            initialSearchValue = isbn;
            initialSearchField = "isbn";
        }
        else if (! kwords.equals("")){
            initialSearchValue = kwords;
            initialSearchField = "description";
        }
        Log.d(logTag, initialSearchValue);

        rootReference.child("books").orderByChild(initialSearchField).equalTo(initialSearchValue).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(logTag, dataSnapshot.toString());
                        for (DataSnapshot subSnapshot : dataSnapshot.getChildren()) {

                            Book curBook = subSnapshot.getValue(Book.class);
                            Log.d(logTag, curBook.getBookid());
                            if ((title.equals("") || curBook.getTitle().equals(title)) &&
                                    (author.equals("") || curBook.getAuthor().equals(author)) &&
                                    (isbn.equals("") || curBook.getISBN().equals(isbn)) &&
                                    (kwords.equals("") || curBook.getDescription().equals("") || curBook.getDescription().contains(kwords) )&&
                                    (author.equals("") || curBook.getAuthor().equals(author))
                            ) {
                                bookList.add(curBook);
                            }

                        }
                        searchBooksReturnValue = bookList;
                        if (successCallback != null)
                            successCallback.run();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(logTag, "Uhh Failed to get books from database...", databaseError.toException());
                        if (failCallback != null)
                            failCallback.run();
                    }
                });


    }

    /**
     *
     * @return ArrayList of books matching the search.
     */
    public ArrayList<Book> getSearchBooksReturnValue(){
        ArrayList<Book> ret = searchBooksReturnValue;
        searchBooksReturnValue = null;
        return ret;
    }

    /**
     * Gets all borrowrecords that have matching Book id.
     *
     * NOTE: this method currently sets a permanant listener for datachange! Meaning it will
     * continually call the succsess callback passed to it whenever the database changes!
     *
     * @param successCallback Runnable
     * @param failCallback Runnable
     * @param bookid String
     */
    public void findBorrowRecordsByBookid(final Runnable successCallback, final Runnable failCallback, final String bookid) {
        getBorrowRecordsByBookidReturnValue = new ArrayList<>();
        rootReference.child("borrowrecords").orderByChild("bookid").equalTo(bookid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot subSnapshot : snapshot.getChildren()) {
                            getBorrowRecordsByBookidReturnValue.add(subSnapshot.getValue(BorrowRecord.class));
                        }
                        if (successCallback != null)
                            successCallback.run();
                    }

                    @Override
                    public void onCancelled(DatabaseError e) {
                        Log.d(logTag, "Database error in getBorrowRecordsByBookid:", e.toException());
                        if (failCallback != null)
                            failCallback.run();
                    }
                });
    }

    /**
     * Used to get the ArrayList of borrowRecords that was generated by findBorrowRecordsByBookId()
     * @return Arraylist<BorrowRecord>
     */
    public ArrayList<BorrowRecord> getFindBorrowRecordsByBookidReturnValue(){
        ArrayList<BorrowRecord> ret = getBorrowRecordsByBookidReturnValue;
        getBorrowRecordsByBookidReturnValue = null;
        return ret;
    }

    /**
     * Fetches a user from database.
     * @param successCallback Runnable
     * @param failCallback Runnable
     * @param username User
     */
    public void fetchUser(final Runnable successCallback, final Runnable failCallback, String username) {
        rootReference.child("users").child(username).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        fetchUserReturnValue = dataSnapshot.getValue(User.class);
                        if (successCallback != null)
                            successCallback.run();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(logTag, "Database error", databaseError.toException());
                        if (failCallback != null)
                            failCallback.run();
                    }
                }
        );
    }

    /**
     * Returns the user that was fetched by fetchUser()
     * This method is only to be called from within the success callback passed to fetchUser()
     *
     * @return User
     */
    public User getFetchUserReturnValue(){
        User ret = fetchUserReturnValue;
        fetchUserReturnValue = null;
        return ret;
    }

    /**
     * Accepts a borrow request.
     *
     * @param successCallback Runnable
     * @param failCallback Runnable
     * @param record BorrowRecord
     */
    public void acceptBorrowRequest(final Runnable successCallback, final Runnable failCallback, final BorrowRecord record){
        // update accepted borrowRecord in database.
        rootReference.child("borrowrecords").child(record.getRecordid()).setValue(record);
        Log.d("ACCEPTING", "BORROW REQUEST");
        // Delete all other not accepted requests for the same book and user from the database.
        rootReference.child("borrowrecords").orderByChild("ownerName").equalTo(record.getOwnerName()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot subSnapshot : snapshot.getChildren()) {
                            String curbookid = (String) subSnapshot.child("bookid").getValue();
                            boolean curRecordApproved = (boolean) subSnapshot.child("approved").getValue();
                            String curRecordId = (String) subSnapshot.child("recordid").getValue();
                            if (curbookid.equals(record.getBookid()) && ! curRecordApproved && ! curRecordId.equals(record.getRecordid())) {
                                // Delete current record from the database.
                                Log.d("$$ REMOVING $$", "Removing borrow record "+ subSnapshot.getKey());
                                rootReference.child("borrowrecords").child(subSnapshot.getKey()).removeValue();
                            }
                        }
                        if (successCallback != null)
                            successCallback.run();
                    }

                    @Override
                    public void onCancelled(DatabaseError e) {
                        if (failCallback != null)
                            failCallback.run();
                    }
                });

    }

    /**
     * Denys a borrow record.
     *
     * @param successCallback Runnable
     * @param failCallback Runnable
     * @param record BorrowRecord
     */
    public void denyBorrowRequest(final Runnable successCallback, final Runnable failCallback, final BorrowRecord record){
        // Remove borrowRecord from database.
        rootReference.child("borrowrecords").child(record.getRecordid()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Log.d(logTag, "Successfully removed record " + record.getRecordid());
                    if (successCallback != null)
                        successCallback.run();
                }
                else{
                    Log.d(logTag, "failed to remove record " + record.getRecordid());
                    if (failCallback != null)
                        failCallback.run();
                }
            }
        });

        // TODO Remove notification from database as well!
    }

    /**
     * Queries the database for snapshots matching the query
     * @param reference the reference being queried
     * @param orderBy the attribute being queried
     * @param equalTo the value to be matched
     * @return the result of the query
     */
    public void queryDatabase(final Runnable successCallback, DatabaseReference reference, final String orderBy, final String equalTo){
        reference.orderByChild(orderBy).equalTo(equalTo).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        queryResult = dataSnapshot.getChildren();
                        successCallback.run();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(logTag, "Query failiure " + orderBy + " " + equalTo, databaseError.toException());
                    }
                }
        );
    }

    public Iterable<DataSnapshot> getQueryResult() {
        return queryResult;
    }

    public DatabaseReference getRootReference(){
        return rootReference;
    }

}

