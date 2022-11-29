package com.cj.splicing.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cj.splicing.R;
import com.cj.splicing.utils.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 图片
 * Created by CaiJing on 2022/1/7
 */

public class Actvity_Image_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> data;
    private int type=0;

    public Actvity_Image_Adapter(Context context) {
        this.context = context;
        this.data=new ArrayList<String>();
    }
    public Actvity_Image_Adapter(Context context, List<String> img) {
        this.context = context;
        this.data=new ArrayList<String>();
        this.data=img;
    }

    public void setData(List<String> list){
        this.data=new ArrayList<String>();
        this.data=list;
        notifyDataSetChanged();
    }

    public void setData(List<String> list,int type){
        this.data=new ArrayList<String>();
        this.data=list;
        this.type=type;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Fragment_Frist_Child_AdapterHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_image, parent, false
                ));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Fragment_Frist_Child_AdapterHolder child_adapterHolder = (Fragment_Frist_Child_AdapterHolder) holder;
        try {
            child_adapterHolder.showFragment_Frist_Child_AdapterHolder(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        if(type==0) {
            if (data.size() == 0) {
                return 1;
            }
            if (data.size() < 3) {
                return data.size() + 1;
            }
            return data.size();
        }
        return data.size();
    }
    class Fragment_Frist_Child_AdapterHolder extends RecyclerView.ViewHolder {

        Fragment_Frist_Child_AdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @BindView(R.id.image)
        RoundImageView image;
        @BindView(R.id.Linear_img)
        LinearLayout Linear_img;
        @BindView(R.id.iv_delete)
        ImageView iv_delete;

        void showFragment_Frist_Child_AdapterHolder(final int position) throws Exception {
            if(type==0) {
                if (data.size() == 0) {
                    Glide.with(context).load(R.mipmap.icon_tianjia).into(image);
                    iv_delete.setVisibility(View.INVISIBLE);
                    image.setOnClickListener(V->{
                        imageListener.voice_iv(position);
                    });
                    return;
                }
                if (data.size() == position) {
                    Glide.with(context).load(R.mipmap.icon_tianjia).into(image);
                    iv_delete.setVisibility(View.INVISIBLE);
                    image.setOnClickListener(V->{
                        imageListener.voice_iv(position);
                    });
                    return;
                }
                Glide.with(context).load(data.get(position)).into(image);
                iv_delete.setVisibility(View.VISIBLE);
            }
            else  {
                Glide.with(context).load(data.get(position)).into(image);
                iv_delete.setVisibility(View.VISIBLE);
            }



            iv_delete.setOnClickListener(v->{
                imageListener.delete(position);
            });

            /*image.setOnLongClickListener(v->{
                if(data.size()!=0&&position+1<=data.size()){
                    imageListener.itemOnclick(position,data.size());
                }
                return true;
            });*/


           /* image.setOnClickListener(v->{
                *//*List<ImagesBean> imagesBeans=new ArrayList<ImagesBean>();
                if(type!=0){
                    for(int i=0;i<data.size();i++){
                        ImagesBean imagesBean=new ImagesBean();
                        imagesBean.setUri(data.get(i));
                        imagesBeans.add(imagesBean);
                    }
                    Intent intent=new Intent(context, ZoomImageViewActivity.class).setFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("position",position);
                    intent.putExtra("Images", (Serializable) imagesBeans);
                    context.startActivity(intent);
                }*//*
                imageListener.itemOnclick(position,1);
            });*/


        }
    }

    private ImageListener imageListener;

    public void setImageListener(ImageListener imageListener) {
        this.imageListener = imageListener;
    }

    public interface ImageListener {
        void voice_iv(int position);

        void itemOnclick(int position, int type);
        void delete(int position);
    }

}
