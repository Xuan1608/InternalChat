package com.example.internalchat;

import static android.content.ContentValues.TAG;
import static android.graphics.Color.WHITE;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SeachActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView;
    private SearchView searchView;
    private FirebaseFirestore firestore;
    private RecyclerView recyclerView;
    private ListView listView;
    private SearchAdapter searchAdapter;
    private List<DataClass> allAddresses;
    String timeItem;
    DataClass data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach);

        textView = findViewById(R.id.back_seach);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeachActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        toolbar = findViewById(R.id.toolbar_seach);
        toolbar.setTitle("Tìm Kiếm");
        toolbar.setTitleTextColor(WHITE);

        // Khởi tạo Firestore
        firestore = FirebaseFirestore.getInstance();

        listView = findViewById(R.id.recycler_search);

        allAddresses = new ArrayList<>();

        searchAdapter = new SearchAdapter(this, allAddresses);

        searchView = (SearchView) findViewById(R.id.search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    filterAddress(query);
                } else {
                    // Nếu newText rỗng, hiển thị tất cả các địa chỉ
                    allAddresses.clear();
                    searchAdapter.notifyDataSetChanged();
                    loadAddresses();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                filterAddress(newText);
                return true;
            }
        });
        loadAddresses();
    }

    private void filterAddress(String query) {
        List<DataClass> filteredData = new ArrayList<>();
        for (DataClass data : allAddresses) {
            if (data.getLocation().toLowerCase().contains(query.toLowerCase())) {
                filteredData.add(data);
            }
        }
//        if (filteredData.isEmpty()) {
//            // Hiển thị thông báo "Không tìm thấy kết quả"
//            Toast.makeText(this, "Không tìm thấy kết quả", Toast.LENGTH_SHORT).show();
//        }
            searchAdapter.clear();
            searchAdapter.addAll(filteredData);
            searchAdapter.notifyDataSetChanged();
            // Áp dụng Span cho văn bản tìm kiếm
            // spannableQuery.setSpan(new StyleSpan(Typeface.BOLD), 0, query.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }


    private void loadAddresses() {

        firestore.collection("roomify")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            allAddresses.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<String> images = (List<String>) document.get("image");
                                String imageURL = images.get(0);
//                              String imageURL = document.getData().get("image").toString();
                                String location = document.getData().get("address").toString();
                                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                                String price = document.getData().get("price").toString();
                                double priceInMillions = Double.parseDouble(String.valueOf(price)) / 1000000;
                                String formattedPrice = decimalFormat.format(priceInMillions);

                                Timestamp timestamp = document.getTimestamp("postingTime");


                                if (timestamp != null) {
                                    // Chuyển đổi timestamp thành Date hoặc Calendar
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTimeInMillis(timestamp.toDate().getTime());

                                    // Tính khoảng cách thời gian giữa thời điểm hiện tại và thời điểm đăng
                                    long currentTime = System.currentTimeMillis();
                                    long postTime = calendar.getTimeInMillis();
                                    long timeDiff = currentTime - postTime;

                                    // Chuyển đổi khoảng cách thời gian thành đơn vị đo thời gian
                                    long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff);
                                    long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
                                    long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
                                    long days = TimeUnit.MILLISECONDS.toDays(timeDiff);

                                    // Hiển thị đơn vị thời gian trên TextView
                                    if (days > 0) {
                                        timeItem = days + " ngày trước";
                                    } else if (hours > 0) {
                                        timeItem = hours + " giờ trước";
                                    } else if (minutes > 0) {
                                        timeItem = minutes + " phút trước";
                                    } else {
                                        timeItem = seconds + " giây trước";
                                    }
                                }

                                String title = document.getData().get("title").toString();
                                String postId = document.getData().get("postId").toString();
                                Log.d(postId, "No such document!!!!!!!");
                                data = new DataClass(imageURL, title, formattedPrice, timeItem, location, postId);
                                allAddresses.add(data);
                                searchAdapter = new SearchAdapter(SeachActivity.this, allAddresses);
                                listView.setAdapter(searchAdapter);
                            }
                            searchAdapter.notifyDataSetChanged();
                        }
                        else {
                            // Xử lý khi truy vấn không thành công
                            Log.e(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(SeachActivity.this, "Không có kết quả" , Toast.LENGTH_SHORT).show();
                        }

                    }

                });
    }

}

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_trang_chu,menu);
//
//        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);
//
//        MenuItem item = menu.findItem(R.id.search);
//        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(@NonNull MenuItem item) {
//                textView.setVisibility(View.GONE);
//
//                return false;
//            }
//        });
//        textView.setVisibility(View.VISIBLE);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        return super.onOptionsItemSelected(item);
//    }

