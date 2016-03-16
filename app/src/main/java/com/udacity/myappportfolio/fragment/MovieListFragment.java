package com.udacity.myappportfolio.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.ProgressView;
import com.udacity.myappportfolio.R;
import com.udacity.myappportfolio.adapter.MovieRecyclerViewAdapter;
import com.udacity.myappportfolio.model.Movie;
import com.udacity.myappportfolio.model.MovieMainBean;
import com.udacity.myappportfolio.presenter.PopularMoviePresenter;
import com.udacity.myappportfolio.presenter.PopularMoviePresenterImpl;
import com.udacity.myappportfolio.util.Constants;
import com.udacity.myappportfolio.util.MyUtil;
import com.udacity.myappportfolio.util.RecyclerItemClickListener;
import com.udacity.myappportfolio.view.MovieMainView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;


public class MovieListFragment extends BaseFragment implements MovieMainView {

    @Bind(R.id.list)
    RecyclerView lv_gridList;

    @Bind(R.id.rl_error)
    RelativeLayout rl_error;

    @Bind(R.id.rl_progress)
    RelativeLayout rl_progress;

    @Bind(R.id.tv_errorMessage)
    TextView tv_errorMessage;

    @Bind(R.id.rl_gridView)
    RelativeLayout rl_gridView;

    @Bind(R.id.progress_pv_circular_colors)
    ProgressView listProgressView;

    @Bind(R.id.tv_NoFavMovieAddedText)
    TextView tv_NoFavMovieAddedText;

    private boolean isSortByPopularityChecked = true, isSortByVotingChecked = false,
            isSortByFavChecked = false;

    private boolean isComingFromBack = false;

    private boolean loading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private GridLayoutManager mLayoutManager;
    private String sortBy = Constants.POPULARITY;
    private int pageNo = 1;
    private ArrayList<Movie> dynamicResultList = new ArrayList<>();
    private MovieRecyclerViewAdapter newsListViewAdapter;
    private OnItemSelectedListener itemSelectListener;
    private PopularMoviePresenter presenter;


