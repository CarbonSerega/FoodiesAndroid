package com.example.foodiesapp.di.modules;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.foodiesapp.client.IFoodiesClient;
import com.example.foodiesapp.di.modules.factory.ViewModelFactory;
import com.example.foodiesapp.repository.Repository;
import com.example.foodiesapp.utils.web.Endpoints;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module(includes = ViewModelModule.class)
public class ApplicationModule {
    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.setLenient().create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        return new Retrofit.Builder()
                .baseUrl(Endpoints.BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    IFoodiesClient getApiCallInterface(Retrofit retrofit) {
        return retrofit.create(IFoodiesClient.class);
    }

    @Provides
    @Singleton
    OkHttpClient getRequestHeader() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .build();
            return chain.proceed(request);
        })
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS);

        return httpClient.build();
    }

    @Provides
    @Singleton
    Repository getRepository(IFoodiesClient foodiesClient) {
        return new Repository(foodiesClient);
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory getViewModelFactory(Repository repository) {
        return new ViewModelFactory((Map<Class<? extends ViewModel>, Provider<ViewModel>>) repository);
    }
}
