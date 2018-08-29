package top.inrating.poststat.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.lang.reflect.Type;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import top.inrating.poststat.R;

/**
 * Created by alexandr on 12.12.17.
 */

public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.HLViewHolder> {


    List<String> listTexts = Collections.emptyList();
    List<String> listImages = Collections.emptyList();
    Context context;
    OkHttpClient client = null;
    Picasso picasso = null;
    Transformation transformation = null;
    View.OnClickListener onClickListener = null;

    public HorizontalListAdapter(String txts, String imgs, Context context) {
        this.listTexts = null;
        this.context = context;
        if (txts != null && txts.length() > 0) {
            Type listOfStrings = new TypeToken<List<String>>() {
            }.getType();
            this.listTexts = new Gson().fromJson(txts, listOfStrings);
            this.listImages = new Gson().fromJson(imgs, listOfStrings);
        }
        Log.d("test", "list txts size = " + getItemCount());
        initComponents();
    }

    private void initComponents() {
        // ----------------------------------------------------------------
        // apply rounding to image
        // see: https://github.com/vinc3m1/RoundedImageView
        // ----------------------------------------------------------------
        /*
        transformation = new RoundedTransformationBuilder()
                .borderColor(getResources().getColor(R.color.theme_color_2))
                .borderWidthDp(5)  // 5
                .cornerRadiusDp(10)
                .oval(false)
                .build();
        */
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .connectTimeout(20000, TimeUnit.MILLISECONDS)
                    .readTimeout(30000, TimeUnit.MILLISECONDS)
                    .build();
        } catch (Exception ex) {

        }

        if (client == null) {
            Log.d("test", "client = null");
            client = new OkHttpClient.Builder()
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .connectTimeout(50000, TimeUnit.MILLISECONDS)
                    .readTimeout(100000, TimeUnit.MILLISECONDS)
                    .build();
        }

        picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))
                .build();

        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getIntFromTag(v, R.string.list_item_click_at_position);
                if (position < 0) return;
                String title = HorizontalListAdapter.this.listTexts.get(position);
                Toast.makeText(context.getApplicationContext(), title, Toast.LENGTH_SHORT).show();
            }
        };
    }

    private int getIntFromTag(View view, int keyResId) {
        int result = -1;
        Object obj = view.getTag(keyResId);
        String tag = null;
        if (obj != null) tag = obj.toString();
        if (tag != null) {
            result = Integer.valueOf(tag);
        }
        return result;
    }

    public class HLViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView txtview;
        public HLViewHolder(View view) {
            super(view);
            imageView=(ImageView) view.findViewById(R.id.imageview);
            txtview=(TextView) view.findViewById(R.id.txtview);
        }
    }

    @Override
    public HLViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

        return new HLViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HLViewHolder holder, final int position) {

         holder.txtview.setText(this.listTexts.get(position));

         if (picasso == null) return;

         //Try web service data source--------------------------------------------------------------

        String url = listImages.get(position); // https://i.imgur.com/0gqnEaY.jpg
        Log.v("test", "url = "+url);

        picasso .load(url) //"http://lorempixel.com/400/200/") //https://httpbin.org/image/jpeg")
                .placeholder(R.drawable.bkgr_image_loading)
                .fit()
                //.transform(transformation)
                .into(holder.imageView);

        // -----------------------------------------------------------------------------------------

        holder.imageView.setTag(R.string.list_item_click_at_position, String.valueOf(position));
        holder.imageView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount()
    {
        Log.d("test", "item count, listTexts is "+
                (listTexts==null?"null":"not null, size = "+listTexts.size()));
        return this.listTexts==null? 0 : this.listTexts.size();
    }
}
