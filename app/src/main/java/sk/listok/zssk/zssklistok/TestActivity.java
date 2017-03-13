package sk.listok.zssk.zssklistok;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;


public class TestActivity extends AppCompatActivity {


    private Subscription subscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        subscription = getPOSTData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e.toString()+"CHYBA rx");
            }

            @Override
            public void onNext(String s) {
                System.out.println("dostal som : "+s);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    public Observable<String> getPOSTData(){
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                try{
                    HttpObject ht =null;
                    return Observable.just(getHtmlPage(ht));
                }catch (Exception e){
                    System.out.println(e.toString());
                    return null;
                }
            }
        });
    }


    @Nullable
    public String getHtmlPage(HttpObject ht){
        return "Toto som stihahol zo stranky";
    }
}
