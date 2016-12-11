package com.m3gv.news.common.view.xrecyclerview;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于https://github.com/jianghejie/XRecyclerView删除一些冗余的设计
 * <p>
 * Created by meikai on 16/8/4.
 */
public class XRecyclerView extends SafeRecyclerView {

    /**
     * 下面的ItemViewType是保留值(ReservedItemViewType),
     * 如果用户的adapter与它们重复将会强制抛出异常。
     * 不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
     * 设置一个很大的数字,尽可能避免和用户的adapter冲突
     */
    private static final int TYPE_REFRESH_HEADER = 10000;
    private static final int TYPE_FOOTER = 10001;
    private static final int HEADER_INIT_INDEX = 10002;

    private static final float DRAG_RATE = 1.3f;

    private boolean pullRefreshEnabled = true;
    private boolean loadingMoreEnabled = true;

    private boolean isLoadingData = false;
    private boolean isNoMore = false;

    private int preLoadCount = 0;

    private float mLastMotionEventY = -1;

    /**
     * 每个header必须有不同的type, 否则滚动的时候顺序会变化
     */
    private static List<Integer> sHeaderTypes = new ArrayList<>();

    private LoadingListener mLoadingListener;

    private View mEmptyView;

    private PullRefreshHeader mRefreshHeader;
    private LoadMoreFooter mFootView;

    private ArrayList<View> mHeaderViews = new ArrayList<>();

    private WrapAdapter mWrapAdapter;
    private GridLayoutManager.SpanSizeLookup customSpanSizeLoopup;

    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();
    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;