    @Override
    public List<Movie> getMovieList() {
        return dynamicResultList;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public void isLoading(boolean isloading) {
        loading = isloading;
    }

    public static MovieListFragment getInstance() {
        MovieListFragment listFragment = new MovieListFragment();
        return listFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        presenter = new PopularMoviePresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movies_list_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prepareUI();

        if (dynamicResultList != null && dynamicResultList.size() > 0) {
            //Means coming from back pressed on detail screen, so everything is ready we just need to setup the adapter.
            isComingFromBack = true;
            //it might happen that user removed the movie from favourites on details screen so only for favourite bother the db again
            if (isSortByFavChecked) {
                dynamicResultList = presenter.fetchMovieFromDb(getActivity());
            }
            
            displayMovieList(dynamicResultList);
        } else {
            presenter.fetchMovieList(getPageNo(), getSortBy());
        }
    }

    private void prepareUI() {
        setUpGridListView();
        setUpListeners();
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.rl_error)
    public void fetchMovieAgain() {
        setPageNo(1);
        clearMovieList();
        presenter.fetchMovieList(getPageNo(), getSortBy());
    }

    private void setUpListeners() {

        lv_gridList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0 && !isSortByFavChecked) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                    Log.d("@@@", "visibleItemCount = " + visibleItemCount + " totalItemCount = " + totalItemCount + " pastVisiblesItems = " + pastVisibleItems);

                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = false;
                            //Do pagination.. i.e. fetch new data
                            presenter.fetchMovieList(getPageNo(), getSortBy());
                        }
                    }
                }

            }
        });

        lv_gridList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        if (MyUtil.notEmpty(dynamicResultList)) {

                            if (itemSelectListener != null)
                                itemSelectListener.itemSelected(dynamicResultList.get(position));
                        }
                    }
                })
        );
    }


    private void setUpGridListView() {
        lv_gridList.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_gridList.setLayoutManager(mLayoutManager);
    }


    private void displayMovieList(List<Movie> movieBeanList) {

        if (movieBeanList.size() == 0) {
            tv_NoFavMovieAddedText.setVisibility(View.VISIBLE);
            allViewGone();
        } else {
            tv_NoFavMovieAddedText.setVisibility(View.GONE);
            if (newsListViewAdapter == null || isSortByFavChecked || isComingFromBack) {
                setListViewAdapter(movieBeanList);
            } else {
                newsListViewAdapter.updateMovieList(movieBeanList);
            }
            showGrid();
        }


    }

    private void setListViewAdapter(List<Movie> movieBeanList) {
        newsListViewAdapter = new MovieRecyclerViewAdapter(
                getActivity(), movieBeanList);
        lv_gridList.setItemAnimator(new FadeInAnimator());
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(newsListViewAdapter);
        ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        lv_gridList.setAdapter(scaleAdapter);
    }

    public void clearMovieList() {
        dynamicResultList.clear();
    }

    public void addMovieList(ArrayList<Movie> movieList) {
        dynamicResultList.addAll(movieList);
    }


    @Override
    public void showProgress() {
        rl_progress.setVisibility(View.VISIBLE);
        rl_gridView.setVisibility(View.GONE);
        rl_error.setVisibility(View.GONE);
        listProgressView.setVisibility(View.GONE);
        tv_NoFavMovieAddedText.setVisibility(View.GONE);
    }

    @Override
    public void showListProgress() {
        rl_progress.setVisibility(View.GONE);
        rl_gridView.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
        listProgressView.setVisibility(View.VISIBLE);
        tv_NoFavMovieAddedText.setVisibility(View.GONE);
    }

    @Override
    public void showGrid() {
        rl_progress.setVisibility(View.GONE);
        rl_gridView.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
        listProgressView.setVisibility(View.GONE);
        tv_NoFavMovieAddedText.setVisibility(View.GONE);
    }

    @Override
    public void allViewGone() {
        rl_progress.setVisibility(View.GONE);
        rl_gridView.setVisibility(View.GONE);
        rl_error.setVisibility(View.GONE);
        listProgressView.setVisibility(View.GONE);
    }


    @Override
    public void showError(String message) {

        loading = true;

        rl_progress.setVisibility(View.GONE);
        listProgressView.setVisibility(View.GONE);

        if (dynamicResultList != null && dynamicResultList.size() > 0) {

            MyUtil.showSnackbar(rl_gridView, message);

        } else {
            rl_gridView.setVisibility(View.GONE);
            rl_error.setVisibility(View.VISIBLE);
            tv_errorMessage.setText(message);
        }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort_movies_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_sort:
                showPopUpMenu(getActivity().findViewById(R.id.action_sort));
                break;
            default:

        }
        return super.onOptionsItemSelected(item);
    }


    private void showPopUpMenu(View menuItemAnchor) {
        PopupMenu popup = new PopupMenu(getActivity(),
                menuItemAnchor);
        popup.getMenuInflater().inflate(R.menu.radio_menu, popup.getMenu());

        MenuItem item_popularity = popup.getMenu().findItem(R.id.sort_by_popularity);
        MenuItem item_voting = popup.getMenu().findItem(R.id.sort_by_voting);
        MenuItem item_fav = popup.getMenu().findItem(R.id.favorites);

        if (isSortByPopularityChecked) {
            item_popularity.setChecked(true);
        } else if (isSortByVotingChecked) {
            item_voting.setChecked(true);
        } else if (isSortByFavChecked) {
            item_fav.setChecked(true);
        }

        PopUpMenuEventHandle popUpMenuEventHandle = new PopUpMenuEventHandle(getActivity());
        popup.setOnMenuItemClickListener(popUpMenuEventHandle);
        popup.show();

    }

    @Override
    public void onSuccess(MovieMainBean bean) {
        if (isAdded()) {
            if (bean != null) {

                if (bean.getResults() != null && bean.getResults().size() > 0) {
                    int page = getPageNo();
                    page++;
                    setPageNo(page);
                    isLoading(true);
                    getMovieList().addAll(bean.getResults());
                    displayMovieList(bean.getResults());
                } else {
                    showError(getResources().getString(R.string.no_results));
                }
            } else {
                showError(getResources().getString(R.string.no_results));
            }
        }

    }

    @Override
    public void onFailure(Throwable t) {
        if (isAdded()) {
            if (t instanceof IOException)
                showError(getResources().getString(R.string.no_internet));
            else
                showError(getResources().getString(R.string.server_error));
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnItemSelectedListener) {
            itemSelectListener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        itemSelectListener = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mLayoutManager.setSpanCount(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);

    }


    //Interface to pass the events to attached activity
    public interface OnItemSelectedListener {
        void itemSelected(Movie movie);
    }


    private class PopUpMenuEventHandle implements PopupMenu.OnMenuItemClickListener {

        private final Context context;

        public PopUpMenuEventHandle(Context context) {
            this.context = context;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.sort_by_popularity) {
                isSortByPopularityChecked = true;
                isSortByVotingChecked = false;
                isSortByFavChecked = false;
                setSortBy(Constants.POPULARITY);
                setPageNo(1);
                clearMovieList();
                presenter.fetchMovieList(getPageNo(), getSortBy());
                return true;
            }
            if (item.getItemId() == R.id.sort_by_voting) {
                isSortByVotingChecked = true;
                isSortByPopularityChecked = false;
                isSortByFavChecked = false;
                setSortBy(Constants.VOTE_AVERAGE);
                setPageNo(1);
                clearMovieList();
                presenter.fetchMovieList(getPageNo(), getSortBy());
                return true;
            }
            if (item.getItemId() == R.id.favorites) {
                isSortByFavChecked = true;
                isSortByPopularityChecked = false;
                isSortByVotingChecked = false;
                ArrayList<Movie> favMovieList = presenter.fetchMovieFromDb(getActivity());
                clearMovieList();
                addMovieList(favMovieList);
                displayMovieList(dynamicResultList);
                return true;
            }
            return false;
        }
    }
}
