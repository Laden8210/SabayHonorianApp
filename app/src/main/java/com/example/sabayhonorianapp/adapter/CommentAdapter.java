package com.example.sabayhonorianapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sabayhonorianapp.R;
import com.example.sabayhonorianapp.model.Comment;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.tvCommenterName.setText(comment.getCommenterName());
        holder.tvCommentText.setText(comment.getCommentText());
        if (comment.getTimestamp() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
            String formattedDate = sdf.format(comment.getTimestamp().toDate());
            holder.tvCommentTimestamp.setText(formattedDate);
        } else {
            holder.tvCommentTimestamp.setText("N/A");
        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView tvCommenterName, tvCommentText, tvCommentTimestamp;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCommenterName = itemView.findViewById(R.id.tvCommenterName);
            tvCommentText = itemView.findViewById(R.id.tvCommentText);
            tvCommentTimestamp = itemView.findViewById(R.id.tvCommentTimestamp);
        }
    }
}
