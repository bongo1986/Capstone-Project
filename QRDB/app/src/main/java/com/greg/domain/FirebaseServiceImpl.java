package com.greg.domain;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.greg.qrdb.R;

import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Greg on 14-11-2016.
 */
public class FirebaseServiceImpl implements FirebaseService {

    FirebaseDatabase mDatabase;
    Context mContext;

    @Inject
    public FirebaseServiceImpl(FirebaseDatabase mDatabase, Context c){
        this.mDatabase = mDatabase;
        this.mContext = c;
    }

    @Override
    public QrCode findQrCodeByUuid(String uuid) {

        Observable<QrCode> myObservable = Observable.create(
                new Observable.OnSubscribe<QrCode>() {
                    @Override
                    public void call(Subscriber<? super QrCode> sub) {


                        if(isOnline() == false){
                            sub.onNext(null);
                            sub.onCompleted();
                        }
                        else {
                            Query query = mDatabase.getReference().child(uuid.toString());
                            ValueEventListener valueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot != null) {
                                        try {
                                            String title = dataSnapshot.child("title").getValue().toString();
                                            String desc = dataSnapshot.child("description").getValue().toString();
                                            int scanCount = Integer.parseInt(dataSnapshot.child("scan_count").getValue().toString());
                                            query.removeEventListener(this);
                                            //QrCode(String mDescription, String mTitle, String mUuid,boolean mIsScanned, int mScanCount)
                                            QrCode code = new QrCode(desc, title, uuid, true, scanCount);
                                            sub.onNext(code);
                                            sub.onCompleted();
                                        } catch (Exception ex) {
                                            sub.onNext(null);
                                            sub.onCompleted();
                                        }
                                    } else {
                                        sub.onNext(null);
                                        sub.onCompleted();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    sub.onNext(null);
                                    sub.onCompleted();
                                }
                            };
                            query.addValueEventListener(valueEventListener);
                        }
                    }
                }
        );
        QrCode result = myObservable.toBlocking().first();

        return result;
       /* UUID guid = UUID.randomUUID();

        mDatabase.child(guid.toString()).child("title").setValue("title AAAAA_BBBB");
        mDatabase.child(guid.toString()).child("description").setValue("description AAAAA_BBBB");


        Query query = mDatabase.child(guid.toString());

        ValueEventListener valueEventListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                String t = dataSnapshot.child("title").getValue().toString();
                String d = dataSnapshot.child("description").getValue().toString();
                mDatabase.child(guid.toString()).child("description").setValue(d + "_updated");
                mDatabase.child(guid.toString()).child("title").setValue(t + "_updated");
                query.removeEventListener(this);
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        };*/

    }

    @Override
    public void insertQrCode(QrCode code) {
        DatabaseReference dbRef =  mDatabase.getReference();
        String guid = code.getmUuid().toString();
        dbRef.child(guid).child("title").setValue(code.getmTitle());
        dbRef.child(guid).child("description").setValue(code.getmDescription());
        dbRef.child(guid).child("scan_count").setValue(code.getmScanCount());
    }

    @Override
    public void updateQrCodeInfo(QrCode code) {
        DatabaseReference dbRef =  mDatabase.getReference();
        String guid = code.getmUuid().toString();
        dbRef.child(guid).child("title").setValue(code.getmTitle());
        dbRef.child(guid).child("description").setValue(code.getmDescription());
    }

    @Override
    public void updateQrCodeScanCount(QrCode code) {
        DatabaseReference dbRef =  mDatabase.getReference();
        String guid = code.getmUuid().toString();
        dbRef.child(guid).child("scan_count").setValue(code.getmScanCount());
    }

    @Override
    public void deleteQrCode(String uuid) {
        DatabaseReference dbRef =  mDatabase.getReference();
        dbRef.child(uuid).removeValue();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
