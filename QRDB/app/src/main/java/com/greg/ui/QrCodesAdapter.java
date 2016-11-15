package com.greg.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.greg.data.QrdbContract;
import com.greg.domain.QrCode;
import com.greg.qrdb.R;

import java.util.UUID;

/**
 * Created by Greg on 07-11-2016.
 */
public class QrCodesAdapter  extends RecyclerView.Adapter<QrCodesAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTitleTextView;
        public ImageView mQrImage;
        public CardView mCardView;

        private QrCodeGridListener mQrCodeGridListener;
        //public QrCode mQrCode;

        public ViewHolder(View v, QrCodeGridListener qrCodeGridListener) {
            super(v);
            //mQrCode = qrCode;
            mTitleTextView = (TextView) v.findViewById(R.id.qr_code_title);
            mQrImage =(ImageView) v.findViewById(R.id.qr_code_img);
            mCardView = (CardView) v.findViewById(R.id.qr_code_card);
            mQrCodeGridListener = qrCodeGridListener;

            mCardView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    QrCode clickedCode = (QrCode) v.getTag();
                    mQrCodeGridListener.QrCodeSelected(clickedCode, mQrImage);
                }
            });
        }
    }
    Cursor mCursor;
    Context mContext;
    QrCodeGridListener mQrCodeGridListener;

    public QrCodesAdapter(Context context, Cursor c, QrCodeGridListener qrCodeGridListener){
        mCursor = c;
        mContext = context;
        mQrCodeGridListener = qrCodeGridListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_qr_code, parent, false);

        ViewHolder vh = new ViewHolder(v, mQrCodeGridListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String title = mCursor.getString(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_TITLE));
        String desc = mCursor.getString(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_DESCRIPTION));
        String uuid = mCursor.getString(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_QR_GUID));
        int isScanned = mCursor.getInt(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_IS_SCANNED));
        int scanCount = mCursor.getInt(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_SCAN_COUNT));
        byte[] imageData = mCursor.getBlob(mCursor.getColumnIndex(QrdbContract.CodeEntry.COLUMN_QR_CODE_IMAGE_DATA));
        holder.mTitleTextView.setText(title);
        Bitmap bm = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        holder.mQrImage.setImageBitmap(bm);
        holder.mCardView.setTag(new QrCode(desc, title, UUID.fromString(uuid), imageData, isScanned == 1, scanCount));

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

}
