package homework.gdg.com.homework;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by vkirillov on 15.08.2017.
 */

public class Adapter extends RecyclerView.Adapter<RowViewHolder> {

    public interface OnUpdateListener{
        void updatePosition(int position);
    }

    private List<RowModel> mDataList;
    private OnUpdateListener mUpdateListener;

    Adapter(List<RowModel> dataList, OnUpdateListener listener) {
        mDataList = dataList;
        mUpdateListener = listener;
    }

    @Override
    public RowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new RowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RowViewHolder holder, int position) {
        holder.bind(mDataList.get(position), mUpdateListener);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    List<RowModel> getDataList() {
        return mDataList;
    }
}
