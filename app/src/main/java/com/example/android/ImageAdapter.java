package com.example.android;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.chessboard.*;

public class ImageAdapter extends BaseAdapter {
    // references to our images
    private Integer[] mThumbIds;
    private Context mContext;
    private Board game;

    public ImageAdapter(Context c, Board b) {
        this.mContext = c;
        this.game = b;
        mThumbIds = getState();
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(93, 93));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(6, 6, 6, 6);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    public Integer getImgID(int position){
        return mThumbIds[position];
    }

    public Integer[] getState() {
        Integer[] state = new Integer[64];

        int counter = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (game.board[i][j] == null) {
                    state[counter] = R.drawable.blank;
                }
                else switch (game.board[i][j].toString()) {
                    case "b_rook":
                        state[counter] = R.drawable.b_rook;
                        break;
                    case "b_knight":
                        state[counter] = R.drawable.b_knight;
                        break;
                    case "b_bishop":
                        state[counter] = R.drawable.b_bishop;
                        break;
                    case "b_king":
                        state[counter] = R.drawable.b_king;
                        break;
                    case "b_queen":
                        state[counter] = R.drawable.b_queen;
                        break;
                    case "b_pawn":
                        state[counter] = R.drawable.b_pawn;
                        break;
                    case "w_rook":
                        state[counter] = R.drawable.w_rook;
                        break;
                    case "w_knight":
                        state[counter] = R.drawable.w_knight;
                        break;
                    case "w_bishop":
                        state[counter] = R.drawable.w_bishop;
                        break;
                    case "w_king":
                        state[counter] = R.drawable.w_king;
                        break;
                    case "w_queen":
                        state[counter] = R.drawable.w_queen;
                        break;
                    case "w_pawn":
                        state[counter] = R.drawable.w_pawn;
                        break;
                    default:
                        state[counter] = R.drawable.blank;
                        break;
                }
                counter++;
            }
        }
        return state;
    }
}

