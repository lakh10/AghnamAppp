package com.nibrasco.freshksa.Model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerItemAdapter
    extends FragmentPagerAdapter
        //extends RecyclerView.Adapter<ViewPagerItemAdapter.ItemHolder>
{
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> titles = new ArrayList<>();


    public ViewPagerItemAdapter(FragmentManager fm) {
        super(fm);
    }
    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }
    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    //private Context ctx;
    //private OnItemClickListener clickListener;

    //public class ItemHolder extends RecyclerView.ViewHolder {
    //    TextView title;
    //    public ImageView resource;
//
    //    public int getResourceId() {
    //        return resource.getId();
    //    }
//
    //    public ItemHolder(@NonNull View itemView) {
    //        super(itemView);
    //        title = (TextView) itemView.findViewById(R.id.title);
    //        resource = (ImageView) itemView.findViewById(R.id.resource);
    //    }
    //}
    //public ViewPagerItemAdapter(Context ctx, List<ItemCategory> items, OnItemClickListener clickListener)
    //{
    //    this.ctx = ctx;
    //    this.items = items;
    //    this.clickListener = clickListener;
    //}
    //@NonNull
    //@Override
    //public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
    //    View view = LayoutInflater
    //            .from(ctx)
    //            .inflate(R.layout.recycler_item, parent, false);
    //    final ItemHolder itemHolder = new ItemHolder(view);
    //    view.setOnClickListener(new View.OnClickListener() {
    //        @Override
    //        public void onClick(View v) {
    //            clickListener.onClick(v, itemHolder.getPosition());
    //        }
    //    });
    //    return itemHolder;
    //}
//
    //@Override
    //public void onBindViewHolder(@NonNull ItemHolder itemHolder, int position) {
    //    ItemCategory category = items.get(position);
    //    itemHolder.resource.setImageResource(category.getImage_drawable());
    //    itemHolder.title.setText(category.getName());
//
    //}
//
    //@Override
    //public int getItemCount() {
    //    return items.size();
    //}
//
    //@Override
    //public int getItemViewType(int position) {
    //    return R.layout.recycler_item;
    //}
}

