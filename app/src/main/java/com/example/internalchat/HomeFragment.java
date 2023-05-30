package com.example.internalchat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.internalchat.widget.TinTucAdapter;
import com.example.internalchat.widget.newsItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {
    View view;
    GridView gridViewSp;
    DataClass data;
    ArrayList<DataClass> dataModalArrayList;
    FirebaseFirestore db;
    private List<newsItem> newsList;
    private RecyclerView recyclerView;
    private TinTucAdapter tinTucAdapter;

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private QuangCaoAdapter quangCaoAdapter;
    private List<QuangCao> listQuangCao;
    private Timer timer;
    Toolbar toolbar;
    private MenuItem menuItem;
    private SearchView searchView;
    private TextView textView;
    String timeItem;
    String time;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_home, container, false);

        db = FirebaseFirestore.getInstance();

        toolbar=view.findViewById(R.id.toolbar_home);
        toolbar.setTitle("Trang Chủ ");
//      quang cao
        viewPager= view.findViewById(R.id.view_pager_qc);
        circleIndicator=view.findViewById(R.id.Circle_Indicator_qc);

        textView = view.findViewById(R.id.search_home);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SeachActivity.class);
                getContext().startActivity(intent);
            }
        });




        listQuangCao= getListQc();
        quangCaoAdapter = new QuangCaoAdapter(getActivity(),listQuangCao);
        viewPager.setAdapter(quangCaoAdapter);

        circleIndicator.setViewPager(viewPager);
        quangCaoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        autoSlideImages();
        //tintuc
        newsList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.list_tinTuc);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        tinTucAdapter = new TinTucAdapter( newsList,getContext());
        recyclerView.setAdapter(tinTucAdapter);

        loadDatainListView();

        tinTucAdapter.setOnItemClickListener(new TinTucAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(newsItem newsItem) {
                // Xử lý logic tương ứng với sự kiện onClick cho item
                Toast.makeText(getContext(), "" + newsItem.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
// list room
        gridViewSp = view.findViewById(R.id.grid_view);
        dataModalArrayList = new ArrayList<>();


        // here we are calling a method
        // to load data in our list view.
        loadDatainGridView();

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_trang_chu, menu);
//        menuItem= menu.findItem(R.id.action_search);
//        searchView= (SearchView) MenuItemCompat.getActionView(menuItem);
//        searchView.setQueryHint("Search");
//
//
//        SearchManager searchManager= (SearchManager) getActivity().getSystemService(getContext().SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                mysearch(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                mysearch(query);
//                return true;
//            }
//
//        });
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//    private void mysearch(String query) {
//
//    }

    private void autoSlideImages() {
        if (listQuangCao==null || listQuangCao.isEmpty()||viewPager==null){
            return;
        }
        //khởi tạo timer
        if(timer==null){
            timer=new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    int currentItem = viewPager.getCurrentItem();
                    int totalItem = listQuangCao.size()-1;
                    if(currentItem<totalItem){
                        currentItem++;
                        viewPager.setCurrentItem(currentItem);
                    }else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
            }
        },300,3000);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
    }
    private List<QuangCao> getListQc() {
    List<QuangCao> list = new ArrayList<>();
    list.add(new QuangCao(R.drawable.gioithieu2));
    list.add(new QuangCao(R.drawable.home_1));
    list.add(new QuangCao(R.drawable.home_2));
    list.add(new QuangCao(R.drawable.home_4));
    list.add(new QuangCao(R.drawable.home_3));

    return list;
    }


    //list room
    private void loadDatainGridView() {
        db.collection("roomify")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
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
                                        timeItem = days + " ngày trước";}
                                    else if (hours > 0) {
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
                                data = new DataClass(imageURL,title,formattedPrice,timeItem,location,postId);
                                dataModalArrayList.add(data);
                                MyAdapter adapter = new MyAdapter(requireActivity(),dataModalArrayList);
                                gridViewSp.setAdapter(adapter);
                            }
                        }
                    }
                });
    }


    private void loadDatainListView() {
        db.collection("TinTuc")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        // Lấy dữ liệu từ mỗi tài liệu và thêm vào danh sách tin tức
                        String imageUrl = document.getString("image");
                        String title = document.getString("title");
                        Timestamp timestamp1 = document.getTimestamp("time");


                        if (timestamp1 != null) {
                            // Chuyển đổi timestamp thành Date hoặc Calendar
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(timestamp1.toDate().getTime());

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
                                time = days + " ngày trước";}
                            else if (hours > 0) {
                                time = hours + " giờ trước";
                            } else if (minutes > 0) {
                                time = minutes + " phút trước";
                            } else {
                                time = seconds + " giây trước";
                            }
                        }

                        newsItem NewsItem = new newsItem(imageUrl, title, time);
                        newsList.add(NewsItem);
                    }

                    // Cập nhật RecyclerView khi dữ liệu được tải thành công
                    tinTucAdapter.notifyDataSetChanged();
                }
            }
        });

    }


}





