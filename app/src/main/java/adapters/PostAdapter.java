package adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import models.Post;
import models.User;
import online.chirpp.www.chirpp.R;
import rest.GetFeed;

public class PostAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Post> mPosts;
    private ImageLoader mImageLoader;
    private ImageView mkImageView;
    private ViewHolder mViewHolder;

    private Bitmap mBitmap;
    private Post mPost;
    private Activity mActivity;

    public PostAdapter(Activity activity, List<Post> posts) {
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mPosts = posts;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return mPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_post, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.authorName = (TextView) convertView.findViewById(R.id.post_author_name);
            mViewHolder.author = (TextView) convertView.findViewById(R.id.post_author);
            mViewHolder.message = (TextView) convertView.findViewById(R.id.post_message);
            mViewHolder.likes_message = (TextView) convertView.findViewById(R.id.post_likes_message);
            mViewHolder.dislikes_message = (TextView) convertView.findViewById(R.id.post_dislikes_message);
            mViewHolder.avatar = (ImageView) convertView.findViewById(R.id.post_author_avatar);

            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mPost = mPosts.get(position);

        mViewHolder.avatar.setClipToOutline(true);
        mViewHolder.authorName.setText(mPost.user.first_name);
        mViewHolder.author.setText(mPost.user.username);
        mViewHolder.message.setText(mPost.message);
        mViewHolder.likes_message.setText(mPost.likes_message);
        mViewHolder.dislikes_message.setText(mPost.dislikes_message);
        UrlImageViewHelper.setUrlDrawable(mViewHolder.avatar, mPost.user.gravatar_url);

        return convertView;
    }



    private static class ViewHolder {
        TextView authorName;
        TextView author;
        TextView message;
        TextView likes_message;
        TextView dislikes_message;
        ImageView avatar;
    }

}

