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
import com.udacity.myappportfolio.net.MovieDBResponseListener;
import com.udacity.myappportfolio.net.MovieRestAPI;
import com.udacity.myappportfolio.net.TheMovieDBClient;
import com.udacity.myappportfolio.util.Constants;
import com.udacity.myappportfolio.util.MyUtil;
import com.udacity.myappportfolio.util.RecyclerItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;


public class MovieListFragment extends BaseFragment implements MovieDBResponseListener {

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

    private boolean setSortByPopularityChecked = true;

    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private GridLayoutManager mLayoutManager;
    private MovieRestAPI myService;
    private String sort_by = Constants.POPULARITY;
    private int page = 1;
    private ArrayList<Movie> dynamicResultList = new ArrayList<>();
    private MovieRecyclerViewAdapter newsListViewAdapter;
    private TheMovieDBClient client;
    private OnItemSelectedListener itemSelectlistener;

    public interface OnItemSelectedListener {
        void itemSelected(Movie movie);
    }

    public static MovieListFragment getInstance() {
        MovieListFragment listFragment = new MovieListFragment();
        return listFragment;
    }

    public OnItemSelectedListener getItemSelectlistener() {
        return itemSelectlistener;
    }

    public void setItemSelectlistener(OnItemSelectedListener itemSelectlistener) {
        this.itemSelectlistener = itemSelectlistener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movies_list_layout, container, false);
        ButterKnife.bind(this, view);

        prepareUI();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(dynamicResultList!=null && dynamicResultList.size()>0){
            displayMovieList(dynamicResultList);
        }else{

            loadMovieList();

        }
    }

    private void prepareUI() {

        setUpGridListView();
        setUpListeners();

    }

    @SuppressWarnings("unused")
    @OnClick(R.id.rl_error)
    public void fetchMovieAgain() {
        dynamicResultList.clear();
        page = 1;
        loadMovieList();
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

                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    Log.d("@@@", "visibleItemCount = " + visibleItemCount + " totalItemCount = " + totalItemCount + " pastVisiblesItems = " + pastVisiblesItems);

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            //Do pagination.. i.e. fetch new data
                            loadMovieList();
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

                            if(itemSelectlistener!=null)
                                itemSelectlistener.itemSelected(dynamicResultList.get(position));
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


    private void loadMovieList() {
        if (dynamicResultList.size() > 0) {
            showListProgress();
        } else {
            showProgress();
        }

        client = TheMovieDBClient.getInstance();

        client.loadMovies(page,sort_by , this);

    }


    private void displayMovieList(List<Movie> movieBeanList) {
        if (page == 2) {
            newsListViewAdapter = new MovieRecyclerViewAdapter(
                    getActivity(), movieBeanList);
            lv_gridList.setItemAnimator(new FadeInAnimator());
            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(newsListViewAdapter);
            ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
            lv_gridList.setAdapter(scaleAdapter);
        } else {
            newsListViewAdapter.updateMovieList(movieBeanList);
        }

        showGrid();

    }

    private void showProgress() {
        rl_progress.setVisibility(View.VISIBLE);
        rl_gridView.setVisibility(View.GONE);
        rl_error.setVisibility(View.GONE);
        listProgressView.setVisibility(View.GONE);
    }

    private void showListProgress() {
        rl_progress.setVisibility(View.GONE);
        rl_gridView.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
        listProgressView.setVisibility(View.VISIBLE);
    }

    private void showGrid() {
        rl_progress.setVisibility(View.GONE);
        rl_gridView.setVisibility(View.VISIBLE);
        rl_error.setVisibility(View.GONE);
        listProgressView.setVisibility(View.GONE);
    }

    private void showError(String message) {

        loading = true;

        rl_progress.setVisibility(View.GONE);
        listProgressView.setVisibility(View.GONE);

        if (dynamicResultList != null && dynamicResultList.size() > 0) {

            MyUtil.showSnackbar(rl_gridView , message);

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
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {

            case R.id.action_sort:
                showPopUpMenu((View) getActivity().findViewById(R.id.action_sort));
                break;
            default:

        }
        return true;
    }



    private void showPopUpMenu(View menuItemAnchor) {
        PopupMenu popup = new PopupMenu(getActivity(),
                menuItemAnchor);
        popup.getMenuInflater().inflate(R.menu.radio_menu, popup.getMenu());

        MenuItem item_popularity = popup.getMenu().findItem(R.id.sort_by_popularity);
        MenuItem item_voting = popup.getMenu().findItem(R.id.sort_by_voting);

        if (setSortByPopularityChecked) {
            item_popularity.setChecked(true);
        } else {
            item_voting.setChecked(false);
        }

        PopUpMenuEventHandle popUpMenuEventHandle = new PopUpMenuEventHandle(getActivity());
        popup.setOnMenuItemClickListener(popUpMenuEventHandle);
        popup.show();
    }

    @Override
    public void onSuccess(MovieMainBean bean) {

        if (bean != null) {

            if (bean.getResults() != null && bean.getResults().size() > 0) {
                page++;
                loading = true;
                dynamicResultList.addAll(bean.getResults());
                displayMovieList(bean.getResults());
            } else {
                showError(getResources().getString(R.string.no_results));
            }
        } else {
            showError(getResources().getString(R.string.no_results));
        }
    }

    @Override
    public void onFailure(Throwable t) {

        if(t instanceof IOException)
            showError(getResources().getString(R.string.no_internet));
        else
            showError(getResources().getString(R.string.server_error));

    }

    public class PopUpMenuEventHandle implements PopupMenu.OnMenuItemClickListener {
        Context context;

        public PopUpMenuEventHandle(Context context) {
            this.context = context;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (!setSortByPopularityChecked && item.getItemId() == R.id.sort_by_popularity) {
                setSortByPopularityChecked = true;
                sort_by = Constants.POPULARITY;
                page = 1;
                dynamicResultList.clear();
                loadMovieList();
                return true;
            }
            if (setSortByPopularityChecked && item.getItemId() == R.id.sort_by_voting) {
                setSortByPopularityChecked = false;
                sort_by = Constants.VOTE_AVERAGE;
                page = 1;
                dynamicResultList.clear();
                loadMovieList();
                return true;
            }
            return false;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnItemSelectedListener) {
            itemSelectlistener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        itemSelectlistener = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mLayoutManager.setSpanCount(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);


    }
}
