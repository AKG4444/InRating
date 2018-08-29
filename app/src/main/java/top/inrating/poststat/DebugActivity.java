package top.inrating.poststat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import top.inrating.poststat.ui.RoundedImageView;
import top.inrating.poststat.ui.RoundedTransformationBuilder;

public class DebugActivity extends AppCompatActivity {

    RecyclerView horizontal_recycler_view;
    HorizontalAdapter horizontalAdapter;
    private List<Data> data;
    private final List<String> imageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        horizontal_recycler_view= (RecyclerView) findViewById(R.id.horizontal_recycler_view);

        data = fill_with_data();


        horizontalAdapter=new HorizontalAdapter(data, getApplication());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(DebugActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
        horizontal_recycler_view.setAdapter(horizontalAdapter);
        Collections.addAll(imageUrls, new ImageURLS().getURLS());

    }

    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();

        data.add(new Data( R.drawable.ic_poststat_action_1, "Image 1"));
        data.add(new Data( R.drawable.ic_poststat_action_1, "Image 2"));
        data.add(new Data( R.drawable.ic_poststat_action_1, "Image 3"));
        data.add(new Data( R.drawable.ic_poststat_action_1, "Image 1"));
        data.add(new Data( R.drawable.ic_poststat_action_1, "Image 2"));
        data.add(new Data( R.drawable.ic_poststat_action_1, "Image 3"));
        data.add(new Data( R.drawable.ic_poststat_action_1, "Image 1"));
        data.add(new Data( R.drawable.ic_poststat_action_1, "Image 2"));
        data.add(new Data( R.drawable.ic_poststat_action_1, "Image 3"));


        return data;
    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {


        List<Data> horizontalList = Collections.emptyList();
        Context context;


        public HorizontalAdapter(List<Data> horizontalList, Context context) {
            this.horizontalList = horizontalList;
            this.context = context;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            RoundedImageView imageView;
            TextView txtview;
            public MyViewHolder(View view) {
                super(view);
                imageView=(RoundedImageView) view.findViewById(R.id.imageview);
                txtview=(TextView) view.findViewById(R.id.txtview);
            }
        }



        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_debug_item, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.imageView.setImageResource(horizontalList.get(position).imageId);
            holder.txtview.setText(horizontalList.get(position).txt);

            // ----------------------------------------------------------------
            // apply rounding to image
            // see: https://github.com/vinc3m1/RoundedImageView
            // ----------------------------------------------------------------
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(getResources().getColor(R.color.theme_color_2))
                    .borderWidthDp(5)  // 5
                    .cornerRadiusDp(10)
                    .oval(false)
                    .build();

            OkHttpClient client = null;
            try {
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init((KeyStore) null);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:"
                             +Arrays.toString(trustManagers));
                }
                X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                client = new OkHttpClient.Builder()
                        .sslSocketFactory(sslSocketFactory, trustManager)
                        .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                        .connectTimeout(50000, TimeUnit.MILLISECONDS)
                        .readTimeout(100000, TimeUnit.MILLISECONDS)
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

            Picasso picasso = new Picasso.Builder(holder.imageView.getContext())
                    .downloader(new OkHttp3Downloader(client))
                    .build();

            String url = imageUrls.get(position); // https://i.imgur.com/0gqnEaY.jpg
            Log.v("test", "url = "+url);
            picasso
                    .load(url) //"http://lorempixel.com/400/200/") //https://httpbin.org/image/jpeg")
                    .placeholder(R.drawable.bkgr_image_loading)
                    .fit()
                    //.transform(transformation)
                    .into(holder.imageView);


            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    String list = horizontalList.get(position).txt.toString();
                    Toast.makeText(DebugActivity.this, list, Toast.LENGTH_SHORT).show();
                }

            });

        }


        @Override
        public int getItemCount()
        {
            return horizontalList.size();
        }
    }

    public class Data {
        public int imageId;
        public String txt;

        Data( int imageId, String text) {

            this.imageId = imageId;
            this.txt=text;
        }
    }

    class ImageURLS {
        final String BASE = "http://i.imgur.com/";
        final String EXT = ".jpg";
        final String[] URLS = {
                BASE + "CqmBjo5" + EXT, BASE + "zkaAooq" + EXT, BASE + "0gqnEaY" + EXT,
                BASE + "9gbQ7YR" + EXT, BASE + "aFhEEby" + EXT, BASE + "0E2tgV7" + EXT,
                BASE + "P5JLfjk" + EXT, BASE + "nz67a4F" + EXT, BASE + "dFH34N5" + EXT,
                BASE + "FI49ftb" + EXT, BASE + "DvpvklR" + EXT, BASE + "DNKnbG8" + EXT,
                BASE + "yAdbrLp" + EXT, BASE + "55w5Km7" + EXT, BASE + "NIwNTMR" + EXT,
                BASE + "DAl0KB8" + EXT, BASE + "xZLIYFV" + EXT, BASE + "HvTyeh3" + EXT,
                BASE + "Ig9oHCM" + EXT, BASE + "7GUv9qa" + EXT, BASE + "i5vXmXp" + EXT,
                BASE + "glyvuXg" + EXT, BASE + "u6JF6JZ" + EXT, BASE + "ExwR7ap" + EXT,
                BASE + "Q54zMKT" + EXT, BASE + "9t6hLbm" + EXT, BASE + "F8n3Ic6" + EXT,
                BASE + "P5ZRSvT" + EXT, BASE + "jbemFzr" + EXT, BASE + "8B7haIK" + EXT,
                BASE + "aSeTYQr" + EXT, BASE + "OKvWoTh" + EXT, BASE + "zD3gT4Z" + EXT,
                BASE + "z77CaIt" + EXT,
        };

        public String[] getURLS() {
            return URLS;
        }

        public ImageURLS() {
            // No instances.
        }
    }
}
