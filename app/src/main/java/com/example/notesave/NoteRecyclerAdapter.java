package com.example.notesave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>{
    @NonNull
    private final Context mContext;
    private final List<NoteInfo> mNotes;
    private final LayoutInflater mLayoutInflater;

    public NoteRecyclerAdapter(@NonNull Context context, List<NoteInfo> notes) {
        mContext = context;
        mNotes = notes;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NoteInfo notes = mNotes.get(position);
        holder.mTextCourse.setText(notes.getCourse().getTitle());

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView mTextCourse;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextCourse = (TextView) itemView.findViewById(R.id.text_course);
           TextView textTitle =(TextView) itemView.findViewById(R.id.text_title);
        }
    }
}
