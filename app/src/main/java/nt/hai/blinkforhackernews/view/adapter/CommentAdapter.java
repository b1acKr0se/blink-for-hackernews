package nt.hai.blinkforhackernews.view.adapter;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nt.hai.blinkforhackernews.R;
import nt.hai.blinkforhackernews.data.model.Item;
import nt.hai.blinkforhackernews.data.remote.HNClient;
import nt.hai.blinkforhackernews.view.OnMenuCommentClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnCommentClickListener {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_COMMENT = 1;
    private List<String> currentLoadingList;
    private List<Item> items;
    private Map<String, List<Item>> collapsedItem;
    private Context context;
    private RecyclerView recyclerView;
    private ValueAnimator animator;
    private int currentMenuPosition = -1;
    private OnMenuCommentClickListener onMenuCommentClickListener;

    public CommentAdapter(Context context, RecyclerView recyclerView, List<Item> list) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.items = list;
        this.currentLoadingList = new ArrayList<>();
        this.collapsedItem = new HashMap<>();
    }

    public void setOnMenuCommentClickListener(OnMenuCommentClickListener onMenuCommentClickListener) {
        this.onMenuCommentClickListener = onMenuCommentClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (!items.get(position).isLoaded())
            return TYPE_COMMENT;
        if (items.get(position).getType() != null && items.get(position).getType().equals("comment"))
            return TYPE_COMMENT;
        return TYPE_HEADER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_COMMENT)
            return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
        return new ItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hn_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final Item item = items.get(position);
        if (viewHolder instanceof CommentViewHolder) {
            final CommentViewHolder holder = (CommentViewHolder) viewHolder;
            final int level = item.getLevel();
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            int marginValue = level * context.getResources().getDimensionPixelSize(R.dimen.view_level_indicator);
            int marginComment = context.getResources().getDimensionPixelSize(R.dimen.padding_card_comment);
            params.setMargins(marginValue, 0, 0, marginComment);
            if (item.isMenuOpened()) {
                unhighlight(holder);
                highlight(holder, position, false);
            } else {
                unhighlight(holder);
            }
            holder.itemView.setLayoutParams(params);
            holder.bind(item, this, collapsedItem.containsKey(item.getId()) ? collapsedItem.get(item.getId()).size() : 0);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = items.indexOf(item);
                    if (currentMenuPosition >= 0) {
                        collapseAndRemove((CommentViewHolder) recyclerView.findViewHolderForAdapterPosition(currentMenuPosition), currentMenuPosition);
                        if (currentMenuPosition == position) {
                            currentMenuPosition = -1;
                            return true;
                        }
                    }
                    highlight(holder, position, true);
                    currentMenuPosition = position;
                    return true;
                }
            });
            if (!item.isLoaded() && !currentLoadingList.contains(item.getId())) {
                currentLoadingList.add(item.getId());
                HNClient.getInstance().getItem(item.getId()).enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        int currentPosition = items.indexOf(item);
                        if (currentPosition < 0 || currentPosition > getItemCount())
                            return;
                        Item responseItem = response.body();
                        responseItem.setLoaded(true);
                        responseItem.setLevel(level);
                        currentLoadingList.remove(responseItem.getId());
                        items.set(currentPosition, responseItem);
                        notifyItemChanged(currentPosition);
                        int pos = currentPosition + 1;
                        int index;
                        if (responseItem.getKids() != null && responseItem.getKids().length > 0) {
                            for (index = 0; index < responseItem.getKids().length; index++) {
                                Item kid = new Item();
                                kid.setId(String.valueOf(responseItem.getKids()[index]));
                                kid.setLevel(level + 1);
                                items.add(pos, kid);
                                if (pos < getItemCount())
                                    notifyItemInserted(pos);
                                pos++;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {

                    }
                });
            }
        } else if (viewHolder instanceof ItemViewHolder) {
            ((ItemViewHolder) viewHolder).bind(item, 0, null);
        }
    }

    private void highlight(CommentViewHolder holder, int position, boolean animate) {
        Item item = items.get(position);
        item.setMenuOpened(true);
        holder.menuArea.removeAllViews();
        holder.menuArea.setVisibility(View.GONE);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final View baseView = inflater.inflate(R.layout.item_comment_menu, holder.menuArea);
        expand(baseView, position, animate);
        RecyclerView.LayoutParams params =
                (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        params.setMargins(0, 0, 0, 0);
        holder.itemView.setLayoutParams(params);
        holder.container.setBackgroundColor(ContextCompat.getColor(context, R.color.selected_background_comment));
    }

    private void expand(final View v, int position, boolean animate) {
        v.setVisibility(View.VISIBLE);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(widthSpec, heightSpec);
        if (animate) {
            animator = slideAnimator(0, v.getMeasuredHeight(), v);
            animator.start();
        } else {
            ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
            layoutParams.height = v.getMeasuredHeight();
            v.setLayoutParams(layoutParams);
        }
        setupListener(v, position);
    }

    private void setupListener(View view, final int position) {
        final CommentViewHolder holder = (CommentViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMenuCommentClickListener == null) return;
                collapseAndRemove(holder, currentMenuPosition);
                currentMenuPosition = -1;
                switch (view.getId()) {
                    case R.id.upvote:
                        onMenuCommentClickListener.onUpvote(position);
                        break;
                    case R.id.profile:
                        onMenuCommentClickListener.onProfile(position);
                        break;
                    case R.id.reply:
                        onMenuCommentClickListener.onReply(position);
                        break;
                    case R.id.copy:
                        onMenuCommentClickListener.onCopy(position);
                        break;
                }
            }
        };
        view.findViewById(R.id.upvote).setOnClickListener(listener);
        view.findViewById(R.id.profile).setOnClickListener(listener);
        view.findViewById(R.id.reply).setOnClickListener(listener);
        view.findViewById(R.id.copy).setOnClickListener(listener);
    }

    private void unhighlight(CommentViewHolder holder) {
        holder.menuArea.removeAllViews();
        holder.menuArea.setVisibility(View.GONE);
        holder.container.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
    }

    private void collapseAndRemove(final CommentViewHolder holder, int position) {
        Item item = items.get(position);
        item.setMenuOpened(false);
        if (holder == null) return;
        holder.container.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        int finalHeight = holder.menuArea.getHeight();
        animator = slideAnimator(finalHeight, 0, holder.menuArea);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                holder.menuArea.removeAllViews();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                holder.menuArea.removeAllViews();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        final int level = item.getLevel();
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        int marginValue = level * context.getResources().getDimensionPixelSize(R.dimen.view_level_indicator);
        int marginComment = context.getResources().getDimensionPixelSize(R.dimen.padding_card_comment);
        params.setMargins(marginValue, 0, 0, marginComment);
    }

    private void expandComment(Item item) {
        int index = items.indexOf(item);
        if (index < 0 || index > getItemCount()) return;
        if (collapsedItem.containsKey(item.getId())) {
            List<Item> collapsed = collapsedItem.get(item.getId());
            int i = index + 1;
            for (int j = collapsed.size() - 1; j >= 0; j--) {
                items.add(i, collapsed.get(j));
            }
            collapsedItem.remove(item.getId());
            notifyItemRangeInserted(i, collapsed.size());
            item.setExpanded(true);
            notifyItemChanged(index);
        }
    }

    private void collapseComment(Item item) {
        int index = items.indexOf(item);
        if (index < 0 || index > getItemCount()) return;
        List<Item> list = new ArrayList<>();
        int j = index + 1;
        for (int i = j; i < items.size(); i++) {
            Item it = items.get(i);
            if (item.getLevel() >= it.getLevel())
                break;
            list.add(it);
            if (currentLoadingList.contains(it.getId())) currentLoadingList.remove(it.getId());
            items.remove(i);
            i--;
        }
        if (list.size() > 0) {
            notifyItemRangeRemoved(index + 1, list.size());
            item.setExpanded(false);
            notifyItemChanged(index);
            collapsedItem.put(item.getId(), list);
        }
    }

    private ValueAnimator slideAnimator(int start, int end, final View v) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onCommentClick(Item item) {
        if (item.isExpanded()) {
            collapseComment(item);
        } else {
            expandComment(item);
        }
    }
}