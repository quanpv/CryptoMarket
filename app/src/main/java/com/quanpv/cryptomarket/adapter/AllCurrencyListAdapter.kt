package com.quanpv.cryptomarket.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.quanpv.cryptomarket.R
import com.quanpv.cryptomarket.extension.inflate
import com.quanpv.cryptomarket.extension.loadUrl
import com.quanpv.cryptomarket.extension.setPercentChangeTextView
import com.quanpv.cryptomarket.fragment.HomeFragment
import com.quanpv.cryptomarket.model.CryptoCoin
import kotlinx.android.synthetic.main.row_currency_list_item.view.*
import java.lang.ref.WeakReference

/**
 * Created by QuanPham on 6/4/18.
 */
class AllCurrencyListAdapter() : RecyclerView.Adapter<AllCurrencyListAdapter.ViewHolder>() {

    var currencyList: ArrayList<CryptoCoin>? = null
    var priceStringResource: String? = null
    var mktCapStringResource: String? = null
    var volumeStringResource: String? = null
    var pctChangeNotAvailableStringResource: String? = null
    var negativePercentStringResource: String? = null
    var positivePercentStringResource: String? = null
    var symbolAndFullNameStringResource: String? = null
    var positiveGreenColor: Int? = null
    var negativeRedColor: Int? = null

    var contextRef: WeakReference<Context>? = null

    var onItemClick: ((Int) -> Unit)? = null
    var listener: OnItemClickListener? = null

    constructor(currencyList: ArrayList<CryptoCoin>, context: Context, listener: OnItemClickListener) : this() {
        this.currencyList = currencyList
        this.contextRef = WeakReference(context)
        this.listener = listener

        this.mktCapStringResource = this.contextRef?.get()?.getString(R.string.mkt_cap_format)
        this.volumeStringResource = this.contextRef?.get()?.getString(R.string.volume_format)
        this.negativePercentStringResource = this.contextRef?.get()?.getString(R.string.negative_pct_change_format)
        this.positivePercentStringResource = this.contextRef?.get()?.getString(R.string.positive_pct_change_format)
        this.priceStringResource = this.contextRef?.get()?.getString(R.string.unrounded_price_format)
        this.pctChangeNotAvailableStringResource = this.contextRef?.get()?.getString(R.string.not_available_pct_change_text_with_time)
        this.symbolAndFullNameStringResource = this.contextRef?.get()?.getString(R.string.nameAndSymbol)
        this.negativeRedColor = this.contextRef?.get()?.resources?.getColor(R.color.percentNegativeRed)
        this.positiveGreenColor = this.contextRef?.get()?.resources?.getColor(R.color.percentPositiveGreen)
    }

    override fun getItemCount(): Int {
        return currencyList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currencyList?.get(position)
        holder.oneHourChangeTextView.setPercentChangeTextView(item?.percent_change_1h,
                HomeFragment.HOUR, negativePercentStringResource!!, positivePercentStringResource!!, negativeRedColor!!, positiveGreenColor!!, pctChangeNotAvailableStringResource!!)
        holder.dayChangeTextView.setPercentChangeTextView(item?.percent_change_24h,
                HomeFragment.DAY, negativePercentStringResource!!, positivePercentStringResource!!, negativeRedColor!!, positiveGreenColor!!, pctChangeNotAvailableStringResource!!)
        holder.weekChangeTextView.setPercentChangeTextView(item?.percent_change_7d,
                HomeFragment.WEEK, negativePercentStringResource!!, positivePercentStringResource!!, negativeRedColor!!, positiveGreenColor!!, pctChangeNotAvailableStringResource!!)
        if (item?.market_cap_usd == null) {
            holder.currencyListMarketcapTextView.text = "N/A"
        } else {
            holder.currencyListMarketcapTextView.text = String.format(mktCapStringResource!!, item?.market_cap_usd.toDouble())
        }
        if (item?.rank == null) {
            holder.rankTextView.text = "N/A"
        } else {
            holder.rankTextView.text = item.rank
        }
        if (item?.day_volume_usd == null) {
            holder.currencyListVolumeTextView.text = "N/A"
        } else {
            holder.currencyListVolumeTextView.text = String.format(volumeStringResource!!, item.day_volume_usd.toDouble())
        }
        if (item?.price_usd == null) {
            holder.currencyListCurrPriceTextView.text = "N/A"
        } else {
            holder.currencyListCurrPriceTextView.text = String.format(priceStringResource!!, item.price_usd)
        }
        holder.currencyListfullNameTextView.text = String.format(this.symbolAndFullNameStringResource!!, item?.name, item?.symbol)
        if (item?.quickSearchID != -1) {
            //Picasso.with(contextRef?.get()).load(String.format(HomeFragment.IMAGE_URL_FORMAT, Integer.toString(item.getQuickSearchID()))).into(holder.currencyListCoinImageView)
            holder.currencyListCoinImageView.loadUrl(String.format(HomeFragment.IMAGE_URL_FORMAT, item?.quickSearchID.toString()))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.row_currency_list_item))
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        val currencyListfullNameTextView = view.currencyListfullNameTextView
        val oneHourChangeTextView = view.oneHourChangeTextView
        val dayChangeTextView = view.dayChangeTextView
        val weekChangeTextView = view.weekChangeTextView
        val rankTextView = view.rankTextView
        val currencyListCurrPriceTextView = view.currencyListCurrPriceTextView
        val currencyListVolumeTextView = view.currencyListVolumeTextView
        val currencyListMarketcapTextView = view.currencyListMarketcapTextView
        val currencyListCoinImageView = view.currencyListCoinImageView
        val favButton = view.currencyListFavButton

//        var onItemClick: ((Int, View) -> Unit)? = null

