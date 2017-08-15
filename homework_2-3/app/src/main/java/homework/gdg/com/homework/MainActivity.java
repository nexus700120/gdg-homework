package homework.gdg.com.homework;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.OnUpdateListener,
        SwipeRefreshLayout.OnRefreshListener {

    private Adapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ((DefaultItemAnimator) recyclerView.getItemAnimator())
                .setSupportsChangeAnimations(false);

        mAdapter = new Adapter(getData(), this);
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private List<RowModel> getData() {
        List<RowModel> dataList = new ArrayList<>();
        dataList.add(new RowModel("Россия", "Москва"));
        dataList.add(new RowModel("Австрия", "Вена"));
        dataList.add(new RowModel("Азербайджан", "Баку"));
        dataList.add(new RowModel("Алжир", "Алжир"));
        dataList.add(new RowModel("Ангола", "Луанда"));
        dataList.add(new RowModel("Андорра", "Андорра-ла-Велья"));
        dataList.add(new RowModel("Аргентина", "Буэнос-Айрес"));
        dataList.add(new RowModel("Армения", "Ереван"));
        dataList.add(new RowModel("Барбадос", "Бриджтаун"));
        dataList.add(new RowModel("Бахрейн", "Манама"));
        dataList.add(new RowModel("Беларусь", "Минск"));
        dataList.add(new RowModel("Белиз", "Бельмопан"));
        dataList.add(new RowModel("Бельгия", "Брюссель"));
        return dataList;
    }

    @Override
    public void updatePosition(int position) {
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void onRefresh() {
        List<RowModel> dataList = mAdapter.getDataList();
        for (RowModel rm : dataList) {
            rm.resetLikes();
        }
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);

    }
}
