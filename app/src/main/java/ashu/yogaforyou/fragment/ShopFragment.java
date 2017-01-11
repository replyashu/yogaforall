package ashu.yogaforyou.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import ashu.yogaforyou.R;
import ashu.yogaforyou.global.AppController;

/**
 * Created by apple on 11/01/17.
 */

public class ShopFragment extends Fragment implements View.OnClickListener{

    private View view;
    private Button btnFit;
    private Button btnPant;
    private Button btnFood;
    private Button btnAccessory;
    private Button btnBook;
    private Button btnExtra;

    private String shopUrl;

    private Tracker mTracker;


    public ShopFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop, container, false);

        AppController appController = (AppController) getActivity().getApplication();
        mTracker = appController.getDefaultTracker();
        initializeLayoutVariables();
        return view;
    }

    private void initializeLayoutVariables(){
        btnFit = (Button) view.findViewById(R.id.yogaFit);
        btnPant = (Button) view.findViewById(R.id.yogaPant);
        btnFood = (Button) view.findViewById(R.id.yogaFood);
        btnAccessory = (Button) view.findViewById(R.id.yogaAccessory);
        btnBook = (Button) view.findViewById(R.id.yogaBooks);
        btnExtra = (Button) view.findViewById(R.id.btnExtra);

        btnExtra.setOnClickListener(this);
        btnBook.setOnClickListener(this);
        btnAccessory.setOnClickListener(this);
        btnFood.setOnClickListener(this);
        btnPant.setOnClickListener(this);
        btnFit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.yogaFit:
                deals("fit");
                break;

            case R.id.yogaPant:
                deals("pant");
                break;

            case R.id.yogaFood:
                deals("food");
                break;

            case R.id.yogaAccessory:
                deals("accessory");
                break;

            case R.id.yogaBooks:
                deals("book");
                break;

            case R.id.btnExtra:
                deals("extra");
                break;
        }
    }

    private void deals(String yoga){
        if(yoga.contains("fit")){
            shopUrl = "https://dl.flipkart.com/dl/sports-fitness/pr?q=yoga&affid=ashuinbit&sid=dep&affExtParam1=yogaApp&affExtParam2=ashu";
        }
        else if(yoga.contains("pant")){
            shopUrl = "https://dl.flipkart.com/dl/search?q=yoga%20pants&affid=ashuinbit&affExtParam1=yogaApp&affExtParam2=ashu";
        }
        else if(yoga.contains("food")){
            shopUrl = "https://dl.flipkart.com/dl/search?q=yoga%20food&affid=ashuinbit&affExtParam1=yogaApp&affExtParam2=ashu";
        }
        else if(yoga.contains("accessory")){
            shopUrl = "https://dl.flipkart.com/dl/search?q=yoga%20accessory&affid=ashuinbit&affExtParam1=yogaApp&affExtParam2=ashu";
        }
        else if(yoga.contains("book")){
            shopUrl = "https://dl.flipkart.com/dl/search?q=yoga%20books&affid=ashuinbit&affExtParam1=yogaApp&affExtParam2=ashu";
        }
        else if(yoga.contains("extra")){
            shopUrl = "https://dl.flipkart.com/dl/offers/deals-of-the-day?affid=ashuinbit&affExtParam1=yogaApp&affExtParam2=ashu";
        }


        if(isInternetOn()) {
            sendForShopping(shopUrl);
            mTracker.setScreenName("Shopping Happening");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Shopping")
                    .setAction(shopUrl)
                    .build());
        }
    }

    private void sendForShopping(String url){
        PackageManager manager = getActivity().getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage("com.flipkart.android");
            if (i == null) {
                throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.ACTION_VIEW);

            i.setData(Uri.parse(url));
            startActivity(i);

        } catch (PackageManager.NameNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://affiliate.flipkart.com/install-app?affid=ashuinbit")));

            mTracker.setScreenName("Fallback Install");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());

            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Install")
                    .setAction(url)
                    .build());
        }
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

               Toast.makeText(getActivity(), " Please Turn your Internet On ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