        override fun onClick(view: View?) {
//            this@AllCurrencyListAdapter.onItemClick?.invoke(adapterPosition, view!!)
            listener?.onItemClick(adapterPosition, view!!)
            onItemClick?.invoke(adapterPosition)
        }

    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        this.onItemClick = listener
    }

    interface OnItemClickListener {
//        var onClick: ((Int, View) -> Unit)? = null

        fun onItemClick(position: Int, view: View)
    }

}
/*
*
* public class AllCurrencyListAdapter extends RecyclerView.Adapter<AllCurrencyListAdapter.ViewHolder> {

    private ArrayList<CMCCoin> currencyList;
    private AllCurrencyListAdapter.ViewHolder viewHolder;
    private String priceStringResource;
    private String mktCapStringResource;
    private String volumeStringResource;
    private String pctChangeNotAvailableStringResource;
    private String negativePercentStringResource;
    private String positivePercentStringResource;
    private String symbolAndFullNameStringResource;
    private int positiveGreenColor;
    private int negativeRedColor;
    private CustomItemClickListener rowListener;
    private WeakReference<AppCompatActivity> contextRef;
    private WeakReference<DatabaseHelperSingleton> dbRef;
    private WeakReference<AllCurrencyListFragment.FavoritesListUpdater> favsUpdateCallbackRef;

    public AllCurrencyListAdapter(AllCurrencyListFragment.FavoritesListUpdater favsUpdateCallback, ArrayList<CMCCoin> currencyList,
                                  DatabaseHelperSingleton db, AppCompatActivity context, CustomItemClickListener listener) {
        this.currencyList = currencyList;
        this.contextRef = new WeakReference<>(context);
        this.rowListener = listener;
        this.dbRef = new WeakReference<>(db);
        this.mktCapStringResource = this.contextRef.get().getString(R.string.mkt_cap_format);
        this.volumeStringResource = this.contextRef.get().getString(R.string.volume_format);
        this.negativePercentStringResource = this.contextRef.get().getString(R.string.negative_pct_change_format);
        this.positivePercentStringResource = this.contextRef.get().getString(R.string.positive_pct_change_format);
        this.priceStringResource = this.contextRef.get().getString(R.string.unrounded_price_format);
        this.pctChangeNotAvailableStringResource = this.contextRef.get().getString(R.string.not_available_pct_change_text_with_time);
        this.symbolAndFullNameStringResource = this.contextRef.get().getString(R.string.nameAndSymbol);
        this.negativeRedColor = this.contextRef.get().getResources().getColor(R.color.percentNegativeRed);
        this.positiveGreenColor = this.contextRef.get().getResources().getColor(R.color.percentPositiveGreen);
        this.favsUpdateCallbackRef = new WeakReference<>(favsUpdateCallback);
    }

    public void setFavoriteButtonClickListener(final AllCurrencyListAdapter.ViewHolder holder, final int position) {
        holder.favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoinFavoritesStructures favs = dbRef.get().getFavorites();
                CMCCoin item = currencyList.get(position);
                if (favs.favoritesMap.get(item.getSymbol()) == null) { // Coin is not a favorite yet. Add it.
                    favs.favoritesMap.put(item.getSymbol(), item.getSymbol());
                    favs.favoriteList.add(item.getSymbol());
                    holder.favButton.setFavorite(true, true);
                    favsUpdateCallbackRef.get().addFavorite(item);
                } else { // Coin is already a favorite, remove it
                    favs.favoritesMap.remove(item.getSymbol());
                    favs.favoriteList.remove(item.getSymbol());
                    holder.favButton.setFavorite(false, true);
                    favsUpdateCallbackRef.get().removeFavorite(item);
                }
                dbRef.get().saveCoinFavorites(favs);
            }
        });
    }

    @Override
    public void onBindViewHolder(final AllCurrencyListAdapter.ViewHolder holder, final int position) {
        CMCCoin item = currencyList.get(position);
        CurrencyListAdapterUtils.setPercentChangeTextView(holder.oneHourChangeTextView, item.getPercent_change_1h(),
                CurrencyListTabsActivity.HOUR, negativePercentStringResource, positivePercentStringResource, negativeRedColor, positiveGreenColor, pctChangeNotAvailableStringResource);
        CurrencyListAdapterUtils.setPercentChangeTextView(holder.dayChangeTextView, item.getPercent_change_24h(),
                CurrencyListTabsActivity.DAY, negativePercentStringResource, positivePercentStringResource, negativeRedColor, positiveGreenColor, pctChangeNotAvailableStringResource);
        CurrencyListAdapterUtils.setPercentChangeTextView(holder.weekChangeTextView, item.getPercent_change_7d(),
                CurrencyListTabsActivity.WEEK, negativePercentStringResource, positivePercentStringResource, negativeRedColor, positiveGreenColor, pctChangeNotAvailableStringResource);
        if (item.getMarket_cap_usd() == null) {
            holder.currencyListMarketcapTextView.setText("N/A");
        } else {
            holder.currencyListMarketcapTextView.setText(String.format(mktCapStringResource, Double.parseDouble(item.getMarket_cap_usd())));
        }
        if (item.getRank() == null) {
            holder.rankTextView.setText("N/A");
        } else {
            holder.rankTextView.setText(item.getRank());
        }
        if (item.getVolume_usd_24h() == null) {
            holder.currencyListVolumeTextView.setText("N/A");
        } else {
            holder.currencyListVolumeTextView.setText(String.format(volumeStringResource, Double.parseDouble(item.getVolume_usd_24h())));
        }
        if (item.getPrice_usd() == null) {
            holder.currencyListCurrPriceTextView.setText("N/A");
        } else {
            holder.currencyListCurrPriceTextView.setText(String.format(priceStringResource, item.getPrice_usd()));
        }
        holder.currencyListfullNameTextView.setText(String.format(this.symbolAndFullNameStringResource, item.getName(), item.getSymbol()));
        if (item.getQuickSearchID() != -1) {
            Picasso.with(contextRef.get()).load(String.format(CurrencyListTabsActivity.IMAGE_URL_FORMAT, Integer.toString(item.getQuickSearchID()))).into(holder.currencyListCoinImageView);
        }
        CoinFavoritesStructures favs = this.dbRef.get().getFavorites();
        boolean isFav = favs.favoritesMap.get(item.getSymbol()) != null;
        holder.favButton.setFavorite(isFav, false);
        setFavoriteButtonClickListener(holder, position);
    }

    @Override
    public AllCurrencyListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_currency_list_item, parent, false);
        viewHolder = new AllCurrencyListAdapter.ViewHolder(itemLayoutView, rowListener);
        return viewHolder;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView currencyListfullNameTextView;
        private TextView oneHourChangeTextView;
        private TextView dayChangeTextView;
        private TextView weekChangeTextView;
        private TextView rankTextView;
        private TextView currencyListCurrPriceTextView;
        private TextView currencyListVolumeTextView;
        private TextView currencyListMarketcapTextView;
        private ImageView currencyListCoinImageView;
        private MaterialFavoriteButton favButton;
        private CustomItemClickListener listener;

        private ViewHolder(View itemLayoutView, CustomItemClickListener listener)
        {
            super(itemLayoutView);
            itemLayoutView.setOnClickListener(this);
            rankTextView = itemLayoutView.findViewById(R.id.rankTextView);
            currencyListfullNameTextView = itemLayoutView.findViewById(R.id.currencyListfullNameTextView);
            currencyListCurrPriceTextView = itemLayoutView.findViewById(R.id.currencyListCurrPriceTextView);
            currencyListCoinImageView = itemLayoutView.findViewById(R.id.currencyListCoinImageView);
            currencyListVolumeTextView = itemLayoutView.findViewById(R.id.currencyListVolumeTextView);
            currencyListMarketcapTextView = itemLayoutView.findViewById(R.id.currencyListMarketcapTextView);
            favButton = itemLayoutView.findViewById(R.id.currencyListFavButton);
            oneHourChangeTextView = itemLayoutView.findViewById(R.id.oneHourChangeTextView);
            dayChangeTextView = itemLayoutView.findViewById(R.id.dayChangeTextView);
            weekChangeTextView = itemLayoutView.findViewById(R.id.weekChangeTextView);
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(getAdapterPosition(), v);
        }
    }

    public int getItemCount() {
        return currencyList.size();
    }

    public void setCurrencyList(ArrayList<CMCCoin> newCurrencyList) {
        this.currencyList = newCurrencyList;
        notifyDataSetChanged();
    }

    public ArrayList<CMCCoin> getCurrencyList() {
        return currencyList;
    }

}
* */