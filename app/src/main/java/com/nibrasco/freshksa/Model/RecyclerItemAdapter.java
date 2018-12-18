package com.nibrasco.freshksa.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nibrasco.freshksa.R;

import java.util.List;

import static android.view.LayoutInflater.*;

public class RecyclerItemAdapter
        extends RecyclerView.Adapter<RecyclerItemAdapter.ItemHolder>
{
    //private final List<Fragment> fragments = new ArrayList<>();
    //private final List<String> titles = new ArrayList<>();
    //public ViewPagerItemAdapter(FragmentManager fm) {
    //    super(fm);
    //}
    //public void addFragment(Fragment fragment, String title) {
    //    fragments.add(fragment);
    //    titles.add(title);
    //}
    //@Override
    //public Fragment getItem(int i) {
    //    return fragments.get(i);
    //}
    //
    //@Override
    //public int getCount() {
    //    return fragments.size();
    //}
    private Context ctx;
    private List<ItemCategory> items;
    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;
        public ImageView resource;
//
        public int getResourceId() {
            return resource.getId();
        }
//
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txtCategory);
            resource = (ImageView) itemView.findViewById(R.id.CategoryResource);
        }
        public void bind(final ItemCategory item) {
            resource.setImageResource(item.getImage_drawable());
            title.setText(item.getName());
        }
    }

    public RecyclerItemAdapter(Context ctx, List<ItemCategory> items)
    {
        this.ctx = ctx;
        this.items = items;
    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = from(ctx)
                .inflate(R.layout.recycler_item, parent, false);
        return new ItemHolder(view);
    }
//
    @Override
    public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {
        itemHolder.bind(items.get(position));

    }
//
    @Override
    public int getItemCount() {
        return items.size();
    }
//
    @Override
    public int getItemViewType(int position) {
        return R.layout.recycler_item;
    }
}