    public void setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
    }

    /**
     * 下拉刷新和加载更多的回调监听
     */
    public interface LoadingListener {
        void onRefresh();

        void onLoadMore();
    }

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        if (pullRefreshEnabled) {
            mRefreshHeader = new PullRefreshHeader(getContext());
        }

        if (loadingMoreEnabled) {
            mFootView = new LoadMoreFooter(getContext());
            mFootView.setVisibility(View.GONE);

            mFootView.setOnReloadListener(new LoadMoreFooter.OnReloadClickListener() {
                @Override
                public void onReload() {
                    isLoadingData = true;
                    mFootView.setState(LoadMoreFooter.STATE_LOADING);
                    mLoadingListener.onLoadMore();
                }
            });
        }
    }

    public void setRefreshImageViewBg(@DrawableRes int backgroundResId) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setRefreshImageViewBg(backgroundResId);
        }
    }

    public GridLayoutManager.SpanSizeLookup getCustomSpanSizeLoopup() {
        return customSpanSizeLoopup;
    }

    public void setCustomSpanSizeLoopup(GridLayoutManager.SpanSizeLookup customSpanSizeLoopup) {
        this.customSpanSizeLoopup = customSpanSizeLoopup;
    }

    public void setPreLoadCount(int preLoadCount) {
        this.preLoadCount = preLoadCount;
    }

    public void addHeaderView(View view) {
        if (view == null) {
            return;
        }

        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.add(view);
    }

    //根据header的ViewType判断是哪个header
    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    //判断一个type是否为HeaderType
    private boolean isHeaderType(int itemViewType) {
        return mHeaderViews.size() > 0 && sHeaderTypes.contains(itemViewType);
    }

    //判断是否是XRecyclerView保留的itemViewType
    private boolean isReservedItemViewType(int itemViewType) {
        return itemViewType == TYPE_REFRESH_HEADER
                || itemViewType == TYPE_FOOTER
                || sHeaderTypes.contains(itemViewType);
    }

    public void loadMoreComplete() {
        isLoadingData = false;
        mFootView.setState(LoadMoreFooter.STATE_COMPLETE);
    }

    public void setNoMore(boolean noMore) {
        isLoadingData = false;
        isNoMore = noMore;
        mFootView.setState(isNoMore ? LoadMoreFooter.STATE_COMPLETE_NO_MORE : LoadMoreFooter.STATE_COMPLETE);
    }

    public void setLoadMoreError() {
        isLoadingData = false;
        mFootView.setState(LoadMoreFooter.STATE_LOAD_ERROR);
    }

    public void reset() {
        setNoMore(false);
        loadMoreComplete();
        refreshComplete();
    }

    public void refreshComplete() {
        this.isLoadingData = false;
        mRefreshHeader.refreshComplete();
    }

    public void setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
    }

    public void setLoadingMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
    }

    public boolean isPullRefreshEnabled() {
        return pullRefreshEnabled;
    }

    public boolean isLoadingMoreEnabled() {
        return loadingMoreEnabled;
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mDataObserver.onChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    private OnScrollListener autoLoadScrollListener;

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (autoLoadScrollListener == null) {
            autoLoadScrollListener = new OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (mLoadingListener != null && !isLoadingData && loadingMoreEnabled) {
                        RecyclerView.LayoutManager layoutManager = getLayoutManager();
                        int lastVisibleItemPosition;
                        if (layoutManager instanceof GridLayoutManager) {
                            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                            lastVisibleItemPosition = findMax(into);
                        } else {
                            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                                    .findLastVisibleItemPosition();
                        }
                        if (layoutManager.getChildCount() > 0
                                && lastVisibleItemPosition >= layoutManager.getItemCount() - (1 + preLoadCount)
                                && layoutManager.getItemCount() >= layoutManager.getChildCount()
                                && !isNoMore
                                && !isLoadingData
                                && mRefreshHeader.getState() != PullRefreshHeader.STATE_REFRESHING
                                && mFootView.getCurrentState() != LoadMoreFooter.STATE_LOAD_ERROR) {

                            isLoadingData = true;
                            mFootView.setState(LoadMoreFooter.STATE_LOADING);
                            mLoadingListener.onLoadMore();
                        }
                    }
                }
            };
            this.addOnScrollListener(autoLoadScrollListener);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastMotionEventY == -1) {
            mLastMotionEventY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionEventY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY4Last = ev.getRawY() - mLastMotionEventY;
                mLastMotionEventY = ev.getRawY();
                if (isOnTop() &&
                        pullRefreshEnabled &&
                        appbarState == AppBarStateChangeListener.State.EXPANDED) {

                    mRefreshHeader.onMove(deltaY4Last / DRAG_RATE);
                    if (mRefreshHeader.getVisibleHeight() > 0
                            && mRefreshHeader.getState() < PullRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                mLastMotionEventY = -1; // reset
                if (isOnTop() && pullRefreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED
                        && mRefreshHeader.releaseAction() && mLoadingListener != null) {
                    mLoadingListener.onRefresh();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private boolean isOnTop() {
        return mRefreshHeader.getParent() != null;
    }


    /**
     * 通过外部 条件 主动触发 下拉刷新
     */
    public boolean startRefreshing() {
        if (!isLoadingData && pullRefreshEnabled && mLoadingListener != null) {

            if (mFootView != null) {
                mFootView.setVisibility(View.GONE);
            }

            this.smoothScrollToPosition(0); // 先 滑动到 顶部
            mRefreshHeader.setState(PullRefreshHeader.STATE_NORMAL);

            mRefreshHeader.startToRefresh(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mRefreshHeader.setState(PullRefreshHeader.STATE_REFRESHING);
                    mLoadingListener.onRefresh();

                    isLoadingData = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

            return true;
        }
        return false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //解决和CollapsingToolbarLayout冲突的问题
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        appbarState = state;
                    }
                });
            }
        }
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            RecyclerView.Adapter<?> adapter = getAdapter();
            if (adapter != null && mEmptyView != null) {
                int emptyCount = 0;
                if (pullRefreshEnabled) {
                    emptyCount++;
                }
                if (loadingMoreEnabled) {
                    emptyCount++;
                }
                if (adapter.getItemCount() == emptyCount) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    XRecyclerView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    XRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    private int getMySpanSize(int position) {
        if (customSpanSizeLoopup == null) {
            return 1;
        }
        return customSpanSizeLoopup.getSpanSize(position);
    }

    /**
     * 包装Adapter
     * 提供
     */
    public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private RecyclerView.Adapter adapter;

        public WrapAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        public boolean isHeader(int position) {
            return position >= 1 && position < mHeaderViews.size() + 1;
        }

        public boolean isFooter(int position) {
            if (loadingMoreEnabled) {
                return position == getItemCount() - 1;
            } else {
                return false;
            }
        }

        public boolean isRefreshHeader(int position) {
            return position == 0;
        }

        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {

                return new SimpleViewHolder(mRefreshHeader);

            } else if (isHeaderType(viewType)) {

                return new SimpleViewHolder(getHeaderViewByType(viewType));

            } else if (viewType == TYPE_FOOTER) {

                return new SimpleViewHolder(mFootView);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }
            int adjPosition = position - (getHeadersCount() + 1);
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                    return;
                }
            }
        }

        @Override
        public int getItemCount() {
            if (loadingMoreEnabled) {
                if (adapter != null) {
                    return getHeadersCount() + adapter.getItemCount() + 2;
                } else {
                    return getHeadersCount() + 2;
                }
            } else {
                if (adapter != null) {
                    return getHeadersCount() + adapter.getItemCount() + 1;
                } else {
                    return getHeadersCount() + 1;
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            int adjPosition = position - (getHeadersCount() + 1);

//            if (isReservedItemViewType(adapter.getItemViewType(adjPosition))) {
//                throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than
// 10000 ");
//            }

            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            if (isHeader(position)) {
                position = position - 1;
                return sHeaderTypes.get(position);
            }
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }

            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeadersCount() + 1) {
                int adjPosition = position - (getHeadersCount() + 1);
                if (adjPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return isHeader(position) || isFooter(position) || isRefreshHeader(position)
                               ? gridManager.getSpanCount() : getMySpanSize(position);
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition())
                    || isRefreshHeader(holder.getLayoutPosition())
                    || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        private class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

}