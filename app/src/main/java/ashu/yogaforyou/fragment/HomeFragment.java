package ashu.yogaforyou.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ashu.yogaforyou.R;
import ashu.yogaforyou.activity.MainActivity;
import ashu.yogaforyou.adapter.HomeFragmentAdapter;
import ashu.yogaforyou.util.IabHelper;
import ashu.yogaforyou.util.IabResult;
import ashu.yogaforyou.util.Inventory;
import ashu.yogaforyou.util.Purchase;

/**
 * Created by apple on 01/01/17.
 */

public class HomeFragment extends Fragment implements HomeFragmentAdapter.ViewHolder.ClickListener{
    private View view;
    private RecyclerView recyclerView;

    private String [] yogaCat = new String[]{"Normal", "Stress", "Breathing", "Yoga Shop"};
    private IabHelper mHelper;

    private SharedPreferences sp;
    private String isPurchased = "no";

    private boolean mSetupDone;
    private boolean mAsyncInProgress;

    private String licenceKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhjB382F2mOqkPtyfcnDuqtlzv5GNI+gpOycJzJT6A+J7BB3VlaqYyoMqBuhIWnZQir1Ax3AxvAXM8I9WD5Kp5skDipXZxRYzlyttOezUME64LKlWnnIfELiyDGI15V1z5QhK86fqWjSLDNsUs+kP4eCG6CDbYTdZZiIztxLz59EA/+eE30SaRjRAq/1kD14fhTrv/NP1B71acVu2kIZ9DWI38nPqco8KtPJnU7/b1tTLi+O2n7xnE0PtId6b+h8VJ2GJHBiIi6kCQaW4MkPInNS8QV+p3/ss0ysDqfxZE/+x+b5RWWPR8VQJyMbzHEgv/Fqt1lGMuJ4XIlEG1B95WQIDAQAB";

    public HomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_main, container, false);
        return view;
    }

    @Override
    public void onStart() {
        intializeLayoutVariables();
        populateRecyclerView();

        setUpInApp();
        queryPurchasedItems();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpInApp();
    }

    private void setUpInApp(){
        String base64EncodedPublicKey =
                licenceKey;

        mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);

        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result) {
                                           if (!result.isSuccess()) {
                                               Log.d("failed", "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d("Setup Ok", "In-app Billing is set up OK");
                                           }
                                       }
                                   });
    }

    public boolean isAsyncInProgress(){
        return mAsyncInProgress;
    }
    public boolean isSetupDone (){
        return mSetupDone;
    }

    private void queryPurchasedItems() {
        //check if user has bought "remove adds"
//        if (mHelper.isSetupDone()) {
//            mHelper.queryInventoryAsync(mGotInventoryListener);
//        }
    }

    private void intializeLayoutVariables(){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        sp = getActivity().getSharedPreferences("purchase", 0);
        isPurchased = sp.getString("isPurchased", "no");
    }

    private void populateRecyclerView(){
        recyclerView.setVisibility(View.VISIBLE);
        //   alertsAdapter = new AlertsAdapter(alerts, siteInfoSparseArray, parentFragment, this ,isList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
//        if(isList ==1) {
            HomeFragmentAdapter adapter = new HomeFragmentAdapter(getActivity(), yogaCat, this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
//        }else {
//            AlertsAdapter adapter = new AlertsAdapter(alerts, siteInfoSparseArray, parentFragment, this ,isList);
//            recyclerView.setLayoutManager(gridLayoutManager);
//            recyclerView.setAdapter(adapter);
//        }
    }

    @Override
    public void onItemClick(int position) {
        switch (position){
            case 0:
                if(isPurchased.equalsIgnoreCase("no")) {

                    mHelper.launchPurchaseFlow(getActivity(), "normal", 10001,
                            mPurchaseFinishedListener, "mypurchasetoken");
                }
                else {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_main, new NormalFragment()).addToBackStack("Normal")
                            .commit();
                }
                break;

            case 1:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, new StressFragment()).addToBackStack("Stress")
                        .commit();
                break;

            case 2:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, new BreathingFragment()).addToBackStack("Breathing")
                        .commit();
                break;

            case 3:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, new ShopFragment()).addToBackStack("Shopping")
                        .commit();
                break;

        }
    }



    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals("normal")) {
                consumeItem();

            }

        }
    };

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase("normal"),
                        mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("isPurchased", "yes");
                        editor.commit();
                        startActivity(new Intent(getActivity(), MainActivity.class));

                    } else {
                        // handle error
                    }
                }
            };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
